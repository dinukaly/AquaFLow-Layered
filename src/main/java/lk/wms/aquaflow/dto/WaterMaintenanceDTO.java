package lk.wms.aquaflow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WaterMaintenanceDTO {
    private String maintenanceId;
    private String description;
    private LocalDate maintenanceDate;
    private double cost;
    private String status;
    private String villageId;
    private List<InventoryMaintenanceDTO> inventoryItems;
    
    // Constructor without completionDate for backward compatibility
    public WaterMaintenanceDTO(String maintenanceId, String description, LocalDate maintenanceDate,
                               double cost, String status, String villageId) {
        this(maintenanceId, description, maintenanceDate, cost, status, villageId, null);
    }
}
