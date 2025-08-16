package lk.wms.aquaflow.bo.custom;

import lk.wms.aquaflow.bo.SuperBO;
import lk.wms.aquaflow.dto.ConsumptionDTO;
import lk.wms.aquaflow.dto.custom.ConsumptionWithHouseVillageDTO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface ConsumptionsBO extends SuperBO {
    public boolean addConsumption(ConsumptionDTO consumptionDTO) throws SQLException, ClassNotFoundException;
    public ConsumptionDTO searchConsumption(String consumptionId);
    public boolean updateConsumption(ConsumptionDTO consumptionDTO) throws SQLException, ClassNotFoundException;
    public boolean deleteConsumption(String consumptionId) throws SQLException, ClassNotFoundException;
    public boolean existConsumption(String consumptionId);
    public String generateConsumptionId() throws SQLException, ClassNotFoundException;
    public ConsumptionDTO getPreviousConsumption(String houseId, String endDate);
    public ConsumptionDTO getConsumptionById(String consumptionId);
    public Map<String, Number> getMonthlyConsumptionForYear(String year);
    public Map<String, Number> getConsumptionByVillageForYear(String year) throws SQLException, ClassNotFoundException;
    public ArrayList<ConsumptionDTO> getAllConsumptions();
    public String getHouseholdIdFromBill(String billId);
    List<ConsumptionWithHouseVillageDTO> getAllConsumptionDetails() throws SQLException, ClassNotFoundException;
}
