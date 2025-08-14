package lk.wms.aquaflow.controller.modal;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lk.wms.aquaflow.bo.custom.BOFactory;
import lk.wms.aquaflow.bo.custom.VillageBO;
import lk.wms.aquaflow.bo.custom.WaterDistributionBO;
import lk.wms.aquaflow.dto.VillageDTO;
import lk.wms.aquaflow.dto.WaterDistributionDTO;
import lk.wms.aquaflow.util.AlertUtil;
import lk.wms.aquaflow.util.InputValidator;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AddDistributionModalController implements Initializable {
   // private final WaterDistributionModel distributionModel = new WaterDistributionModel();
  //  private final VillageModel villageModel = new VillageModel();

    private final WaterDistributionBO waterDistributionBO = (WaterDistributionBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.DISTRIBUTION);
    private final VillageBO villageBO = (VillageBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.VILLAGE);

    public TextField txtTotalAllocation;
    public Button addButton;
    public Button btnDiscard;
    public Text lblDistribution;
    public ComboBox<VillageDTO> cmbVillageName;
    public DatePicker dateDueDate;
    public Text titleLabel;

    private Mode currentMode;
    private String updateDistributionId;



    private void loadNextId() throws SQLException, ClassNotFoundException {
        String nextDistributionId = waterDistributionBO.generateWaterDistributionId();
        lblDistribution.setText(nextDistributionId);
    }

    public void loadComboBox() throws SQLException, ClassNotFoundException {
        ObservableList<VillageDTO> villageList = FXCollections.observableArrayList(villageBO.getAllVillages());
        cmbVillageName.setItems(villageList);
        cmbVillageName.setPromptText("Select Village");
    }

    public void setModeAndData(Mode mode, WaterDistributionDTO distribution) {
        this.currentMode = mode;

        System.out.println(mode);

        if (mode == Mode.UPDATE && distribution != null) {
            txtTotalAllocation.setText(String.valueOf(distribution.getTotalAllocation()));
            dateDueDate.setValue(distribution.getDistributionDate());
            updateDistributionId = distribution.getDistributionId();

            // Set village selection
            cmbVillageName.getItems().stream()
                    .filter(village -> village.getVillageId().equals(distribution.getVillageId()))
                    .findFirst()
                    .ifPresent(village -> cmbVillageName.setValue(village));
        }

        updateModalUI();
    }

    private void updateModalUI() {
        if (currentMode == Mode.ADD) {
            titleLabel.setText("Add New Distribution");
            addButton.setText("Save");
        } else {
            titleLabel.setText("Update Distribution");
            addButton.setText("Update");
        }
    }

    public void addButtonOnAction(ActionEvent actionEvent) {
        if (!validateInputs()) return;
        if (!validateForm()) return;

        try {
            String distributionId = (currentMode == Mode.ADD) ? lblDistribution.getText() : updateDistributionId;
            String villageId = cmbVillageName.getValue().getVillageId();
            double totalAllocation = Double.parseDouble(txtTotalAllocation.getText());
            LocalDate dueDate = dateDueDate.getValue();

            WaterDistributionDTO distributionDTO = new WaterDistributionDTO(
                    distributionId,
                    totalAllocation,
                    "Scheduled",
                    totalAllocation,
                    dueDate,
                    villageId
            );

            boolean result = (currentMode == Mode.ADD)
                    ? waterDistributionBO.addWaterDistribution(distributionDTO)
                    : waterDistributionBO.updateWaterDistribution(distributionDTO);

            if (result) {
                AlertUtil.showSuccess(currentMode == Mode.ADD ?
                        "Distribution has been added successfully" :
                        "Distribution has been updated successfully");
                ((Stage) addButton.getScene().getWindow()).close();
            } else {
                AlertUtil.showFailure(currentMode == Mode.ADD ?
                        "Distribution has not been added" :
                        "Distribution has not been updated");
            }

        } catch (NumberFormatException e) {
            AlertUtil.showError("Please enter valid numbers for Total Allocation!");
        } catch (Exception e) {
            AlertUtil.showError("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean validateInputs() {
        return InputValidator.isNotEmpty(txtTotalAllocation, "Total allocation") &&
                InputValidator.isSelected(cmbVillageName, "village") &&
                InputValidator.isDateSelected(dateDueDate, "due date");
    }

    public void btnDiscardOnAction(ActionEvent actionEvent) {
        ((Stage) btnDiscard.getScene().getWindow()).close();
    }

    private boolean validateForm() {
        InputValidator.clearStyle(txtTotalAllocation, cmbVillageName, dateDueDate);

        boolean isValid = true;

        if (!InputValidator.isNotEmpty(txtTotalAllocation, "Total allocation")) {
            AlertUtil.showError("Total allocation amount is required");
            isValid = false;
        } else if (!InputValidator.isNumeric(txtTotalAllocation, "Total allocation")) {
            AlertUtil.showError("Total allocation must be a valid number");
            isValid = false;
        } else if (Double.parseDouble(txtTotalAllocation.getText()) <= 0) {
            AlertUtil.showError("Total allocation must be greater than 0");
            isValid = false;
        }

        if (!InputValidator.isSelected(cmbVillageName, "Village")) {
            AlertUtil.showError("Please select a village");
            isValid = false;
        }

        if (!InputValidator.isDateSelected(dateDueDate, "Due date")) {
            AlertUtil.showError("Please select a due date");
            isValid = false;
        }

        return isValid;
    }

    public enum Mode {
        ADD, UPDATE
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            loadNextId();
            loadComboBox();
            dateDueDate.setValue(LocalDate.now());
        } catch (Exception e) {
            AlertUtil.showError("Initialization error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}