package lk.wms.aquaflow.view.tm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DistributionTM {
    private String distributionId;
    private String distributionDate;
    private String villageName;
    private String sourceName;
    private String amount;
    private String status;
}
