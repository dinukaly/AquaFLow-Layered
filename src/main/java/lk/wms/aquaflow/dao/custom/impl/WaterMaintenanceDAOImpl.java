package lk.wms.aquaflow.dao.custom.impl;

import lk.wms.aquaflow.dao.custom.WaterMaintenanceDAO;
import lk.wms.aquaflow.entity.WaterMaintenance;
import lk.wms.aquaflow.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class WaterMaintenanceDAOImpl implements WaterMaintenanceDAO {
    @Override
    public boolean add(WaterMaintenance waterMaintenance) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO water_maintenances VALUES (?,?,?,?,?,?)", waterMaintenance.getMaintenanceId(), waterMaintenance.getDescription(), waterMaintenance.getMaintenanceDate(), waterMaintenance.getCost(), waterMaintenance.getStatus(), waterMaintenance.getVillageId());
    }

    @Override
    public boolean update(WaterMaintenance waterMaintenance) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE water_maintenances SET description=?, maintenance_date=?, cost=?, status=?, village_id=? WHERE maintenance_id=?", waterMaintenance.getDescription(), waterMaintenance.getMaintenanceDate(), waterMaintenance.getCost(), waterMaintenance.getStatus(), waterMaintenance.getVillageId(), waterMaintenance.getMaintenanceId());
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM water_maintenances WHERE maintenance_id=?", id);
    }

    @Override
    public String generateNewId() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT maintenance_id FROM water_maintenances ORDER BY maintenance_id DESC LIMIT 1");
        if (rst.next()) {
            String id = rst.getString(1);
            int newId = Integer.parseInt(id.replaceAll("[^0-9]", "")) + 1;
            return String.format("M%03d", newId);
        } else {
            return "M001";
        }
    }

    @Override
    public WaterMaintenance get(String id) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT * FROM water_maintenances WHERE maintenance_id=?", id);
        if (rst.next()) {
            return new WaterMaintenance(rst.getString(1), rst.getString(2), rst.getDate(3).toLocalDate(), rst.getDouble(4), rst.getString(5), rst.getString(6));
        }
        return null;
    }

    @Override
    public ArrayList<WaterMaintenance> getAll() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT * FROM water_maintenances");
        ArrayList<WaterMaintenance> allMaintenances = new ArrayList<>();
        while (rst.next()) {
            allMaintenances.add(new WaterMaintenance(rst.getString(1), rst.getString(2), rst.getDate(3).toLocalDate(), rst.getDouble(4), rst.getString(5), rst.getString(6)));
        }
        return allMaintenances;
    }

    @Override
    public WaterMaintenance search(String keyword) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT * FROM water_maintenances WHERE maintenance_id=?", keyword);
        if (rst.next()) {
            return new WaterMaintenance(rst.getString(1), rst.getString(2), rst.getDate(3).toLocalDate(), rst.getDouble(4), rst.getString(5), rst.getString(6));
        }
        return null;
    }
}