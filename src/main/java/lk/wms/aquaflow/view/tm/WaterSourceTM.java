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
    private String waterSourceId;
    private String location;
    private String sourceType;
    private String villageName; // Will be populated later if needed
    private String status;
}
