package lk.wms.aquaflow.bo.custom;

import lk.wms.aquaflow.bo.SuperBO;
import lk.wms.aquaflow.dto.InventoryDTO;

import java.util.List;

public interface InventoryBO extends SuperBO {
    public boolean addInventory(InventoryDTO inventoryDTO);
    public InventoryDTO searchInventory(String inventoryId);
    public boolean updateInventory(InventoryDTO inventoryDTO);
    public boolean deleteInventory(String inventoryId);
    public boolean existInventory(String inventoryId);
    public String generateInventoryId();
    public InventoryDTO getInventoryById(String inventoryId);

    public List<InventoryDTO> getAllInventories();
}
