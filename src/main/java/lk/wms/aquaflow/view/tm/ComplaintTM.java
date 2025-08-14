package lk.wms.aquaflow.view.tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ComplaintTM {
    private String complaintId;
    private String date;
    private String description;
    private String ownerEmail;
    private String status;
}
