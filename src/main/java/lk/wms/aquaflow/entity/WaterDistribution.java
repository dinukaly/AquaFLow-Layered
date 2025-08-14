package lk.wms.aquaflow.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WaterDistribution {
    private String distributionId;
    private BigDecimal totalAllocation;
    private String status;
    private BigDecimal usedAmount;
    private Date distributionDate;
    private String villageId; // FK
}