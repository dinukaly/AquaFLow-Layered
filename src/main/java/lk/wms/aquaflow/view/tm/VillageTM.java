package lk.wms.aquaflow.view.tm;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class VillageTM {
    private String villageId;
    private String villageName;
    private String population;
    private String waterRequirement;
    private String area;
    private String district;
    private String officerName;
}
