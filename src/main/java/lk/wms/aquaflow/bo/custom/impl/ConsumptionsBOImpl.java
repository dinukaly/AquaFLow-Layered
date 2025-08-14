package lk.wms.aquaflow.bo.custom.impl;

import lk.wms.aquaflow.bo.custom.ConsumptionsBO;
import lk.wms.aquaflow.dto.ConsumptionDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConsumptionsBOImpl implements ConsumptionsBO {
    @Override
    public boolean addConsumption(ConsumptionDTO consumptionDTO) {
        return false;
    }

    @Override
    public ConsumptionDTO searchConsumption(String consumptionId) {
        return null;
    }

    @Override
    public boolean updateConsumption(ConsumptionDTO consumptionDTO) {
        return false;
    }

    @Override
    public boolean deleteConsumption(String consumptionId) {
        return false;
    }

    @Override
    public boolean existConsumption(String consumptionId) {
        return false;
    }

    @Override
    public String generateConsumptionId() {
        return "";
    }

    @Override
    public ConsumptionDTO getPreviousConsumption(String houseId, String endDate) {
        return null;
    }

    @Override
    public ConsumptionDTO getConsumptionById(String consumptionId) {
        return null;
    }

    @Override
    public Map<String, Number> getMonthlyConsumptionForYear(String year) {
        return Map.of();
    }

    @Override
    public Map<String, Number> getConsumptionByVillageForYear(String year) {
        return Map.of();
    }

    @Override
    public ArrayList<ConsumptionDTO> getAllConsumptions() {
        return new ArrayList<>();
    }

    @Override
    public String getHouseholdIdFromBill(String billId) {
        return "";
    }
}
