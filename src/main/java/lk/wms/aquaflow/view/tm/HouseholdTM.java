package lk.wms.aquaflow.view.tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class HouseholdTM {

    private String houseId;
    private String ownerName;
    private String address;
    private int noOfMembers;
    private String email;
    private String villageName;  // Added for joined query results


}
