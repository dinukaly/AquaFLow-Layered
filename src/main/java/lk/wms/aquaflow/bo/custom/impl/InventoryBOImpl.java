package lk.wms.aquaflow.bo.custom.impl;

import lk.wms.aquaflow.bo.custom.InventoryBO;
import lk.wms.aquaflow.dto.InventoryDTO;

import java.util.List;

public class InventoryBOImpl implements InventoryBO {
    @Override
    public boolean addInventory(InventoryDTO inventoryDTO) {
        return false;
    }

    @Override
    public InventoryDTO searchInventory(String inventoryId) {
        return null;
    }

    @Override
    public boolean updateInventory(InventoryDTO inventoryDTO) {
        return false;
    }

    @Override
    public boolean deleteInventory(String inventoryId) {
        return false;
    }

    @Override
    public boolean existInventory(String inventoryId) {
        return false;
    }

    @Override
    public String generateInventoryId() {
        return "";
    }

    @Override
    public List<InventoryDTO> getAllInventories() {
        return List.of();
    }
}
