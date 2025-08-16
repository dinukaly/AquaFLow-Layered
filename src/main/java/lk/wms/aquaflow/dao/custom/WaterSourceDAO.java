package lk.wms.aquaflow.dao.custom;

import lk.wms.aquaflow.dao.CrudDAO;
import lk.wms.aquaflow.entity.WaterSource;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface WaterSourceDAO extends CrudDAO<WaterSource> {
    public String getWaterSourceIdForVillage(String waterSourceId) throws SQLException, ClassNotFoundException;
    public List<WaterSource> getWaterAllocationsByWaterSourceId(String waterSourceId) throws SQLException, ClassNotFoundException;
    public String getWaterSourceNameById(String waterSourceId) throws SQLException, ClassNotFoundException;
    public WaterSource getWaterSourceById(String waterSourceId) throws SQLException, ClassNotFoundException;
    boolean existWaterSource(String waterSourceId);
    double getRemainingCapacity(String waterSourceId);
    boolean updateWaterSourceCapacity(String waterSourceId, double newCapacity);
    Map<String, Integer> getStatusCount() throws SQLException, ClassNotFoundException;
}