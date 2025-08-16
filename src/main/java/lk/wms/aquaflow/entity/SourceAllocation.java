package lk.wms.aquaflow.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SourceAllocation {
    private String allocationId;
    private double allocationAmount;
    private String watersourceId; // FK
    private String villageId; // FK
    private Date allocationDate;
}
