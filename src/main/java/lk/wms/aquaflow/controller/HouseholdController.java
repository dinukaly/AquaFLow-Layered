package lk.wms.aquaflow.controller;


import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import lk.wms.aquaflow.bo.custom.BOFactory;
import lk.wms.aquaflow.bo.custom.HouseholdsBO;
import lk.wms.aquaflow.bo.custom.VillageBO;
import lk.wms.aquaflow.controller.modal.AddHouseholdModalController;
import lk.wms.aquaflow.db.DBConnection;
import lk.wms.aquaflow.dto.HouseholdDTO;
import lk.wms.aquaflow.util.TableActionCell;
import lk.wms.aquaflow.view.tm.HouseholdTM;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

import java.net.URL;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

public class HouseholdController implements Initializable {

    public TextField txtSearch;
    public AnchorPane childRoot;
   // HouseholdModel householdModel = new HouseholdModel();
    //VillageModel villageModel = new VillageModel();

    private final HouseholdsBO householdsBO = (HouseholdsBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.HOUSEHOLD);
    private final VillageBO villageBO = (VillageBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.VILLAGE);


    public TableView<HouseholdTM> householdTableView;
    public TableColumn<HouseholdTM, String> householdIdCol;
    public TableColumn<HouseholdTM, String> ownerNameCol;
    public TableColumn<HouseholdTM, String> addressCol;
    public TableColumn<HouseholdTM, String> noOfMemCol;
    public TableColumn<HouseholdTM, String> emailCol;
    public TableColumn<HouseholdTM, String> villageCol;
    public TableColumn<HouseholdTM, String> actionCol;

    //TODO: move reports to a custom method
    public void btnReportsOnAction(ActionEvent actionEvent) {
        try {
            JasperReport report = JasperCompileManager.compileReport(
                    getClass().getResourceAsStream("/lk/aquaflowwms/reports/household.jrxml")
            );
            Connection connection = DBConnection.getInstance().getConnection();
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("P_DATE", LocalDate.now().toString());
            JasperPrint jasperPrint = JasperFillManager.fillReport(
                    report,
                    parameters,
                    connection
            );
            JasperViewer.viewReport(jasperPrint, false);

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error generating report: " + e.getMessage()).show();
        }
    }

    public void txtSearchOnAction(ActionEvent actionEvent) {
        try {
            searchHouseholds(txtSearch.getText());
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error while searching: " + e.getMessage()).show();
        }
    }

    private void searchHouseholds(String searchText) throws Exception {
        if (searchText == null || searchText.isEmpty()) {
            loadAllHouseholds();
            return;
        }

        householdTableView.setItems(FXCollections.observableArrayList(
                householdsBO.searchHouseholds(searchText).stream()
                        .map(this::convertToTM)
                        .toList()
        ));
    }

    public void cmbVillagesOnAction(ActionEvent actionEvent) {
        // Implement village filter functionality
    }

    public void addHouseholdBtnOnAction(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lk/aquaflowwms/view/modalViews/addHousehold-Modal.fxml"));
            AnchorPane loadModal = loader.load();

            AddHouseholdModalController controller = loader.getController();
            controller.setModeAndData(AddHouseholdModalController.Mode.ADD, null);

            Stage stage = new Stage();
            stage.setScene(new Scene(loadModal));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
            stage.showAndWait();

            loadAllHouseholds();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error loading household form").show();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            setupTableColumns();
            loadAllHouseholds();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error initializing household data").show();
        }
    }

    private void setupTableColumns() {
        householdIdCol.setCellValueFactory(new PropertyValueFactory<>("houseId"));  // was "householdId"
        ownerNameCol.setCellValueFactory(new PropertyValueFactory<>("ownerName"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        noOfMemCol.setCellValueFactory(new PropertyValueFactory<>("noOfMembers"));  // was "noOfMem"
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        villageCol.setCellValueFactory(new PropertyValueFactory<>("villageName"));
        actionCol.setCellFactory(TableActionCell.create(
                this::handleEdit,
                this::handleDelete
        ));
    }

    private HouseholdTM convertToTM(HouseholdDTO householdDTO) {
        return new HouseholdTM(
                householdDTO.getHouseId(),
                householdDTO.getOwnerName(),
                householdDTO.getAddress(),
                householdDTO.getNoOfMembers(),
                householdDTO.getEmail(),
                householdDTO.getVillageName() != null ? householdDTO.getVillageName(): "Unknown Village"
        );
    }

    private void loadAllHouseholds() throws Exception {
        householdTableView.setItems(FXCollections.observableArrayList(
                householdsBO.getAllHouseholds().stream()
                        .map(this::convertToTM)
                        .toList()
        ));
    }

    private void handleEdit(HouseholdTM householdTM) {
        try {
            HouseholdDTO householdDTO = householdsBO.getHouseholdById(householdTM.getHouseId());

            if (householdDTO == null) {

                String villageId = villageBO.getVillageIdByName(householdTM.getVillageName());
                if (villageId == null) {
                    new Alert(Alert.AlertType.ERROR, "Could not find Village ID for name: " + householdTM.getVillageName()).show();
                    return;
                }

                householdDTO = new HouseholdDTO(
                        householdTM.getHouseId(),
                        householdTM.getOwnerName(),
                        householdTM.getAddress(),
                        householdTM.getNoOfMembers(),
                        householdTM.getEmail(),
                        villageId
                );
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lk/aquaflowwms/view/modalViews/addHousehold-Modal.fxml"));
            AnchorPane modalRoot = loader.load();

            AddHouseholdModalController modalController = loader.getController();
            modalController.setModeAndData(AddHouseholdModalController.Mode.UPDATE, householdDTO);

            Stage stage = new Stage();
            stage.setScene(new Scene(modalRoot));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(householdTableView.getScene().getWindow());
            stage.showAndWait();

            loadAllHouseholds();
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid number format in data!").show();
            e.printStackTrace();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error loading Household data: " + e.getMessage()).show();
            e.printStackTrace();
        }
    }

    private void handleDelete(HouseholdTM householdTM) {

        String householdId = householdTM.getHouseId();

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to delete Household ID: " + householdId + "?",
                ButtonType.YES, ButtonType.NO
        );
        confirm.setHeaderText("Confirm Delete");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            try {
                boolean isDeleted = householdsBO.deleteHousehold(householdId);
                if (isDeleted) {
                    loadAllHouseholds();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Failed to delete household!").show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Error occurred while deleting!").show();
            }
        }
    }

    public void viewConsumptionPageBtn(ActionEvent actionEvent) {
        childRoot.getChildren().clear();
        try {
            AnchorPane newPane = FXMLLoader.load(getClass().getResource("/lk/aquaflowwms/view/consumptions-view.fxml"));
            childRoot.getChildren().add(newPane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}