package lk.wms.aquaflow.dao.custom.impl;



import lk.wms.aquaflow.dao.custom.LoginDAO;
import lk.wms.aquaflow.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginDAOImpl implements LoginDAO {
    @Override
    public boolean checkCredentials(String username, String password, String userType) throws SQLException, ClassNotFoundException {
        String sql;
        if ("ADMIN".equals(userType)) {
            sql = "SELECT * FROM admin WHERE username = ? AND password = ?";
        } else if ("OPERATOR".equals(userType)) {
            sql = "SELECT * FROM operator WHERE username = ? AND password = ?";
        } else {
            return false;
        }

        ResultSet resultSet = CrudUtil.execute(sql, username, password);

        return resultSet.next();
    }
}