package lk.wms.aquaflow.controller;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lk.wms.aquaflow.bo.custom.BOFactory;
import lk.wms.aquaflow.bo.custom.VillageBO;
import lk.wms.aquaflow.bo.custom.WaterAllocationBO;
import lk.wms.aquaflow.bo.custom.WaterSourceBO;
import lk.wms.aquaflow.controller.modal.AddSourceAllocationModalController;
import lk.wms.aquaflow.dto.WaterAllocationDTO;
import lk.wms.aquaflow.dto.WaterSourceDTO;
import lk.wms.aquaflow.util.TableActionCell;
import lk.wms.aquaflow.view.tm.SourceAllocationTM;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class WaterAllocationController implements Initializable {
    public AnchorPane waterAllocationRootPane;
    public ListView<WaterSourceDTO> waterSourcesList; // Use generics for type safety
    public Text lblTotalCapacity;
    public Text lblAllocatedAmount;
    public Text lblRemainingAmount;
    public TableView<SourceAllocationTM> allocationDetailsTBV;
    public TableColumn<SourceAllocationTM, String> allocatedToCol;
    public TableColumn<SourceAllocationTM, String> allocationAmountCol;
    public TableColumn<SourceAllocationTM, String> dateCol;
    public TableColumn<SourceAllocationTM, String> actionsCol;

  //  private final WaterSourceModel waterSourceModel = new WaterSourceModel();
   // private final WaterAllocationModel allocationModel = new WaterAllocationModel();
  //  private final VillageModel villageModel = new VillageModel();

    private final WaterSourceBO waterSourceBO = (WaterSourceBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.WATER_SOURCE);
    private final WaterAllocationBO allocationBO = (WaterAllocationBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.WATER_ALLOCATION);
    private final VillageBO villageBO = (VillageBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.VILLAGE);

    public void cmbVillageOnAction(ActionEvent actionEvent) {}

    public void cmbDurationOnAction(ActionEvent actionEvent) {}

    public void txtSearch(ActionEvent actionEvent) {}

    public void viewWaterSourcesBtnOnAction(ActionEvent actionEvent) {
        waterAllocationRootPane.getChildren().clear();
        try {
            AnchorPane newPane = FXMLLoader.load(getClass().getResource("/lk/wms/aquaflow/view/waterSources-view.fxml"));
            waterAllocationRootPane.getChildren().add(newPane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupTable();
        loadAllWaterSources();

        waterSourcesList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                loadAllocationsForWaterSource(newVal.getWatersource_id());
            } else {
                allocationDetailsTBV.setItems(FXCollections.observableArrayList());
            }
        });
    }

    private void setupTable() {
        allocatedToCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("villageName"));
        allocationAmountCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("allocationAmount"));
        dateCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("allocationDate"));

        actionsCol.setCellFactory(TableActionCell.create(
                this::handleEditAllocation,
                this::handleDeleteAllocation
        ));
    }

    private void loadAllWaterSources() {
        try {
            List<WaterSourceDTO> allWaterSources = waterSourceBO.getAllWaterSources();
            ObservableList<WaterSourceDTO> observableList = FXCollections.observableArrayList(allWaterSources);
            waterSourcesList.setItems(observableList);

            // Update the cell factory in your loadAllWaterSources() method
            waterSourcesList.setCellFactory(param -> new ListCell<WaterSourceDTO>() {
                @Override
                protected void updateItem(WaterSourceDTO item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setGraphic(null);
                        getStyleClass().remove("source-item");
                        getStyleClass().remove("active");
                    } else {
                        // Create custom layout
                        VBox container = new VBox();
                        container.getStyleClass().add("source-container");

                        Label nameLabel = new Label(item.getLocation());
                        nameLabel.getStyleClass().add("source-name");

                        HBox detailsBox = new HBox();
                        detailsBox.getStyleClass().add("source-details");

                        Label capacityLabel = new Label(String.format("Capacity: %.2f", item.getCapacity()));
                        Label statusLabel = new Label("Status: " + item.getStatus());

                        detailsBox.getChildren().addAll(capacityLabel, statusLabel);
                        container.getChildren().addAll(nameLabel, detailsBox);

                        setGraphic(container);
                        getStyleClass().add("source-item");

                        // Add active class if needed (for selection)
                        if (isSelected()) {
                            getStyleClass().add("active");
                        }
                    }
                }
            });


            waterSourcesList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
                waterSourcesList.refresh();
            });

            if (!observableList.isEmpty()) {
                waterSourcesList.getSelectionModel().selectFirst();
                WaterSourceDTO first = waterSourcesList.getSelectionModel().getSelectedItem();
                if (first != null) {
                    loadAllocationsForWaterSource(first.getWatersource_id());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                    javafx.scene.control.Alert.AlertType.ERROR,
                    "Failed to load water sources: " + e.getMessage()
            );
            alert.show();
        }
    }

    private void loadAllocationsForWaterSource(String waterSourceId) {
        try {
            List<WaterAllocationDTO> allocations = allocationBO.getAllocationsByWaterSourceId(waterSourceId);

            ObservableList<SourceAllocationTM> tableData = FXCollections.observableArrayList();
            double totalAllocated = 0;
            for (WaterAllocationDTO dto : allocations) {
                // TODO: get village name
                String villageName = villageBO.getVillageById(dto.getVillageId()).getVillageName();
                tableData.add(new SourceAllocationTM(
                        dto.getAllocationId(),
                        villageName,
                        String.valueOf(dto.getAllocationAmount()),
                        dto.getAllocationDate().toString()
                ));
                totalAllocated += dto.getAllocationAmount();
            }

            allocationDetailsTBV.setItems(tableData);

            // update summary labels
            WaterSourceDTO source = waterSourceBO.getWaterSourceById(waterSourceId);
            if (source != null) {
                lblTotalCapacity.setText(String.format("%.2f", source.getCapacity()));
                lblAllocatedAmount.setText(String.format("%.2f", totalAllocated));
                lblRemainingAmount.setText(String.format("%.2f", source.getCapacity() - totalAllocated));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleEditAllocation(SourceAllocationTM tm) {
        try {
            // Find corresponding DTO
            WaterSourceDTO selectedSource = waterSourcesList.getSelectionModel().getSelectedItem();
            if (selectedSource == null) return;

            WaterAllocationDTO dto = new WaterAllocationDTO(
                    tm.getAllocationId(),
                    Double.parseDouble(tm.getAllocationAmount()),
                    selectedSource.getWatersource_id(),
                    villageBO.getVillageIdByName(tm.getVillageName()),
                    java.time.LocalDate.parse(tm.getAllocationDate())
            );

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lk/wms/aquaflow/view/modalViews/addSourceAllocation.fxml"));
            AnchorPane loadModal = loader.load();

            AddSourceAllocationModalController controller = loader.getController();
            controller.setModeAndData(AddSourceAllocationModalController.Mode.UPDATE, dto);

            Stage stage = new Stage();
            stage.setScene(new Scene(loadModal));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(allocationDetailsTBV.getScene().getWindow());
            stage.showAndWait();

            // reload table
            loadAllocationsForWaterSource(selectedSource.getWatersource_id());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleDeleteAllocation(SourceAllocationTM tm) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to delete Allocation ID: " + tm.getAllocationId() + "?", ButtonType.YES, ButtonType.NO);
        confirm.setHeaderText("Confirm Delete");
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            try {
                boolean deleted = allocationBO.deleteWaterAllocation(tm.getAllocationId());
                if (deleted) {
                    WaterSourceDTO selectedSource = waterSourcesList.getSelectionModel().getSelectedItem();
                    if (selectedSource != null) {
                        loadAllocationsForWaterSource(selectedSource.getWatersource_id());
                    }
                } else {
                    new Alert(Alert.AlertType.ERROR, "Failed to delete allocation!").show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Error occurred while deleting!").show();
            }
        }
    }

    public void waterAllocationBtnOnAction(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lk/wms/aquaflow/view/modalViews/addSourceAllocation.fxml"));
            AnchorPane loadModal = loader.load();

            AddSourceAllocationModalController controller = loader.getController();
            controller.setModeAndData(AddSourceAllocationModalController.Mode.ADD, null);

            Stage stage = new Stage();
            stage.setScene(new Scene(loadModal));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
            stage.showAndWait();

            // reload after add
            WaterSourceDTO selectedSource = waterSourcesList.getSelectionModel().getSelectedItem();
            if (selectedSource != null) {
                loadAllocationsForWaterSource(selectedSource.getWatersource_id());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}