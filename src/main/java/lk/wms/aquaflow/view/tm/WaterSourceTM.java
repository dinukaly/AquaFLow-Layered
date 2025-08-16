package lk.wms.aquaflow.view.tm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class WaterSourceTM {
    private String watersource_id;
    private String source_name;
    private String source_type;
    private String location;
    private double capacity;
    private double remaining_capacity;
    private String status;
}