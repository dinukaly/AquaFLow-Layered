package lk.wms.aquaflow.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lk.wms.aquaflow.bo.custom.BOFactory;
import lk.wms.aquaflow.bo.custom.BillingBO;
import lk.wms.aquaflow.controller.modal.AddPaymentModalController;
import lk.wms.aquaflow.controller.modal.ViewBillModalController;
import lk.wms.aquaflow.dto.BillDTO;
import lk.wms.aquaflow.util.AlertUtil;
import lk.wms.aquaflow.view.tm.BillTM;


import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

public class BillingsController implements Initializable {
    @FXML
    public Text lblTotalBilledThisMonth;
    @FXML
    public Text lblTotalCollected;
    @FXML
    public Text lblPendingPayment;
    @FXML
    public Text lblOverduePayment;
    @FXML
    public Button btnAllBills;
    @FXML
    public Button btnPaid;
    @FXML
    public Button btnPending;
    @FXML
    public Button btnOverDue;
    @FXML
    public TableView<BillTM> billingTBV;
    @FXML
    public TableColumn<BillTM, String> billNoCol;
    @FXML
    public TableColumn<BillTM, String> householdCol;
    @FXML
    public TableColumn<BillTM, String> villageCol;
    @FXML
    public TableColumn<BillTM, String> billingDateCol;
    @FXML
    public TableColumn<BillTM, String> amountCol;
    @FXML
    public TableColumn<BillTM, String> statusCol;
    @FXML
    public TableColumn<BillTM, String> actionsCol;

    //private final BillModel billModel = new BillModel();
    private final BillingBO billingBO = (BillingBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.BILLING);
    private ObservableList<BillTM> billTMList = FXCollections.observableArrayList();
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("en", "LK"));

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        initializeTableColumns();


        generateBillsForEligibleConsumptions();

        loadAllBills();

        updateSummaryStatistics();

        setActiveButton(btnAllBills);
    }

    private void initializeTableColumns() {
        billNoCol.setCellValueFactory(new PropertyValueFactory<>("billId"));
        householdCol.setCellValueFactory(new PropertyValueFactory<>("householdName"));
        villageCol.setCellValueFactory(new PropertyValueFactory<>("villageName"));
        billingDateCol.setCellValueFactory(new PropertyValueFactory<>("billingDate"));
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Style the status column
        statusCol.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    switch (item) {
                        case "Paid":
                            setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                            break;
                        case "Unpaid":
                            setStyle("-fx-text-fill: orange; -fx-font-weight: bold;");
                            break;
                        case "Overdue":
                            setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                            break;
                        default:
                            setStyle("");
                    }
                }
            }
        });

        // Set up action column with dynamic buttons
        setupActionColumn();
    }

    private void setupActionColumn() {
        actionsCol.setCellFactory(column -> new TableCell<>() {
            private final Button viewButton = new Button("View");
            private final Button reminderButton = new Button("Send Reminder");
            private final HBox buttonsBox = new HBox(5, viewButton);

            {
                viewButton.setStyle("-fx-background-color: #007bff; -fx-text-fill: white;");
                reminderButton.setStyle("-fx-background-color: #ffc107; -fx-text-fill: #212529;");

                viewButton.setOnAction(event -> {
                    BillTM bill = getTableView().getItems().get(getIndex());
                    openBillViewModal(bill.getBillId());
                });

                reminderButton.setOnAction(event -> {
                    BillTM bill = getTableView().getItems().get(getIndex());
                    sendPaymentReminder(bill.getBillId());
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    BillTM bill = getTableView().getItems().get(getIndex());

                    // Check if bill is overdue
                    if ("Overdue".equals(bill.getStatus())) {
                        if (!buttonsBox.getChildren().contains(reminderButton)) {
                            buttonsBox.getChildren().add(reminderButton);
                        }
                    } else {
                        buttonsBox.getChildren().remove(reminderButton);
                    }

                    setGraphic(buttonsBox);
                }
            }
        });
    }

    private void generateBillsForEligibleConsumptions() {
        try {
            boolean success = billingBO.checkAndGenerateBillsForEligibleConsumptions();
            if (success) {
                System.out.println("Bills generated for eligible consumptions");
            }
        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.showError("Failed to generate bills: " + e.getMessage());
        }
    }

    private void loadAllBills() {
        try {
            ArrayList<BillDTO> billList = billingBO.getAllBills();
            populateBillTable(billList);
        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.showError("Failed to load bills: " + e.getMessage());
        }
    }

    private void loadBillsByStatus(String status) {
        try {
            ArrayList<BillDTO> billList = billingBO.getBillsByStatus(status);
            populateBillTable(billList);
        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.showError("Failed to load bills: " + e.getMessage());
        }
    }

    private void loadOverdueBills() {
        try {
            ArrayList<BillDTO> billList = billingBO.getOverdueBills();
            populateBillTable(billList);
        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.showError("Failed to load overdue bills: " + e.getMessage());
        }
    }

    private void populateBillTable(ArrayList<BillDTO> billList) {
        billTMList.clear();

        for (BillDTO dto : billList) {
            String status = dto.getStatus();


            if ("Unpaid".equals(status)) {
                try {
                    LocalDate dueDate = LocalDate.parse(dto.getDueDate());
                    LocalDate today = LocalDate.now();

                    if (today.isAfter(dueDate)) {
                        status = "Overdue";
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            String formattedDate = formatDate(dto.getBillDate());


            String formattedAmount = formatCurrency(Double.parseDouble(dto.getTotalCost()));

            BillTM tm = new BillTM(
                    dto.getBillId(),
                    dto.getHouseholdName(),
                    dto.getVillageName(),
                    formattedDate,
                    formattedAmount,
                    status
            );

            billTMList.add(tm);
        }

        billingTBV.setItems(billTMList);
    }

    private void updateSummaryStatistics() {
        try {

            double totalCollected = billingBO.getTotalCollectedAmount();
            lblTotalCollected.setText(formatCurrency(totalCollected));


            double pendingPayment = billingBO.getPendingPaymentAmount();
            lblPendingPayment.setText(formatCurrency(pendingPayment));


            double overduePayment = billingBO.getOverduePaymentAmount();
            lblOverduePayment.setText(formatCurrency(overduePayment));

            double totalBilledThisMonth = billingBO.getTotalBilledThisMonth();
            lblTotalBilledThisMonth.setText(formatCurrency(totalBilledThisMonth));

        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.showError("Failed to update summary statistics: " + e.getMessage());
        }
    }

    private void openBillViewModal(String billId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lk/aquaflowwms/view/modalViews/viewBill-Modal.fxml"));
            AnchorPane modalPane = loader.load();

            ViewBillModalController controller = loader.getController();
            controller.loadBillData(billId);

            Stage stage = new Stage();
            stage.setScene(new Scene(modalPane));
            stage.setTitle("Bill Details");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();


            loadAllBills();
            updateSummaryStatistics();

        } catch (IOException e) {
            e.printStackTrace();
            AlertUtil.showError("Failed to open bill view modal: " + e.getMessage());
        }
    }

    private void sendPaymentReminder(String billId) {
        try {

            openBillViewModal(billId);
        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.showError("Failed to send payment reminder: " + e.getMessage());
        }
    }

    @FXML
    public void addButtonOnAction(ActionEvent actionEvent) {
        try {

            ArrayList<BillDTO> unpaidBills = billingBO.getBillsByStatus("Unpaid");
            if (unpaidBills.isEmpty()) {
                AlertUtil.showInfo("No unpaid bills available");
                return;
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lk/aquaflowwms/view/modalViews/addPayment-Modal.fxml"));
            AnchorPane root = loader.load();

            AddPaymentModalController controller = loader.getController();
            controller.initialize(unpaidBills);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Add Payment");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            loadAllBills();
            updateSummaryStatistics();
        } catch (Exception e) {
            AlertUtil.showError("Failed to check unpaid bills: " + e.getMessage());
        }
    }

    @FXML
    public void btnReportOnAction(ActionEvent actionEvent) {
        // Generate bills for eligible consumptions
        generateBillsForEligibleConsumptions();
        loadAllBills();
        updateSummaryStatistics();
        AlertUtil.showSuccess("Bills generated for eligible consumptions");
    }

    @FXML
    public void btnAllBillsOnAction(ActionEvent actionEvent) {
        loadAllBills();
        setActiveButton(btnAllBills);
    }

    @FXML
    public void btnPaidOnAction(ActionEvent actionEvent) {
        loadBillsByStatus("Paid");
        setActiveButton(btnPaid);
    }

    @FXML
    public void btnPendingOnAction(ActionEvent actionEvent) {
        loadBillsByStatus("Unpaid");
        setActiveButton(btnPending);
    }

    @FXML
    public void btnOverdueOnAction(ActionEvent actionEvent) {
        loadOverdueBills();
        setActiveButton(btnOverDue);
    }

    private void setActiveButton(Button activeButton) {
        // Reset all buttons
        btnAllBills.setStyle("-fx-background-color: #f8f9fa; -fx-text-fill: #212529;");
        btnPaid.setStyle("-fx-background-color: #f8f9fa; -fx-text-fill: #212529;");
        btnPending.setStyle("-fx-background-color: #f8f9fa; -fx-text-fill: #212529;");
        btnOverDue.setStyle("-fx-background-color: #f8f9fa; -fx-text-fill: #212529;");

        // Set active button style
        activeButton.setStyle("-fx-background-color: #007bff; -fx-text-fill: white;");
    }

    private String formatDate(String dateStr) {
        try {
            LocalDate date = LocalDate.parse(dateStr);
            return date.format(DateTimeFormatter.ofPattern("dd MMM yyyy"));
        } catch (Exception e) {
            return dateStr;
        }
    }

    private String formatCurrency(double amount) {
        return currencyFormat.format(amount);
    }
}
