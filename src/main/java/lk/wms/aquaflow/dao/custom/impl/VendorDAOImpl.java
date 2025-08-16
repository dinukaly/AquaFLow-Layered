package lk.wms.aquaflow.dao.custom.impl;

import lk.wms.aquaflow.dao.custom.VendorDAO;
import lk.wms.aquaflow.entity.Supplier;
import lk.wms.aquaflow.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class VendorDAOImpl implements VendorDAO {
    @Override
    public boolean add(Supplier supplier) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO supplier VALUES (?,?,?,?,?)", supplier.getSupplierId(), supplier.getName(), supplier.getAddress(), supplier.getEmail(), supplier.getTel());
    }

    @Override
    public boolean update(Supplier supplier) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE supplier SET name=?, address=?, email=?, tel=? WHERE supplier_id=?", supplier.getName(), supplier.getAddress(), supplier.getEmail(), supplier.getTel(), supplier.getSupplierId());
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM supplier WHERE supplier_id=?", id);
    }

    @Override
    public String generateNewId() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT supplier_id FROM supplier ORDER BY supplier_id DESC LIMIT 1");
        if (rst.next()) {
            String id = rst.getString(1);
            int newId = Integer.parseInt(id.replace("S", "")) + 1;
            return String.format("S%03d", newId);
        } else {
            return "S001";
        }
    }

    @Override
    public Supplier get(String id) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT * FROM supplier WHERE supplier_id=?", id);
        if (rst.next()) {
            return new Supplier(rst.getString(1), rst.getString(2), rst.getString(3), rst.getString(4), rst.getString(5));
        }
        return null;
    }

    @Override
    public ArrayList<Supplier> getAll() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT * FROM supplier");
        ArrayList<Supplier> allSuppliers = new ArrayList<>();
        while (rst.next()) {
            allSuppliers.add(new Supplier(rst.getString(1), rst.getString(2), rst.getString(3), rst.getString(4), rst.getString(5)));
        }
        return allSuppliers;
    }

    @Override
    public Supplier search(String keyword) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT * FROM supplier WHERE supplier_id=? OR name=?", keyword, keyword);
        if (rst.next()) {
            return new Supplier(rst.getString(1), rst.getString(2), rst.getString(3), rst.getString(4), rst.getString(5));
        }
        return null;
    }

    @Override
    public String getSupplierIdByName(String supplierName) {
        try {
            ResultSet rst = CrudUtil.execute("SELECT supplier_id FROM supplier WHERE name=?", supplierName);
            if (rst.next()) {
                return rst.getString(1);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}