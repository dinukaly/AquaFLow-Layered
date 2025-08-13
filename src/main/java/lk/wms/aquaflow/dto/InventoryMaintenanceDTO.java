package lk.wms.aquaflow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryMaintenanceDTO {
    private String inventoryId;
    private String maintenanceId;
    private int quantityUsed;
    private LocalDate dateUsed;
}
