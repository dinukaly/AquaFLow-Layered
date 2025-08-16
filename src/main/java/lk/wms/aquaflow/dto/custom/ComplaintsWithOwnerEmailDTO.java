package lk.wms.aquaflow.dto.custom;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ComplaintsWithOwnerEmailDTO {
    private String complaintId;
    private String date;
    private String description;
    private String status;
    private String houseId;
    private String ownerEmail;
}
