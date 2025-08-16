package lk.wms.aquaflow.bo.custom;

import lk.wms.aquaflow.bo.SuperBO;

import java.sql.SQLException;

public interface LoginBO extends SuperBO {
    public boolean login(String username, String password);
    public boolean register(String username, String password);
    public boolean updatePassword(String username, String password);
    public boolean validateUser(String username, String password, String userType) throws SQLException, ClassNotFoundException;
}