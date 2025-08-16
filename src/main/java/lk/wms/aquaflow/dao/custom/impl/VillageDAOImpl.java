package lk.wms.aquaflow.dao.custom.impl;

import lk.wms.aquaflow.dao.custom.VillageDAO;
import lk.wms.aquaflow.entity.Village;
import lk.wms.aquaflow.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VillageDAOImpl implements VillageDAO {
    @Override
    public boolean add(Village village) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO village VALUES (?,?,?,?,?,?,?)", village.getVillageId(), village.getVillageName(), village.getPopulation(), village.getWaterRequirement(), village.getArea(), village.getDistrict(), village.getOfficerId());
    }

    @Override
    public boolean update(Village village) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE village SET village_name=?, population=?, water_requirement=?, area=?, district=?, officer_id=? WHERE village_id=?", village.getVillageName(), village.getPopulation(), village.getWaterRequirement(), village.getArea(), village.getDistrict(), village.getOfficerId(), village.getVillageId());
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM village WHERE village_id=?", id);
    }

    @Override
    public String generateNewId() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT village_id FROM village ORDER BY village_id DESC LIMIT 1");
        if (rst.next()) {
            String id = rst.getString(1);
            int newId = Integer.parseInt(id.replace("V", "")) + 1;
            return String.format("V%03d", newId);
        } else {
            return "V001";
        }
    }

    @Override
    public Village get(String id) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT * FROM village WHERE village_id=?", id);
        if (rst.next()) {
            return new Village(rst.getString(1), rst.getString(2), rst.getInt(3), rst.getDouble(4), rst.getDouble(5), rst.getString(6), rst.getString(7));
        }
        return null;
    }

    @Override
    public ArrayList<Village> getAll() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT * FROM village");
        ArrayList<Village> allVillages = new ArrayList<>();
        while (rst.next()) {
            allVillages.add(new Village(rst.getString(1), rst.getString(2), rst.getInt(3), rst.getDouble(4), rst.getDouble(5), rst.getString(6), rst.getString(7)));
        }
        return allVillages;
    }

    @Override
    public Village search(String keyword) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT * FROM village WHERE village_id=? OR village_name=?", keyword, keyword);
        if (rst.next()) {
            return new Village(rst.getString(1), rst.getString(2), rst.getInt(3), rst.getDouble(4), rst.getDouble(5), rst.getString(6), rst.getString(7));
        }
        return null;
    }

    @Override
    public String getVillageIdByName(String villageName) {
        try {
            ResultSet rst = CrudUtil.execute("SELECT village_id FROM village WHERE village_name=?", villageName);
            if (rst.next()) {
                return rst.getString(1);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<String> getAllVillageNames() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT village_name FROM village");
        List<String> villageNames = new ArrayList<>();
        while (result.next()) {
            villageNames.add(result.getString(1));
        }
        return villageNames;
    }
}