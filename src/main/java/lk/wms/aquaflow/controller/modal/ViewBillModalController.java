package lk.wms.aquaflow.controller.modal;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import lk.wms.aquaflow.bo.custom.BOFactory;
import lk.wms.aquaflow.bo.custom.BillingBO;
import lk.wms.aquaflow.bo.custom.HouseholdsBO;
import lk.wms.aquaflow.dto.BillDTO;
import lk.wms.aquaflow.dto.HouseholdDTO;
import lk.wms.aquaflow.dto.custom.CustomBillDTO;
import lk.wms.aquaflow.entity.custom.CustomBill;
import lk.wms.aquaflow.util.AlertUtil;
import lk.wms.aquaflow.util.EmailUtil;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ViewBillModalController {
    @FXML
    public Label lblBillId;

    @FXML
    public Label lblHouseholdName;

    @FXML
    public Label lblVillageName;

    @FXML
    public Label lblBillingDate;

    @FXML
    public Label lblDueDate;

    @FXML
    public Label lblStatus;

    @FXML
    public ListView<String> billDetailsList;

    @FXML
    public Button sendReminderBtn;

    @FXML
    public Button closeButton;

    //private BillModel billModel = new BillModel();
    //private HouseholdModel householdModel = new HouseholdModel();
    private BillingBO billBO = (BillingBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.BILLING);
    private HouseholdsBO householdsBO = (HouseholdsBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.HOUSEHOLD);
    private CustomBillDTO currentBill;

    public void initialize() {
        // Initially hide the reminder button
        sendReminderBtn.setVisible(false);
    }

    public void loadBillData(String billId) {
        try {
            currentBill = billBO.getBillById(billId);

            if (currentBill != null) {
                // Set basic bill info
                lblBillId.setText(currentBill.getBillId());
                lblHouseholdName.setText(currentBill.getHouseholdName());
                lblVillageName.setText(currentBill.getVillageName());
                lblBillingDate.setText(formatDate(currentBill.getBillDate()));
                lblDueDate.setText(formatDate(currentBill.getDueDate()));
                lblStatus.setText(currentBill.getStatus());

                // Set status color
                switch (currentBill.getStatus()) {
                    case "Paid":
                        lblStatus.setStyle("-fx-text-fill: green;");
                        break;
                    case "Unpaid":
                        lblStatus.setStyle("-fx-text-fill: orange;");
                        break;
                    default:
                        lblStatus.setStyle("-fx-text-fill: black;");
                }

                // Add details to list view
                billDetailsList.getItems().clear();
                billDetailsList.getItems().add("Total Units: " + currentBill.getTotalAmountOfUnits() + " units");
                billDetailsList.getItems().add("Cost Per Unit: Rs. " + currentBill.getCostPerUnit());
                billDetailsList.getItems().add("Total Cost: Rs. " + currentBill.getTotalCost());

                // Check if bill is overdue
                if (isOverdue(currentBill.getDueDate()) && "Unpaid".equals(currentBill.getStatus())) {
                    sendReminderBtn.setVisible(true);
                    lblStatus.setText("Overdue");
                    lblStatus.setStyle("-fx-text-fill: red;");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.showError("Failed to load bill data: " + e.getMessage());
        }
    }

    @FXML
    public void handleSendReminder() {
        try {
            // Get household email
            HouseholdDTO household = householdsBO.getHouseholdById(getHouseholdIdFromBill());

            if (household != null && household.getEmail() != null && !household.getEmail().isEmpty()) {
                // Send reminder email
                String subject = "REMINDER: Overdue Water Bill - " + currentBill.getBillId();
                String body = "Dear " + household.getOwnerName() + ",\n\n" +
                        "This is a reminder that your water bill (ID: " + currentBill.getBillId() + ") is overdue.\n\n" +
                        "Bill Details:\n" +
                        "- Total Units: " + currentBill.getTotalAmountOfUnits() + " units\n" +
                        "- Cost Per Unit: Rs. " + currentBill.getCostPerUnit() + "\n" +
                        "- Total Amount Due: Rs. " + currentBill.getTotalCost() + "\n" +
                        "- Due Date: " + formatDate(currentBill.getDueDate()) + " (OVERDUE)\n\n" +
                        "Please make your payment as soon as possible to avoid any service interruptions.\n\n" +
                        "Thank you,\n" +
                        "AquaFlow Water Management System";

                EmailUtil.sendEmail(household.getEmail(), subject, body);
                AlertUtil.showSuccess("Payment reminder has been sent to " + household.getEmail());
            } else {
                AlertUtil.showError("Could not send reminder. No email address found for this household.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.showError("Failed to send reminder: " + e.getMessage());
        }
    }

    @FXML
    public void handleClose() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    // TODO: move this to custome method
    private String getHouseholdIdFromBill() throws SQLException, ClassNotFoundException {
        return billBO.getHouseholdIdFromBill(currentBill.getBillId());
        // Get consumption record to find household ID
//        String consumptionId = currentBill.getConsumptionId();
//        ResultSet resultSet = lk.aquaFlow.util.CrudUtil.execute(
//                "SELECT house_id FROM consumption WHERE consumption_id = ?",
//                consumptionId
//        );
//
//        if (resultSet.next()) {
//            return resultSet.getString("house_id");
//        }
    }

    private boolean isOverdue(String dueDateStr) {
        try {
            LocalDate dueDate = LocalDate.parse(dueDateStr);
            LocalDate today = LocalDate.now();
            return today.isAfter(dueDate);
        } catch (Exception e) {
            return false;
        }
    }

    private String formatDate(String dateStr) {
        try {
            LocalDate date = LocalDate.parse(dateStr);
            return date.format(DateTimeFormatter.ofPattern("dd MMM yyyy"));
        } catch (Exception e) {
            return dateStr;
        }
    }
}