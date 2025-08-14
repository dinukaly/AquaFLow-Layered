package lk.wms.aquaflow.bo.custom;

import lk.wms.aquaflow.bo.SuperBO;
import lk.wms.aquaflow.dto.ConsumptionDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface ConsumptionsBO extends SuperBO {
    public boolean addConsumption(ConsumptionDTO consumptionDTO);
    public ConsumptionDTO searchConsumption(String consumptionId);
    public boolean updateConsumption(ConsumptionDTO consumptionDTO);
    public boolean deleteConsumption(String consumptionId);
    public boolean existConsumption(String consumptionId);
    public String generateConsumptionId();
    public ConsumptionDTO getPreviousConsumption(String houseId, String endDate);
    public ConsumptionDTO getConsumptionById(String consumptionId);
    public Map<String, Number> getMonthlyConsumptionForYear(String year);
    public Map<String, Number> getConsumptionByVillageForYear(String year);
    public ArrayList<ConsumptionDTO> getAllConsumptions();
    public String getHouseholdIdFromBill(String billId);
}
