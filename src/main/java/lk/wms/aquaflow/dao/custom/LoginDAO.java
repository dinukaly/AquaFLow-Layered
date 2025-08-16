package lk.wms.aquaflow.dao.custom;

import lk.wms.aquaflow.dao.SuperDAO;

import java.sql.SQLException;

public interface LoginDAO extends SuperDAO {
    boolean checkCredentials(String username, String password, String userType) throws SQLException, ClassNotFoundException;
}