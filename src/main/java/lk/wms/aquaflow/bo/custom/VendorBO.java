package lk.wms.aquaflow.bo.custom;

import lk.wms.aquaflow.bo.SuperBO;
import lk.wms.aquaflow.dto.SupplierDTO;

import java.sql.SQLException;
import java.util.List;

public interface VendorBO extends SuperBO {
    public boolean addVendor(SupplierDTO vendorDTO) throws SQLException, ClassNotFoundException;
    public SupplierDTO searchVendor(String supplierId);
    public boolean updateVendor(SupplierDTO vendorDTO) throws SQLException, ClassNotFoundException;
    public boolean deleteVendor(String vendorId) throws SQLException, ClassNotFoundException;
    public boolean existVendor(String vendorId);
    public String generateVendorId() throws SQLException, ClassNotFoundException;
    public String getSupplierIdByName(String supplierName);
    public List<SupplierDTO> getAllVendors() throws SQLException, ClassNotFoundException;
}
