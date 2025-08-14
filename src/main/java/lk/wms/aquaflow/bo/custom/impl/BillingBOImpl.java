package lk.wms.aquaflow.bo.custom.impl;

import lk.wms.aquaflow.bo.custom.BillingBO;
import lk.wms.aquaflow.dto.BillDTO;

import java.time.LocalDate;
import java.util.ArrayList;

public class BillingBOImpl implements BillingBO {
    @Override
    public boolean checkAndGenerateBillsForEligibleConsumptions() {
        return false;
    }

    @Override
    public ArrayList<BillDTO> getAllBills() {
        return null;
    }

    @Override
    public BillDTO getBillById(String billId) {
        return null;
    }

    @Override
    public boolean updateBillStatus(String billId, String status) {
        return false;
    }

    @Override
    public ArrayList<BillDTO> getBillsByStatus(String status) {
        return null;
    }

    @Override
    public ArrayList<BillDTO> getOverdueBills() {
        return null;
    }

    @Override
    public double getTotalCollectedAmount() {
        return 0;
    }

    @Override
    public double getPendingPaymentAmount() {
        return 0;
    }

    @Override
    public double getOverduePaymentAmount() {
        return 0;
    }

    @Override
    public double getTotalBilledThisMonth() {
        return 0;
    }

    @Override
    public boolean recordPayment(String billId, double amount, LocalDate paidDate, String paymentMethod) {
        return false;
    }
}
