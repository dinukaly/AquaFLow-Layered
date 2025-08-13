package lk.wms.aquaflow.dto;

import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class WaterAllocationDTO {
    private String allocationId;
    private double allocationAmount;
    private String waterSourceId;
    private String villageId;// Foreign key to village table
    private LocalDate allocationDate;

}