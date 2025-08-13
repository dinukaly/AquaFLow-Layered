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
    private String ownerEmail; // For displaying in the table
    
    // Constructor without ownerEmail for database operations
    public ComplaintDTO(String complaintId, String date, String description, String status, String houseId) {
        this.complaintId = complaintId;
        this.date = date;
        this.description = description;
        this.status = status;
        this.houseId = houseId;
    }
}
