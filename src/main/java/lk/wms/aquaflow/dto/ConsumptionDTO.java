package lk.wms.aquaflow.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConsumptionDTO {
    private String consumptionId;
    private String amountOfUnits;
    private String startDate;
    private String endDate;
    private String houseId;
}
