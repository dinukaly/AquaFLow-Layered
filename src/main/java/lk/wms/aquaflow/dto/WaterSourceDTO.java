package lk.wms.aquaflow.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class WaterSourceDTO {
    private String waterSourceId;
    private String sourceName;
    private String sourceType;
    private String location;
    private double capacity;
    private String status;

    @Override
    public String toString() {
        return location + " (" + waterSourceId + ")";
    }
}

