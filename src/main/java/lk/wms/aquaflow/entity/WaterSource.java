package lk.wms.aquaflow.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class WaterSource {
    private String watersource_id;
    private String source_name;
    private String source_type;
    private String location;
    private double capacity;
    private double remaining_capacity;
    private String status;
    public WaterSource(String watersource_id, String source_name, String source_type, String location, double capacity, String status) {
        this.watersource_id = watersource_id;
        this.source_name = source_name;
        this.source_type = source_type;
        this.location = location;
        this.capacity = capacity;
        this.status = status;
    }
}