package lk.wms.aquaflow.controller.modal;


import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lk.wms.aquaflow.bo.custom.BOFactory;
import lk.wms.aquaflow.bo.custom.OfficersBO;
import lk.wms.aquaflow.dto.OfficerDTO;
import lk.wms.aquaflow.util.AlertUtil;
import lk.wms.aquaflow.util.InputValidator;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddOfficerModalController implements Initializable {
    public Text titleLabel;
    public Button addButton;

    public enum Mode{
        ADD,UPDATE
    }
    private Mode currentMode;
    private String updateOfficerId;

    public Text lblEmployerId;
    public TextField txtName;
    public TextField txtAddress;
    public TextField txtContactNumber;
    public TextField txtEmail;

    //TM - ListView model data


    private OfficersBO officersBO = (OfficersBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.OFFICER);

    public void addButtonOnAction(ActionEvent actionEvent) {
        InputValidator.clearStyle(txtName, txtAddress, txtEmail, txtContactNumber);

        if (!validateInputs()) {
            return;
        }

        String officerId = (currentMode == Mode.ADD) ? lblEmployerId.getText() : updateOfficerId;
        String name = txtName.getText();
        String address = txtAddress.getText();
        String email = txtEmail.getText();
        String contactNumber = txtContactNumber.getText();

        OfficerDTO officerDTO = new OfficerDTO(officerId, name, address, email, contactNumber);

        try {
            boolean result;
            if (currentMode == Mode.ADD) {
                result = officersBO.addOfficer(officerDTO);
                AlertUtil.showSuccess("Officer added successfully!");
            } else {
                result = officersBO.updateOfficer(officerDTO);
                AlertUtil.showSuccess("Officer updated successfully!");
            }

            if (result) {
                ((Stage) txtName.getScene().getWindow()).close();
            } else {
                AlertUtil.showError("Operation failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.showError("Database error: " + e.getMessage());
        }
    }

    private boolean validateInputs() {
        boolean isValid = true;

        if (!InputValidator.isNotEmpty(txtName, "Officer name")) {
            isValid = false;
        } else if (!InputValidator.isAlphabetic(txtName, "Officer name")) {
            isValid = false;
        }

        if (!InputValidator.isNotEmpty(txtAddress, "Address")) {
            isValid = false;
        }

        if (!InputValidator.isNotEmpty(txtEmail, "Email")) {
            isValid = false;
        } else if (!InputValidator.isValidEmail(txtEmail)) {
            isValid = false;
        }

        if (!InputValidator.isNotEmpty(txtContactNumber, "Contact number")) {
            isValid = false;
        } else if (!InputValidator.isValidPhone(txtContactNumber)) {
            isValid = false;
        }

        return isValid;
    }

    public void btnDiscardOnAction(ActionEvent actionEvent) {

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
        String nextOfficerId = officersBO.generateOfficerId();
        lblEmployerId.setText(nextOfficerId);
    }

    public void setModeAndData(Mode mode, OfficerDTO officer) {
        this.currentMode = mode;

        if (mode == Mode.UPDATE && officer != null) {
            txtName.setText(officer.getName());
            txtEmail.setText(officer.getEmail());
            txtAddress.setText(officer.getAddress());
            txtContactNumber.setText(officer.getTelephone());
            updateOfficerId = officer.getOfficerId();
        }

        updateModalUI();
    }
    private void updateModalUI() {
        if (currentMode == Mode.ADD) {
            titleLabel.setText("Add New Employer");     // set this in FXML
            addButton.setText("Save");
        } else if (currentMode == Mode.UPDATE) {
            titleLabel.setText("Update Employer");
            addButton.setText("Update");
        }
    }
}

