package lk.wms.aquaflow.dao.custom.impl;

import lk.wms.aquaflow.dao.QueryDAO;
import lk.wms.aquaflow.entity.Complaint;
import lk.wms.aquaflow.entity.custom.*;
import lk.wms.aquaflow.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryDAOImpl implements QueryDAO {
    @Override
    public ArrayList<HouseholdWithVillage> getAllHouseholdWithVillage() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT h.*, v.village_name FROM household h JOIN village v ON h.village_id = v.village_id");
        ArrayList<HouseholdWithVillage> allHouseholds = new ArrayList<>();
        while (rst.next()) {
            allHouseholds.add(new HouseholdWithVillage(rst.getString(1), rst.getString(2), rst.getString(3), rst.getInt(4), rst.getString(5), rst.getString(6), rst.getString(7)));
        }
        return allHouseholds;
    }

    @Override
    public Complaint getComplaintById(String complaintId) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT * FROM complaint WHERE complaint_id=?", complaintId);
        if (rst.next()) {
            return new Complaint(rst.getString(1), rst.getDate(2), rst.getString(3), rst.getString(4), rst.getString(5));
        }
        return null;
    }

    @Override
    public ComplaintWithOwnerEmail getComplaintByIdWithEmail(String complaintId) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT c.*, h.email FROM complaint c JOIN household h ON c.house_id = h.house_id WHERE c.complaint_id=?", complaintId);
        if (rst.next()) {
            return new ComplaintWithOwnerEmail(rst.getString(1), rst.getString(2), rst.getString(3), rst.getString(4), rst.getString(5), rst.getString(6));
        }
        return null;
    }

    @Override
    public ArrayList<ComplaintWithOwnerEmail> getRecentComplaints() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT c.*, h.email FROM complaint c JOIN household h ON c.house_id = h.house_id ORDER BY c.date DESC");
        ArrayList<ComplaintWithOwnerEmail> recentComplaints = new ArrayList<>();
        while (rst.next()) {
            recentComplaints.add(new ComplaintWithOwnerEmail(rst.getString(1), rst.getString(2), rst.getString(3), rst.getString(4), rst.getString(5), rst.getString(6)));
        }
        return recentComplaints;
    }

    @Override
    public ArrayList<ComplaintWithOwnerEmail> getAllComplaints() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT c.*, h.email FROM complaint c JOIN household h ON c.house_id = h.house_id");
        ArrayList<ComplaintWithOwnerEmail> allComplaints = new ArrayList<>();
        while (rst.next()) {
            allComplaints.add(new ComplaintWithOwnerEmail(rst.getString(1), rst.getDate(2).toString(), rst.getString(3), rst.getString(4), rst.getString(5), rst.getString(6)));
        }
        return allComplaints;
    }

    @Override
    public List<ConsumptionWithHouseVillage> getConsumptionDetails() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT c.*, hh.owner_name, v.village_name FROM consumption c JOIN household hh ON c.house_id = hh.house_id JOIN village v ON hh.village_id = v.village_id");
        List<ConsumptionWithHouseVillage> consumptionDetails = new ArrayList<>();
        while (rst.next()) {
            consumptionDetails.add(new ConsumptionWithHouseVillage(
                    rst.getString(1),                 // consumption_id
                    rst.getString(2),                 // amount_of_units
                    rst.getDate(3).toLocalDate(),     // start_date (as LocalDate)
                    rst.getDate(4).toLocalDate(),     // end_date (as LocalDate)
                    rst.getString(5),                 // house_id
                    rst.getString(6),                 // owner_name
                    rst.getString(7)                  // village_name
            ));
        }
        return consumptionDetails;
    }

    @Override
    public Map<String, Number> getConsumptionByVillageForYear(String year) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT v.village_name, SUM(c.amount_of_units) FROM consumption c JOIN household h ON c.house_id = h.house_id JOIN village v ON h.village_id = v.village_id WHERE YEAR(c.end_date) = ? GROUP BY v.village_name", year);
        Map<String, Number> consumptionByVillage = new HashMap<>();
        while (rst.next()) {
            consumptionByVillage.put(rst.getString(1), rst.getDouble(2));
        }
        return consumptionByVillage;
    }

    @Override
    public InventoryWithSupplierName getInventoryById(String inventoryId) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT i.*, s.name FROM inventory i JOIN supplier s ON i.supplier_id = s.supplier_id WHERE i.inventory_id=?", inventoryId);
        if (rst.next()) {
            return new InventoryWithSupplierName(rst.getString(1), rst.getString(2), rst.getString(3), rst.getDouble(4), rst.getString(5), rst.getString(6));
        }
        return null;
    }

    @Override
    public ArrayList<InventoryWithSupplierName> getAllInventory() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT i.*, s.name FROM inventory i JOIN supplier s ON i.supplier_id = s.supplier_id");
        ArrayList<InventoryWithSupplierName> allInventory = new ArrayList<>();
        while (rst.next()) {
            allInventory.add(new InventoryWithSupplierName(rst.getString(1), rst.getString(2), rst.getString(3), rst.getDouble(4), rst.getString(5), rst.getString(6)));
        }
        return allInventory;
    }

    @Override
    public ArrayList<VillageWithOfficers> searchVillage(String villageId) {

        return null;
    }

    @Override
    public ArrayList<VillageWithOfficers> getAllVillages() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT v.*, o.name FROM village v JOIN officer o ON v.officer_id = o.officer_id");
        ArrayList<VillageWithOfficers> allVillages = new ArrayList<>();
        while (rst.next()) {
            allVillages.add(new VillageWithOfficers(rst.getString(1), rst.getString(2), rst.getInt(3), rst.getDouble(4), rst.getDouble(5), rst.getString(6), rst.getString(7), rst.getString(8)));
        }
        return allVillages;
    }

    @Override
    public List<CustomBill> getAllCustomBills() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT b.*, hh.owner_name, v.village_name FROM bill b JOIN consumption c ON b.consumption_id = c.consumption_id JOIN household hh ON c.house_id = hh.house_id JOIN village v ON hh.village_id = v.village_id");
        List<CustomBill> customBills = new ArrayList<>();
        while (rst.next()) {
            customBills.add(new CustomBill(rst.getString(1), rst.getString(2), rst.getString(3), rst.getString(4), rst.getString(5), rst.getString(6), rst.getString(7), rst.getString(8), rst.getString(9), rst.getString(10)));
        }
        return customBills;
    }

    @Override
    public List<CustomBill> getCustomBillsByStatus(String status) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT b.*, hh.owner_name, v.village_name FROM bill b JOIN consumption c ON b.consumption_id = c.consumption_id JOIN household hh ON c.house_id = hh.house_id JOIN village v ON hh.village_id = v.village_id WHERE b.status=?", status);
        List<CustomBill> customBills = new ArrayList<>();
        while (rst.next()) {
            customBills.add(new CustomBill(rst.getString(1), rst.getString(2), rst.getString(3), rst.getString(4), rst.getString(5), rst.getString(6), rst.getString(7), rst.getString(8), rst.getString(9), rst.getString(10)));
        }
        return customBills;
    }

    @Override
    public List<CustomBill> getOverdueCustomBills() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT b.*, hh.owner_name, v.village_name FROM bill b JOIN consumption c ON b.consumption_id = c.consumption_id JOIN household hh ON c.house_id = hh.house_id JOIN village v ON hh.village_id = v.village_id WHERE b.status='Unpaid' AND b.due_date < CURDATE()");
        List<CustomBill> customBills = new ArrayList<>();
        while (rst.next()) {
            customBills.add(new CustomBill(rst.getString(1), rst.getString(2), rst.getString(3), rst.getString(4), rst.getString(5), rst.getString(6), rst.getString(7), rst.getString(8), rst.getString(9), rst.getString(10)));
        }
        return customBills;
    }

    @Override
    public boolean checkAndGenerateBillsForEligibleConsumptions() throws SQLException, ClassNotFoundException {

        return false;
    }

    @Override
    public CustomBill getBillById(String billId) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT b.*, hh.owner_name, v.village_name FROM bill b JOIN consumption c ON b.consumption_id = c.consumption_id JOIN household hh ON c.house_id = hh.house_id JOIN village v ON hh.village_id = v.village_id WHERE b.bill_id=?", billId);
        if (rst.next()) {
            return new CustomBill(rst.getString(1), rst.getString(2), rst.getString(3), rst.getString(4), rst.getString(5), rst.getString(6), rst.getString(7), rst.getString(8), rst.getString(9), rst.getString(10));
        }
        return null;
    }
}