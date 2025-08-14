package lk.wms.aquaflow.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Household {
    private String houseId;
    private String ownerName;
    private String address;
    private int noOfMembers;
    private String email;
    private String villageId; // FK
}