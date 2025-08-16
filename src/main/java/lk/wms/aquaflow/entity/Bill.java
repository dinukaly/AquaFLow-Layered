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
        private double totalAmountOfUnits;  // Changed to double
        private double costPerUnit;         // Changed to double
        private String status;
        private double totalCost;           // Changed to double
        private Date billDate;              // Changed to Date
        private Date dueDate;               // Changed to Date
        private String consumptionId;
    }