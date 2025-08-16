package lk.wms.aquaflow.bo.custom;

import lk.wms.aquaflow.bo.SuperBO;
import lk.wms.aquaflow.dto.InventoryDTO;
import lk.wms.aquaflow.dto.custom.InventoryWithSupplierNameDTO;

import java.sql.SQLException;
import java.util.List;

public interface InventoryBO extends SuperBO {
    public boolean addInventory(InventoryDTO inventoryDTO) throws SQLException, ClassNotFoundException;
    public InventoryDTO searchInventory(String inventoryId);
    public boolean updateInventory(InventoryDTO inventoryDTO) throws SQLException, ClassNotFoundException;
    public boolean deleteInventory(String inventoryId) throws SQLException, ClassNotFoundException;
    public boolean existInventory(String inventoryId);
    public String generateInventoryId() throws SQLException, ClassNotFoundException;
    public InventoryWithSupplierNameDTO getInventoryByIdWithSupplierName(String inventoryId) throws SQLException, ClassNotFoundException;
    public InventoryDTO getInventoryById(String inventoryId) throws SQLException, ClassNotFoundException;

    public List<InventoryWithSupplierNameDTO> getAllInventories() throws SQLException, ClassNotFoundException;
    List<InventoryDTO> getAvailableInventoryItems() throws SQLException, ClassNotFoundException;
}