package lk.wms.aquaflow.bo.custom;

import lk.wms.aquaflow.bo.SuperBO;
import lk.wms.aquaflow.dto.custom.CustomBillDTO;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public interface BillingBO extends SuperBO {
    public String generateBillId() throws SQLException, ClassNotFoundException;
    public String getHouseholdIdFromBill(String billId) throws SQLException, ClassNotFoundException;
    public boolean checkAndGenerateBillsForEligibleConsumptions();
    public ArrayList<CustomBillDTO> getAllBills() throws SQLException, ClassNotFoundException;
    public CustomBillDTO getBillById(String billId) throws SQLException, ClassNotFoundException;
    public boolean updateBillStatus(String billId, String status) throws SQLException, ClassNotFoundException;
    public List<CustomBillDTO> getBillsByStatus(String status) throws SQLException, ClassNotFoundException;
    public List<CustomBillDTO> getOverdueBills() throws SQLException, ClassNotFoundException;
    public double getTotalCollectedAmount() throws SQLException, ClassNotFoundException;
    public double getPendingPaymentAmount() throws SQLException, ClassNotFoundException;
    public double getOverduePaymentAmount() throws SQLException, ClassNotFoundException;
    public double getTotalBilledThisMonth() throws SQLException, ClassNotFoundException;
    public boolean recordPayment(String billId, double amount, LocalDate paidDate, String paymentMethod) throws SQLException;
}