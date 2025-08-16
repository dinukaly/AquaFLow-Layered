package lk.wms.aquaflow.dao.custom;

import lk.wms.aquaflow.dao.CrudDAO;
import lk.wms.aquaflow.entity.Consumption;

import java.util.ArrayList;
import java.util.Map;

public interface ConsumptionDAO extends CrudDAO<Consumption> {
    Consumption getConsumptionById(String consumptionId);
    ArrayList<Consumption> getConsumptionsByHouseId(String houseId);
   Map<String, Number> getMonthlyConsumptionForYear(String year);
}