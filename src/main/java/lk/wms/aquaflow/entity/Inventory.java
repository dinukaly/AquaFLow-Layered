package lk.wms.aquaflow.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Inventory {
    private String inventoryId;
    private String type;
    private String quantity;
    private double unitPrice;
    private String supplierId; // FK
}