package lk.wms.aquaflow.controller.modal;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lk.wms.aquaflow.bo.custom.BOFactory;
import lk.wms.aquaflow.bo.custom.VillageBO;
import lk.wms.aquaflow.bo.custom.WaterAllocationBO;
import lk.wms.aquaflow.bo.custom.WaterSourceBO;
import lk.wms.aquaflow.dto.VillageDTO;
import lk.wms.aquaflow.dto.WaterAllocationDTO;
import lk.wms.aquaflow.dto.WaterSourceDTO;
import lk.wms.aquaflow.util.AlertUtil;
import lk.wms.aquaflow.util.InputValidator;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AddSourceAllocationModalController implements Initializable {

    public ComboBox<VillageDTO> cmbVillageName;
    public ComboBox<WaterSourceDTO> cmbWatersourceName;
    public TextField txtAllocateAmount;
    public Text lblAllocationId;
    public DatePicker allocationDate;

  //  private WaterAllocationModel allocationModel = new WaterAllocationModel();
  //  private VillageModel villageModel = new VillageModel();
  //  private WaterSourceModel waterSourceModel = new WaterSourceModel();

    private final WaterSourceBO waterSourceBO = (WaterSourceBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.WATER_SOURCE);
    private final WaterAllocationBO allocationBO = (WaterAllocationBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.WATER_ALLOCATION);
    private final VillageBO villageBO = (VillageBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.VILLAGE);
    private Mode currentMode;
    private String updateAllocationId;


    public void btnDiscardOnAction(ActionEvent actionEvent) {
//        ((Stage) btnDiscard.getScene().getWindow()).close();
    }

    private String loadNextId() {
        try {
            String nextId = allocationBO.generateWaterAllocationId();
            lblAllocationId.setText(nextId);

            return nextId;
        } catch (Exception e) {
            System.err.println("ID Generation Error: " + e.getMessage());
            lblAllocationId.setText("ERROR");
            throw new RuntimeException("ID generation failed", e);
        }
    }



    public void setModeAndData(Mode mode, WaterAllocationDTO allocation) {
        this.currentMode = mode;

        if (mode == Mode.UPDATE && allocation != null) {
            // Find and select the village
            cmbVillageName.getItems().stream()
                    .filter(village -> village.getVillageId().equals(allocation.getVillageId()))
                    .findFirst()
                    .ifPresent(village -> cmbVillageName.setValue(village));

            // Find and select the water source
            cmbWatersourceName.getItems().stream()
                    .filter(source -> source.getWaterSourceId().equals(allocation.getWaterSourceId()))
                    .findFirst()
                    .ifPresent(source -> cmbWatersourceName.setValue(source));

//            txtAllocateAmount.setText(String.valueOf(allocation.getAllocateAmount()));
            updateAllocationId = allocation.getAllocationId();
        }

        updateModalUI();
    }



    private void updateModalUI() {
        if (currentMode == Mode.ADD) {
            // call again to load next id if its empty
            if (lblAllocationId.getText() == null || lblAllocationId.getText().trim().isEmpty() || lblAllocationId.getText().equals("Loading...")) {
                loadNextId();
            }
        } else if (currentMode == Mode.UPDATE) {
            // For update mode, show the existing allocation ID
            if (updateAllocationId != null) {
                lblAllocationId.setText(updateAllocationId);
            }
        }
    }

    public void addButtonOnAction(ActionEvent actionEvent) {

        try {
            // Get ID from label first
            String allocationId = lblAllocationId.getText();
            if (allocationId == null || allocationId.isEmpty() || allocationId.equals("ERROR")) {
                allocationId = loadNextId();
            }

            // Verify combo boxes have values
            if (!validateInputs()) {
                return;
            }

            WaterAllocationDTO allocationDTO = new WaterAllocationDTO(
                    allocationId,
                    Double.parseDouble(txtAllocateAmount.getText()),
                    cmbWatersourceName.getValue().getWaterSourceId(),
                    cmbVillageName.getValue().getVillageId(),
                    allocationDate.getValue()
            );
            // Perform allocation

            boolean result = allocationBO.addWaterAllocation(allocationDTO);

            if (result) {
                new Alert(Alert.AlertType.INFORMATION, "Water allocation successful!").show();
            }

        } catch (Exception e) {
            System.err.println("[FINAL ERROR] " + e.getMessage());
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Operation failed: " + e.getMessage()).show();
        }
    }


    private boolean validateInputs() {
        InputValidator.clearStyle(txtAllocateAmount, cmbVillageName, cmbWatersourceName, allocationDate);

        boolean isValid = true;

        if (!InputValidator.isSelected(cmbVillageName, "Village")) {
            AlertUtil.showError("Please select a village");
            isValid = false;
        }

        if (!InputValidator.isSelected(cmbWatersourceName, "Water source")) {
            AlertUtil.showError("Please select a water source");
            isValid = false;
        }

        if (!InputValidator.isNotEmpty(txtAllocateAmount, "Allocation amount")) {
            AlertUtil.showError("Allocation amount cannot be empty");
            isValid = false;
        } else if (!InputValidator.isNumeric(txtAllocateAmount, "Allocation amount")) {
            AlertUtil.showError("Allocation amount must be a number");
            isValid = false;
        } else if (Double.parseDouble(txtAllocateAmount.getText()) <= 0) {
            AlertUtil.showError("Allocation amount must be greater than 0");
            isValid = false;
        }

        if (!InputValidator.isDateSelected(allocationDate, "Allocation date")) {
            AlertUtil.showError("Please select an allocation date");
            isValid = false;
        }

        return isValid;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            loadNextId();

            // Load villages
            ObservableList<VillageDTO> villageList = FXCollections.observableArrayList(villageBO.getAllVillages());
            cmbVillageName.setItems(villageList);
            cmbVillageName.setPromptText("Select Village");

            // Load water sources
            ObservableList<WaterSourceDTO> waterSourceList = FXCollections.observableArrayList(waterSourceBO.getAllWaterSources());
            cmbWatersourceName.setItems(waterSourceList);
            cmbWatersourceName.setPromptText("Select Water Source");

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Failed to load data: " + e.getMessage()).show();
            e.printStackTrace();
        }
    }

    public enum Mode {
        ADD, UPDATE
    }
}
