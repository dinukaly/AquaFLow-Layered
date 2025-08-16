package lk.wms.aquaflow.dao.custom.impl;

import lk.wms.aquaflow.dao.custom.InventoryDAO;
import lk.wms.aquaflow.entity.Inventory;
import lk.wms.aquaflow.entity.InventoryMaintenance;
import lk.wms.aquaflow.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InventoryDAOImpl implements InventoryDAO {
    @Override
    public boolean add(Inventory inventory) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO inventory (inventory_id, type, quantity, unit_price, supplier_id) VALUES (?, ?, ?, ?, ?)",
                inventory.getInventoryId(),
                inventory.getType(),
                inventory.getQuantity(),
                inventory.getUnitPrice(),
                inventory.getSupplierId());
    }

    @Override
    public boolean update(Inventory inventory) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE inventory SET type = ?, quantity = ?, unit_price = ?, supplier_id = ? WHERE inventory_id = ?",
                inventory.getType(),
                inventory.getQuantity(),
                inventory.getUnitPrice(),
                inventory.getSupplierId(),
                inventory.getInventoryId());
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM inventory WHERE inventory_id = ?", id);
    }

    @Override
    public String generateNewId() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT inventory_id FROM inventory ORDER BY inventory_id DESC LIMIT 1");
        if (rst.next()) {
            String id = rst.getString("inventory_id");
            int newInventoryId = Integer.parseInt(id.replace("I", "")) + 1;
            return String.format("I%03d", newInventoryId);
        } else {
            return "I001";
        }
    }

    @Override
    public Inventory get(String id) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM inventory WHERE inventory_id = ?", id);
        if (resultSet.next()) {
            return new Inventory(
                    resultSet.getString("inventory_id"),
                    resultSet.getString("type"),
                    resultSet.getString("quantity"),
                    resultSet.getDouble("unit_price"),
                    resultSet.getString("supplier_id")
            );
        }
        return null;
    }

    @Override
    public ArrayList<Inventory> getAll() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM inventory");
        ArrayList<Inventory> inventories = new ArrayList<>();
        while (resultSet.next()) {
            inventories.add(new Inventory(
                    resultSet.getString("inventory_id"),
                    resultSet.getString("type"),
                    resultSet.getString("quantity"),
                    resultSet.getDouble("unit_price"),
                    resultSet.getString("supplier_id")
            ));
        }
        return inventories;
    }

    @Override
    public Inventory search(String keyword) throws SQLException, ClassNotFoundException {
        return get(keyword);
    }

    @Override
    public boolean updateInventoryQuantity(InventoryMaintenance inventoryMaintenance) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE inventory SET quantity = quantity - ? WHERE inventory_id = ?",
                inventoryMaintenance.getQuantityUsed(),
                inventoryMaintenance.getInventoryId());
    }

    @Override
    public Inventory getInventoryById(String inventoryId) throws SQLException, ClassNotFoundException {
        return get(inventoryId);
    }

    @Override
    public List<Inventory> getAvailableItems() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM inventory WHERE quantity > 0");
        List<Inventory> inventories = new ArrayList<>();
        while (result.next()) {
            inventories.add(new Inventory(
                    result.getString("inventory_id"),
                    result.getString("type"),
                    result.getString("quantity"),
                    result.getDouble("unit_price"),
                    result.getString("supplier_id")
            ));
        }
        return inventories;
    }
}