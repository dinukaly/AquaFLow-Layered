package lk.wms.aquaflow.bo.custom.impl;

import lk.wms.aquaflow.bo.custom.WaterAllocationBO;
import lk.wms.aquaflow.dto.WaterAllocationDTO;

import java.util.List;

public class WaterAllocationBOImpl implements WaterAllocationBO {
    @Override
    public boolean addWaterAllocation(WaterAllocationDTO waterAllocationDTO) {
        return false;
    }

    @Override
    public WaterAllocationDTO searchWaterAllocation(String waterAllocationId) {
        return null;
    }

    @Override
    public boolean updateWaterAllocation(WaterAllocationDTO waterAllocationDTO) {
        return false;
    }

    @Override
    public boolean deleteWaterAllocation(String waterAllocationId) {
        return false;
    }

    @Override
    public boolean existWaterAllocation(String waterAllocationId) {
        return false;
    }

    @Override
    public String generateWaterAllocationId() {
        return "";
    }

    @Override
    public String getWaterSourceIdForVillage(String villageId) {
        return "";
    }

    @Override
    public List<WaterAllocationDTO> getAllocationsByWaterSourceId(String waterSourceId) {
        return List.of();
    }

    @Override
    public List<WaterAllocationDTO> getAllWaterAllocations() {
        return List.of();
    }
}
