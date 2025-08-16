package lk.wms.aquaflow.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BillDTO {
    private String billId;
    private String totalAmountOfUnits;
    private String costPerUnit;
    private String status;
    private String totalCost;
    private String billDate;
    private String dueDate;
    private String consumptionId;

}