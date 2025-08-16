package lk.wms.aquaflow.dao.custom;

import lk.wms.aquaflow.dao.CrudDAO;
import lk.wms.aquaflow.entity.Bill;

import java.sql.SQLException;
import java.util.List;

public interface BillingDAO extends CrudDAO<Bill> {
    boolean updateStatus(String billId, String status) throws SQLException, ClassNotFoundException;
    List<Bill> getByStatus(String status) throws SQLException, ClassNotFoundException;
    List<Bill> getOverdueBills() throws SQLException, ClassNotFoundException;

    double getTotalCollectedAmount() throws SQLException, ClassNotFoundException;
    double getPendingPaymentAmount() throws SQLException, ClassNotFoundException;
    double getOverduePaymentAmount() throws SQLException, ClassNotFoundException;
    double getTotalBilledThisMonth() throws SQLException, ClassNotFoundException;
}