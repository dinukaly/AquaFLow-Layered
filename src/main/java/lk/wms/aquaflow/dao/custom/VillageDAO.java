package lk.wms.aquaflow.dao.custom;

import lk.wms.aquaflow.dao.CrudDAO;
import lk.wms.aquaflow.entity.Village;

import java.sql.SQLException;
import java.util.List;

public interface VillageDAO extends CrudDAO<Village> {
    String getVillageIdByName(String villageName);
    List<String> getAllVillageNames() throws SQLException, ClassNotFoundException;
}