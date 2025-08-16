package lk.wms.aquaflow.bo.custom.impl;

import lk.wms.aquaflow.bo.custom.LoginBO;
import lk.wms.aquaflow.dao.DAOFactory;
import lk.wms.aquaflow.dao.custom.LoginDAO;

import java.sql.SQLException;

public class LoginBOImpl implements LoginBO {
    public final LoginDAO loginDAO = (LoginDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.LOGIN);
    @Override
    public boolean login(String username, String password) {
        return false;
    }

    @Override
    public boolean register(String username, String password) {
        return false;
    }

    @Override
    public boolean updatePassword(String username, String password) {
        return false;
    }

    @Override
    public boolean validateUser(String username, String password, String userType) throws SQLException, ClassNotFoundException {
        System.out.println("loginDAO is: " + loginDAO);

        return loginDAO.checkCredentials(username, password, userType);
    }
}