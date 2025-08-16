package lk.wms.aquaflow.dto.custom;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class InventoryWithSupplierNameDTO {
    private String inventoryId;
    private String type;
    private String quantity;
    private double unitPrice;
    private String supplierId;
    private String supplierName;
}
