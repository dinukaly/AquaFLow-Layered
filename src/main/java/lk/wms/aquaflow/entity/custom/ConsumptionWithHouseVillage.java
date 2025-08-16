package lk.wms.aquaflow.entity.custom;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class ConsumptionWithHouseVillage {
    private String consumptionId;
    private String amountOfUnits;
    private LocalDate startDate;
    private LocalDate endDate;
    private String houseId;
    private String ownerName;
    private String villageName;
}
