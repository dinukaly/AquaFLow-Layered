package lk.wms.aquaflow.entity.custom;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomBill {
    private String billId;
    private String totalAmountOfUnits;
    private String costPerUnit;
    private String status;
    private String totalCost;
    private String billDate;
    private String dueDate;
    private String consumptionId;

    private String householdName;
    private String villageName;
}