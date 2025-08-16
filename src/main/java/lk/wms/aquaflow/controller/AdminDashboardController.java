package lk.wms.aquaflow.controller;

import com.mysql.cj.protocol.ResultStreamer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lk.wms.aquaflow.bo.custom.*;
import lk.wms.aquaflow.controller.modal.AddDistributionModalController;
import lk.wms.aquaflow.dto.WaterDistributionDTO;
import lk.wms.aquaflow.util.CrudUtil;
import lk.wms.aquaflow.util.TableActionCell;
import lk.wms.aquaflow.view.tm.DistributionTM;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

public class AdminDashboardController implements Initializable {

    public AnchorPane changingAnchor;
    public AnchorPane adminDashBoardRootPane;
    public TableView<DistributionTM> recentDistributionTBV;
    public TableColumn<DistributionTM, String> dateCol;
    public TableColumn<DistributionTM, String> villageCol;
    public TableColumn<DistributionTM, String> sourceCol;
    public TableColumn<DistributionTM, String> amountCol;
    public TableColumn<DistributionTM, String> statusCol;
    public TableColumn<DistributionTM, String> actionCol;

  //  private final WaterDistributionModel distributionModel = new WaterDistributionModel();
   // private final VillageModel villageModel = new VillageModel();
   // private final WaterAllocationModel allocationModel = new WaterAllocationModel();
   // private final WaterSourceModel sourceModel = new WaterSourceModel();

    private final WaterDistributionBO waterDistributionBO = (WaterDistributionBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.DISTRIBUTION);
    private final VillageBO villageBO = (VillageBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.VILLAGE);
    private final WaterAllocationBO waterAllocationBO = (WaterAllocationBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.WATER_ALLOCATION);
    private final WaterSourceBO waterSourceBO = (WaterSourceBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.WATER_SOURCE);

    public PieChart waterSourceChart;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize table columns
        dateCol.setCellValueFactory(new PropertyValueFactory<>("distributionDate"));
        villageCol.setCellValueFactory(new PropertyValueFactory<>("villageName"));
        sourceCol.setCellValueFactory(new PropertyValueFactory<>("sourceName"));
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Custom cell factory for the Status column with styled labels
        statusCol.setCellFactory(column -> new TableCell<DistributionTM, String>() {
            private final Label label = new Label();

            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setGraphic(null);
                } else {
                    label.setText(status);
                    label.setStyle(getStatusStyle(status));
                    setGraphic(label);
                }
            }

            private String getStatusStyle(String status) {
                return switch (status.toLowerCase()) {
                    case "completed" -> "-fx-background-color: #d4edda; -fx-text-fill: #155724; -fx-background-radius: 15; -fx-padding: 5 10;";
                    case "scheduled" -> "-fx-background-color: #f8d7da; -fx-text-fill: #721c24; -fx-background-radius: 15; -fx-padding: 5 10;";
                    default -> "-fx-background-color: lightgray; -fx-background-radius: 15; -fx-padding: 5 10;";
                };
            }
        });

        // Use the new TableActionCell method for complete and delete actions
        actionCol.setCellFactory(TableActionCell.createWithCompleteAndDelete(
                this::handleComplete,
                this::handleDelete
        ));

        try {
            loadAllDistributions();
            loadStatusPieChart();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load distributions: " + e.getMessage()).show();
        }
    }

    private void handleComplete(DistributionTM distributionTM) {
        // Only allow completion if status is currently "Scheduled"
        if (!"Scheduled".equalsIgnoreCase(distributionTM.getStatus())) {
            new Alert(Alert.AlertType.INFORMATION, "Only scheduled distributions can be marked as completed.").show();
            return;
        }

        // Ask for confirmation
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to mark Distribution ID: " + distributionTM.getDistributionId() + " as completed?",
                ButtonType.YES, ButtonType.NO
        );
        confirm.setHeaderText("Confirm Status Change");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            try {
                WaterDistributionDTO distributionDTO = waterDistributionBO.getDistributionById(distributionTM.getDistributionId());

                if (distributionDTO != null) {
                    // Update the status to "Completed"
                    distributionDTO.setStatus("Completed");

                    // Save the updated distribution
                    boolean updated = waterDistributionBO.updateWaterDistribution(distributionDTO);

                    if (updated) {
                        loadAllDistributions();
                        new Alert(Alert.AlertType.INFORMATION, "Distribution marked as completed successfully!").show();
                    } else {
                        new Alert(Alert.AlertType.ERROR, "Failed to update distribution status!").show();
                    }
                } else {
                    new Alert(Alert.AlertType.ERROR, "Could not find distribution with ID: " + distributionTM.getDistributionId()).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Error updating distribution status: " + e.getMessage()).show();
            }
        }
    }


    private String getWaterSourceNameForVillage(String villageId) throws SQLException, ClassNotFoundException {
        String waterSourceId = waterAllocationBO.getWaterSourceIdForVillage(villageId);

        if (waterSourceId != null) {
            return waterSourceBO.getWaterSourceNameById(waterSourceId);
        }

        return "No Source Allocated";
    }

    private DistributionTM convertToTM(WaterDistributionDTO distributionDTO) {
        try {

            String formattedDate = distributionDTO.getDistributionDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            String villageName = "Unknown";
            try {
                villageName = villageBO.getVillageById(distributionDTO.getVillageId()).getVillageName();
            } catch (Exception e) {
                e.printStackTrace();
            }


            String sourceName = getWaterSourceNameForVillage(distributionDTO.getVillageId());

            return new DistributionTM(
                    distributionDTO.getDistributionId(),
                    formattedDate,
                    villageName,
                    sourceName,
                    String.format("%.2f L", distributionDTO.getTotalAllocation()),
                    distributionDTO.getStatus()
            );
        } catch (Exception e) {
            e.printStackTrace();
            return new DistributionTM(
                    distributionDTO.getDistributionId(),
                    distributionDTO.getDistributionDate().toString(),
                    "Error",
                    "Error",
                    String.valueOf(distributionDTO.getTotalAllocation()),
                    distributionDTO.getStatus()
            );
        }
    }

    private void loadAllDistributions() throws Exception {
        recentDistributionTBV.setItems(FXCollections.observableArrayList(
                waterDistributionBO.getAllDistributions().stream()
                        .map(this::convertToTM)
                        .toList()
        ));
    }

    public void btnAlertsOnAction(MouseEvent mouseEvent) {
    }

    public void btnReceiveEmailOnAction(MouseEvent mouseEvent) {
    }

    public void addButtonOnAction(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lk/wms/aquaflow/view/modalViews/addDistribution-Modal.fxml"));
            AnchorPane loadModal = loader.load();

            AddDistributionModalController controller = loader.getController();
            controller.setModeAndData(AddDistributionModalController.Mode.ADD, null);

            Stage stage = new Stage();
            stage.setScene(new Scene(loadModal));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
            stage.showAndWait();

            // Refresh table after adding
            loadAllDistributions();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error opening add distribution modal: " + e.getMessage()).show();
        }
    }

    private void handleDelete(DistributionTM distributionTM) {
        String distributionId = distributionTM.getDistributionId();

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to delete Distribution ID: " + distributionId + "?",
                ButtonType.YES, ButtonType.NO
        );
        confirm.setHeaderText("Confirm Delete");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            try {
                boolean isDeleted = waterDistributionBO.deleteWaterDistribution(distributionId);
                if (isDeleted) {
                    loadAllDistributions();
                    new Alert(Alert.AlertType.INFORMATION, "Distribution deleted successfully!").show();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Failed to delete distribution!").show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Error occurred while deleting: " + e.getMessage()).show();
            }
        }
    }

    // TODO
    private void loadStatusPieChart() {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        try {
            Map<String, Integer> statusCounts = waterSourceBO.getStatusCount();
            if (statusCounts != null) {
                statusCounts.forEach((status, count) ->
                    pieChartData.add(new PieChart.Data(status, count))
                );
            }
            waterSourceChart.setData(pieChartData);
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error loading status data: " + e.getMessage()).show();
        }
    }
    public void viewButtonOnAction(ActionEvent actionEvent) {
        changingAnchor.getChildren().clear();
        try {
            AnchorPane newPane = FXMLLoader.load(getClass().getResource("/lk/aquaflowwms/view/waterDistributions-view.fxml"));
            changingAnchor.getChildren().add(newPane);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void btnVillageOnAction(ActionEvent actionEvent) {
        changingAnchor.getChildren().clear();
        try {
            AnchorPane newPane = FXMLLoader.load(getClass().getResource("/lk/wms/aquaflow/view/villages-view.fxml"));
            changingAnchor.getChildren().add(newPane);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void btnWaterAllocationOnAction(ActionEvent actionEvent) {
        changingAnchor.getChildren().clear();
        try {
            // Load the FXML
            AnchorPane newPane = FXMLLoader.load(getClass().getResource("/lk/wms/aquaflow/view/waterAllocations-view.fxml"));

            // Add the new pane to your changingAnchor
            changingAnchor.getChildren().add(newPane);

//            // Get the scene and add stylesheet
//            Scene scene = changingAnchor.getScene();
//            if (scene != null) {
//                // Clear any existing stylesheets to avoid duplicates
//                scene.getStylesheets().clear();
//                // Add your custom stylesheet
//                scene.getStylesheets().add(
//                        getClass().getResource("/lk/aquaflowwms/styles/waterAllocation.css").toExternalForm()
//                );
//            }
        } catch (Exception e) {
            // Proper error handling
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load water allocations view: " + e.getMessage()).show();
        }
    }


    public void btnHouseHoldsOnAction(ActionEvent actionEvent) {
        changingAnchor.getChildren().clear();
        try {
            AnchorPane newPane = FXMLLoader.load(getClass().getResource("/lk/wms/aquaflow/view/household-view.fxml"));
            changingAnchor.getChildren().add(newPane);
        }catch (Exception e){

        }
    }

    public void btnMaintenanceOnAction(ActionEvent actionEvent) {
        changingAnchor.getChildren().clear();
        try {
            AnchorPane newPane = FXMLLoader.load(getClass().getResource("/lk/wms/aquaflow/view/maintenance-view.fxml"));
            changingAnchor.getChildren().add(newPane);
        }catch (Exception e){

        }
    }

    public void btnEmployeeOnAction(ActionEvent actionEvent) {
        changingAnchor.getChildren().clear();
        try {
            AnchorPane newPane = FXMLLoader.load(getClass().getResource("/lk/wms/aquaflow/view/officers-view.fxml"));
            changingAnchor.getChildren().add(newPane);
        }catch (Exception e){

        }
    }

    public void btnComplaintsOnAction(ActionEvent actionEvent) {
        changingAnchor.getChildren().clear();
        try {
            AnchorPane newPane = FXMLLoader.load(getClass().getResource("/lk/wms/aquaflow/view/complaints-view.fxml"));
            changingAnchor.getChildren().add(newPane);
        }catch (Exception e){

        }
    }

    public void btnBillingOnAction(ActionEvent actionEvent) {
        changingAnchor.getChildren().clear();
        try {
            AnchorPane newPane = FXMLLoader.load(getClass().getResource("/lk/wms/aquaflow/view/billings-view.fxml"));
            changingAnchor.getChildren().add(newPane);
        }catch (Exception e){

        }

    }

    public void btnSignoutOnAction(ActionEvent actionEvent) {

    }

    public void btnVendorOnAction(ActionEvent actionEvent) {
        changingAnchor.getChildren().clear();
        try {
            AnchorPane newPane = FXMLLoader.load(getClass().getResource("/lk/wms/aquaflow/view/vendors-view.fxml"));
            changingAnchor.getChildren().add(newPane);
        }catch (Exception e){

        }
    }

    public void btnInventoryOnAction(ActionEvent actionEvent) {
        changingAnchor.getChildren().clear();
        try {
            AnchorPane newPane = FXMLLoader.load(getClass().getResource("/lk/wms/aquaflow/view/inventories-view.fxml"));
            changingAnchor.getChildren().add(newPane);
        }catch (Exception e){

        }
    }

    public void btnDashboardOnAction(ActionEvent actionEvent) {
        adminDashBoardRootPane.getChildren().clear();
        try {
            AnchorPane newPane = FXMLLoader.load(getClass().getResource("/lk/wms/aquaflow/view/adminDashboard-view.fxml"));
            adminDashBoardRootPane.getChildren().add(newPane);
        }catch (Exception e){

        }
    }
}