package lk.wms.aquaflow.dao.custom;

import lk.wms.aquaflow.dao.CrudDAO;
import lk.wms.aquaflow.entity.Officer;

import java.sql.SQLException;

public interface OfficerDAO extends CrudDAO<Officer> {
    String getOfficerIdByName(String name) throws SQLException, ClassNotFoundException;
}