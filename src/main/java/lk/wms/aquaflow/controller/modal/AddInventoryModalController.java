package lk.wms.aquaflow.controller.modal;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lk.wms.aquaflow.bo.custom.BOFactory;
import lk.wms.aquaflow.bo.custom.InventoryBO;
import lk.wms.aquaflow.bo.custom.VendorBO;
import lk.wms.aquaflow.dto.InventoryDTO;
import lk.wms.aquaflow.dto.SupplierDTO;
import lk.wms.aquaflow.util.AlertUtil;
import lk.wms.aquaflow.util.InputValidator;


import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddInventoryModalController implements Initializable {


    private final String[] types = {
            "Water Tanks",
            "Pipes (PVC, GI)",
            "Valves and Fittings",
            "Water Meters",
            "Pumps (Submersible, Surface)",
            "Filters and Purifiers",
            "Chlorine and Treatment Chemicals",
            "Storage Drums and Containers",
            "Solar Panels (for pump systems)",
            "Water Quality Testing Kits",
            "Maintenance Tools",
            "Spare Parts (for pumps, valves)",
            "Safety Gear (gloves, boots, helmets)",
            "Electric Panels and Switches",
            "Flow Sensors and Pressure Gauges"
    };

    public Text titleLabel;
    public TextField txtQuantity;
    public Text lblInventoryId;
    public ComboBox<String> cmbType;
    public ComboBox<SupplierDTO> cmbVendorName;
    public Button addButton;
    public Button btnDiscard;
    public TextField txtUnitPrice;

    //    private Mode currentMode;
    public enum Mode{
        ADD,UPDATE
    }
    private AddInventoryModalController.Mode currentMode;
    private String updateInventoryId;


  // private final VendorModel vendorModel = new VendorModel();
   // private final InventoryModel inventoryModel = new InventoryModel();
  private final InventoryBO inventoryBO = (InventoryBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.INVENTORY);
    private final VendorBO vendorBO = (VendorBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.VENDOR);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cmbType.getItems().addAll(types);
        try {
            loadNextId();
            ObservableList<SupplierDTO> supplierList = FXCollections.observableArrayList(vendorBO.getAllVendors());
            cmbVendorName.setItems(supplierList);
            cmbVendorName.setPromptText("Select Vendor");
        } catch (Exception e) {
            AlertUtil.showError("Initialization error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadNextId() throws SQLException, ClassNotFoundException {
        String nextInventoryId = inventoryBO.generateInventoryId();
        lblInventoryId.setText(nextInventoryId);
    }

    public void addButtonOnAction(ActionEvent actionEvent) {

        if (!validateInputs()) return;
        if (!validateForm()) return;


        try {
            String inventoryId = (currentMode == AddInventoryModalController.Mode.ADD) ? lblInventoryId.getText() : updateInventoryId;
            String type = cmbType.getValue();
            String quantity = txtQuantity.getText();
            double unitPrice = Double.parseDouble(txtUnitPrice.getText());
            String supplierId = cmbVendorName.getValue().getSupplierId();
            System.out.println("vendorrrr"+cmbVendorName.getValue().getSupplierId());

            InventoryDTO inventoryDTO = new InventoryDTO(inventoryId,type, quantity,unitPrice,supplierId);

            boolean result = (currentMode == AddInventoryModalController.Mode.ADD)
                    ? inventoryBO.addInventory(inventoryDTO)
                    : inventoryBO.updateInventory(inventoryDTO);

            if (result) {
                AlertUtil.showSuccess(currentMode == AddInventoryModalController.Mode.ADD ?
                        "Inventory has been added successfully" :
                        "Inventory has been updated successfully");
                ((Stage) addButton.getScene().getWindow()).close();
            } else {
                AlertUtil.showFailure(currentMode == AddInventoryModalController.Mode.ADD ?
                        "Inventory has not been added" :
                        "Inventory has not been updated");
            }

        } catch (NumberFormatException e) {
            AlertUtil.showError("Please enter valid numbers");
        } catch (Exception e) {
            AlertUtil.showError("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void btnDiscardOnAction(ActionEvent actionEvent) {
        ((Stage) btnDiscard.getScene().getWindow()).close();

    }

    public void cmbTypeOnAction(ActionEvent actionEvent) {
    }

    public void cmbVendorNameOnAction(ActionEvent actionEvent) {

    }

    public void setModeAndData(Mode mode, InventoryDTO inventoryDTO) {
        this.currentMode = mode;

        if (mode == AddInventoryModalController.Mode.UPDATE && inventoryDTO != null) {
            cmbType.setValue(inventoryDTO.getType());
            txtQuantity.setText(inventoryDTO.getQuantity());
            txtUnitPrice.setText(String.valueOf(inventoryDTO.getUnitPrice()));
            updateInventoryId = inventoryDTO.getInventoryId();

            // Set officer selection
            cmbVendorName.getItems().stream()
                    .filter(supplier -> supplier.getSupplierId().equals(inventoryDTO.getSupplierId()))
                    .findFirst()
                    .ifPresent(supplier -> cmbVendorName.setValue(supplier));
        }

        updateModalUI();
    }

    private void updateModalUI() {
        if (currentMode == AddInventoryModalController.Mode.ADD) {
            titleLabel.setText("Add New Inventory");
            addButton.setText("Save");
        } else {
            titleLabel.setText("Update Inventory");
            addButton.setText("Update");
        }
    }

    private boolean validateInputs() {
        InputValidator.clearStyle(txtQuantity, txtUnitPrice, cmbType, cmbVendorName);

        boolean isValid = true;

        if (!InputValidator.isSelected(cmbType, "Inventory type")) {
            AlertUtil.showError("Please select an inventory type");
            isValid = false;
        }

        if (!InputValidator.isNotEmpty(txtQuantity, "Quantity")) {
            AlertUtil.showError("Quantity cannot be empty");
            isValid = false;
        } else if (!InputValidator.isNumeric(txtQuantity, "Quantity")) {
            AlertUtil.showError("Quantity must be a number");
            isValid = false;
        }

        if (!InputValidator.isNotEmpty(txtUnitPrice, "Unit price")) {
            AlertUtil.showError("Unit price cannot be empty");
            isValid = false;
        } else if (!InputValidator.isNumeric(txtUnitPrice, "Unit price")) {
            AlertUtil.showError("Unit price must be a number");
            isValid = false;
        }

        if (!InputValidator.isSelected(cmbVendorName, "Vendor")) {
            AlertUtil.showError("Please select a vendor");
            isValid = false;
        }

        return isValid;
    }

    private boolean validateForm() {
        boolean valid = true;

        // Clear all previous styles
        InputValidator.clearStyle(txtQuantity, cmbType, cmbVendorName);

        if (!InputValidator.isNotEmpty(txtQuantity, "Quantity")) {
            InputValidator.markInvalid(txtQuantity);
            valid = false;
        }


        if (!InputValidator.isSelected(cmbType, "type")) {
            InputValidator.markInvalid(cmbType);
            valid = false;
        }

        if (!InputValidator.isSelected(cmbVendorName, "supplier")) {
            InputValidator.markInvalid(cmbVendorName);
            valid = false;
        }

        if (!valid) {
            AlertUtil.showError("Please correct the highlighted fields.");
        }

        return valid;
    }



}

