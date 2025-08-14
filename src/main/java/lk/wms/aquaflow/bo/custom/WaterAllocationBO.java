package lk.wms.aquaflow.bo.custom;

import lk.wms.aquaflow.bo.SuperBO;
import lk.wms.aquaflow.dto.WaterAllocationDTO;

import java.util.List;

public interface WaterAllocationBO extends SuperBO {
    public boolean addWaterAllocation(WaterAllocationDTO waterAllocationDTO);
    public WaterAllocationDTO searchWaterAllocation(String waterAllocationId);
    public boolean updateWaterAllocation(WaterAllocationDTO waterAllocationDTO);
    public boolean deleteWaterAllocation(String waterAllocationId);
    public boolean existWaterAllocation(String waterAllocationId);
    public String generateWaterAllocationId();
    public String getWaterSourceIdForVillage(String villageId);
    public List<WaterAllocationDTO> getAllocationsByWaterSourceId(String waterSourceId);
    public List<WaterAllocationDTO> getAllWaterAllocations();
}
