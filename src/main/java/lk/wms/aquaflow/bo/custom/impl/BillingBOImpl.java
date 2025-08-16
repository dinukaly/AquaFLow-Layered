package lk.wms.aquaflow.bo.custom.impl;

import lk.wms.aquaflow.bo.custom.BillingBO;
import lk.wms.aquaflow.dao.DAOFactory;
import lk.wms.aquaflow.dao.QueryDAO;
import lk.wms.aquaflow.dao.custom.BillingDAO;
import lk.wms.aquaflow.dao.custom.ConsumptionDAO;
import lk.wms.aquaflow.dao.custom.PaymentDAO;
import lk.wms.aquaflow.db.DBConnection;
import lk.wms.aquaflow.dto.custom.CustomBillDTO;
import lk.wms.aquaflow.entity.Payment;
import lk.wms.aquaflow.entity.custom.CustomBill;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BillingBOImpl implements BillingBO {
    private final BillingDAO billingDAO = (BillingDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.BILLING);
    private final PaymentDAO paymentDAO = (PaymentDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.PAYMENT);
    private final ConsumptionDAO consumptionDAO = (ConsumptionDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.CONSUMPTION);
    private final QueryDAO queryDAO = (QueryDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.QUERY);


    @Override
    public String generateBillId() throws SQLException, ClassNotFoundException {
        return billingDAO.generateNewId();
    }

    @Override
    public String getHouseholdIdFromBill(String billId) throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public boolean checkAndGenerateBillsForEligibleConsumptions() {
        return false;
    }

    @Override
    public ArrayList<CustomBillDTO> getAllBills() throws SQLException, ClassNotFoundException {
        List<CustomBill> all = queryDAO.getAllCustomBills();
        ArrayList<CustomBillDTO> billDTOS = new ArrayList<>();
        for (CustomBill bill : all) {
            billDTOS.add(new CustomBillDTO(bill.getBillId(), bill.getTotalAmountOfUnits(), bill.getCostPerUnit(), bill.getStatus(), bill.getTotalCost(), bill.getBillDate(), bill.getDueDate(), bill.getConsumptionId(), bill.getHouseholdName(), bill.getVillageName()));
        }
        return billDTOS;
    }

    @Override
    public CustomBillDTO getBillById(String billId) throws SQLException, ClassNotFoundException {
        CustomBill bill = queryDAO.getBillById(billId);
        if (bill != null) {
            return new CustomBillDTO(bill.getBillId(), bill.getTotalAmountOfUnits(), bill.getCostPerUnit(), bill.getStatus(), bill.getTotalCost(), bill.getBillDate(), bill.getDueDate(), bill.getConsumptionId(), bill.getHouseholdName(), bill.getVillageName());
        }
        return null;
    }


    @Override
    public boolean updateBillStatus(String billId, String status) throws SQLException, ClassNotFoundException {
        return billingDAO.updateStatus(billId, status);
    }

    @Override
    public List<CustomBillDTO> getBillsByStatus(String status) throws SQLException, ClassNotFoundException {
        List<CustomBill> list = queryDAO.getCustomBillsByStatus(status);
        List<CustomBillDTO> dtos = new ArrayList<>();
        for (CustomBill b : list) {
            dtos.add(new CustomBillDTO(
                    b.getBillId(),
                    b.getTotalAmountOfUnits(),
                    b.getCostPerUnit(),
                    b.getStatus(),
                    b.getTotalCost(),
                    b.getBillDate(),
                    b.getDueDate(),
                    b.getConsumptionId(),
                    b.getHouseholdName(),
                    b.getVillageName()
            ));
        }
        return dtos;
    }

    @Override
    public List<CustomBillDTO> getOverdueBills() throws SQLException, ClassNotFoundException {
        List<CustomBill> list = queryDAO.getOverdueCustomBills();
        List<CustomBillDTO> dtos = new ArrayList<>();
        for (CustomBill b : list) {
            dtos.add(new CustomBillDTO(
                    b.getBillId(),
                    b.getTotalAmountOfUnits(),
                    b.getCostPerUnit(),
                    b.getStatus(),
                    b.getTotalCost(),
                    b.getBillDate(),
                    b.getDueDate(),
                    b.getConsumptionId(),
                    b.getHouseholdName(),
                    b.getVillageName()
            ));
        }
        return dtos;
    }

    @Override
    public double getTotalCollectedAmount() throws SQLException, ClassNotFoundException {
        return billingDAO.getTotalCollectedAmount();
    }

    @Override
    public double getPendingPaymentAmount() throws SQLException, ClassNotFoundException {
        return billingDAO.getPendingPaymentAmount();
    }

    @Override
    public double getOverduePaymentAmount() throws SQLException, ClassNotFoundException {
        return billingDAO.getOverduePaymentAmount();
    }

    @Override
    public double getTotalBilledThisMonth() throws SQLException, ClassNotFoundException {
        return billingDAO.getTotalBilledThisMonth();
    }

    @Override
    public boolean recordPayment(String billId, double amount, LocalDate paidDate, String paymentMethod) throws SQLException {
        Connection connection = null;
        try {
            connection = DBConnection.getInstance().getConnection();
            connection.setAutoCommit(false);

            String paymentId = "P" + System.currentTimeMillis() % 10000;
            Payment payment = new Payment(paymentId, java.sql.Date.valueOf(paidDate), amount, paymentMethod, billId);
            boolean paymentAdded = paymentDAO.add(payment);

            if (!paymentAdded) {
                connection.rollback();
                return false;
            }

            boolean statusUpdated = billingDAO.updateStatus(billId, "Paid");
            if (!statusUpdated) {
                connection.rollback();
                return false;
            }

            connection.commit();
            return true;

        } catch (SQLException | ClassNotFoundException e) {
            if (connection != null) {
                connection.rollback();
            }
            throw new RuntimeException(e);
        } finally {
            if (connection != null) {
                connection.setAutoCommit(true);
            }
        }
    }
}