package lk.wms.aquaflow.controller.modal;



import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lk.wms.aquaflow.bo.custom.BOFactory;
import lk.wms.aquaflow.bo.custom.VendorBO;
import lk.wms.aquaflow.dto.SupplierDTO;
import lk.wms.aquaflow.util.AlertUtil;
import lk.wms.aquaflow.util.InputValidator;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddVendorModalController implements Initializable {

    public Text titleLabel;
    public Button addButton;

    public enum Mode{
        ADD,UPDATE
    }
    private AddVendorModalController.Mode currentMode;
    private String updateVendorId;

    public Text lblVendorId;
    public TextField txtName;
    public TextField txtAddress;
    public TextField txtContactNumber;
    public TextField txtEmail;

    //TM - ListView model data



    //private final VendorModel vendorModal = new VendorModel();
    private final VendorBO vendorBO = (VendorBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.VENDOR);

    public void addButtonOnAction(ActionEvent actionEvent) {
        // Clear previous validation styles
        InputValidator.clearStyle(txtName, txtAddress, txtEmail, txtContactNumber);

        // Validate all fields with detailed feedback
        boolean isValid = true;

        if (!InputValidator.isNotEmpty(txtName, "Name")) {
            AlertUtil.showError("Vendor name cannot be empty!");
            isValid = false;
        } else if (!InputValidator.isAlphabetic(txtName, "Name")) {
            AlertUtil.showError("Name can only contain letters");
            isValid = false;
        }

        if (!InputValidator.isNotEmpty(txtAddress, "Address")) {
            AlertUtil.showError("Address cannot be empty!");
            isValid = false;
        }

        if (!InputValidator.isValidEmail(txtEmail)) {
            AlertUtil.showError("Please enter a valid email address\nExample: example@domain.com");
            isValid = false;
        }

        if (!InputValidator.isValidPhone(txtContactNumber)) {
            AlertUtil.showError("Please enter a valid phone number\nFormats: 0771234567 or +94771234567");
            isValid = false;
        }

        if (!isValid) {
            return;
        }

        String supplierId = (currentMode == Mode.ADD) ? lblVendorId.getText() : updateVendorId;
        String name = txtName.getText();
        String address = txtAddress.getText();
        String email = txtEmail.getText();
        String contactNumber = txtContactNumber.getText();

        SupplierDTO supplierDTO = new SupplierDTO(supplierId, name, address, email, contactNumber);

        try {
            boolean result;
            if (currentMode == Mode.ADD) {
                result = vendorBO.addVendor(supplierDTO);
                AlertUtil.showSuccess("Vendor added successfully!");
            } else {
                result = vendorBO.updateVendor(supplierDTO);
                AlertUtil.showSuccess("Vendor updated successfully!");
            }

            if (result) {
                ((Stage) txtName.getScene().getWindow()).close();
            } else {
                AlertUtil.showError("Failed to save vendor");
            }
        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.showError("Database error: " + e.getMessage());
        }
    }

    public void btnDiscardOnAction(ActionEvent actionEvent) {
        // Clear all input fields
        txtName.clear();
        txtAddress.clear();
        txtEmail.clear();
        txtContactNumber.clear();

        // Clear any validation styles
        InputValidator.clearStyle(txtName, txtAddress, txtEmail, txtContactNumber);

        // If in ADD mode, regenerate vendor ID
        if (currentMode == Mode.ADD) {
            try {
                String nextId = vendorBO.generateVendorId();
                lblVendorId.setText(nextId);
            } catch (Exception e) {
                e.printStackTrace();
                AlertUtil.showError("Failed to generate new vendor ID");
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            loadNextId();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void loadNextId() throws SQLException, ClassNotFoundException {
        String nextVendorId = vendorBO.generateVendorId();
        lblVendorId.setText(nextVendorId);
    }

    public void setModeAndData(AddVendorModalController.Mode mode, SupplierDTO supplierDTO) {
        this.currentMode = mode;

        if (mode == AddVendorModalController.Mode.UPDATE && supplierDTO != null) {
            txtName.setText(supplierDTO.getName());
            txtEmail.setText(supplierDTO.getEmail());
            txtAddress.setText(supplierDTO.getAddress());
            txtContactNumber.setText(supplierDTO.getTel());
            updateVendorId = supplierDTO.getSupplierId();
        }

        updateModalUI();
    }
    private void updateModalUI() {
        if (currentMode == AddVendorModalController.Mode.ADD) {
            titleLabel.setText("Add New Vendor");     // set this in FXML
            addButton.setText("Save");
        } else if (currentMode == AddVendorModalController.Mode.UPDATE) {
            titleLabel.setText("Update Vendor");
            addButton.setText("Update");
        }
    }

    private boolean validateInputs() {
        // Clear previous validation styles
        InputValidator.clearStyle(txtName, txtAddress, txtEmail, txtContactNumber);

        // Validate all fields with detailed feedback
        boolean isValid = true;

        if (!InputValidator.isNotEmpty(txtName, "Name")) {
            AlertUtil.showError("Vendor name cannot be empty!");
            isValid = false;
        } else if (!InputValidator.isAlphabetic(txtName, "Name")) {
            AlertUtil.showError("Name can only contain letters");
            isValid = false;
        }

        if (!InputValidator.isNotEmpty(txtAddress, "Address")) {
            AlertUtil.showError("Address cannot be empty!");
            isValid = false;
        }

        if (!InputValidator.isValidEmail(txtEmail)) {
            AlertUtil.showError("Please enter a valid email address\nExample: example@domain.com");
            isValid = false;
        }

        if (!InputValidator.isValidPhone(txtContactNumber)) {
            AlertUtil.showError("Please enter a valid phone number\nFormats: 0771234567 or +94771234567");
            isValid = false;
        }

        return isValid;
    }
}
