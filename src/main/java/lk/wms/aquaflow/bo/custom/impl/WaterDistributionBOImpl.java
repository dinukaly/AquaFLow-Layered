package lk.wms.aquaflow.bo.custom.impl;

import lk.wms.aquaflow.bo.custom.WaterDistributionBO;
import lk.wms.aquaflow.dao.DAOFactory;
import lk.wms.aquaflow.dao.custom.WaterDistributionDAO;
import lk.wms.aquaflow.dto.WaterDistributionDTO;
import lk.wms.aquaflow.entity.WaterDistribution;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WaterDistributionBOImpl implements WaterDistributionBO {
    private final WaterDistributionDAO waterDistributionDAO = (WaterDistributionDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.DISTRIBUTION);
    @Override
    public boolean addWaterDistribution(WaterDistributionDTO waterDistributionDTO) throws SQLException, ClassNotFoundException {
        return waterDistributionDAO.add(
                new WaterDistribution(
                        waterDistributionDTO.getDistributionId(),
                        waterDistributionDTO.getTotalAllocation(),
                        waterDistributionDTO.getStatus(),
                        waterDistributionDTO.getUsedAmount(),
                        Date.valueOf(waterDistributionDTO.getDistributionDate()),
                        waterDistributionDTO.getVillageId()
                )
        );
    }

    @Override
    public boolean updateWaterDistribution(WaterDistributionDTO waterDistributionDTO) throws SQLException, ClassNotFoundException {
        return waterDistributionDAO.update(
                new WaterDistribution(
                        waterDistributionDTO.getDistributionId(),
                        waterDistributionDTO.getTotalAllocation(),
                        waterDistributionDTO.getStatus(),
                        waterDistributionDTO.getUsedAmount(),
                        Date.valueOf(waterDistributionDTO.getDistributionDate()),
                        waterDistributionDTO.getVillageId()
                )
        );
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
    public String generateWaterDistributionId() throws SQLException, ClassNotFoundException {
        return waterDistributionDAO.generateNewId();
    }

    @Override
    public boolean deleteWaterDistribution(String ring) throws SQLException, ClassNotFoundException {
        return waterDistributionDAO.delete(ring);
    }

    @Override
    public WaterDistributionDTO getDistributionById(String distributionId) throws SQLException, ClassNotFoundException {
        WaterDistribution waterDistribution = waterDistributionDAO.get(distributionId);
        return new WaterDistributionDTO(
                waterDistribution.getDistributionId(),
                waterDistribution.getTotalAllocation(),
                waterDistribution.getStatus(),
                waterDistribution.getUsedAmount(),
                waterDistribution.getDistributionDate().toLocalDate(),
                waterDistribution.getVillageId()
        );
    }

    @Override
    public List<WaterDistributionDTO> getAllDistributions() throws SQLException, ClassNotFoundException {
        List<WaterDistribution> all = waterDistributionDAO.getAll();
        List<WaterDistributionDTO> dtos = new ArrayList<WaterDistributionDTO>();
        for (WaterDistribution waterDistribution : all) {
            dtos.add(
                    new WaterDistributionDTO(
                            waterDistribution.getDistributionId(),
                            waterDistribution.getTotalAllocation(),
                            waterDistribution.getStatus(),
                            waterDistribution.getUsedAmount(),
                            waterDistribution.getDistributionDate().toLocalDate(),
                            waterDistribution.getVillageId()
                    )
            );
        }
        return dtos;
    }

}
