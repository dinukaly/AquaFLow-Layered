package lk.wms.aquaflow.controller.modal;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lk.wms.aquaflow.bo.custom.BOFactory;
import lk.wms.aquaflow.bo.custom.HouseholdsBO;
import lk.wms.aquaflow.bo.custom.VillageBO;
import lk.wms.aquaflow.dto.HouseholdDTO;
import lk.wms.aquaflow.dto.VillageDTO;
import lk.wms.aquaflow.util.AlertUtil;
import lk.wms.aquaflow.util.InputValidator;


import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddHouseholdModalController implements Initializable {
    public ComboBox<VillageDTO> cmbVillageName;
    public TextField txtHouseNo;
    public TextField txtOwnerName;
    public TextField txtAddress;
    public TextField txtNumOfMembers;
    public TextField txtEmail;

    public Button addButton;
    public Button btnDiscard;
    public Text lblHouseholdId;
    public Text titleLabel;

    //HouseholdModel householdModel = new HouseholdModel();
   // VillageModel villageModel = new VillageModel();
    private final HouseholdsBO householdsBO = (HouseholdsBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.HOUSEHOLD);
    private final VillageBO villageBO = (VillageBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.VILLAGE);

    public enum Mode {
        ADD, UPDATE
    }
    private Mode currentMode;
    private String updateHouseholdId;

    public void addButtonOnAction(ActionEvent actionEvent) {
        if (!validateInputs()) {
            return;
        }

        try {
            String householdId = (currentMode == Mode.ADD) ? lblHouseholdId.getText() : updateHouseholdId;
            String ownerName = txtOwnerName.getText();
            String address = txtAddress.getText();
            int noOfMembers = Integer.parseInt(txtNumOfMembers.getText());
            String email = txtEmail.getText();

            String villageId = cmbVillageName.getValue().getVillageId();

            HouseholdDTO householdDTO = new HouseholdDTO(householdId, ownerName, address,noOfMembers,email,villageId);

            boolean result;
            if (currentMode == Mode.ADD) {
                result = householdsBO.addHousehold(householdDTO);
            } else {
                result = householdsBO.updateHousehold(householdDTO);
            }

            Alert alert = new Alert(result ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
            alert.setTitle(result ? "Success" : "Failed");
            alert.setHeaderText(result ?
                    (currentMode == Mode.ADD ? "Household has been added successfully" : "Household has been updated successfully") :
                    (currentMode == Mode.ADD ? "Household has not been added" : "Household has not been updated"));
            alert.showAndWait();

            if (result) {
                ((Stage) addButton.getScene().getWindow()).close();
            }

        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Please enter valid numbers for Member Count and Water Usage!").show();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "An error occurred: " + e.getMessage()).show();
            e.printStackTrace();
        }
    }

    private boolean validateInputs() {
        InputValidator.clearStyle(txtOwnerName, txtAddress, txtNumOfMembers, txtEmail, cmbVillageName);

        boolean isValid = true;

        if (!InputValidator.isNotEmpty(txtOwnerName, "Owner name")) {
            AlertUtil.showError("Owner name cannot be empty!");
            isValid = false;
        } else if (!InputValidator.isAlphabetic(txtOwnerName, "Owner name")) {
            AlertUtil.showError("Owner name can only contain letters");
            isValid = false;
        }

        if (!InputValidator.isNotEmpty(txtAddress, "Address")) {
            AlertUtil.showError("Address cannot be empty!");
            isValid = false;
        }

        if (!InputValidator.isNotEmpty(txtNumOfMembers, "Member count")) {
            AlertUtil.showError("Member count cannot be empty!");
            isValid = false;
        } else if (!InputValidator.isNumeric(txtNumOfMembers, "Member count")) {
            AlertUtil.showError("Member count must be a number");
            isValid = false;
        }

        if (!InputValidator.isValidEmail(txtEmail)) {
            AlertUtil.showError("Please enter a valid email address");
            isValid = false;
        }

        if (!InputValidator.isSelected(cmbVillageName, "Village")) {
            AlertUtil.showError("Please select a village!");
            isValid = false;
        }

        return isValid;
    }

    public void btnDiscardOnAction(ActionEvent actionEvent) {
        ((Stage) btnDiscard.getScene().getWindow()).close();
    }

    private void loadNextId() throws SQLException, ClassNotFoundException {
        String nextHouseholdId = householdsBO.generateHouseholdId();
        lblHouseholdId.setText(nextHouseholdId);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            loadNextId();
            ObservableList<VillageDTO> villageList = FXCollections.observableArrayList(villageBO.getAllVillages());
            cmbVillageName.setItems(villageList);
            cmbVillageName.setPromptText("Select Village");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setModeAndData(Mode mode, HouseholdDTO household) {
        this.currentMode = mode;

        if (mode == Mode.UPDATE && household != null) {
//            txtHouseNo.setText(household.getHouseId());
            txtOwnerName.setText(household.getOwnerName());
            txtAddress.setText(household.getAddress());
            txtNumOfMembers.setText(String.valueOf(household.getNoOfMembers()));
            txtEmail.setText(household.getEmail());
            updateHouseholdId = household.getHouseId();

            // Find and select the correct village
            cmbVillageName.getItems().stream()
                    .filter(village -> village.getVillageId().equals(household.getVillageId()))
                    .findFirst()
                    .ifPresent(village -> cmbVillageName.setValue(village));
        }

        updateModalUI();
    }

    private void updateModalUI() {
        if (currentMode == Mode.ADD) {
            titleLabel.setText("Add New Household");
            addButton.setText("Save");
        } else if (currentMode == Mode.UPDATE) {
            titleLabel.setText("Update Household");
            addButton.setText("Update");
        }
    }

    public void cmbVillageNameOnAction(ActionEvent actionEvent) {

    }
}
