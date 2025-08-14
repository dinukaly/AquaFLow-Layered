package lk.wms.aquaflow.bo.custom.impl;

import lk.wms.aquaflow.bo.custom.ConsumptionsBO;
import lk.wms.aquaflow.dto.ConsumptionDTO;

import java.util.List;

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
    public List<ConsumptionDTO> getAllConsumptions() {
        return List.of();
    }
}
