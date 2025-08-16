package lk.wms.aquaflow.bo.custom;

import lk.wms.aquaflow.bo.SuperBO;
import lk.wms.aquaflow.dto.WaterAllocationDTO;
import lk.wms.aquaflow.dto.WaterDistributionDTO;

import java.sql.SQLException;
import java.util.List;

public interface WaterDistributionBO extends SuperBO {
    public boolean addWaterDistribution(WaterDistributionDTO waterDistributionDTO) throws SQLException, ClassNotFoundException;
    public boolean updateWaterDistribution(WaterDistributionDTO waterDistributionDTO) throws SQLException, ClassNotFoundException;
    public WaterDistributionDTO searchWaterDistribution(String waterDistributionId);
    public boolean existWaterDistribution(String waterDistributionId);
    public String generateWaterDistributionId() throws SQLException, ClassNotFoundException;
    public boolean deleteWaterDistribution(String ring) throws SQLException, ClassNotFoundException;
    public WaterDistributionDTO getDistributionById(String distributionId) throws SQLException, ClassNotFoundException;
    public List<WaterDistributionDTO> getAllDistributions() throws SQLException, ClassNotFoundException;
}
