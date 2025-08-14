package lk.wms.aquaflow.view.tm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConsumptionTM {
    private String consumptionId;
    private String householdName;
    private String villageName;
    private String currentConsumption;
    private String previousMonth;
    private String change;
    private String houseId; // Hidden field for reference
}
