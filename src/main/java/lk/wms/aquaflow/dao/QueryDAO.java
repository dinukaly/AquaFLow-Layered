package lk.wms.aquaflow.dao;

import lk.wms.aquaflow.entity.Complaint;
import lk.wms.aquaflow.entity.custom.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface QueryDAO extends SuperDAO{
    ArrayList<HouseholdWithVillage> getAllHouseholdWithVillage() throws SQLException, ClassNotFoundException;

    Complaint getComplaintById(String complaintId) throws SQLException, ClassNotFoundException;
    ComplaintWithOwnerEmail getComplaintByIdWithEmail(String complaintId) throws SQLException, ClassNotFoundException;

    //consumptions joins
    ArrayList<ComplaintWithOwnerEmail> getRecentComplaints() throws SQLException, ClassNotFoundException;
    ArrayList<ComplaintWithOwnerEmail> getAllComplaints() throws SQLException, ClassNotFoundException;
    List<ConsumptionWithHouseVillage> getConsumptionDetails() throws SQLException, ClassNotFoundException;
    Map<String, Number> getConsumptionByVillageForYear(String year) throws SQLException, ClassNotFoundException;


    //supplier joins
    InventoryWithSupplierName getInventoryById(String inventoryId) throws SQLException, ClassNotFoundException;
    ArrayList<InventoryWithSupplierName> getAllInventory() throws SQLException, ClassNotFoundException;

    //village joins
    ArrayList<VillageWithOfficers> searchVillage(String villageId);
    ArrayList<VillageWithOfficers> getAllVillages() throws SQLException, ClassNotFoundException;

    List<CustomBill> getAllCustomBills() throws SQLException, ClassNotFoundException;
    List<CustomBill> getCustomBillsByStatus(String status) throws SQLException, ClassNotFoundException;
    List<CustomBill> getOverdueCustomBills() throws SQLException, ClassNotFoundException;
    boolean checkAndGenerateBillsForEligibleConsumptions() throws SQLException, ClassNotFoundException;

    CustomBill getBillById(String billId) throws SQLException, ClassNotFoundException;

}