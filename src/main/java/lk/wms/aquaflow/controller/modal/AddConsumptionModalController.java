package lk.wms.aquaflow.controller.modal;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lk.wms.aquaflow.bo.custom.BOFactory;
import lk.wms.aquaflow.bo.custom.ConsumptionsBO;
import lk.wms.aquaflow.bo.custom.HouseholdsBO;
import lk.wms.aquaflow.dto.ConsumptionDTO;
import lk.wms.aquaflow.dto.HouseholdDTO;
import lk.wms.aquaflow.util.AlertUtil;
import lk.wms.aquaflow.util.InputValidator;


import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddConsumptionModalController {
    @FXML
    public Text lblConsumptionId;

    @FXML
    public ComboBox<String> cmbHouseId;

    @FXML
    public TextField txtTotalUnits;

    @FXML
    public DatePicker dateStart;

    @FXML
    public DatePicker dateEnd;

    @FXML
    public Button addButton;

    @FXML
    public Text titleLabel;

   // private ConsumptionModel consumptionModel = new ConsumptionModel();
    //private HouseholdModel householdModel = new HouseholdModel();
   private final ConsumptionsBO consumptionsBO = (ConsumptionsBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.CONSUMPTION);
    private final HouseholdsBO householdsBO = (HouseholdsBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.HOUSEHOLD);

    // Map to store household IDs by display name
    private Map<String, String> householdMap = new HashMap<>();

    // For edit mode
    private boolean isEditMode = false;
    private String consumptionIdToEdit;

    // Callback for refreshing the main view
    private Runnable refreshCallback;

    public enum Mode {
        ADD, EDIT
    }

    public void initialize() {
        // Set default dates
        dateStart.setValue(LocalDate.now().minusDays(30)); // Default to 30 days ago
        dateEnd.setValue(LocalDate.now()); // Default to today

        // Load households
        loadHouseholds();

        // Generate next consumption ID
        generateNextConsumptionId();
    }

    private void loadHouseholds() {
        try {
            ArrayList<HouseholdDTO> households = householdsBO.getAllHouseholds();

            for (HouseholdDTO household : households) {
                String displayName = household.getOwnerName() + " (" + household.getHouseId() + ")";
                householdMap.put(displayName, household.getHouseId());
                cmbHouseId.getItems().add(displayName);
            }
        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.showError("Failed to load household data: " + e.getMessage());
        }
    }

    private void generateNextConsumptionId() {
        try {
            String nextId = consumptionsBO.generateConsumptionId();
            lblConsumptionId.setText(nextId);
        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.showError("Failed to generate consumption ID: " + e.getMessage());
        }
    }

    public void setModeAndData(Mode mode, ConsumptionDTO dto) {
        if (mode == Mode.EDIT && dto != null) {
            isEditMode = true;
            consumptionIdToEdit = dto.getConsumptionId();

            // Set form values
            lblConsumptionId.setText(dto.getConsumptionId());
            txtTotalUnits.setText(dto.getAmountOfUnits());

            // Set dates
            if (dto.getStartDate() != null && !dto.getStartDate().isEmpty()) {
                dateStart.setValue(LocalDate.parse(dto.getStartDate()));
            }

            if (dto.getEndDate() != null && !dto.getEndDate().isEmpty()) {
                dateEnd.setValue(LocalDate.parse(dto.getEndDate()));
            }

            // Set household
            String houseId = dto.getHouseId();
            for (Map.Entry<String, String> entry : householdMap.entrySet()) {
                if (entry.getValue().equals(houseId)) {
                    cmbHouseId.setValue(entry.getKey());
                    break;
                }
            }

            // Update UI for edit mode
            titleLabel.setText("Edit Consumption");
            addButton.setText("Update");
        } else {
            isEditMode = false;
            titleLabel.setText("Add Consumption");
            addButton.setText("Save");
        }
    }

    public void setRefreshCallback(Runnable callback) {
        this.refreshCallback = callback;
    }

    @FXML
    public void addButtonOnAction(ActionEvent actionEvent) {
        if (validateInputs()) {
            try {
                String consumptionId = lblConsumptionId.getText();
                String amountOfUnits = txtTotalUnits.getText();
                String startDate = dateStart.getValue().toString();
                String endDate = dateEnd.getValue().toString();
                String selectedHousehold = cmbHouseId.getValue();
                String houseId = householdMap.get(selectedHousehold);

                ConsumptionDTO dto = new ConsumptionDTO(
                        consumptionId,
                        amountOfUnits,
                        startDate,
                        endDate,
                        houseId
                );

                boolean success;
                if (isEditMode) {
                    success = consumptionsBO.updateConsumption(dto);
                    if (success) {
                        AlertUtil.showSuccess("Consumption record has been updated successfully.");
                    } else {
                        AlertUtil.showFailure("Failed to update consumption record.");
                    }
                } else {
                    success = consumptionsBO.addConsumption(dto);
                    if (success) {
                        AlertUtil.showSuccess("New consumption record has been added successfully.");
                    } else {
                        AlertUtil.showFailure("Failed to add consumption record.");
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

    private boolean validateInputs() {
        // Clear previous validation styles
        InputValidator.clearStyle(cmbHouseId, txtTotalUnits, dateStart, dateEnd);

        boolean isValid = true;

        // Validate household selection
        if (!InputValidator.isSelected(cmbHouseId, "household")) {
            InputValidator.markInvalid(cmbHouseId);
            isValid = false;
        }

        // Validate units
        if (!InputValidator.isNotEmpty(txtTotalUnits, "Amount of units")) {
            InputValidator.markInvalid(txtTotalUnits);
            isValid = false;
        } else if (!InputValidator.isNumeric(txtTotalUnits, "Amount of units")) {
            InputValidator.markInvalid(txtTotalUnits);
            isValid = false;
        }

        // Validate start date
        if (!InputValidator.isDateSelected(dateStart, "start date")) {
            InputValidator.markInvalid(dateStart);
            isValid = false;
        }

        // Validate end date
        if (!InputValidator.isDateSelected(dateEnd, "end date")) {
            InputValidator.markInvalid(dateEnd);
            isValid = false;
        }

        // Validate date range
        if (dateStart.getValue() != null && dateEnd.getValue() != null) {
            if (dateEnd.getValue().isBefore(dateStart.getValue())) {
                AlertUtil.showError("End date cannot be before start date.");
                InputValidator.markInvalid(dateStart);
                InputValidator.markInvalid(dateEnd);
                isValid = false;
            }
        }

        return isValid;
    }

    private void closeStage() {
        Stage stage = (Stage) addButton.getScene().getWindow();
        stage.close();
    }
}
