package lk.wms.aquaflow.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class WaterSourceDTO {
    private String watersource_id;
    private String source_name;
    private String source_type;
    private String location;
    private double capacity;
    private double remaining_capacity;
    private String status;

    public WaterSourceDTO(String watersource_id, String source_name, String source_type, String location, double capacity, String status) {
        this.watersource_id = watersource_id;
        this.source_name = source_name;
        this.source_type = source_type;
        this.location = location;
        this.capacity = capacity;
        this.status = status;
    }

    @Override
    public String toString() {
        return location + " (" + watersource_id + ")";
    }
}