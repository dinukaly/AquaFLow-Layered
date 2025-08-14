package lk.wms.aquaflow.controller.modal;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lk.wms.aquaflow.bo.custom.BOFactory;
import lk.wms.aquaflow.bo.custom.OfficersBO;
import lk.wms.aquaflow.bo.custom.VillageBO;
import lk.wms.aquaflow.dto.OfficerDTO;
import lk.wms.aquaflow.dto.VillageDTO;
import lk.wms.aquaflow.util.AlertUtil;
import lk.wms.aquaflow.util.InputValidator;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddVillageModalController implements Initializable {
    private final String[] districts = {
            "Ampara", "Anuradhapura", "Badulla", "Batticaloa", "Colombo", "Galle", "Gampaha", "Hambantota", "Jaffna",
            "Kalutara", "Kandy", "Kegalle", "Kilinochchi", "Kurunegala", "Mannar", "Matale", "Matara", "Monaragala",
            "Mullaitivu", "Nuwara Eliya", "Polonnaruwa", "Puttalam", "Ratnapura", "Trincomalee", "Vavuniya"
    };

  //  private final OfficerModel officerModel = new OfficerModel();
    //private final VillageModel villageModel = new VillageModel();
    private final OfficersBO officersBO = (OfficersBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.OFFICER);
    private final VillageBO villageBO = (VillageBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.VILLAGE);


    public ComboBox<String> cmbDistrict;
    public ComboBox<OfficerDTO> cmbOfficerName;
    public TextField txtName;
    public TextField txtPopulation;
    public TextField txtWaterRequirement;
    public TextField txtArea;
    public Button addButton;
    public Button btnDiscard;
    public Text lblVillageId;
    public Text titleLabel;
    private Mode currentMode;
    private String updateVillageId;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cmbDistrict.getItems().addAll(districts);
        try {
            loadNextId();
            ObservableList<OfficerDTO> officerList = FXCollections.observableArrayList(officersBO.getAllOfficers());
            cmbOfficerName.setItems(officerList);
            cmbOfficerName.setPromptText("Select Officer");
        } catch (Exception e) {
            AlertUtil.showError("Initialization error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadNextId() throws SQLException, ClassNotFoundException {
        String nextVillageId = villageBO.generateVillageId();
        lblVillageId.setText(nextVillageId);
    }

    public void setModeAndData(Mode mode, VillageDTO village) {
        this.currentMode = mode;

        if (mode == Mode.UPDATE && village != null) {
            txtName.setText(village.getVillageName());
            txtPopulation.setText(String.valueOf(village.getPopulation()));
            txtWaterRequirement.setText(String.valueOf(village.getWaterRequirement()));
            txtArea.setText(String.valueOf(village.getArea()));
            cmbDistrict.setValue(village.getDistrict());
            updateVillageId = village.getVillageId();

            // Set officer selection
            cmbOfficerName.getItems().stream()
                    .filter(officer -> officer.getOfficerId().equals(village.getOfficerId()))
                    .findFirst()
                    .ifPresent(officer -> cmbOfficerName.setValue(officer));
        }

        updateModalUI();
    }

    private void updateModalUI() {
        if (currentMode == Mode.ADD) {
            titleLabel.setText("Add New Village");
            addButton.setText("Save");
        } else {
            titleLabel.setText("Update Village");
            addButton.setText("Update");
        }
    }

    public void addButtonOnAction(ActionEvent actionEvent) {
        if (!validateInputs()) return;
        if (!validateForm()) return;


        try {
            String villageId = (currentMode == Mode.ADD) ? lblVillageId.getText() : updateVillageId;
            String villageName = txtName.getText();
            int population = Integer.parseInt(txtPopulation.getText());
            double waterRequirement = Double.parseDouble(txtWaterRequirement.getText());
            double area = Double.parseDouble(txtArea.getText());
            String district = cmbDistrict.getValue();
            String officerId = cmbOfficerName.getValue().getOfficerId();

            VillageDTO villageDTO = new VillageDTO(villageId, villageName, population, waterRequirement, area, district, officerId);

            boolean result = (currentMode == Mode.ADD)
                    ? villageBO.addVillage(villageDTO)
                    : villageBO.updateVillage(villageDTO);

            if (result) {
                AlertUtil.showSuccess(currentMode == Mode.ADD ?
                        "Village has been added successfully" :
                        "Village has been updated successfully");
                ((Stage) addButton.getScene().getWindow()).close();
            } else {
                AlertUtil.showFailure(currentMode == Mode.ADD ?
                        "Village has not been added" :
                        "Village has not been updated");
            }

        } catch (NumberFormatException e) {
            AlertUtil.showError("Please enter valid numbers for Population, Water Requirement and Area!");
        } catch (Exception e) {
            AlertUtil.showError("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean validateInputs() {
        return InputValidator.isNotEmpty(txtName, "Village name") &&
                InputValidator.isNotEmpty(txtPopulation, "Population") &&
                InputValidator.isNotEmpty(txtWaterRequirement, "Water requirement") &&
                InputValidator.isNotEmpty(txtArea, "Area") &&
                InputValidator.isSelected(cmbDistrict, "district") &&
                InputValidator.isSelected(cmbOfficerName, "officer");
    }

    public void btnDiscardOnAction(ActionEvent actionEvent) {
        ((Stage) btnDiscard.getScene().getWindow()).close();
    }

    private boolean validateForm() {
        boolean valid = true;

        // Clear all previous styles
        InputValidator.clearStyle(txtName, txtPopulation, txtWaterRequirement, txtArea, cmbDistrict, cmbOfficerName);

        if (!InputValidator.isNotEmpty(txtName, "Village name")) {
            InputValidator.markInvalid(txtName);
            valid = false;
        }

        if (!InputValidator.isNotEmpty(txtPopulation, "Population")) {
            InputValidator.markInvalid(txtPopulation);
            valid = false;
        }

        if (!InputValidator.isNotEmpty(txtWaterRequirement, "Water requirement")) {
            InputValidator.markInvalid(txtWaterRequirement);
            valid = false;
        }

        if (!InputValidator.isNotEmpty(txtArea, "Area")) {
            InputValidator.markInvalid(txtArea);
            valid = false;
        }

        if (!InputValidator.isSelected(cmbDistrict, "district")) {
            InputValidator.markInvalid(cmbDistrict);
            valid = false;
        }

        if (!InputValidator.isSelected(cmbOfficerName, "officer")) {
            InputValidator.markInvalid(cmbOfficerName);
            valid = false;
        }

        if (!valid) {
            AlertUtil.showError("Please correct the highlighted fields.");
        }

        return valid;
    }

    public enum Mode {
        ADD, UPDATE
    }


}
