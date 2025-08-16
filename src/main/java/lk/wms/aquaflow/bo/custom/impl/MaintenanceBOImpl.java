package lk.wms.aquaflow.bo.custom.impl;

import lk.wms.aquaflow.bo.custom.MaintenanceBO;
import lk.wms.aquaflow.dao.DAOFactory;
import lk.wms.aquaflow.dao.custom.InventoryDAO;
import lk.wms.aquaflow.dao.custom.InventoryMaintenanceDAO;
import lk.wms.aquaflow.dao.custom.WaterMaintenanceDAO;
import lk.wms.aquaflow.db.DBConnection;
import lk.wms.aquaflow.dto.InventoryMaintenanceDTO;
import lk.wms.aquaflow.dto.WaterMaintenanceDTO;
import lk.wms.aquaflow.entity.InventoryMaintenance;
import lk.wms.aquaflow.entity.WaterMaintenance;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MaintenanceBOImpl implements MaintenanceBO {
    private final WaterMaintenanceDAO waterMaintenanceDAO =
            (WaterMaintenanceDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.WATER_MAINTENANCE);
    private final InventoryMaintenanceDAO inventoryMaintenanceDAO =
            (InventoryMaintenanceDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.INVENTORY_MAINTENANCE);
    private final InventoryDAO inventoryDAO =
            (InventoryDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.INVENTORY);
    @Override
    public boolean addMaintenance(WaterMaintenanceDTO maintenanceDTO) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        try {
            connection.setAutoCommit(false);

            boolean mainSaved = waterMaintenanceDAO.add(
                    new WaterMaintenance(
                            maintenanceDTO.getMaintenanceId(),
                            maintenanceDTO.getDescription(),
                            maintenanceDTO.getMaintenanceDate(),
                            maintenanceDTO.getCost(),
                            maintenanceDTO.getStatus(),
                            maintenanceDTO.getVillageId()
                    )
            );
            if (!mainSaved) {
                connection.rollback();
                return false;
            }

            for (InventoryMaintenanceDTO inv : maintenanceDTO.getInventoryItems()) {
                boolean invSaved = inventoryMaintenanceDAO.add(
                        new InventoryMaintenance(
                                inv.getInventoryId(),
                                maintenanceDTO.getMaintenanceId(),
                                inv.getQuantityUsed(),
                                inv.getDateUsed()
                        )
                );
                if (!invSaved) {
                    connection.rollback();
                    return false;
                }
                boolean invUpdated = inventoryDAO.updateInventoryQuantity(
                        new InventoryMaintenance(
                                inv.getInventoryId(),
                                maintenanceDTO.getMaintenanceId(),
                                inv.getQuantityUsed(),
                                inv.getDateUsed()
                        )
                );
                if (!invUpdated) {
                    connection.rollback();
                    return false;
                }
            }

            connection.commit();
            return true;
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    @Override
    public WaterMaintenanceDTO searchMaintenance(String maintenanceId) {
        return null;
    }

    @Override
    public boolean updateMaintenance(WaterMaintenanceDTO maintenanceDTO) {
        return false;
    }

    @Override
    public boolean deleteMaintenance(String maintenanceId) {
        return false;
    }

    @Override
    public boolean existMaintenance(String maintenanceId) {
        return false;
    }

    @Override
    public String generateMaintenanceId() throws SQLException, ClassNotFoundException {
        return waterMaintenanceDAO.generateNewId();
    }

    @Override
    public List<WaterMaintenanceDTO> getAllMaintenances() throws SQLException, ClassNotFoundException {
        List<WaterMaintenance> all = waterMaintenanceDAO.getAll();
        ArrayList<WaterMaintenanceDTO> allMaintenances = new ArrayList<>();
        for (WaterMaintenance maintenance : all) {
            allMaintenances.add(new WaterMaintenanceDTO(
                    maintenance.getMaintenanceId(),
                    maintenance.getDescription(),
                    maintenance.getMaintenanceDate(),
                    maintenance.getCost(),
                    maintenance.getStatus(),
                    maintenance.getVillageId()
            ));
        }
        return allMaintenances;
    }

    @Override
    public double calculateTotalCost(List<InventoryMaintenanceDTO> inventoryDTOs) {
        return 0;
    }
}