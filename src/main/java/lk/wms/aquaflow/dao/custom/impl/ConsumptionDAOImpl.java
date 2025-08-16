package lk.wms.aquaflow.dao.custom.impl;

import lk.wms.aquaflow.dao.custom.ConsumptionDAO;
import lk.wms.aquaflow.entity.Consumption;
import lk.wms.aquaflow.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ConsumptionDAOImpl implements ConsumptionDAO {
    @Override
    public boolean add(Consumption consumption) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO consumption (consumption_id, amount_of_units, start_date, end_date, house_id) VALUES (?, ?, ?, ?, ?)",
                consumption.getConsumptionId(),
                consumption.getAmountOfUnits(),
                consumption.getStartDate(),
                consumption.getEndDate(),
                consumption.getHouseId());
    }

    @Override
    public boolean update(Consumption consumption) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE consumption SET amount_of_units = ?, start_date = ?, end_date = ?, house_id = ? WHERE consumption_id = ?",
                consumption.getAmountOfUnits(),
                consumption.getStartDate(),
                consumption.getEndDate(),
                consumption.getHouseId(),
                consumption.getConsumptionId());
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM consumption WHERE consumption_id = ?", id);
    }

    @Override
    public String generateNewId() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT consumption_id FROM consumption ORDER BY consumption_id DESC LIMIT 1");
        if (rst.next()) {
            String id = rst.getString("consumption_id");
            int newConsumptionId = Integer.parseInt(id.replace("C", "")) + 1;
            return String.format("C%03d", newConsumptionId);
        } else {
            return "C001";
        }
    }

    @Override
    public Consumption get(String id) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM consumption WHERE consumption_id = ?", id);
        if (resultSet.next()) {
            return new Consumption(
                    resultSet.getString("consumption_id"),
                    resultSet.getString("amount_of_units"),
                    resultSet.getString("start_date"),
                    resultSet.getString("end_date"),
                    resultSet.getString("house_id")
            );
        }
        return null;
    }

    @Override
    public ArrayList<Consumption> getAll() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM consumption");
        ArrayList<Consumption> consumptions = new ArrayList<>();
        while (resultSet.next()) {
            consumptions.add(new Consumption(
                    resultSet.getString("consumption_id"),
                    resultSet.getString("amount_of_units"),
                    resultSet.getString("start_date"),
                    resultSet.getString("end_date"),
                    resultSet.getString("house_id")
            ));
        }
        return consumptions;
    }

    @Override
    public Consumption search(String keyword) throws SQLException, ClassNotFoundException {
        return get(keyword);
    }

    @Override
    public Consumption getConsumptionById(String consumptionId) {
        try {
            return get(consumptionId);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<Consumption> getConsumptionsByHouseId(String houseId) {
        try {
            ResultSet resultSet = CrudUtil.execute("SELECT * FROM consumption WHERE house_id = ?", houseId);
            ArrayList<Consumption> consumptions = new ArrayList<>();
            while (resultSet.next()) {
                consumptions.add(new Consumption(
                        resultSet.getString("consumption_id"),
                        resultSet.getString("amount_of_units"),
                        resultSet.getString("start_date"),
                        resultSet.getString("end_date"),
                        resultSet.getString("house_id")
                ));
            }
            return consumptions;
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<String, Number> getMonthlyConsumptionForYear(String year) {
        Map<String, Number> monthlyConsumption = new HashMap<>();
        try {
            ResultSet resultSet = CrudUtil.execute(
                    "SELECT MONTH(end_date) as month, SUM(amount_of_units) as total_units " +
                            "FROM consumption " +
                            "WHERE YEAR(end_date) = ? " +
                            "GROUP BY MONTH(end_date)", year);
            while (resultSet.next()) {
                monthlyConsumption.put(String.valueOf(resultSet.getInt("month")), resultSet.getInt("total_units"));
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return monthlyConsumption;
    }
}