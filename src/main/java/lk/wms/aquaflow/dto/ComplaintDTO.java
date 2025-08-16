package lk.wms.aquaflow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ComplaintDTO {
    private String complaintId;
    private String date;
    private String description;
    private String status;
    private String houseId;

}
