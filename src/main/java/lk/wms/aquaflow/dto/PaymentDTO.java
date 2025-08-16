package lk.wms.aquaflow.dto;

import java.sql.Date;

public class PaymentDTO {
    private String paymentId;
    private Date paidDate;
    private double amount;
    private String paymentMethod;
    private String billId; // FK
}
