package lk.wms.aquaflow.dao.custom.impl;

import lk.wms.aquaflow.dao.custom.HouseholdDAO;
import lk.wms.aquaflow.entity.Household;
import lk.wms.aquaflow.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class HouseholdDAOImpl implements HouseholdDAO {
    @Override
    public boolean add(Household household) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO household (house_id, owner_name, address, no_of_members, email, village_id) VALUES (?, ?, ?, ?, ?, ?)",
                household.getHouseId(),
                household.getOwnerName(),
                household.getAddress(),
                household.getNoOfMembers(),
                household.getEmail(),
                household.getVillageId());
    }

    @Override
    public boolean update(Household household) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE household SET owner_name = ?, address = ?, no_of_members = ?, email = ?, village_id = ? WHERE house_id = ?",
                household.getOwnerName(),
                household.getAddress(),
                household.getNoOfMembers(),
                household.getEmail(),
                household.getVillageId(),
                household.getHouseId());
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM household WHERE house_id = ?", id);
    }

    @Override
    public String generateNewId() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT house_id FROM household ORDER BY house_id DESC LIMIT 1");
        if (rst.next()) {
            String id = rst.getString("house_id");
            int newHouseholdId = Integer.parseInt(id.replace("H", "")) + 1;
            return String.format("H%03d", newHouseholdId);
        } else {
            return "H001";
        }
    }

    @Override
    public Household get(String id) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM household WHERE house_id = ?", id);
        if (resultSet.next()) {
            return new Household(
                    resultSet.getString("house_id"),
                    resultSet.getString("owner_name"),
                    resultSet.getString("address"),
                    resultSet.getInt("no_of_members"),
                    resultSet.getString("email"),
                    resultSet.getString("village_id")
            );
        }
        return null;
    }

    @Override
    public ArrayList<Household> getAll() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM household");
        ArrayList<Household> households = new ArrayList<>();
        while (resultSet.next()) {
            households.add(new Household(
                    resultSet.getString("house_id"),
                    resultSet.getString("owner_name"),
                    resultSet.getString("address"),
                    resultSet.getInt("no_of_members"),
                    resultSet.getString("email"),
                    resultSet.getString("village_id")
            ));
        }
        return households;
    }

    @Override
    public Household search(String keyword) throws SQLException, ClassNotFoundException {
        return get(keyword);
    }
}