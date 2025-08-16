package lk.wms.aquaflow.dao.custom.impl;

import lk.wms.aquaflow.dao.custom.WaterSourceDAO;
import lk.wms.aquaflow.entity.WaterSource;
import lk.wms.aquaflow.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WaterSourceDAOImpl implements WaterSourceDAO {

    @Override
    public String getWaterSourceIdForVillage(String waterSourceId) throws SQLException, ClassNotFoundException {

        return waterSourceId;
    }

    @Override
    public List<WaterSource> getWaterAllocationsByWaterSourceId(String waterSourceId) throws SQLException, ClassNotFoundException {

        return new ArrayList<>();
    }

    @Override
    public String getWaterSourceNameById(String waterSourceId) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT source_name FROM water_source WHERE watersource_id=?", waterSourceId);
        if (rst.next()) {
            return rst.getString(1);
        }
        return null;
    }

    @Override
    public WaterSource getWaterSourceById(String waterSourceId) throws SQLException, ClassNotFoundException {
        return get(waterSourceId);
    }

    @Override
    public boolean existWaterSource(String waterSourceId) {
        try {
            return get(waterSourceId) != null;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public double getRemainingCapacity(String waterSourceId) {
        try {
            WaterSource source = get(waterSourceId);
            if (source != null) {
                return source.getCapacity();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public boolean updateWaterSourceCapacity(String waterSourceId, double newCapacity) {
        try {
            return CrudUtil.execute("UPDATE water_source SET capacity=? WHERE watersource_id=?", newCapacity, waterSourceId);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Map<String, Integer> getStatusCount() throws SQLException, ClassNotFoundException {
        String sql = "SELECT status, COUNT(*) FROM water_source GROUP BY status";
        ResultSet rst = CrudUtil.execute(sql);
        Map<String, Integer> statusCountMap = new HashMap<>();
        while (rst.next()) {
            statusCountMap.put(rst.getString(1), rst.getInt(2));
        }
        return statusCountMap;
    }

    @Override
    public boolean add(WaterSource waterSource) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO water_source VALUES (?,?,?,?,?,?)", waterSource.getWatersource_id(), waterSource.getSource_name(), waterSource.getSource_type(), waterSource.getLocation(), waterSource.getCapacity(), waterSource.getStatus());
    }

    @Override
    public boolean update(WaterSource waterSource) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE water_source SET source_name=?, source_type=?, location=?, capacity=?, status=? WHERE watersource_id=?", waterSource.getSource_name(), waterSource.getSource_type(), waterSource.getLocation(), waterSource.getCapacity(), waterSource.getStatus(), waterSource.getWatersource_id());
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM water_source WHERE watersource_id=?", id);
    }

    @Override
    public String generateNewId() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT watersource_id FROM water_source ORDER BY watersource_id DESC LIMIT 1");
        if (rst.next()) {
            String id = rst.getString(1);
            int newId = Integer.parseInt(id.replace("W", "")) + 1;
            return String.format("W%03d", newId);
        } else {
            return "W001";
        }
    }

    @Override
    public WaterSource get(String id) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT * FROM water_source WHERE watersource_id=?", id);
        if (rst.next()) {
            return new WaterSource(rst.getString(1), rst.getString(2), rst.getString(3), rst.getString(4), rst.getDouble(5), rst.getString(6));
        }
        return null;
    }

    @Override
    public ArrayList<WaterSource> getAll() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT * FROM water_source");
        ArrayList<WaterSource> allSources = new ArrayList<>();
        while (rst.next()) {
            allSources.add(new WaterSource(rst.getString(1), rst.getString(2), rst.getString(3), rst.getString(4), rst.getDouble(5), rst.getString(6)));
        }
        return allSources;
    }

    @Override
    public WaterSource search(String keyword) throws SQLException, ClassNotFoundException {
        return get(keyword);
    }
}