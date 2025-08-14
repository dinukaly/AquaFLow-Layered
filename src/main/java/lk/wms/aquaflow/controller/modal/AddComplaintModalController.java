package lk.wms.aquaflow.controller.modal;



import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lk.wms.aquaflow.bo.custom.BOFactory;
import lk.wms.aquaflow.bo.custom.ComplaintsBO;
import lk.wms.aquaflow.bo.custom.HouseholdsBO;
import lk.wms.aquaflow.dto.ComplaintDTO;
import lk.wms.aquaflow.dto.HouseholdDTO;
import lk.wms.aquaflow.util.AlertUtil;
import lk.wms.aquaflow.util.InputValidator;


import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddComplaintModalController {

    @FXML
    public Text lblComplaintId;

    @FXML
    public ComboBox<String> cmbOwnerName;

    @FXML
    public TextArea textareaDescription;

    @FXML
    public DatePicker datePicker;

    @FXML
    public Button addButton;

   // private ComplaintModel complaintModel = new ComplaintModel();
   // private HouseholdModel householdModel = new HouseholdModel();
   private final HouseholdsBO householdsBO = (HouseholdsBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.HOUSEHOLD);
   private final ComplaintsBO complaintBO = (ComplaintsBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.COMPLAINTS);
    // Map to store household IDs by owner name
    private Map<String, String> householdMap = new HashMap<>();

    // For edit mode
    private boolean isEditMode = false;
    private String complaintIdToEdit;
    private String currentStatus = "Pending"; // Default status

    // Callback for refreshing the main view
    private Runnable refreshCallback;

    public void initialize() throws SQLException, ClassNotFoundException {
        // Set current date as default
        datePicker.setValue(LocalDate.now());

        // Load household owners into combo box
        loadHouseholdOwners();

        // Generate next complaint ID
        generateNextComplaintId();
    }

    private void loadHouseholdOwners() {
        try {
            ArrayList<HouseholdDTO> households = householdsBO.getAllHouseholds();

            for (HouseholdDTO household : households) {
                String ownerName = household.getOwnerName() + " (" + household.getEmail() + ")";
                householdMap.put(ownerName, household.getHouseId());
                cmbOwnerName.getItems().add(ownerName);
            }
        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.showError("Failed to load household data: " + e.getMessage());
        }
    }

    private void generateNextComplaintId() {
        try {
            String nextId = complaintBO.getNextComplaintId();
            lblComplaintId.setText(nextId);
        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.showError("Failed to generate complaint ID: " + e.getMessage());
        }
    }

    public void setAddMode() {
        isEditMode = false;
        addButton.setText("Save");
        currentStatus = "Pending"; // Default for new complaints
    }

    public void setComplaintForEdit(ComplaintDTO complaintDTO) {
        isEditMode = true;
        complaintIdToEdit = complaintDTO.getComplaintId();

        // Set values in the form
        lblComplaintId.setText(complaintDTO.getComplaintId());
        textareaDescription.setText(complaintDTO.getDescription());

        // Set date
        try {
            LocalDate date = LocalDate.parse(complaintDTO.getDate());
            datePicker.setValue(date);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Store the current status
        currentStatus = complaintDTO.getStatus();

        // Set household owner
        for (Map.Entry<String, String> entry : householdMap.entrySet()) {
            if (entry.getValue().equals(complaintDTO.getHouseId())) {
                cmbOwnerName.setValue(entry.getKey());
                break;
            }
        }

        addButton.setText("Update");
    }

    public void setRefreshCallback(Runnable callback) {
        this.refreshCallback = callback;
    }

    @FXML
    public void addButtonOnAction(ActionEvent actionEvent) {
        if (validateInputs()) {
            try {
                String complaintId = lblComplaintId.getText();
                String description = textareaDescription.getText();
                String date = datePicker.getValue().toString();
                String selectedOwner = cmbOwnerName.getValue();
                String houseId = householdMap.get(selectedOwner);

                ComplaintDTO complaintDTO = new ComplaintDTO(
                        complaintId,
                        date,
                        description,
                        currentStatus, // Use the current status (Pending for new, or existing for edits)
                        houseId
                );

                boolean success;
                if (isEditMode) {
                    success = complaintBO.updateComplaint(complaintDTO);
                    if (success) {
                        AlertUtil.showSuccess("Complaint has been updated successfully.");
                    } else {
                        AlertUtil.showFailure("Failed to update complaint.");
                    }
                } else {
                    success = complaintBO.addComplaint(complaintDTO);
                    if (success) {
                        AlertUtil.showSuccess("New complaint has been added successfully.");
                    } else {
                        AlertUtil.showFailure("Failed to add complaint.");
                    }
                }

                if (success && refreshCallback != null) {
                    refreshCallback.run();
                    closeStage();
                }

            } catch (Exception e) {
                e.printStackTrace();
                AlertUtil.showError("Operation Failed: " + e.getMessage());
            }
        }
    }

    @FXML
    public void btnDiscardOnAction(ActionEvent actionEvent) {
        closeStage();
    }

    @FXML
    public void cmbOwnerNameOnAction(ActionEvent actionEvent) {
        // This method can be used if you need to perform any action when an owner is selected
    }

    private boolean validateInputs() {
        InputValidator.clearStyle(cmbOwnerName, textareaDescription, datePicker);

        boolean isValid = true;

        if (!InputValidator.isSelected(cmbOwnerName, "Household owner")) {
            isValid = false;
        }

        if (!InputValidator.isNotEmpty(textareaDescription, "Complaint description")) {
            isValid = false;
        } else if (textareaDescription.getText().trim().length() < 10) {
            AlertUtil.showError("Description must be at least 10 characters");
            InputValidator.markInvalid(textareaDescription);
            isValid = false;
        } else if (textareaDescription.getText().trim().length() > 500) {
            AlertUtil.showError("Description cannot exceed 500 characters");
            InputValidator.markInvalid(textareaDescription);
            isValid = false;
        }

        if (!InputValidator.isDateSelected(datePicker, "Complaint date")) {
            isValid = false;
        } else if (datePicker.getValue().isAfter(LocalDate.now())) {
            AlertUtil.showError("Complaint date cannot be in the future");
            InputValidator.markInvalid(datePicker);
            isValid = false;
        }

        return isValid;
    }

    private void closeStage() {
        Stage stage = (Stage) addButton.getScene().getWindow();
        stage.close();
    }
}

