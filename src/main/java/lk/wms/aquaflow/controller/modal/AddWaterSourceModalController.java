package lk.wms.aquaflow.controller.modal;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lk.wms.aquaflow.bo.custom.BOFactory;
import lk.wms.aquaflow.bo.custom.WaterSourceBO;
import lk.wms.aquaflow.dto.WaterSourceDTO;
import lk.wms.aquaflow.util.AlertUtil;
import lk.wms.aquaflow.util.InputValidator;


import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddWaterSourceModalController implements Initializable {
    public TextField txtLocation;
    public TextField txtCapacity;
    public Button addButton;
    public Button btnDiscard;
    public Text lblWaterSourceId;
    public ComboBox<String> cmbSourceType;
    public ComboBox<String> cmbStatus;
    public Text titleLabel;
    // Add new field for source name
    public TextField txtSourceName;

    String[] status = {"Active", "Inactive", "Maintenance", "Contaminated", "Low", "Dry"};
    String[] sourceType = {"Open Well", "Tube Well", "Rain Water", "Piped Water", "Other"};

  //  WaterSourceModel waterSourceModel = new WaterSourceModel();
  private final WaterSourceBO waterSourceBO = (WaterSourceBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.WATER_SOURCE);
    public enum Mode {
        ADD, EDIT
    }

    private Mode mode;
    private WaterSourceDTO editingSource;

    /**
     * Sets the mode (ADD or EDIT) and loads data if provided.
     * @param mode ADD or EDIT
     * @param data WaterSourceDTO to edit, or null for ADD
     */
    public void setModeAndData(Mode mode, WaterSourceDTO data) {
        this.mode = mode;
        this.editingSource = data;

        if (mode == Mode.ADD) {
            titleLabel.setText("Add New Water Source");
            addButton.setText("Save");
            clearFields();
            try {
                loadNextId();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (mode == Mode.EDIT && data != null) {
            titleLabel.setText("Edit Water Source");
            addButton.setText("Update");
            lblWaterSourceId.setText(data.getWaterSourceId());
            txtSourceName.setText(data.getSourceName());
            cmbSourceType.setValue(data.getSourceType());
            txtLocation.setText(data.getLocation());
            txtCapacity.setText(String.valueOf(data.getCapacity()));
            cmbStatus.setValue(data.getStatus());
        }
    }

    private void clearFields() {
        lblWaterSourceId.setText("");
        txtSourceName.clear();
        cmbSourceType.setValue(null);
        txtLocation.clear();
        txtCapacity.clear();
        cmbStatus.setValue(null);
    }

    public void addButtonOnAction(ActionEvent actionEvent) {
        if (!validateInputs()) {
            return;
        }
        String id = lblWaterSourceId.getText();
        String sourceName = txtSourceName.getText();
        String type = cmbSourceType.getValue();
        String location = txtLocation.getText();
        double capacity = Double.parseDouble(txtCapacity.getText());
        String status = cmbStatus.getValue();
        WaterSourceDTO dto = new WaterSourceDTO(id, sourceName, type, location, capacity, status);
        boolean success;
        if (mode == Mode.ADD) {
            try {
                success = waterSourceBO.addWaterSource(dto);
                if (success) {
                    new Alert(Alert.AlertType.INFORMATION, "Water source added successfully!").show();
                    ((Stage) addButton.getScene().getWindow()).close();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Failed to add water source.").show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).show();
            }
        } else if (mode == Mode.EDIT) {
            success = waterSourceBO.updateWaterSource(dto);
            if (success) {
                new Alert(Alert.AlertType.INFORMATION, "Water source updated successfully!").show();
                ((Stage) addButton.getScene().getWindow()).close();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to update water source.").show();
            }
        }
    }

    private boolean validateInputs() {
        InputValidator.clearStyle(txtLocation, txtCapacity, txtSourceName, cmbSourceType, cmbStatus);

        boolean isValid = true;

        if (!InputValidator.isNotEmpty(txtSourceName, "Source name")) {
            AlertUtil.showError("Water source name is required");
            isValid = false;
        }

        if (!InputValidator.isNotEmpty(txtLocation, "Location")) {
            AlertUtil.showError("Location is required");
            isValid = false;
        }

        if (!InputValidator.isNotEmpty(txtCapacity, "Capacity")) {
            AlertUtil.showError("Capacity is required");
            isValid = false;
        } else if (!InputValidator.isNumeric(txtCapacity, "Capacity")) {
            AlertUtil.showError("Capacity must be a valid number");
            isValid = false;
        }

        if (!InputValidator.isSelected(cmbSourceType, "Source type")) {
            AlertUtil.showError("Please select a source type");
            isValid = false;
        }

        if (!InputValidator.isSelected(cmbStatus, "Status")) {
            AlertUtil.showError("Please select a status");
            isValid = false;
        }

        return isValid;
    }

    public void btnDiscardOnAction(ActionEvent actionEvent) {
        ((Stage) btnDiscard.getScene().getWindow()).close();
    }

    private void loadNextId() throws SQLException, ClassNotFoundException {
        lblWaterSourceId.setText(waterSourceBO.generateWaterSourceId());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> statusList = FXCollections.observableArrayList(status);
        ObservableList<String> sourceTypeList = FXCollections.observableArrayList(sourceType);
        cmbStatus.setItems(statusList);
        cmbSourceType.setItems(sourceTypeList);

        setModeAndData(Mode.ADD, null);
    }

    public void cmbSourceTypeOnAction(ActionEvent actionEvent) {}
    public void cmbStatusOnAction(ActionEvent actionEvent) {}
}