package lk.wms.aquaflow.view.tm;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class InventoryTM {
    private String inventoryId;
    private String type;
    private String quantity;
    private String unitPrice;
    private String supplierName;
}

