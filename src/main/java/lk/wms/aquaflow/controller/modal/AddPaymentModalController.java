package lk.wms.aquaflow.controller.modal;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lk.wms.aquaflow.bo.custom.BOFactory;
import lk.wms.aquaflow.bo.custom.BillingBO;
import lk.wms.aquaflow.dto.BillDTO;
import lk.wms.aquaflow.util.AlertUtil;
import lk.wms.aquaflow.util.InputValidator;


import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class AddPaymentModalController {
    @FXML
    public Text lblPaymentId;
    @FXML
    public DatePicker datePaidDate;
    @FXML
    public TextField txtAmount;
    @FXML
    public ComboBox<String> cmbPaymentMethod;
    @FXML
    public ComboBox<String> cmbBillId;
    @FXML
    public Button addButton;
    @FXML
    public Button btnDiscard;

   // private BillModel billModel = new BillModel();
   private final BillingBO billingBO = (BillingBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.BILLING);
    private ObservableList<BillDTO> unpaidBills = FXCollections.observableArrayList();

    public void initialize(ArrayList<BillDTO> unpaidBills) {
        this.unpaidBills.setAll(unpaidBills);

        // Setup payment method options
        cmbPaymentMethod.setItems(FXCollections.observableArrayList(
                "Cash", "Credit Card", "Bank Transfer", "Cheque"));

        // Set current date
        datePaidDate.setValue(LocalDate.now());

        // Generate payment ID
        lblPaymentId.setText("P" + System.currentTimeMillis() % 10000);

        // Setup bill ID combo box
        ObservableList<String> billIds = unpaidBills.stream()
                .map(BillDTO::getBillId)
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
        cmbBillId.setItems(billIds);

        // Make amount field read-only
        txtAmount.setEditable(false);

        // Listen for bill selection to update amount
        cmbBillId.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                BillDTO selectedBill = unpaidBills.stream()
                        .filter(b -> b.getBillId().equals(newVal))
                        .findFirst()
                        .orElse(null);
                if (selectedBill != null) {
                    txtAmount.setText(selectedBill.getTotalCost());
                }
            }
        });
    }

    @FXML
    public void addButtonOnAction(ActionEvent actionEvent) {
        InputValidator.clearStyle(txtAmount, cmbPaymentMethod, cmbBillId);

        if (!validateInputs()) {
            return;
        }

        try {
            String billId = cmbBillId.getValue();
            double amount = Double.parseDouble(txtAmount.getText());
            String paymentMethod = cmbPaymentMethod.getValue();
            LocalDate paidDate = datePaidDate.getValue();

            boolean result = billingBO.recordPayment(
                    billId,
                    amount,
                    paidDate,
                    paymentMethod
            );
            if (result) {
                AlertUtil.showSuccess("Payment recorded successfully!");
                ((Stage) txtAmount.getScene().getWindow()).close();
            } else {
                AlertUtil.showError("Failed to record payment");
            }
        } catch (NumberFormatException e) {
            AlertUtil.showError("Invalid amount format");
        } catch (RuntimeException e) {
            e.printStackTrace();
            AlertUtil.showError("Database error: " + e.getMessage());
        }
    }

    private boolean validateInputs() {
        boolean isValid = true;

        if (!InputValidator.isSelected(cmbBillId, "Bill")) {
            AlertUtil.showError("Please select a bill");
            isValid = false;
        }

        if (!InputValidator.isNotEmpty(txtAmount, "Amount")) {
            AlertUtil.showError("Amount cannot be empty");
            isValid = false;
        } else if (!InputValidator.isNumeric(txtAmount, "Amount")) {
            AlertUtil.showError("Amount must be a valid number");
            isValid = false;
        } else if (Double.parseDouble(txtAmount.getText()) <= 0) {
            AlertUtil.showError("Amount must be greater than 0");
            isValid = false;
        }

        if (!InputValidator.isSelected(cmbPaymentMethod, "Payment method")) {
            AlertUtil.showError("Please select a payment method");
            isValid = false;
        }

        return isValid;
    }

    @FXML
    public void btnDiscardOnAction(ActionEvent actionEvent) {
        ((Stage) txtAmount.getScene().getWindow()).close();
    }
}

