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


    @Override
    public String toString() {
        return type + " (" + inventoryId + ")";
    }
}

