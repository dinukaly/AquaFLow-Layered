package lk.wms.aquaflow.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WaterMaintenance {
    private String maintenanceId;
    private String description;
    private Date maintenanceDate;
    private BigDecimal cost;
    private String status;
    private String villageId; // FK
}
