package lk.wms.aquaflow.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Date;

@Data
@NoArgsConstructor

@AllArgsConstructor
public class Bill {
    private String billId;
    private BigDecimal totalAmountOfUnits;
    private BigDecimal costPerUnit;
    private String status;
    private BigDecimal totalCost;
    private Date billDate;
    private Date dueDate;
    private String consumptionId; // FK
}