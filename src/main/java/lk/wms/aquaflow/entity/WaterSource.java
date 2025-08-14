package lk.wms.aquaflow.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WaterSource {
    private String watersourceId;
    private String sourceName;
    private String sourceType;
    private String location;
    private BigDecimal capacity;
    private String status;
    private BigDecimal remainingCapacity;
}