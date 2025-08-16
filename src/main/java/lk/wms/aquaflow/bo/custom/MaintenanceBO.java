package lk.wms.aquaflow.bo.custom;

import lk.wms.aquaflow.bo.SuperBO;
import lk.wms.aquaflow.dto.InventoryMaintenanceDTO;
import lk.wms.aquaflow.dto.WaterMaintenanceDTO;

import java.sql.SQLException;
import java.util.List;

public interface MaintenanceBO extends SuperBO {
    public boolean addMaintenance(WaterMaintenanceDTO maintenanceDTO) throws SQLException, ClassNotFoundException;
    public WaterMaintenanceDTO searchMaintenance(String maintenanceId);
    public boolean updateMaintenance(WaterMaintenanceDTO maintenanceDTO);
    public boolean deleteMaintenance(String maintenanceId);
    public boolean existMaintenance(String maintenanceId);
    public String generateMaintenanceId() throws SQLException, ClassNotFoundException;
    public List<WaterMaintenanceDTO> getAllMaintenances() throws SQLException, ClassNotFoundException;
    public double calculateTotalCost(List<InventoryMaintenanceDTO> inventoryDTOs);
}
