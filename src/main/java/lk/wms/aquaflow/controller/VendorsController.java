package lk.wms.aquaflow.controller;


import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lk.wms.aquaflow.bo.custom.BOFactory;
import lk.wms.aquaflow.bo.custom.VendorBO;
import lk.wms.aquaflow.controller.modal.AddVendorModalController;
import lk.wms.aquaflow.dto.SupplierDTO;
import lk.wms.aquaflow.util.TableActionCell;
import lk.wms.aquaflow.view.tm.SupplierTM;


import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class VendorsController implements Initializable {


    public TableView<SupplierTM> vendorTableView;
    public TableColumn<SupplierTM, String> vendorIdCol;
    public TableColumn <SupplierTM, String> nameCol;
    public TableColumn <SupplierTM, String> addressCol;
    public TableColumn <SupplierTM, String> emailCol;
    public TableColumn <SupplierTM, String> contactCol;
    public TableColumn <SupplierTM, String> actionCol;

   // private final VendorModel vendorModel = new VendorModel();
    private final VendorBO vendorBO = (VendorBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.VENDOR);


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("VendorController: initialize() called");
        vendorIdCol.setCellValueFactory(new PropertyValueFactory<>("supplierId"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        contactCol.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        actionCol.setCellFactory(TableActionCell.create(
                this::handleEdit,
                this::handleDelete
        ));




        try {
            loadAllVendors();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void loadAllVendors() throws SQLException, ClassNotFoundException {
        vendorTableView.setItems(FXCollections.observableArrayList(
                vendorBO.getAllVendors().stream()
                        .map(supplierDTO->new SupplierTM(
                                supplierDTO.getSupplierId(),
                                supplierDTO.getName(),
                                supplierDTO.getAddress(),
                                supplierDTO.getEmail(),
                                supplierDTO.getTel()
                        )).toList()
        ));
    }


    public void txtSearch(ActionEvent actionEvent) {
        //to do
    }

    public void addButtonOnAction(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lk/aquaflowwms/view/modalViews/addVendor-Modal.fxml"));
            AnchorPane loadModal = loader.load();

            AddVendorModalController controller = loader.getController();
            controller.setModeAndData(AddVendorModalController.Mode.ADD, null);
            Stage stage = new Stage();
            stage.setScene(new Scene(loadModal));

            // 👇 Block the main stage
            stage.initModality(Modality.WINDOW_MODAL);

            // 👇 Set the owner (main window)
            stage.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());

            // 👇 Show the modal and wait until it’s closed
            stage.showAndWait();
            loadAllVendors();
        }catch (Exception e){
            e.printStackTrace();
        }
    }



    public void btnReportsOnAction(ActionEvent actionEvent) {
        //to do
    }


    private void handleEdit(SupplierTM supplierTM) {
        SupplierDTO supplierDTO = new SupplierDTO(
                supplierTM.getSupplierId(),
                supplierTM.getName(),
                supplierTM.getAddress(),
                supplierTM.getEmail(),
                supplierTM.getTel()
        );

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lk/aquaflowwms/view/modalViews/addVendor-Modal.fxml"));
            AnchorPane modalRoot = loader.load();

            AddVendorModalController modalController = loader.getController();
            modalController.setModeAndData(AddVendorModalController.Mode.UPDATE, supplierDTO); // Use DTO here

            Stage stage = new Stage();
            stage.setScene(new Scene(modalRoot));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(vendorTableView.getScene().getWindow());
            stage.showAndWait();

            loadAllVendors();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleDelete(SupplierTM supplierTM) {
        String supplierId = supplierTM.getSupplierId(); // okay to pass just the ID here

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to delete Vendor ID: " + supplierId + "?",
                ButtonType.YES, ButtonType.NO
        );
        confirm.setHeaderText("Confirm Delete");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            try {
                boolean isDeleted = vendorBO.deleteVendor(supplierId);
                if (isDeleted) {
                    loadAllVendors();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Failed to delete vendor!").show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Error occurred while deleting!").show();
            }
        }
    }

}

