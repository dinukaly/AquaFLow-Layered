package lk.wms.aquaflow.view.tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentTM {
    private String paymentId;
    private String paidDate;
    private double amount;
    private String paymentMethod;
}
