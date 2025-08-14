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
import lk.wms.aquaflow.bo.custom.OfficersBO;
import lk.wms.aquaflow.bo.custom.VillageBO;
import lk.wms.aquaflow.controller.modal.AddVillageModalController;
import lk.wms.aquaflow.dto.VillageDTO;
import lk.wms.aquaflow.util.TableActionCell;
import lk.wms.aquaflow.view.tm.VillageTM;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class VillageController implements Initializable {

    public TableView<VillageTM> villageTableView;
    public TableColumn<VillageTM, String> villageIdCol;
    public TableColumn<VillageTM, String> villageNameCol;
    public TableColumn<VillageTM, String> populationCol;
    public TableColumn<VillageTM, String> waterRequirementCol;
    public TableColumn<VillageTM, String> areaCol;
    public TableColumn<VillageTM, String> districtCol;
    public TableColumn<VillageTM, String> officerNameCol;
    public TableColumn<VillageTM, String> actionCol;
    public TextField txtSearch;

    //private final VillageModel villageModel = new VillageModel();
   // private final OfficerModel officerModel = new OfficerModel();

    private final VillageBO villageBO = (VillageBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.VILLAGE);
    private final OfficersBO officersBO = (OfficersBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.OFFICER);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        villageIdCol.setCellValueFactory(new PropertyValueFactory<>("villageId"));
        villageNameCol.setCellValueFactory(new PropertyValueFactory<>("villageName"));
        populationCol.setCellValueFactory(new PropertyValueFactory<>("population"));
        waterRequirementCol.setCellValueFactory(new PropertyValueFactory<>("waterRequirement"));
        areaCol.setCellValueFactory(new PropertyValueFactory<>("area"));
        districtCol.setCellValueFactory(new PropertyValueFactory<>("district"));
        officerNameCol.setCellValueFactory(new PropertyValueFactory<>("officerName"));
        actionCol.setCellFactory(TableActionCell.create(
                this::handleEdit,
                this::handleDelete
        ));

        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                searchVillages(newValue);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        try {
            loadAllVillages();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private VillageTM convertToTM(VillageDTO villageDTO) {
        return new VillageTM(
                villageDTO.getVillageId(),
                villageDTO.getVillageName(),
                String.valueOf(villageDTO.getPopulation()),
                String.valueOf(villageDTO.getWaterRequirement()),
                String.valueOf(villageDTO.getArea()),
                villageDTO.getDistrict(),
                villageDTO.getOfficerName() != null ? villageDTO.getOfficerName() : "Unknown Officer"
        );
    }

    private void loadAllVillages() throws SQLException, ClassNotFoundException {
        villageTableView.setItems(FXCollections.observableArrayList(
                villageBO.getAllVillages().stream()
                        .map(this::convertToTM)
                        .toList()
        ));
    }

    private void searchVillages(String searchText) throws SQLException, ClassNotFoundException {
        if (searchText == null || searchText.isEmpty()) {
            loadAllVillages();
            return;
        }

        villageTableView.setItems(FXCollections.observableArrayList(
                villageBO.searchVillage(searchText).stream()
                        .map(this::convertToTM)
                        .toList()
        ));
    }

    public void addButtonOnAction(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lk/aquaflowwms/view/modalViews/addVillage-Modal.fxml"));
            AnchorPane loadModal = loader.load();

            AddVillageModalController controller = loader.getController();
            controller.setModeAndData(AddVillageModalController.Mode.ADD, null);

            Stage stage = new Stage();
            stage.setScene(new Scene(loadModal));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
            stage.showAndWait();

            loadAllVillages();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleEdit(VillageTM villageTM) {
        try {
            VillageDTO villageDTO = villageBO.getVillageById(villageTM.getVillageId());

            if (villageDTO == null) {
                String officerId = officersBO.getOfficerIdByName(villageTM.getOfficerName());
                if (officerId == null) {
                    new Alert(Alert.AlertType.ERROR, "Could not find officer ID for name: " + villageTM.getOfficerName()).show();
                    return;
                }

                villageDTO = new VillageDTO(
                        villageTM.getVillageId(),
                        villageTM.getVillageName(),
                        Integer.parseInt(villageTM.getPopulation()),
                        Double.parseDouble(villageTM.getWaterRequirement()),
                        Double.parseDouble(villageTM.getArea()),
                        villageTM.getDistrict(),
                        officerId
                );
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lk/aquaflowwms/view/modalViews/addVillage-Modal.fxml"));
            AnchorPane modalRoot = loader.load();

            AddVillageModalController modalController = loader.getController();
            modalController.setModeAndData(AddVillageModalController.Mode.UPDATE, villageDTO);

            Stage stage = new Stage();
            stage.setScene(new Scene(modalRoot));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(villageTableView.getScene().getWindow());
            stage.showAndWait();

            loadAllVillages();
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid number format in data!").show();
            e.printStackTrace();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error loading village data: " + e.getMessage()).show();
            e.printStackTrace();
        }
    }

    private void handleDelete(VillageTM villageTM) {
        String villageId = villageTM.getVillageId();

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to delete Village ID: " + villageId + "?",
                ButtonType.YES, ButtonType.NO
        );
        confirm.setHeaderText("Confirm Delete");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            try {
                boolean isDeleted = villageBO.deleteVillage(villageId);
                if (isDeleted) {
                    loadAllVillages();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Failed to delete village!").show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Error occurred while deleting!").show();
            }
        }
    }

    public void txtSearch(ActionEvent actionEvent) {
        try {
            searchVillages(txtSearch.getText());
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error while searching: " + e.getMessage()).show();
        }
    }
}

