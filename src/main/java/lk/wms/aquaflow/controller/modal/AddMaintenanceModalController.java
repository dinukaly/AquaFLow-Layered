package lk.wms.aquaflow.controller.modal;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.wms.aquaflow.bo.custom.BOFactory;
import lk.wms.aquaflow.bo.custom.MaintenanceBO;
import lk.wms.aquaflow.dto.InventoryMaintenanceDTO;
import lk.wms.aquaflow.dto.WaterMaintenanceDTO;
import lk.wms.aquaflow.view.tm.InventoryEntry;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AddMaintenanceModalController {
    public Label lblMaintenance;
    public ComboBox<String> cmbVillageName;
    public TextArea textareaDescription;
    public ComboBox<String> cmbStatus;
    public ComboBox<String> cmbInventoryType;
    public TextField txtQuantity;
    public TableView<InventoryEntry> tblInventory;
    public TableColumn<InventoryEntry, String> colType;
    public TableColumn<InventoryEntry, Integer> colQty;
    public TableColumn<InventoryEntry, Double> colUnitPrice;
    public TableColumn<InventoryEntry, Double> colTotal;
    public TableColumn<InventoryEntry, String> colDate;

    public ObservableList<InventoryEntry> inventoryList = FXCollections.observableArrayList();

    private MaintenanceBO maintenanceBO = (MaintenanceBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.MAINTENANCE);

    public void initialize() {
        try {
            // Generate maintenance ID
            lblMaintenance.setText(maintenanceBO.generateMaintenanceId());

            // Load village names
            loadVillages();

            // Load status options
            cmbStatus.getItems().addAll("Scheduled", "In Progress", "Completed", "Cancelled");
            cmbStatus.getSelectionModel().selectFirst();

            // Load inventory types with prices
            loadInventoryTypes();

            // Setup table columns
            colType.setCellValueFactory(new PropertyValueFactory<>("type"));
            colQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
            colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
            colTotal.setCellValueFactory(new PropertyValueFactory<>("totalCost"));
            colDate.setCellValueFactory(new PropertyValueFactory<>("dateUsed"));

            tblInventory.setItems(inventoryList);

        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to initialize form: " + e.getMessage()).show();
        }
    }

    // TODO : Move this to a custom method
    private void loadVillages() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT village_id, village_name FROM village");
        if (result == null) {
            System.out.println("[DEBUG] Village query returned null result");
            return;
        }

        boolean hasResults = false;
        while (result.next()) {
            hasResults = true;
            cmbVillageName.getItems().add(result.getString(1) + " - " + result.getString(2));
        }

        if (!hasResults) {
            System.out.println("[DEBUG] No villages found in database");
            new Alert(Alert.AlertType.WARNING, "No villages found in database").show();
        }
    }

    // TODO : move this to inventory BO
    private void loadInventoryTypes() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT inventory_id, type, unit_price FROM inventory WHERE quantity > 0");
        if (result == null) {
            System.out.println("[DEBUG] Inventory query returned null result");
            return;
        }

        boolean hasResults = false;
        while (result.next()) {
            hasResults = true;
            cmbInventoryType.getItems().add(
                    result.getString(1) + " - " +
                            result.getString(2) + " (" +
                            result.getDouble(3) + ")"
            );
        }

        if (!hasResults) {
            System.out.println("[DEBUG] No inventory items found in database");
            new Alert(Alert.AlertType.WARNING, "No available inventory items found").show();
        }
    }

    @FXML
    void addButtonOnAction(ActionEvent actionEvent) {
        try {
            String maintenanceId = lblMaintenance.getText();
            String villageId = cmbVillageName.getValue().split(" - ")[0];
            String description = textareaDescription.getText();
            String status = cmbStatus.getValue();

            // Convert inventory list to DTOs
            List<InventoryMaintenanceDTO> inventoryDTOs = new ArrayList<>();
            for (InventoryEntry entry : inventoryList) {
                String inventoryId = entry.getType().split(" - ")[0];
                inventoryDTOs.add(new InventoryMaintenanceDTO(
                        inventoryId,
                        maintenanceId,
                        entry.getQuantity(),
                        entry.getDateUsed()
                ));
            }

            // Calculate total cost
            double totalCost = maintenanceBO.calculateTotalCost(inventoryDTOs);

            // Create maintenance DTO
            WaterMaintenanceDTO dto = new WaterMaintenanceDTO();
            dto.setMaintenanceId(maintenanceId);
            dto.setDescription(description);
            dto.setMaintenanceDate(LocalDate.now());
            dto.setCost(totalCost);
            dto.setStatus(status);
            dto.setVillageId(villageId);
            dto.setInventoryItems(inventoryDTOs);

            // Save to database
            boolean isSaved = maintenanceBO.addMaintenance(dto);

            if (isSaved) {
                new Alert(Alert.AlertType.INFORMATION, "Maintenance saved successfully!").show();
                clearForm();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to save maintenance").show();
            }

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Database error: " + e.getMessage()).show();
        }
    }

    @FXML
    void btnDiscardOnAction(ActionEvent actionEvent) {
        clearForm();
    }

    @FXML
    void cmbVillageNameOnAction(ActionEvent actionEvent) {
    }

    @FXML
    void cmbStatusOnAction(ActionEvent actionEvent) {
    }

    @FXML
    void btnAddInventoryOnAction(ActionEvent actionEvent) {
        try {
            String inventoryItem = cmbInventoryType.getValue();
            int quantity = Integer.parseInt(txtQuantity.getText());

            if (inventoryItem == null || inventoryItem.isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Please select an inventory item").show();
                return;
            }

            if (quantity <= 0) {
                new Alert(Alert.AlertType.WARNING, "Quantity must be positive").show();
                return;
            }

            // Extract unit price from the inventory string (format: "ID - Type (price)")
            String priceStr = inventoryItem.substring(inventoryItem.lastIndexOf("(") + 1, inventoryItem.lastIndexOf(")"));
            double unitPrice = Double.parseDouble(priceStr);

            // Add to table
            inventoryList.add(new InventoryEntry(
                    inventoryItem,
                    quantity,
                    unitPrice,
                    LocalDate.now()
            ));

            // Clear selection
            cmbInventoryType.getSelectionModel().clearSelection();
            txtQuantity.clear();

        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid quantity format").show();
        }
    }

    private void clearForm() {
        lblMaintenance.setText("");
        cmbVillageName.getSelectionModel().clearSelection();
        textareaDescription.clear();
        cmbStatus.getSelectionModel().selectFirst();
        inventoryList.clear();

        try {
            // Regenerate maintenance ID
            lblMaintenance.setText(maintenanceBO.generateMaintenanceId());
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Failed to generate new ID: " + e.getMessage()).show();
        }
    }
}

