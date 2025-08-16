package lk.wms.aquaflow.bo.custom;


import lk.wms.aquaflow.bo.SuperBO;
import lk.wms.aquaflow.dto.WaterAllocationDTO;
import lk.wms.aquaflow.dto.WaterSourceDTO;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface WaterSourceBO extends SuperBO {
    public boolean addWaterSource(WaterSourceDTO waterSourceDTO) throws SQLException, ClassNotFoundException;
    public WaterSourceDTO searchWaterSource(String waterSourceId);
    public boolean updateWaterSource(WaterSourceDTO waterSourceDTO) throws SQLException, ClassNotFoundException;
    public boolean deleteWaterSource(String waterSourceId) throws SQLException, ClassNotFoundException;
    public boolean existWaterSource(String waterSourceId);
    public String generateWaterSourceId() throws SQLException, ClassNotFoundException;
    public WaterSourceDTO getWaterSourceById(String waterSourceId) throws SQLException, ClassNotFoundException;
    public String getWaterSourceNameById(String waterSourceId) throws SQLException, ClassNotFoundException;
    public List<WaterSourceDTO> getAllWaterSources() throws SQLException, ClassNotFoundException;
    public Map<String, Integer> getStatusCount();
}