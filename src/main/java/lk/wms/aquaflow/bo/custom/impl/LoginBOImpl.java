package lk.wms.aquaflow.bo.custom.impl;

import lk.wms.aquaflow.bo.custom.LoginBO;

public class LoginBOImpl implements LoginBO {
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
    public boolean validateUser(String username, String password) {
        return false;
    }
}
