package lk.wms.aquaflow.bo.custom.impl;

import lk.wms.aquaflow.bo.custom.VendorBO;
import lk.wms.aquaflow.dto.SupplierDTO;

import java.util.List;

public class VendorBOImpl implements VendorBO {
    @Override
    public boolean addVendor(SupplierDTO vendorDTO) {
        return false;
    }

    @Override
    public SupplierDTO searchVendor(String supplierId) {
        return null;
    }

    @Override
    public boolean updateVendor(SupplierDTO vendorDTO) {
        return false;
    }

    @Override
    public boolean deleteVendor(String vendorId) {
        return false;
    }

    @Override
    public boolean existVendor(String vendorId) {
        return false;
    }

    @Override
    public String generateVendorId() {
        return "";
    }

    @Override
    public String getSupplierIdByName(String supplierName) {
        return "";
    }

    @Override
    public List<SupplierDTO> getAllVendors() {
        return List.of();
    }
}
