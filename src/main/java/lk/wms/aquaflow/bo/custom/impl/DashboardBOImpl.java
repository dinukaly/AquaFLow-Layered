package lk.wms.aquaflow.bo.custom.impl;

import lk.wms.aquaflow.bo.custom.DashboardBO;
import lk.wms.aquaflow.dto.VillageDTO;
import lk.wms.aquaflow.dto.WaterDistributionDTO;

import java.util.List;

public class DashboardBOImpl implements DashboardBO {
    @Override
    public boolean addNewDistribution(WaterDistributionDTO waterDistributionDTO) {
        return false;
    }

    @Override
    public boolean deleteDistribution(String distributionId) {
        return false;
    }

    @Override
    public WaterDistributionDTO getDistribution(String distributionId) {
        return null;
    }

    @Override
    public boolean updateDistribution(WaterDistributionDTO waterDistributionDTO) {
        return false;
    }

    @Override
    public String getWaterSourceNameForVillage(String villageId) {
        return "";
    }

    @Override
    public VillageDTO getVillageById(String villageId) {
        return null;
    }

    @Override
    public List<WaterDistributionDTO> getAllDistributions() {
        return List.of();
    }
}
