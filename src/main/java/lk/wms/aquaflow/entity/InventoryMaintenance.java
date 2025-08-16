package lk.wms.aquaflow.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryMaintenance {
    private String inventoryId; // PK (part)
    private String maintenanceId; // PK (part)
    private int quantityUsed;
    private LocalDate dateUsed;
}