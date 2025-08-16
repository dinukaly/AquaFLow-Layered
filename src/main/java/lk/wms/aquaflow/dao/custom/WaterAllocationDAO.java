package lk.wms.aquaflow.dao.custom;

import lk.wms.aquaflow.dao.CrudDAO;
import lk.wms.aquaflow.entity.SourceAllocation;

import java.sql.SQLException;
import java.util.List;

public interface WaterAllocationDAO extends CrudDAO<SourceAllocation> {
    public boolean addAllocation(SourceAllocation allocation) throws SQLException, ClassNotFoundException;
    public boolean updateAllocation(SourceAllocation allocation) throws SQLException, ClassNotFoundException;
    public String getWaterSourceIdForVillage(String villageId);
    public List<SourceAllocation> getAllocationsByWaterSourceId(String waterSourceId);

}
