package lk.wms.aquaflow.bo.custom;


import lk.wms.aquaflow.bo.SuperBO;
import lk.wms.aquaflow.dto.WaterSourceDTO;

import java.util.List;
import java.util.Map;

public interface WaterSourceBO extends SuperBO {
    public boolean addWaterSource(WaterSourceDTO waterSourceDTO);
    public WaterSourceDTO searchWaterSource(String waterSourceId);
    public boolean updateWaterSource(WaterSourceDTO waterSourceDTO);
    public boolean deleteWaterSource(String waterSourceId);
    public boolean existWaterSource(String waterSourceId);
    public String generateWaterSourceId();
    public WaterSourceDTO getWaterSourceById(String waterSourceId);
    public String getWaterSourceNameById(String waterSourceId);
    public List<WaterSourceDTO> getAllWaterSources();
    
    public Map<String, Integer> getStatusCount();
}
