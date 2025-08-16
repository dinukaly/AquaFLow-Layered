package lk.wms.aquaflow.dto.custom;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class HouseholdWithVillageDTO {
    private String houseId;
    private String ownerName;
    private String address;
    private int noOfMembers;
    private String email;
    private String villageId;
    private String villageName;
}
