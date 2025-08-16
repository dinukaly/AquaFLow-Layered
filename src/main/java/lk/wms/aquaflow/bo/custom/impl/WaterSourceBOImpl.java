package lk.wms.aquaflow.bo.custom.impl;

import lk.wms.aquaflow.bo.custom.WaterSourceBO;
import lk.wms.aquaflow.dao.DAOFactory;
import lk.wms.aquaflow.dao.custom.WaterSourceDAO;
import lk.wms.aquaflow.dto.WaterAllocationDTO;
import lk.wms.aquaflow.dto.WaterSourceDTO;
import lk.wms.aquaflow.entity.WaterSource;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WaterSourceBOImpl implements WaterSourceBO {
    private final WaterSourceDAO waterSourceDAO = (WaterSourceDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.WATER_SOURCE);
    @Override
    public boolean addWaterSource(WaterSourceDTO waterSourceDTO) throws SQLException, ClassNotFoundException {
        return waterSourceDAO.add(
                new WaterSource(
                        waterSourceDTO.getWatersource_id(),
                        waterSourceDTO.getSource_name(),
                        waterSourceDTO.getSource_type(),
                        waterSourceDTO.getLocation(),
                        waterSourceDTO.getCapacity(),
                        waterSourceDTO.getStatus()));
    }

    @Override
    public WaterSourceDTO searchWaterSource(String waterSourceId) {
        return null;
    }

    @Override
    public boolean updateWaterSource(WaterSourceDTO waterSourceDTO) throws SQLException, ClassNotFoundException {
        return waterSourceDAO.update(
                new WaterSource(
                        waterSourceDTO.getWatersource_id(),
                        waterSourceDTO.getSource_name(),
                        waterSourceDTO.getSource_type(),
                        waterSourceDTO.getLocation(),
                        waterSourceDTO.getCapacity(),
                        waterSourceDTO.getStatus()
                )
        );
    }

    @Override
    public boolean deleteWaterSource(String waterSourceId) throws SQLException, ClassNotFoundException {
        return waterSourceDAO.delete(waterSourceId);
    }

    @Override
    public boolean existWaterSource(String waterSourceId) {
        return false;
    }

    @Override
    public String generateWaterSourceId() throws SQLException, ClassNotFoundException {
        return waterSourceDAO.generateNewId();

    }

    @Override
    public WaterSourceDTO getWaterSourceById(String waterSourceId) throws SQLException, ClassNotFoundException {
        WaterSource waterSource = waterSourceDAO.getWaterSourceById(waterSourceId);
        return new WaterSourceDTO(
                waterSource.getWatersource_id(),
                waterSource.getSource_name(),
                waterSource.getSource_type(),
                waterSource.getLocation(),
                waterSource.getCapacity(),
                waterSource.getStatus()
        );
    }


    @Override
    public String getWaterSourceNameById(String waterSourceId) throws SQLException, ClassNotFoundException {

        return waterSourceDAO.getWaterSourceNameById(waterSourceId);
    }

    @Override
    public List<WaterSourceDTO> getAllWaterSources() throws SQLException, ClassNotFoundException {
        List<WaterSourceDTO> waterSourceDTOList = new ArrayList<>();
        List<WaterSource> waterSourceList = waterSourceDAO.getAll();
        for (WaterSource waterSource : waterSourceList) {
            waterSourceDTOList.add(new WaterSourceDTO(
                    waterSource.getWatersource_id(),
                    waterSource.getSource_name(),
                    waterSource.getSource_type(),
                    waterSource.getLocation(),
                    waterSource.getCapacity(),
                    waterSource.getStatus()
            ));
        }
        return waterSourceDTOList;
    }
    
    @Override
    public Map<String, Integer> getStatusCount() {
        try {
            return waterSourceDAO.getStatusCount();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }


}