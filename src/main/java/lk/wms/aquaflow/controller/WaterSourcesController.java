package lk.wms.aquaflow.controller;


import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lk.wms.aquaflow.bo.custom.BOFactory;
import lk.wms.aquaflow.bo.custom.WaterSourceBO;
import lk.wms.aquaflow.controller.modal.AddWaterSourceModalController;
import lk.wms.aquaflow.dto.WaterSourceDTO;
import lk.wms.aquaflow.util.TableActionCell;
import lk.wms.aquaflow.view.tm.WaterSourceTM;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class WaterSourcesController implements Initializable {

    @FXML
    public TableView<WaterSourceTM> waterSourceTBV;

    @FXML
    public TableColumn<WaterSourceTM, String> watersourceIdCol;

    @FXML
    public TableColumn<WaterSourceTM, String> sourceNameCol;

    @FXML
    public TableColumn<WaterSourceTM, String> sourceTypeCol;

    @FXML
    public TableColumn<WaterSourceTM, String> locationCol;

    @FXML
    public TableColumn<WaterSourceTM, Double> capacityCol;

    @FXML
    public TableColumn<WaterSourceTM, Double> remainingCapacityCol;

    @FXML
    public TableColumn<WaterSourceTM, String> statusCol;

    @FXML
    public TableColumn<WaterSourceTM, String> actionsCol;

  //  private final WaterSourceModel waterSourceModel = new WaterSourceModel();
    private final WaterSourceBO waterSourceBO = (WaterSourceBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.WATER_SOURCE);
    public AnchorPane childRoot;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize table columns
        watersourceIdCol.setCellValueFactory(new PropertyValueFactory<>("watersource_id"));
        sourceNameCol.setCellValueFactory(new PropertyValueFactory<>("source_name"));
        sourceTypeCol.setCellValueFactory(new PropertyValueFactory<>("source_type"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        capacityCol.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        remainingCapacityCol.setCellValueFactory(new PropertyValueFactory<>("remaining_capacity"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Custom cell factory for status column with styled labels
        statusCol.setCellFactory(column -> new TableCell<WaterSourceTM, String>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setText(null);
                } else {
                    setText(status);
                    setStyle(getStatusStyle(status));
                }
            }

            private String getStatusStyle(String status) {
                return switch (status.toLowerCase()) {
                    case "active" -> "-fx-text-fill: #155724; -fx-font-weight: bold;";
                    case "inactive" -> "-fx-text-fill: #721c24; -fx-font-weight: bold;";
                    case "maintenance" -> "-fx-text-fill: #856404; -fx-font-weight: bold;";
                    case "contaminated" -> "-fx-text-fill: #dc3545; -fx-font-weight: bold;";
                    case "low" -> "-fx-text-fill: #fd7e14; -fx-font-weight: bold;";
                    case "dry" -> "-fx-text-fill: #6c757d; -fx-font-weight: bold;";
                    default -> "";
                };
            }
        });

        actionsCol.setCellFactory(TableActionCell.create(
                this::handleEdit,
                this::handleDelete
        ));

        loadWaterSources();
    }

    private void handleEdit(WaterSourceTM waterSourceTM) {
        try {
            WaterSourceDTO sourceDTO = waterSourceBO.getWaterSourceById(waterSourceTM.getWatersource_id());
            if (sourceDTO == null) {
                new Alert(Alert.AlertType.ERROR, "Could not find water source with ID: " + waterSourceTM.getWatersource_id()).show();
                return;
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lk/wms/aquaflow/view/modalViews/addWaterSource-Modal.fxml"));
            AnchorPane loadModal = loader.load();

            AddWaterSourceModalController controller = loader.getController();
            controller.setModeAndData(AddWaterSourceModalController.Mode.EDIT, sourceDTO);

            Stage stage = new Stage();
            stage.setScene(new Scene(loadModal));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            loadWaterSources();

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error opening edit modal: " + e.getMessage()).show();
        }
    }

    private void handleDelete(WaterSourceTM waterSourceTM) {
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to delete water source '" + waterSourceTM.getLocation() + "'?",
                ButtonType.YES, ButtonType.NO);
        confirmDialog.setTitle("Confirm Delete");
        confirmDialog.setHeaderText("Delete Water Source");

        Optional<ButtonType> result = confirmDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            try {
                boolean deleted = waterSourceBO.deleteWaterSource(waterSourceTM.getWatersource_id());
                if (deleted) {
                    new Alert(Alert.AlertType.INFORMATION, "Water source deleted successfully.").show();

                    loadWaterSources();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Failed to delete water source. It may be in use by the system.").show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Error deleting water source: " + e.getMessage()).show();
            }
        }
    }

    private void loadWaterSources() {
        try {
            List<WaterSourceDTO> sources = waterSourceBO.getAllWaterSources();
            String[] statusArray = {"Active", "Inactive", "Maintenance", "Contaminated", "Low", "Dry"};
            List<WaterSourceTM> tmList = sources.stream()
                    .map(dto -> {
                        String statusStr = dto.getStatus();
                        try {
                            double statusDouble = Double.parseDouble(statusStr);
                            int statusIndex = (int) statusDouble;
                            if (statusIndex >= 0 && statusIndex < statusArray.length) {
                                statusStr = statusArray[statusIndex];
                            }
                        } catch (NumberFormatException e) {
                            // status is not a number, use it as is
                        }

                        return new WaterSourceTM(
                                dto.getWatersource_id(),
                                dto.getSource_name(),
                                dto.getSource_type(),
                                dto.getLocation(),
                                dto.getCapacity(),
                                dto.getRemaining_capacity(),
                                statusStr
                        );
                    })
                    .collect(Collectors.toList());

            waterSourceTBV.setItems(FXCollections.observableArrayList(tmList));
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error loading water sources: " + e.getMessage()).show();
        }
    }

    public void cmbDistrictOnAction(ActionEvent actionEvent) {
        // Will be implemented later
    }

    public void txtSearch(ActionEvent actionEvent) {
        // Will be implemented later
    }

    public void addWaterSourceBtnOnAction(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lk/wms/aquaflow/view/modalViews/addWaterSource-Modal.fxml"));
            AnchorPane loadModal = loader.load();

            AddWaterSourceModalController controller = loader.getController();
            controller.setModeAndData(AddWaterSourceModalController.Mode.ADD, null);

            Stage stage = new Stage();
            stage.setScene(new Scene(loadModal));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
            stage.showAndWait();


            loadWaterSources();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error opening add water source modal: " + e.getMessage()).show();
        }
    }
}