package lk.wms.aquaflow.dao.custom.impl;

import lk.wms.aquaflow.dao.custom.OfficerDAO;
import lk.wms.aquaflow.entity.Officer;
import lk.wms.aquaflow.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OfficerDAOImpl implements OfficerDAO {
    @Override
    public boolean add(Officer officer) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO officer VALUES (?,?,?,?,?)", officer.getOfficerId(), officer.getName(), officer.getAddress(), officer.getEmail(), officer.getTelephone());
    }

    @Override
    public boolean update(Officer officer) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(
                "UPDATE officer SET name=?, address=?, email=?, telephone=? WHERE officer_id=?",
                officer.getName(),
                officer.getAddress(),
                officer.getEmail(),
                officer.getTelephone(),
                officer.getOfficerId()
        );
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM officer WHERE officer_id=?", id);
    }

    @Override
    public String generateNewId() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT officer_id FROM officer ORDER BY officer_id DESC LIMIT 1");
        char officerId = 'O';
        if (resultSet.next()) {
            String lastId = resultSet.getString(1);
            String lastIdNumberString = lastId.substring(1);
            int lastIdNumber = Integer.parseInt(lastIdNumberString);
            int nextIdNumber = lastIdNumber + 1;
            return String.format("%s%03d", officerId, nextIdNumber);
        }
        return officerId + "001";
    }

    @Override
    public Officer get(String id) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM officer WHERE officer_id=?", id);
        if (resultSet.next()) {
            return new Officer(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5)
            );
        }
        return null;
    }

    @Override
    public ArrayList<Officer> getAll() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM officer");
        ArrayList<Officer> officers = new ArrayList<>();
        while (resultSet.next()) {
            officers.add(new Officer(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5)
            ));
        }
        return officers;
    }

    @Override
    public Officer search(String keyword) throws SQLException, ClassNotFoundException {

        return get(keyword);
    }

    @Override
    public String getOfficerIdByName(String name) {
        try {
            ResultSet resultSet = CrudUtil.execute("SELECT officer_id FROM officer WHERE name=?", name);
            if (resultSet.next()) {
                return resultSet.getString(1);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}