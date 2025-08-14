package lk.wms.aquaflow.bo.custom;

import lk.wms.aquaflow.bo.SuperBO;
import lk.wms.aquaflow.dto.SupplierDTO;

import java.util.List;

public interface VendorBO extends SuperBO {
    public boolean addVendor(SupplierDTO vendorDTO);
    public SupplierDTO searchVendor(String supplierId);
    public boolean updateVendor(SupplierDTO vendorDTO);
    public boolean deleteVendor(String vendorId);
    public boolean existVendor(String vendorId);
    public String generateVendorId();
    public String getSupplierIdByName(String supplierName);
    public List<SupplierDTO> getAllVendors();
}
