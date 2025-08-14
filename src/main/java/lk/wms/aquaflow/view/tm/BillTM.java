package lk.wms.aquaflow.view.tm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BillTM {
    private String billId;
    private String householdName;
    private String villageName;
    private String billingDate;
    private String amount;
    private String status;
}
