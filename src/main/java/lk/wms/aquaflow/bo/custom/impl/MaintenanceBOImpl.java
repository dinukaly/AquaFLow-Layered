package lk.wms.aquaflow.bo.custom.impl;

import lk.wms.aquaflow.bo.custom.MaintenanceBO;
import lk.wms.aquaflow.dto.InventoryMaintenanceDTO;
import lk.wms.aquaflow.dto.WaterMaintenanceDTO;

import java.util.List;

public class MaintenanceBOImpl implements MaintenanceBO {
    @Override
    public boolean addMaintenance(WaterMaintenanceDTO maintenanceDTO) {
        return false;
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
    public String generateMaintenanceId() {
        return "";
    }

    @Override
    public List<WaterMaintenanceDTO> getAllMaintenances() {
        return List.of();
    }

    @Override
    public double calculateTotalCost(List<InventoryMaintenanceDTO> inventoryDTOs) {
        return 0;
    }
}
