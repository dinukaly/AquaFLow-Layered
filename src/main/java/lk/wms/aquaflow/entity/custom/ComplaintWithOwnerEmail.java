package lk.wms.aquaflow.entity.custom;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ComplaintWithOwnerEmail {
    private String complaintId;
    private String date;
    private String description;
    private String status;
    private String houseId;
    private String ownerEmail;
}
