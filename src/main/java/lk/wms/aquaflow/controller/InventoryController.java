package lk.wms.aquaflow.controller;


import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lk.wms.aquaflow.bo.custom.BOFactory;
import lk.wms.aquaflow.bo.custom.InventoryBO;
import lk.wms.aquaflow.bo.custom.VendorBO;
import lk.wms.aquaflow.controller.modal.AddInventoryModalController;
import lk.wms.aquaflow.dto.InventoryDTO;
import lk.wms.aquaflow.util.TableActionCell;
import lk.wms.aquaflow.view.tm.InventoryTM;


import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;


public class InventoryController implements Initializable {


    public TableView<InventoryTM> tableViewInventory;
    public TableColumn<InventoryTM,String> inventoryIdCol;
    public TableColumn<InventoryTM,String> typeCol;
    public TableColumn<InventoryTM,String> quantityCol;
    public TableColumn<InventoryTM,String> unitPriceCol;
    public TableColumn<InventoryTM,String> supplierNameCol;
    public TableColumn<InventoryTM,String> actionCol;

    //private final InventoryModel inventoryModel = new InventoryModel();
    //private final VendorModel vendorModel = new VendorModel();
    private final InventoryBO inventoryBO = (InventoryBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.INVENTORY);
    private final VendorBO vendorBO = (VendorBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.VENDOR);
    public Button addButton;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("InventoryController: initialize() called");
        inventoryIdCol.setCellValueFactory(new PropertyValueFactory<>("inventoryId"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        unitPriceCol.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        System.out.println("man awaaaa"+unitPriceCol.getText());
        supplierNameCol.setCellValueFactory(new PropertyValueFactory<>("supplierName"));

        actionCol.setCellFactory(TableActionCell.create(
                this::handleEdit,
                this::handleDelete
        ));


        try {
            loadAllInventory();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private InventoryTM convertToTM(InventoryDTO inventoryDTO) {
        return new InventoryTM(
                inventoryDTO.getInventoryId(),
                inventoryDTO.getType(),
                inventoryDTO.getQuantity(),
                String.valueOf(inventoryDTO.getUnitPrice()),
                inventoryDTO.getSupplierName() != null ? inventoryDTO.getSupplierName() : "Unknown Supplier"
        );
    }

    private void loadAllInventory() throws SQLException, ClassNotFoundException {
        tableViewInventory.setItems(FXCollections.observableArrayList(
                inventoryBO.getAllInventories().stream()
                        .map(this::convertToTM)
                        .toList()
        ));
    }


    public void addButtonOnAction(ActionEvent actionEvent) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lk/aquaflowwms/view/modalViews/addInventory-Modal.fxml"));
            AnchorPane loadModal = loader.load();

            AddInventoryModalController controller = loader.getController();
            controller.setModeAndData(AddInventoryModalController.Mode.ADD, null);
            Stage stage = new Stage();
            stage.setScene(new Scene(loadModal));

            // 👇 Block the main stage
            stage.initModality(Modality.WINDOW_MODAL);

            // 👇 Set the owner (main window)
            stage.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());

            // 👇 Show the modal and wait until it’s closed
            stage.showAndWait();
            loadAllInventory();
        }catch (Exception e){
            e.printStackTrace();
        }
    }



    private void handleEdit(InventoryTM inventoryTM) {
        try {
            InventoryDTO inventoryDTO = inventoryBO.getInventoryById(inventoryTM.getInventoryId());

            if (inventoryDTO == null) {
                String supplierId = vendorBO.getSupplierIdByName(inventoryTM.getSupplierName());
                if (supplierId == null) {
                    new Alert(Alert.AlertType.ERROR, "Could not find supplier ID for name: " + inventoryTM.getSupplierName()).show();
                    return;
                }

                // Create DTO from TM data
                inventoryDTO = new InventoryDTO(
                        inventoryTM.getInventoryId(),
                        inventoryTM.getType(),
                        inventoryTM.getQuantity(),
                        Double.parseDouble(inventoryTM.getUnitPrice()),
                        supplierId
                );
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lk/aquaflowwms/view/modalViews/addInventory-Modal.fxml"));
            AnchorPane modalRoot = loader.load();

            AddInventoryModalController modalController = loader.getController();
            modalController.setModeAndData(AddInventoryModalController.Mode.UPDATE, inventoryDTO);

            Stage stage = new Stage();
            stage.setScene(new Scene(modalRoot));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(tableViewInventory.getScene().getWindow());
            stage.showAndWait();

            loadAllInventory();
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid number format in data!").show();
            e.printStackTrace();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error loading inventory data: " + e.getMessage()).show();
            e.printStackTrace();
        }
    }

    private void handleDelete(InventoryTM inventoryTM) {
        String inventoryId = inventoryTM.getInventoryId();

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to delete Inventory ID: " + inventoryId + "?",
                ButtonType.YES, ButtonType.NO
        );
        confirm.setHeaderText("Confirm Delete");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            try {
                boolean isDeleted = inventoryBO.deleteInventory(inventoryId);
                if (isDeleted) {
                    loadAllInventory();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Failed to delete inventory!").show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Error occurred while deleting!").show();
            }
        }
    }

    public void btnReportOnAction(ActionEvent actionEvent) {

    }
}
