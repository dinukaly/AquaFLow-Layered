package lk.wms.aquaflow.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class VillageDTO {
    private String villageId;
    private String villageName;
    private int population;
    private double waterRequirement;
    private double area;
    private String district;
    private String officerId;
    //private String officerName;

//    public VillageDTO(String villageId, String villageName, int population, double waterRequirement, double area, String district, String officerId) {
//        this.villageId = villageId;
//        this.villageName = villageName;
//        this.population = population;
//        this.waterRequirement = waterRequirement;
//        this.area = area;
//        this.district = district;
//        this.officerId = officerId;
//
//    }

    @Override
    public String toString() {
        return villageName + " (" + villageId + ")";
    }
}
