package lk.wms.aquaflow.dao.custom.impl;

import lk.wms.aquaflow.dao.custom.ComplaintDAO;
import lk.wms.aquaflow.entity.Complaint;
import lk.wms.aquaflow.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ComplaintDAOImpl implements ComplaintDAO {
    @Override
    public boolean add(Complaint complaint) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO complaint (complaint_id, date, description, status, house_id) VALUES (?, ?, ?, ?, ?)",
                complaint.getComplaintId(), complaint.getDate(), complaint.getDescription(), complaint.getStatus(), complaint.getHouseId());
    }

    @Override
    public boolean update(Complaint complaint) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE complaint SET date = ?, description = ?, status = ?, house_id = ? WHERE complaint_id = ?",
                complaint.getDate(), complaint.getDescription(), complaint.getStatus(), complaint.getHouseId(), complaint.getComplaintId());
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM complaint WHERE complaint_id = ?", id);
    }

    @Override
    public String generateNewId() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT complaint_id FROM complaint ORDER BY complaint_id DESC LIMIT 1");
        if (resultSet.next()) {
            String lastId = resultSet.getString(1);
            String lastIdNumberString = lastId.substring(1);
            int lastIdNumber = Integer.parseInt(lastIdNumberString);
            int nextIdNumber = lastIdNumber + 1;
            return String.format("C%03d", nextIdNumber);
        }
        return "C001";
    }

    @Override
    public Complaint get(String id) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM complaint WHERE complaint_id = ?", id);
        if (resultSet.next()) {
            return new Complaint(
                    resultSet.getString("complaint_id"),
                    resultSet.getDate("date"),
                    resultSet.getString("description"),
                    resultSet.getString("status"),
                    resultSet.getString("house_id")
            );
        }
        return null;
    }

    @Override
    public ArrayList<Complaint> getAll() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM complaint");
        ArrayList<Complaint> complaints = new ArrayList<>();
        while (resultSet.next()) {
            complaints.add(new Complaint(
                    resultSet.getString("complaint_id"),
                    resultSet.getDate("date"),
                    resultSet.getString("description"),
                    resultSet.getString("status"),
                    resultSet.getString("house_id")
            ));
        }
        return complaints;
    }

    @Override
    public Complaint search(String keyword) throws SQLException, ClassNotFoundException {
        return get(keyword);
    }

    @Override
    public boolean updateComplaintStatus(String complaintId, String status) {
        try {
            return CrudUtil.execute("UPDATE complaint SET status = ? WHERE complaint_id = ?", status, complaintId);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Complaint getComplaintById(String complaintId) {
        try {
            return get(complaintId);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getTotalComplaintsCount() {
        try {
            ResultSet resultSet = CrudUtil.execute("SELECT COUNT(*) FROM complaint");
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    @Override
    public int getSolvedComplaintsCount() {
        try {
            ResultSet resultSet = CrudUtil.execute("SELECT COUNT(*) FROM complaint WHERE status = 'Solved'");
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    @Override
    public int getScheduledComplaintsCount() {
        try {
            ResultSet resultSet = CrudUtil.execute("SELECT COUNT(*) FROM complaint WHERE status = 'Scheduled'");
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    @Override
    public int getNewComplaintsThisMonth() {
        try {
            ResultSet resultSet = CrudUtil.execute("SELECT COUNT(*) FROM complaint WHERE MONTH(date) = MONTH(CURDATE()) AND YEAR(date) = YEAR(CURDATE())");
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }
}