package lk.wms.aquaflow.dto.custom;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Date;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class ConsumptionWithHouseVillageDTO {
    private String consumptionId;
    private String amountOfUnits;
    private Date startDate;
    private Date endDate;
    private String houseId;
    private String ownerName;
    private String villageName;
    private String previousMonth;
    private String change;

    public ConsumptionWithHouseVillageDTO(String consumptionId, String amountOfUnits, Date startDate, Date endDate, String houseId, String ownerName, String villageName) {
        this.consumptionId = consumptionId;
        this.amountOfUnits = amountOfUnits;
        this.startDate = startDate;
        this.endDate = endDate;
        this.houseId = houseId;
        this.ownerName = ownerName;
        this.villageName = villageName;
    }
}