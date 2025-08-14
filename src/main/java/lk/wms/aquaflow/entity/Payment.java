package lk.wms.aquaflow.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    private String paymentId;
    private Date paidDate;
    private BigDecimal amount;
    private String paymentMethod;
    private String billId; // FK
}
