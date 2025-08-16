package lk.wms.aquaflow.dao.custom.impl;

import lk.wms.aquaflow.dao.custom.WaterDistributionDAO;
import lk.wms.aquaflow.entity.WaterDistribution;
import lk.wms.aquaflow.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class WaterDistributionDAOImpl implements WaterDistributionDAO {

    @Override
    public boolean add(WaterDistribution waterDistribution) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO water_distribution VALUES (?,?,?,?,?,?)", waterDistribution.getDistributionId(), waterDistribution.getTotalAllocation(), waterDistribution.getStatus(), waterDistribution.getUsedAmount(), waterDistribution.getDistributionDate(), waterDistribution.getVillageId());
    }

    @Override
    public boolean update(WaterDistribution waterDistribution) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE water_distribution SET total_allocation=?, status=?, used_amount=?, distribution_date=?, village_id=? WHERE distribution_id=?", waterDistribution.getTotalAllocation(), waterDistribution.getStatus(), waterDistribution.getUsedAmount(), waterDistribution.getDistributionDate(), waterDistribution.getVillageId(), waterDistribution.getDistributionId());
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM water_distribution WHERE distribution_id=?", id);
    }

    @Override
    public String generateNewId() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT distribution_id FROM water_distribution ORDER BY distribution_id DESC LIMIT 1");
        if (rst.next()) {
            String id = rst.getString(1);
            String prefix = id.replaceAll("[0-9]", "");
            int number = Integer.parseInt(id.replaceAll("[^0-9]", ""));
            int newId = number + 1;
            return prefix + String.format("%03d", newId);
        } else {
            return "W001";
        }
    }

    @Override
    public WaterDistribution get(String id) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT * FROM water_distribution WHERE distribution_id=?", id);
        if (rst.next()) {
            return new WaterDistribution(rst.getString(1), rst.getDouble(2), rst.getString(3), rst.getDouble(4), rst.getDate(5), rst.getString(6));
        }
        return null;
    }

    @Override
    public ArrayList<WaterDistribution> getAll() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT * FROM water_distribution");
        ArrayList<WaterDistribution> allDistributions = new ArrayList<>();
        while (rst.next()) {
            allDistributions.add(new WaterDistribution(rst.getString(1), rst.getDouble(2), rst.getString(3), rst.getDouble(4), rst.getDate(5), rst.getString(6)));
        }
        return allDistributions;
    }

    @Override
    public WaterDistribution search(String keyword) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT * FROM water_distribution WHERE distribution_id=?", keyword);
        if (rst.next()) {
            return new WaterDistribution(rst.getString(1), rst.getDouble(2), rst.getString(3), rst.getDouble(4), rst.getDate(5), rst.getString(6));
        }
        return null;
    }
}