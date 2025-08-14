package lk.wms.aquaflow.bo.custom.impl;

import lk.wms.aquaflow.bo.custom.WaterDistributionBO;
import lk.wms.aquaflow.dto.WaterDistributionDTO;

import java.util.List;

public class WaterDistributionBOImpl implements WaterDistributionBO {
    @Override
    public boolean addWaterDistribution(WaterDistributionDTO waterDistributionDTO) {
        return false;
    }

    @Override
    public boolean updateWaterDistribution(WaterDistributionDTO waterDistributionDTO) {
        return false;
    }

    @Override
    public WaterDistributionDTO searchWaterDistribution(String waterDistributionId) {
        return null;
    }

    @Override
    public boolean existWaterDistribution(String waterDistributionId) {
        return false;
    }

    @Override
    public String generateWaterDistributionId() {
        return "";
    }

    @Override
    public boolean deleteWaterDistribution(String ring) {
        return false;
    }

    @Override
    public WaterDistributionDTO getDistributionById(String distributionId) {
        return null;
    }

    @Override
    public List<WaterDistributionDTO> getAllDistributions() {
        return List.of();
    }

}
