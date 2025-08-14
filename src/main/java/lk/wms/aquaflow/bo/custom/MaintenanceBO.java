package lk.wms.aquaflow.bo.custom;

import lk.wms.aquaflow.bo.SuperBO;
import lk.wms.aquaflow.dto.InventoryMaintenanceDTO;
import lk.wms.aquaflow.dto.WaterMaintenanceDTO;

import java.util.List;

public interface MaintenanceBO extends SuperBO {
    public boolean addMaintenance(WaterMaintenanceDTO maintenanceDTO);
    public WaterMaintenanceDTO searchMaintenance(String maintenanceId);
    public boolean updateMaintenance(WaterMaintenanceDTO maintenanceDTO);
    public boolean deleteMaintenance(String maintenanceId);
    public boolean existMaintenance(String maintenanceId);
    public String generateMaintenanceId();
    public List<WaterMaintenanceDTO> getAllMaintenances();
    public double calculateTotalCost(List<InventoryMaintenanceDTO> inventoryDTOs);
}
