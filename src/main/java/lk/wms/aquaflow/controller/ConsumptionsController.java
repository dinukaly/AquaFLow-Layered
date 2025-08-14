package lk.wms.aquaflow.controller;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;


import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import lk.wms.aquaflow.bo.custom.BOFactory;
import lk.wms.aquaflow.bo.custom.ConsumptionsBO;
import lk.wms.aquaflow.bo.custom.HouseholdsBO;
import lk.wms.aquaflow.bo.custom.VillageBO;
import lk.wms.aquaflow.controller.modal.AddConsumptionModalController;
import lk.wms.aquaflow.dto.ConsumptionDTO;
import lk.wms.aquaflow.util.AlertUtil;
import lk.wms.aquaflow.util.TableActionCell;
import lk.wms.aquaflow.view.tm.ConsumptionTM;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.Month;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ConsumptionsController implements Initializable {
    @FXML
    public ComboBox<String> cmbVillage;
    @FXML
    public Button addButton;
    @FXML
    public ComboBox<String> cmbMonth;
    @FXML
    public ComboBox<String> cmbLastYear;
    @FXML
    public ComboBox<String> cmbLastYearByVillage;
    @FXML
    public TableView<ConsumptionTM> consumptionTBV;
    @FXML
    public TableColumn<ConsumptionTM, String> householdCol;
    @FXML
    public TableColumn<ConsumptionTM, String> villageCol;
    @FXML
    public TableColumn<ConsumptionTM, String> currentConsumptionCol;
    @FXML
    public TableColumn<ConsumptionTM, String> previousMonthCol;
    @FXML
    public TableColumn<ConsumptionTM, String> changeCol;
    @FXML
    public TableColumn<ConsumptionTM, String> actionsCol;

    @FXML
    public LineChart<String, Number> monthlyConsumptionChart;
    @FXML
    public LineChart<String, Number> monthlyConsumptionChartByVillage;

   // private final ConsumptionModel consumptionModel = new ConsumptionModel();
  //  private final HouseholdModel householdModel = new HouseholdModel();
    //private final VillageModel villageModel = new VillageModel();

    private final ConsumptionsBO consumptionsBO = (ConsumptionsBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.CONSUMPTION);
    private final HouseholdsBO householdsBO = (HouseholdsBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.HOUSEHOLD);
    private final VillageBO villageBO = (VillageBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.VILLAGE);


    private ObservableList<ConsumptionTM> consumptionTMList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeTableColumns();

        initializeChart();

        loadAllConsumptions();

        loadVillages();

        initializeFilterComboBoxes();

        int currentYear = java.time.Year.now().getValue();
        cmbLastYear.setValue(String.valueOf(currentYear));
        cmbLastYearByVillage.setValue(String.valueOf(currentYear));

        updateMonthlyConsumptionChart();
        updateConsumptionByVillageChart();
    }

    private void initializeTableColumns() {
        householdCol.setCellValueFactory(new PropertyValueFactory<>("householdName"));
        villageCol.setCellValueFactory(new PropertyValueFactory<>("villageName"));
        currentConsumptionCol.setCellValueFactory(new PropertyValueFactory<>("currentConsumption"));
        previousMonthCol.setCellValueFactory(new PropertyValueFactory<>("previousMonth"));
        changeCol.setCellValueFactory(new PropertyValueFactory<>("change"));

        actionsCol.setCellFactory(TableActionCell.create(
                this::handleEditAction,
                this::handleDeleteAction
        ));
    }

    private void initializeChart() {
        if (monthlyConsumptionChart != null) {
            monthlyConsumptionChart.setTitle("Monthly Consumption Trend");
            monthlyConsumptionChart.setAnimated(false);
        }

        if (monthlyConsumptionChartByVillage != null) {
            monthlyConsumptionChartByVillage.setTitle("Village-wise Consumption");
            monthlyConsumptionChartByVillage.setAnimated(false);
        }
    }

    private void loadAllConsumptions() {
        try {
            ArrayList<ConsumptionDTO> consumptionList = consumptionsBO.getAllConsumptions();
            consumptionTMList.clear();

            for (ConsumptionDTO dto : consumptionList) {
                // Get household info
                String houseId = dto.getHouseId();
                String householdName = "";
                String villageName = "";

                try {
                    var household = householdsBO.getHouseholdById(houseId);
                    if (household != null) {
                        householdName = household.getOwnerName();
                        villageName = household.getVillageName();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                String currentConsumption = dto.getAmountOfUnits();
                String previousMonth = "0";
                String change = "0";

                try {
                    ConsumptionDTO previousConsumption = consumptionsBO.getPreviousConsumption(houseId, dto.getEndDate());
                    if (previousConsumption != null) {
                        previousMonth = previousConsumption.getAmountOfUnits();

                        // Calculate change
                        int current = Integer.parseInt(currentConsumption);
                        int previous = Integer.parseInt(previousMonth);
                        int changeValue = current - previous;
                        change = String.valueOf(changeValue);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                ConsumptionTM tm = new ConsumptionTM(
                        dto.getConsumptionId(),
                        householdName,
                        villageName,
                        currentConsumption,
                        previousMonth,
                        change,
                        houseId
                );

                consumptionTMList.add(tm);
            }

            consumptionTBV.setItems(consumptionTMList);

        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.showError("Failed to load consumption data: " + e.getMessage());
        }
    }

    private void loadVillages() {
        try {
            cmbVillage.getItems().clear();
            cmbVillage.getItems().add("All Villages");

            var villages = villageBO.getAllVillages();
            for (var village : villages) {
                cmbVillage.getItems().add(village.getVillageName());
            }

            cmbVillage.setValue("All Villages");

        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.showError("Failed to load village data: " + e.getMessage());
        }
    }

    private void initializeFilterComboBoxes() {
        cmbMonth.getItems().addAll("January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December");

        int currentYear = java.time.Year.now().getValue();
        for (int i = currentYear; i >= currentYear - 5; i--) {
            cmbLastYear.getItems().add(String.valueOf(i));
            cmbLastYearByVillage.getItems().add(String.valueOf(i));
        }
    }

    @FXML
    public void addButtonOnAction(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lk/aquaflowwms/view/modalViews/addConsumption-Modal.fxml"));
            AnchorPane loadModal = loader.load();

            AddConsumptionModalController controller = loader.getController();
            controller.setModeAndData(AddConsumptionModalController.Mode.ADD, null);
            controller.setRefreshCallback(this::loadAllConsumptions);

            Stage stage = new Stage();
            stage.setScene(new javafx.scene.Scene(loadModal));
            stage.setTitle("Add Consumption");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            AlertUtil.showError("Failed to open add consumption modal: " + e.getMessage());
        }
    }

    private void handleEditAction(ConsumptionTM tm) {
        try {
            ConsumptionDTO dto = consumptionsBO.getConsumptionById(tm.getConsumptionId());

            if (dto != null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/lk/aquaflowwms/view/modalViews/addConsumption-Modal.fxml"));
                AnchorPane loadModal = loader.load();

                AddConsumptionModalController controller = loader.getController();
                controller.setModeAndData(AddConsumptionModalController.Mode.EDIT, dto);
                controller.setRefreshCallback(this::loadAllConsumptions);

                Stage stage = new Stage();
                stage.setScene(new javafx.scene.Scene(loadModal));
                stage.setTitle("Edit Consumption");
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();
            } else {
                AlertUtil.showError("Consumption record not found.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.showError("Failed to open edit consumption modal: " + e.getMessage());
        }
    }

    private void handleDeleteAction(ConsumptionTM tm) {
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirm Delete");
        confirmDialog.setHeaderText("Delete Consumption Record");
        confirmDialog.setContentText("Are you sure you want to delete this consumption record?");

        Optional<ButtonType> result = confirmDialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                boolean success = consumptionsBO.deleteConsumption(tm.getConsumptionId());

                if (success) {
                    AlertUtil.showSuccess("Consumption record has been deleted successfully.");
                    loadAllConsumptions(); // Refresh the table
                } else {
                    AlertUtil.showFailure("Failed to delete consumption record.");
                }

            } catch (Exception e) {
                e.printStackTrace();
                AlertUtil.showError("Delete operation failed: " + e.getMessage());
            }
        }
    }

    public void cmbVillageOnAction(ActionEvent actionEvent) {
        filterTableData();
    }

    public void cmbMonthOnAction(ActionEvent actionEvent) {
        AlertUtil.showInfo("Filtering by month is not yet implemented.");
    }

    @FXML
    public void cmbLastYearOnAction(ActionEvent actionEvent) {
        if (cmbLastYear.getScene() == null) return;
        updateMonthlyConsumptionChart();
    }

    @FXML
    public void cmbLastYearByVillageOnAction(ActionEvent actionEvent) {
        if (cmbLastYearByVillage.getScene() == null) return;
        updateConsumptionByVillageChart();
    }

    private void filterTableData() {
        String selectedVillage = cmbVillage.getValue();
        if (selectedVillage == null || "All Villages".equals(selectedVillage)) {
            consumptionTBV.setItems(consumptionTMList);
        } else {
            ObservableList<ConsumptionTM> filteredList = consumptionTMList.stream()
                    .filter(tm -> tm.getVillageName().equals(selectedVillage))
                    .collect(Collectors.toCollection(FXCollections::observableArrayList));
            consumptionTBV.setItems(filteredList);
        }
    }

    private void updateMonthlyConsumptionChart() {
        monthlyConsumptionChart.getData().clear();
        String selectedYear = cmbLastYear.getValue();
        if (selectedYear == null) {
            selectedYear = String.valueOf(java.time.Year.now().getValue());
            cmbLastYear.setValue(selectedYear);
        }

        try {
            Map<String, Number> monthlyData = consumptionsBO.getMonthlyConsumptionForYear(selectedYear);

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName(selectedYear + " Consumption");

            for (Month month : Month.values()) {
                String monthName = month.toString().substring(0, 1).toUpperCase() + month.toString().substring(1).toLowerCase();
                Number value = monthlyData.getOrDefault(monthName, 0);
                series.getData().add(new XYChart.Data<>(monthName.substring(0, 3), value));
            }

            monthlyConsumptionChart.getData().add(series);
            monthlyConsumptionChart.setTitle("Monthly Consumption Trend for " + selectedYear);

        } catch (Exception e) {
            AlertUtil.showError("Error loading monthly chart data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void updateConsumptionByVillageChart() {
        monthlyConsumptionChartByVillage.getData().clear();
        String selectedYear = cmbLastYearByVillage.getValue();
        if (selectedYear == null) {
            selectedYear = String.valueOf(java.time.Year.now().getValue());
            cmbLastYearByVillage.setValue(selectedYear);
        }

        try {
            Map<String, Number> villageData = consumptionsBO.getConsumptionByVillageForYear(selectedYear);

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Consumption in " + selectedYear);

            for (Map.Entry<String, Number> entry : villageData.entrySet()) {
                series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
            }

            monthlyConsumptionChartByVillage.getData().add(series);
            monthlyConsumptionChartByVillage.setTitle("Village-wise Consumption for " + selectedYear);

        } catch (Exception e) {
            AlertUtil.showError("Error loading village chart data: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
