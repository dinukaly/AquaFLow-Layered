package lk.wms.aquaflow.bo.custom.impl;

import lk.wms.aquaflow.bo.custom.WaterAllocationBO;
import lk.wms.aquaflow.dao.DAOFactory;
import lk.wms.aquaflow.dao.custom.WaterAllocationDAO;
import lk.wms.aquaflow.dao.custom.WaterSourceDAO;
import lk.wms.aquaflow.db.DBConnection;
import lk.wms.aquaflow.dto.WaterAllocationDTO;
import lk.wms.aquaflow.entity.SourceAllocation;
import lk.wms.aquaflow.entity.WaterSource;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public class WaterAllocationBOImpl implements WaterAllocationBO {
    private final WaterAllocationDAO waterAllocationDAO = (WaterAllocationDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.WATER_ALLOCATION);
    private final WaterSourceDAO waterSourceDAO = (WaterSourceDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.WATER_SOURCE);

    @Override
    public boolean allocateWater(WaterAllocationDTO allocationDTO) throws SQLException {
        try {
            // Step 0.1: Check if water source exists
            boolean exists = waterSourceDAO.existWaterSource(allocationDTO.getWaterSourceId());
            if (!exists) {
                System.out.println("Water source not found. Allocation cancelled.");
                return false;
            }

            // Step 0.2: Check remaining capacity
            double remainingCapacity = waterSourceDAO.getRemainingCapacity(allocationDTO.getWaterSourceId());
            if (allocationDTO.getAllocationAmount() > remainingCapacity) {
                System.out.println("Not enough remaining capacity. Allocation cancelled.");
                return false;
            }

            // Step 1: Start transaction
            Connection connection = DBConnection.getInstance().getConnection();
            connection.setAutoCommit(false);

            try {
                // Insert allocation record
                boolean allocationSaved = waterAllocationDAO.addAllocation(
                        new SourceAllocation(
                                allocationDTO.getAllocationId(),
                                allocationDTO.getAllocationAmount(),
                                allocationDTO.getWaterSourceId(),
                                allocationDTO.getVillageId(),
                                Date.valueOf(allocationDTO.getAllocationDate())
                        )
                );
                if (!allocationSaved) {
                    connection.rollback();
                    return false;
                }

                // Update remaining capacity
                boolean capacityUpdated = waterSourceDAO.updateWaterSourceCapacity(
                        allocationDTO.getWaterSourceId(),
                        remainingCapacity - allocationDTO.getAllocationAmount()
                );
                if (!capacityUpdated) {
                    connection.rollback();
                    return false;
                }

                // Commit transaction
                connection.commit();
                return true;

            } catch (SQLException e) {
                connection.rollback();
                e.printStackTrace();
                return false;
            } finally {
                connection.setAutoCommit(true);
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public WaterAllocationDTO searchWaterAllocation(String waterAllocationId) {
        return null;
    }

    @Override
    public boolean deleteWaterAllocation(String waterAllocationId) throws SQLException, ClassNotFoundException {
        return waterAllocationDAO.delete(waterAllocationId);
    }

    @Override
    public String generateWaterAllocationId() throws SQLException, ClassNotFoundException {
        return waterAllocationDAO.generateNewId();
    }

    @Override
    public String getWaterSourceIdForVillage(String villageId) {
        return waterAllocationDAO.getWaterSourceIdForVillage(villageId);
    }

    @Override
    public List<WaterAllocationDTO> getAllocationsByWaterSourceId(String waterSourceId) {
        List<SourceAllocation> allocations = waterAllocationDAO.getAllocationsByWaterSourceId(waterSourceId);
        List<WaterAllocationDTO> allocationDTOs = new java.util.ArrayList<>();
        for (SourceAllocation allocation : allocations) {
            allocationDTOs.add(new WaterAllocationDTO(
                    allocation.getAllocationId(),
                    allocation.getAllocationAmount(),
                    allocation.getWatersourceId(),
                    allocation.getVillageId(),
                    allocation.getAllocationDate().toLocalDate()
            ));
        }
        return allocationDTOs;
    }

    @Override
    public List<WaterAllocationDTO> getAllWaterAllocations() {
        try {
            List<SourceAllocation> all = waterAllocationDAO.getAll();
            List<WaterAllocationDTO> dtoList = new java.util.ArrayList<>();
            for (SourceAllocation sourceAllocation : all) {
                dtoList.add(new WaterAllocationDTO(sourceAllocation.getAllocationId(), sourceAllocation.getAllocationAmount(), sourceAllocation.getWatersourceId(), sourceAllocation.getVillageId(), sourceAllocation.getAllocationDate().toLocalDate()));
            }
            return dtoList;
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean updateWaterAllocation(WaterAllocationDTO allocationDTO) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        connection.setAutoCommit(false);

        try {
            SourceAllocation oldAllocation = waterAllocationDAO.get(allocationDTO.getAllocationId());
            if (oldAllocation == null) {
                return false;
            }

            double oldAmount = oldAllocation.getAllocationAmount();
            double newAmount = allocationDTO.getAllocationAmount();

            double remainingCapacity = waterSourceDAO.getRemainingCapacity(allocationDTO.getWaterSourceId());
            double newRemainingCapacity = remainingCapacity + oldAmount - newAmount;

            if (newRemainingCapacity < 0) {
                connection.rollback();
                return false;
            }

            boolean allocationUpdated = waterAllocationDAO.update(new SourceAllocation(
                    allocationDTO.getAllocationId(),
                    allocationDTO.getAllocationAmount(),
                    allocationDTO.getWaterSourceId(),
                    allocationDTO.getVillageId(),
                    Date.valueOf(allocationDTO.getAllocationDate())
            ));

            if (!allocationUpdated) {
                connection.rollback();
                return false;
            }

            boolean capacityUpdated = waterSourceDAO.updateWaterSourceCapacity(
                    allocationDTO.getWaterSourceId(),
                    newRemainingCapacity
            );

            if (!capacityUpdated) {
                connection.rollback();
                return false;
            }

            connection.commit();
            return true;

        } catch (SQLException | ClassNotFoundException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }
}