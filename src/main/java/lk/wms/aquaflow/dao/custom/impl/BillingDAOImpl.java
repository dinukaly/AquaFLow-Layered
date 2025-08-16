package lk.wms.aquaflow.dao.custom.impl;

import lk.wms.aquaflow.dao.custom.BillingDAO;
import lk.wms.aquaflow.entity.Bill;
import lk.wms.aquaflow.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BillingDAOImpl implements BillingDAO {
    @Override
    public boolean add(Bill bill) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO bill (bill_id, total_amount_of_units, cost_per_unit, status, total_cost, bill_date, due_date, consumption_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                bill.getBillId(), bill.getTotalAmountOfUnits(), bill.getCostPerUnit(), bill.getStatus(), bill.getTotalCost(), bill.getBillDate(), bill.getDueDate(), bill.getConsumptionId());
    }

    @Override
    public boolean update(Bill bill) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE bill SET total_amount_of_units = ?, cost_per_unit = ?, status = ?, total_cost = ?, bill_date = ?, due_date = ?, consumption_id = ? WHERE bill_id = ?",
                bill.getTotalAmountOfUnits(), bill.getCostPerUnit(), bill.getStatus(), bill.getTotalCost(), bill.getBillDate(), bill.getDueDate(), bill.getConsumptionId(), bill.getBillId());
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM bill WHERE bill_id = ?", id);
    }

    @Override
    public String generateNewId() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT bill_id FROM bill ORDER BY bill_id DESC LIMIT 1");
        if (resultSet.next()) {
            String lastId = resultSet.getString(1);
            String lastIdNumberString = lastId.substring(1);
            int lastIdNumber = Integer.parseInt(lastIdNumberString);
            int nextIdNumber = lastIdNumber + 1;
            return String.format("B%03d", nextIdNumber);
        }
        return "B001";
    }

    @Override
    public Bill get(String id) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM bill WHERE bill_id = ?", id);
        if (resultSet.next()) {
            return new Bill(
                    resultSet.getString("bill_id"),
                    resultSet.getDouble("total_amount_of_units"),
                    resultSet.getDouble("cost_per_unit"),
                    resultSet.getString("status"),
                    resultSet.getDouble("total_cost"),
                    resultSet.getDate("bill_date"),
                    resultSet.getDate("due_date"),
                    resultSet.getString("consumption_id")
            );
        }
        return null;
    }

    @Override
    public ArrayList<Bill> getAll() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM bill");
        ArrayList<Bill> bills = new ArrayList<>();
        while (resultSet.next()) {
            bills.add(new Bill(
                    resultSet.getString("bill_id"),
                    resultSet.getDouble("total_amount_of_units"),
                    resultSet.getDouble("cost_per_unit"),
                    resultSet.getString("status"),
                    resultSet.getDouble("total_cost"),
                    resultSet.getDate("bill_date"),
                    resultSet.getDate("due_date"),
                    resultSet.getString("consumption_id")
            ));
        }
        return bills;
    }

    @Override
    public Bill search(String keyword) throws SQLException, ClassNotFoundException {
        // Implementation for search can be similar to get(id) or more complex based on requirements
        return get(keyword);
    }

    @Override
    public boolean updateStatus(String billId, String status) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE bill SET status = ? WHERE bill_id = ?", status, billId);
    }

    @Override
    public List<Bill> getByStatus(String status) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM bill WHERE status = ?", status);
        List<Bill> bills = new ArrayList<>();
        while (resultSet.next()) {
            bills.add(new Bill(
                    resultSet.getString("bill_id"),
                    resultSet.getDouble("total_amount_of_units"),
                    resultSet.getDouble("cost_per_unit"),
                    resultSet.getString("status"),
                    resultSet.getDouble("total_cost"),
                    resultSet.getDate("bill_date"),
                    resultSet.getDate("due_date"),
                    resultSet.getString("consumption_id")
            ));
        }
        return bills;
    }

    @Override
    public List<Bill> getOverdueBills() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM bill WHERE due_date < CURDATE() AND status = 'Unpaid'");
        List<Bill> bills = new ArrayList<>();
        while (resultSet.next()) {
            bills.add(new Bill(
                    resultSet.getString("bill_id"),
                    resultSet.getDouble("total_amount_of_units"),
                    resultSet.getDouble("cost_per_unit"),
                    resultSet.getString("status"),
                    resultSet.getDouble("total_cost"),
                    resultSet.getDate("bill_date"),
                    resultSet.getDate("due_date"),
                    resultSet.getString("consumption_id")
            ));
        }
        return bills;
    }

    @Override
    public double getTotalCollectedAmount() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT SUM(total_cost) FROM bill WHERE status = 'Paid'");
        if (resultSet.next()) {
            return resultSet.getDouble(1);
        }
        return 0;
    }

    @Override
    public double getPendingPaymentAmount() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT SUM(total_cost) FROM bill WHERE status = 'Unpaid'");
        if (resultSet.next()) {
            return resultSet.getDouble(1);
        }
        return 0;
    }

    @Override
    public double getOverduePaymentAmount() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT SUM(total_cost) FROM bill WHERE due_date < CURDATE() AND status = 'Unpaid'");
        if (resultSet.next()) {
            return resultSet.getDouble(1);
        }
        return 0;
    }

    @Override
    public double getTotalBilledThisMonth() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT SUM(total_cost) FROM bill WHERE MONTH(bill_date) = MONTH(CURDATE()) AND YEAR(bill_date) = YEAR(CURDATE())");
        if (resultSet.next()) {
            return resultSet.getDouble(1);
        }
        return 0;
    }
}