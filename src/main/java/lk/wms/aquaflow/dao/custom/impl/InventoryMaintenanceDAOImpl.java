package lk.wms.aquaflow.dao.custom.impl;

import lk.wms.aquaflow.dao.custom.InventoryMaintenanceDAO;
import lk.wms.aquaflow.entity.InventoryMaintenance;
import lk.wms.aquaflow.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InventoryMaintenanceDAOImpl implements InventoryMaintenanceDAO {

    @Override
    public boolean add(InventoryMaintenance inventoryMaintenance) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO inventory_maintenance (inventory_id, maintenance_id, quantity_used, date_used) VALUES (?, ?, ?, ?)",
                inventoryMaintenance.getInventoryId(),
                inventoryMaintenance.getMaintenanceId(),
                inventoryMaintenance.getQuantityUsed(),
                inventoryMaintenance.getDateUsed());
    }

    @Override
    public boolean update(InventoryMaintenance inventoryMaintenance) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE inventory_maintenance SET quantity_used = ?, date_used = ? WHERE inventory_id = ? AND maintenance_id = ?",
                inventoryMaintenance.getQuantityUsed(),
                inventoryMaintenance.getDateUsed(),
                inventoryMaintenance.getInventoryId(),
                inventoryMaintenance.getMaintenanceId());
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM inventory_maintenance WHERE inventory_id = ? AND maintenance_id = ?", id.split(":")[0], id.split(":")[1]);
    }

    @Override
    public String generateNewId() throws SQLException, ClassNotFoundException {
        return "";
    }

    @Override
    public InventoryMaintenance get(String id) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT * FROM inventory_maintenance WHERE inventory_id = ? AND maintenance_id = ?", id.split(":")[0], id.split(":")[1]);
        if (rst.next()) {
            return new InventoryMaintenance(
                    rst.getString("inventory_id"),
                    rst.getString("maintenance_id"),
                    rst.getInt("quantity_used"),
                    rst.getDate("date_used").toLocalDate()
            );
        }
        return null;
    }

    @Override
    public ArrayList<InventoryMaintenance> getAll() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT * FROM inventory_maintenance");
        ArrayList<InventoryMaintenance> allInventoryMaintenance = new ArrayList<>();
        while (rst.next()) {
            allInventoryMaintenance.add(new InventoryMaintenance(
                    rst.getString("inventory_id"),
                    rst.getString("maintenance_id"),
                    rst.getInt("quantity_used"),
                    rst.getDate("date_used").toLocalDate()
            ));
        }
        return allInventoryMaintenance;
    }

    @Override
    public InventoryMaintenance search(String keyword) throws SQLException, ClassNotFoundException {
        return null;
    }
}