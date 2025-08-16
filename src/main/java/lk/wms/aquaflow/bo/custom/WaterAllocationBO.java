package lk.wms.aquaflow.bo.custom;

import lk.wms.aquaflow.bo.SuperBO;
import lk.wms.aquaflow.dto.WaterAllocationDTO;

import java.sql.SQLException;
import java.util.List;

public interface WaterAllocationBO extends SuperBO {
    public boolean allocateWater(WaterAllocationDTO waterAllocationDTO) throws SQLException;
    public WaterAllocationDTO searchWaterAllocation(String waterAllocationId);
    public boolean deleteWaterAllocation(String waterAllocationId) throws SQLException, ClassNotFoundException;
    public boolean updateWaterAllocation(WaterAllocationDTO waterAllocationDTO) throws SQLException, ClassNotFoundException;
    public String generateWaterAllocationId() throws SQLException, ClassNotFoundException;
    public String getWaterSourceIdForVillage(String villageId);
    public List<WaterAllocationDTO> getAllocationsByWaterSourceId(String waterSourceId);
    public List<WaterAllocationDTO> getAllWaterAllocations();
}