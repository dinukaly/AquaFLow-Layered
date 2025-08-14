package lk.wms.aquaflow.bo.custom;

import lk.wms.aquaflow.bo.SuperBO;
import lk.wms.aquaflow.dto.WaterAllocationDTO;
import lk.wms.aquaflow.dto.WaterDistributionDTO;

import java.util.List;

public interface WaterDistributionBO extends SuperBO {
    public boolean addWaterDistribution(WaterDistributionDTO waterDistributionDTO);
    public boolean updateWaterDistribution(WaterDistributionDTO waterDistributionDTO);
    public WaterDistributionDTO searchWaterDistribution(String waterDistributionId);
    public boolean existWaterDistribution(String waterDistributionId);
    public String generateWaterDistributionId();
    public boolean deleteWaterDistribution(String ring);
    public WaterDistributionDTO getDistributionById(String distributionId);
    public List<WaterDistributionDTO> getAllDistributions();
}
