package lk.wms.aquaflow.dao.custom;

import lk.wms.aquaflow.dao.CrudDAO;
import lk.wms.aquaflow.entity.Inventory;
import lk.wms.aquaflow.entity.InventoryMaintenance;

import java.sql.SQLException;
import java.util.List;

public interface InventoryDAO extends CrudDAO<Inventory> {
    boolean updateInventoryQuantity(InventoryMaintenance inventoryMaintenance) throws SQLException, ClassNotFoundException;
    Inventory getInventoryById(String inventoryId) throws SQLException, ClassNotFoundException;
    List<Inventory> getAvailableItems() throws SQLException, ClassNotFoundException;
}