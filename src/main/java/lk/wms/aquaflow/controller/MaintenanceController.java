package lk.wms.aquaflow.controller;



import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lk.wms.aquaflow.bo.custom.BOFactory;
import lk.wms.aquaflow.bo.custom.MaintenanceBO;
import lk.wms.aquaflow.dto.WaterMaintenanceDTO;


import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class MaintenanceController implements Initializable {
    public MaintenanceController() {
        System.out.println("Controller constructor called");
    }
    public TableView<WaterMaintenanceDTO> tblMaintenance;
    public TableColumn<WaterMaintenanceDTO, String> colId;
    public TableColumn<WaterMaintenanceDTO, String> colVillage;
    public TableColumn<WaterMaintenanceDTO, String> colDate;
    public TableColumn<WaterMaintenanceDTO, String> colStatus;
    public TableColumn<WaterMaintenanceDTO, Double> colCost;

    public ObservableList<WaterMaintenanceDTO> maintenanceList = FXCollections.observableArrayList();
    private MaintenanceBO maintenanceBO = (MaintenanceBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.MAINTENANCE);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Initializable.initialize() called");
        initializeMaintenanceView();
    }

    @FXML
    public void initialize() {
        System.out.println("@FXML initialize() called");
        initializeMaintenanceView();
    }

    private void initializeMaintenanceView() {
        System.out.println("Setting up maintenance view");
        setupTableColumns();
        loadAllMaintenances();
    }

    private void setupTableColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("maintenanceId"));
        colVillage.setCellValueFactory(new PropertyValueFactory<>("villageId"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("maintenanceDate"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colCost.setCellValueFactory(new PropertyValueFactory<>("cost"));

        tblMaintenance.setItems(maintenanceList);
    }

    private void loadAllMaintenances() {
        System.out.println("Loading maintenance data...");
        try {
            List<WaterMaintenanceDTO> maintenances = maintenanceBO.getAllMaintenances();
            System.out.println("Found " + maintenances.size() + " maintenance records");
            maintenanceList.setAll(maintenances);
        } catch (Exception e) {
            System.err.println("Error loading maintenances: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public void addButtonOnAction(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lk/wms/aquaflow/view/modalViews/addMaintenance-Modal.fxml"));
            AnchorPane pane = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(pane));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Add New Maintenance");
            stage.setResizable(false);

            // Refresh table after modal closes
            stage.setOnHidden(e -> loadAllMaintenances());

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void btnRefreshOnAction(ActionEvent event) {
        loadAllMaintenances();
    }
}

