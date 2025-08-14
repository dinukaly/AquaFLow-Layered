package lk.wms.aquaflow.bo.custom;

import lk.wms.aquaflow.bo.SuperBO;
import lk.wms.aquaflow.dto.BillDTO;

import java.time.LocalDate;
import java.util.ArrayList;

public interface BillingBO extends SuperBO {
    public boolean checkAndGenerateBillsForEligibleConsumptions();
    public ArrayList<BillDTO> getAllBills();
    public BillDTO getBillById(String billId);
    public boolean updateBillStatus(String billId, String status);
    public ArrayList<BillDTO> getBillsByStatus(String status);
    public ArrayList<BillDTO> getOverdueBills();
    public double getTotalCollectedAmount();
    public double getPendingPaymentAmount();
    public double getOverduePaymentAmount();
    public double getTotalBilledThisMonth();
    public boolean recordPayment(String billId, double amount, LocalDate paidDate, String paymentMethod);
}
