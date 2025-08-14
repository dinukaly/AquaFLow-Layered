package lk.wms.aquaflow.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Complaint {
    private String complaintId;
    private Date date;
    private String description;
    private String status;
    private String houseId; // FK
}