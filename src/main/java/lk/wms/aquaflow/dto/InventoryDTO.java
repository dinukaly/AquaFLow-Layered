package lk.wms.aquaflow.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class InventoryDTO {
    private String inventoryId;
    private String type;
    private String quantity;
    private double unitPrice;
    private String supplierId;
    private String supplierName;

    public InventoryDTO(String inventoryId, String type, String quantity, double unitPrice, String supplierId) {
        this.inventoryId = inventoryId;
        this.type = type;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.supplierId = supplierId;

    }

    @Override
    public String toString() {
        return type + " (" + inventoryId + ")";
    }
}

