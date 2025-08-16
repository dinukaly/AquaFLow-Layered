package lk.wms.aquaflow.dao.custom.impl;

import lk.wms.aquaflow.dao.custom.PaymentDAO;
import lk.wms.aquaflow.entity.Payment;
import lk.wms.aquaflow.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PaymentDAOImpl implements PaymentDAO {
    @Override
    public ArrayList<Payment> getAll() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT * FROM payment");
        ArrayList<Payment> allPayments = new ArrayList<>();
        while (rst.next()) {
            allPayments.add(new Payment(rst.getString(1), rst.getDate(2), rst.getDouble(3), rst.getString(4), rst.getString(5)));
        }
        return allPayments;
    }

    @Override
    public boolean add(Payment entity) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO payment (payment_Id, paid_date, amount, payment_method, bill_Id) VALUES (?, ?, ?, ?, ?)";
        return CrudUtil.execute(sql, entity.getPaymentId(), entity.getPaidDate(), entity.getAmount(), entity.getPaymentMethod(), entity.getBillId());
    }

    @Override
    public boolean update(Payment entity) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE payment SET paidDate=?, amount=?, paymentMethod=?, billId=? WHERE paymentId=?",
                entity.getPaidDate(), entity.getAmount(), entity.getPaymentMethod(), entity.getBillId(), entity.getPaymentId());
    }


    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM payment WHERE paymentId=?", id);
    }

    @Override
    public String generateNewId() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT paymentId FROM payment ORDER BY paymentId DESC LIMIT 1");
        if (rst.next()) {
            String id = rst.getString(1);
            int newPaymentId = Integer.parseInt(id.replace("P", "")) + 1;
            return String.format("P%03d", newPaymentId);
        } else {
            return "P001";
        }
    }

    @Override
    public Payment get(String id) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT * FROM payment WHERE paymentId=?", id);
        if (rst.next()) {
            return new Payment(rst.getString(1), rst.getDate(2), rst.getDouble(3), rst.getString(4), rst.getString(5));
        }
        return null;
    }

    @Override
    public Payment search(String id) throws SQLException, ClassNotFoundException {
        return get(id);
    }
}