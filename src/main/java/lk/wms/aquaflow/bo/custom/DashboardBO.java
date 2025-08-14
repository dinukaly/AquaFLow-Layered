package lk.wms.aquaflow.bo.custom;

import lk.wms.aquaflow.bo.SuperBO;
import lk.wms.aquaflow.dto.VillageDTO;
import lk.wms.aquaflow.dto.WaterDistributionDTO;

import java.util.List;

public interface DashboardBO extends SuperBO {
    public boolean addNewDistribution(WaterDistributionDTO waterDistributionDTO);
    public boolean deleteDistribution(String distributionId);
    public WaterDistributionDTO getDistribution(String distributionId);
    boolean updateDistribution(WaterDistributionDTO waterDistributionDTO);
    public String getWaterSourceNameForVillage(String villageId);
    public VillageDTO getVillageById(String villageId);
    public List<WaterDistributionDTO> getAllDistributions();
}
