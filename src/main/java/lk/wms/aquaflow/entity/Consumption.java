package lk.wms.aquaflow.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Consumption {
    private String consumptionId;
    private BigDecimal amountOfUnits;
    private Date startDate;
    private Date endDate;
    private String houseId; // FK
}