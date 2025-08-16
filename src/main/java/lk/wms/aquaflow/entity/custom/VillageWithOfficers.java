package lk.wms.aquaflow.entity.custom;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class VillageWithOfficers {
    private String villageId;
    private String villageName;
    private int population;
    private double waterRequirement;
    private double area;
    private String district;
    private String officerId;
    private String officerName;
}
