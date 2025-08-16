package lk.wms.aquaflow.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class HouseholdDTO {
    private String houseId;
    private String ownerName;
    private String address;
    private int noOfMembers;
    private String email;
    private String villageId;
//    private String villageName;  // Added for joined query results
//
//    // Add this constructor for queries that don't need villageName
//    public HouseholdDTO(String houseId, String ownerName, String address,
//                        int noOfMembers, String email, String villageId) {
//        this(houseId, ownerName, address, noOfMembers, email, villageId, null);
//    }
}
