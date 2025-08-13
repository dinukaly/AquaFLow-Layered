package lk.wms.aquaflow.util;

import lk.wms.aquaflow.db.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class CrudUtil {

    public static <T> T execute(String sql, Object... obj) throws SQLException, ClassNotFoundException {
        System.out.println("[SQL] Executing: " + sql);
        System.out.println("[SQL] Params: " + Arrays.toString(obj));
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pst = connection.prepareStatement(sql);


        for (int i = 0; i < obj.length; i++) {
            System.out.printf("[SQL] Binding param %d: %s (%s)%n",
                    i+1, obj[i], (obj[i] != null ? obj[i].getClass().getSimpleName() : "null"));
            pst.setObject(i + 1, obj[i]);
        }

        if (sql.startsWith("select") || sql.startsWith("SELECT")) {

            ResultSet resultSet = pst.executeQuery();

            return (T) resultSet;
        } else {


            int i = pst.executeUpdate();

            boolean isSuccess = i > 0;

            return (T) (Boolean) isSuccess;
        }
    }
}
