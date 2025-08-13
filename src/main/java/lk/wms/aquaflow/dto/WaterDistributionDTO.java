package lk.wms.aquaflow.dto;


import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class WaterDistributionDTO {
    private String distributionId;
    private double totalAllocation;
    private String status;
    private double usedAmount;
    private LocalDate distributionDate;
    private String villageId;
}

