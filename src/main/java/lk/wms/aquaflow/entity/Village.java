package lk.wms.aquaflow.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Village {
    private String villageId;
    private String villageName;
    private int population;
    private BigDecimal waterRequirement;
    private BigDecimal area;
    private String district;
    private String officerId; // FK
}
