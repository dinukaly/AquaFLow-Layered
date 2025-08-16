package lk.wms.aquaflow.entity.custom;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class HouseholdWithVillage {
    private String houseId;
    private String ownerName;
    private String address;
    private int noOfMembers;
    private String email;
    private String villageId;
    private String villageName;
}
