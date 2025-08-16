package lk.wms.aquaflow.dao.custom;

import lk.wms.aquaflow.dao.CrudDAO;
import lk.wms.aquaflow.entity.Supplier;

public interface VendorDAO extends CrudDAO<Supplier> {
    String getSupplierIdByName(String supplierName);
}
