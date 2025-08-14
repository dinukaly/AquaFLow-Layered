package lk.wms.aquaflow.controller;


import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lk.wms.aquaflow.bo.custom.BOFactory;
import lk.wms.aquaflow.bo.custom.OfficersBO;
import lk.wms.aquaflow.controller.modal.AddOfficerModalController;
import lk.wms.aquaflow.dto.OfficerDTO;
import lk.wms.aquaflow.util.TableActionCell;
import lk.wms.aquaflow.view.tm.OfficerTM;

import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class OfficerController implements Initializable {

    //TM
    public TableView<OfficerTM> employeeTableView;
    public TableColumn<OfficerTM, String> officerIdCol;
    public TableColumn<OfficerTM, String> nameCol;
    public TableColumn<OfficerTM, String> addressCol;
    public TableColumn<OfficerTM, String> emailCol;
    public TableColumn<OfficerTM, String> contactCol;
    public TableColumn<OfficerTM, String> actionCol;


    private final OfficersBO officersBO = (OfficersBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.OFFICER);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("OfficersController: initialize() called");
        officerIdCol.setCellValueFactory(new PropertyValueFactory<>("officerId"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        contactCol.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        actionCol.setCellFactory(TableActionCell.create(
                this::handleEdit,
                this::handleDelete
        ));




        try {
            loadAllOfficers();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void loadAllOfficers() throws SQLException, ClassNotFoundException {
        employeeTableView.setItems(FXCollections.observableArrayList(
                officersBO.getAllOfficers().stream()
                        .map(officerDTO->new OfficerTM(
                                officerDTO.getOfficerId(),
                                officerDTO.getName(),
                                officerDTO.getAddress(),
                                officerDTO.getEmail(),
                                officerDTO.getTelephone()
                        )).toList()
        ));
    }
    public void txtSearch(ActionEvent actionEvent) {
        // TODO document why this method is empty
    }

    public void addButtonOnAction(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lk/aquaflowwms/view/modalViews/addOfficer-Modal.fxml"));
            AnchorPane loadModal = loader.load();

            AddOfficerModalController controller = loader.getController();
            controller.setModeAndData(AddOfficerModalController.Mode.ADD, null);
            Stage stage = new Stage();
            stage.setScene(new Scene(loadModal));

            // 👇 Block the main stage
            stage.initModality(Modality.WINDOW_MODAL);

            // 👇 Set the owner (main window)
            stage.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());

            // 👇 Show the modal and wait until it’s closed
            stage.showAndWait();
            loadAllOfficers();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void handleEdit(OfficerTM officerTM) {
        OfficerDTO officerDTO = new OfficerDTO(
                officerTM.getOfficerId(),
                officerTM.getName(),
                officerTM.getAddress(),
                officerTM.getEmail(),
                officerTM.getTelephone()
        );

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lk/aquaflowwms/view/modalViews/addOfficer-Modal.fxml"));
            AnchorPane modalRoot = loader.load();

            AddOfficerModalController modalController = loader.getController();
            modalController.setModeAndData(AddOfficerModalController.Mode.UPDATE, officerDTO); // Use DTO here

            Stage stage = new Stage();
            stage.setScene(new Scene(modalRoot));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(employeeTableView.getScene().getWindow());
            stage.showAndWait();

            loadAllOfficers();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleDelete(OfficerTM officerTM) {
        String officerId = officerTM.getOfficerId(); // okay to pass just the ID here

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to delete Officer ID: " + officerId + "?",
                ButtonType.YES, ButtonType.NO
        );
        confirm.setHeaderText("Confirm Delete");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            try {
                boolean isDeleted = officersBO.deleteOfficer(officerId);
                if (isDeleted) {
                    loadAllOfficers();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Failed to delete officer!").show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Error occurred while deleting!").show();
            }
        }
    }




}

