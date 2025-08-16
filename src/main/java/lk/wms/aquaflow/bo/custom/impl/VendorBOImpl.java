package lk.wms.aquaflow.bo.custom.impl;

import lk.wms.aquaflow.bo.custom.VendorBO;
import lk.wms.aquaflow.dao.DAOFactory;
import lk.wms.aquaflow.dao.custom.VendorDAO;
import lk.wms.aquaflow.dto.SupplierDTO;
import lk.wms.aquaflow.entity.Supplier;

import java.sql.SQLException;
import java.util.List;

public class VendorBOImpl implements VendorBO {
    VendorDAO vendorDAO = (VendorDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.VENDOR);
    @Override
    public boolean addVendor(SupplierDTO vendorDTO) throws SQLException, ClassNotFoundException {
        return vendorDAO.add(
                new Supplier(
                        vendorDTO.getSupplierId(),
                        vendorDTO.getName(),
                        vendorDTO.getAddress(),
                        vendorDTO.getEmail(),
                        vendorDTO.getTel()
                )
        );
    }

    @Override
    public SupplierDTO searchVendor(String supplierId) {
        return null;
    }

    @Override
    public boolean updateVendor(SupplierDTO vendorDTO) throws SQLException, ClassNotFoundException {
        return vendorDAO.update(
                new Supplier(
                        vendorDTO.getSupplierId(),
                        vendorDTO.getName(),
                        vendorDTO.getAddress(),
                        vendorDTO.getEmail(),
                        vendorDTO.getTel()
                )
        );
    }

    @Override
    public boolean deleteVendor(String vendorId) throws SQLException, ClassNotFoundException {
        return vendorDAO.delete(vendorId);
    }

    @Override
    public boolean existVendor(String vendorId) {
        return false;
    }

    @Override
    public String generateVendorId() throws SQLException, ClassNotFoundException {
        return vendorDAO.generateNewId();
    }

    @Override
    public String getSupplierIdByName(String supplierName) {
        return vendorDAO.getSupplierIdByName(supplierName);
    }

    @Override
    public List<SupplierDTO> getAllVendors() throws SQLException, ClassNotFoundException {
        return vendorDAO.getAll().stream().map(vendor -> new SupplierDTO(
                vendor.getSupplierId(),
                vendor.getName(),
                vendor.getAddress(),
                vendor.getEmail(),
                vendor.getTel()
        )).toList();
    }
}
