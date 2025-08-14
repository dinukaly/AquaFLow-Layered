package lk.wms.aquaflow.controller;



import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lk.wms.aquaflow.bo.custom.BOFactory;
import lk.wms.aquaflow.bo.custom.ComplaintsBO;
import lk.wms.aquaflow.controller.modal.AddComplaintModalController;
import lk.wms.aquaflow.dto.ComplaintDTO;
import lk.wms.aquaflow.util.EmailUtil;
import lk.wms.aquaflow.util.TableActionCell;
import lk.wms.aquaflow.view.tm.ComplaintTM;


import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class ComplaintsController {

    @FXML
    public TableView<ComplaintTM> complaintsTBH;

    @FXML
    public TableColumn<ComplaintTM, String> dateCol;

    @FXML
    public TableColumn<ComplaintTM, String> descriptionCol;

    @FXML
    public TableColumn<ComplaintTM, String> ownerEmailCol;

    @FXML
    public TableColumn<ComplaintTM, String> statusCol;

    @FXML
    public TableColumn<ComplaintTM, String> actionCol;

    @FXML
    public Text lblTotalComplaints;

    @FXML
    public Text lblSolved;


    @FXML
    public Text thisMonth;

    //private final ComplaintModel complaintModel = new ComplaintModel();
    private final ComplaintsBO complaintBO = (ComplaintsBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.COMPLAINTS);

    public void initialize() {
        setCellValueFactory();
        loadAllComplaints();
        loadDashboardData();
    }

    private void setCellValueFactory() {
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        ownerEmailCol.setCellValueFactory(new PropertyValueFactory<>("ownerEmail"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));


        statusCol.setCellFactory(column -> {
            return new TableCell<ComplaintTM, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(item);


                        switch (item) {
                            case "Pending":
                                setStyle("-fx-background-color: #FFF3CD; -fx-text-fill: #856404; -fx-padding: 5px; -fx-alignment: CENTER;");
                                break;
                            case "Completed":
                                setStyle("-fx-background-color: #D4EDDA; -fx-text-fill: #155724; -fx-padding: 5px; -fx-alignment: CENTER;");
                                break;
                            default:
                                setStyle("");
                                break;
                        }
                    }
                }
            };
        });


        setupActionColumn();
    }

    private void setupActionColumn() {

        actionCol.setCellFactory(TableActionCell.createWithEditCompleteDelete(
                this::openEditModal,
                this::markAsCompleted,
                this::deleteComplaint
        ));
    }

    private void openEditModal(ComplaintTM complaint) {
        try {
            openComplaintModal(complaint);
        } catch (IOException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to open edit modal", e.getMessage());
        }
    }

    private void deleteComplaint(ComplaintTM complaint) {
        try {
            Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION,
                    "Are you sure you want to delete this complaint?",
                    ButtonType.YES, ButtonType.NO);
            confirmDialog.setHeaderText("Confirm Delete");

            Optional<ButtonType> result = confirmDialog.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.YES) {
                boolean deleted = complaintBO.deleteComplaint(complaint.getComplaintId());

                if (deleted) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Complaint Deleted", "Complaint has been deleted successfully.");
                    refreshData();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Delete Failed", "Failed to delete the complaint.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete complaint", e.getMessage());
        }
    }

    private void markAsCompleted(ComplaintTM complaintTM) {
        try {
            if ("Completed".equals(complaintTM.getStatus())) {
                showAlert(Alert.AlertType.INFORMATION, "Information", "Already Completed", "This complaint is already marked as completed.");
                return;
            }

            Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION,
                    "Are you sure you want to mark this complaint as completed?",
                    ButtonType.YES, ButtonType.NO);
            confirmDialog.setHeaderText("Confirm Status Change");

            Optional<ButtonType> result = confirmDialog.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.YES) {
                boolean updated = complaintBO.updateComplaintStatus(complaintTM.getComplaintId(), "Completed");

                if (updated) {
                    ComplaintDTO complaintDTO = complaintBO.getComplaintById(complaintTM.getComplaintId());

                    if (complaintDTO != null && complaintDTO.getOwnerEmail() != null && !complaintDTO.getOwnerEmail().isEmpty()) {
                        String emailBody = EmailUtil.createComplaintResolutionEmailBody(
                                complaintDTO.getComplaintId(),
                                complaintDTO.getDescription()
                        );

                        boolean emailSent = EmailUtil.sendEmail(
                                complaintDTO.getOwnerEmail(),
                                "Your Complaint Has Been Resolved - AquaFlow WMS",
                                emailBody
                        );

                        if (emailSent) {
                            System.out.println("Email notification sent to: " + complaintDTO.getOwnerEmail());
                            showAlert(Alert.AlertType.INFORMATION, "Success", "Status Updated",
                                    "Complaint has been marked as completed and email notification sent.");
                        } else {
                            System.out.println("Failed to send email notification to: " + complaintDTO.getOwnerEmail());
                            showAlert(Alert.AlertType.INFORMATION, "Success", "Status Updated",
                                    "Complaint has been marked as completed but email notification failed.");
                        }
                    } else {
                        showAlert(Alert.AlertType.INFORMATION, "Success", "Status Updated",
                                "Complaint has been marked as completed. No email address available for notification.");
                    }

                    refreshData();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Update Failed", "Failed to update the complaint status.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to update complaint status", e.getMessage());
        }
    }

    private void loadAllComplaints() {
        try {
            ArrayList<ComplaintDTO> allComplaints = complaintBO.getAllComplaints();
            ObservableList<ComplaintTM> complaintTMS = FXCollections.observableArrayList();

            for (ComplaintDTO dto : allComplaints) {
                complaintTMS.add(new ComplaintTM(
                        dto.getComplaintId(),
                        dto.getDate(),
                        dto.getDescription(),
                        dto.getOwnerEmail(),
                        dto.getStatus()
                ));
            }

            complaintsTBH.setItems(complaintTMS);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load complaints", e.getMessage());
        }
    }

    private void loadDashboardData() {
        try {
            int totalComplaints = complaintBO.getTotalComplaintsCount();
            int solvedComplaints = complaintBO.getSolvedComplaintsCount();
            int scheduledComplaints = complaintBO.getScheduledComplaintsCount();
            int newThisMonth = complaintBO.getNewComplaintsThisMonth();

            lblTotalComplaints.setText(String.valueOf(totalComplaints));
            lblSolved.setText(String.valueOf(solvedComplaints));
            thisMonth.setText(newThisMonth + " new this month");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load dashboard data", e.getMessage());
        }
    }

    @FXML
    public void addButtonOnAction(ActionEvent actionEvent) {
        try {
            openComplaintModal(null);
        } catch (IOException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to open add modal", e.getMessage());
        }
    }

    @FXML
    public void btnReportOnAction(ActionEvent actionEvent) {
        // Implement report generation functionality here
        showAlert(Alert.AlertType.INFORMATION, "Reports", "Report Generation", "Report generation will be implemented in a future update.");
    }

    private void openComplaintModal(ComplaintTM complaintTM) throws IOException, SQLException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/lk/aquaflowwms/view/modalViews/addComplaint-Modal.fxml"));
        Parent root = loader.load();

        AddComplaintModalController controller = loader.getController();

        if (complaintTM != null) {
            ComplaintDTO complaintDTO = complaintBO.getComplaintById(complaintTM.getComplaintId());
            controller.setComplaintForEdit(complaintDTO);
        } else {
            controller.setAddMode();
        }

        controller.setRefreshCallback(this::refreshData);

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(complaintTM == null ? "Add New Complaint" : "Edit Complaint");
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }

    private void refreshData() {
        loadAllComplaints();
        loadDashboardData();
    }

    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}

