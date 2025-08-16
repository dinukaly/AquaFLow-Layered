package lk.wms.aquaflow.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WaterMaintenance {
    private String maintenanceId;
    private String description;
    private LocalDate maintenanceDate;
    private double cost;
    private String status;
    private String villageId; // FK
}
