package lk.wms.aquaflow.dao;

import lk.wms.aquaflow.dao.custom.impl.*;

public class DAOFactory {
    public static DAOFactory daoFactory;
    private DAOFactory(){

    }
    public static DAOFactory getDaoFactory(){
        return (daoFactory == null) ? daoFactory = new DAOFactory() : daoFactory;
    }

    public enum DAOTypes{
        OFFICER, VENDOR, HOUSEHOLD, CONSUMPTION, INVENTORY, INVENTORY_MAINTENANCE, DASHBOARD, LOGIN, VILLAGE, DISTRIBUTION, COMPLAINTS, WATER_ALLOCATION, WATER_SOURCE, BILLING, WATER_MAINTENANCE, QUERY, PAYMENT;    }
    public SuperDAO getDAO(DAOTypes types){
        switch (types){
            case QUERY:
                return new QueryDAOImpl();
            case OFFICER:
                return new OfficerDAOImpl();
            case VILLAGE:
                return new VillageDAOImpl();
            case HOUSEHOLD:
                return new HouseholdDAOImpl();
            case WATER_SOURCE:
                return new WaterSourceDAOImpl();
            case WATER_ALLOCATION:
                return new WaterAllocationDAOImpl();
            case INVENTORY_MAINTENANCE:
                return new InventoryMaintenanceDAOImpl();
            case DISTRIBUTION:
                return new WaterDistributionDAOImpl();
            case COMPLAINTS:
                return new ComplaintDAOImpl();
            case CONSUMPTION:
                return new ConsumptionDAOImpl();
            case BILLING:
                return new BillingDAOImpl();
            case VENDOR:
                return new VendorDAOImpl();
            case INVENTORY:
                return new InventoryDAOImpl();
//            case DASHBOARD:
//                return new DashboardDAOImpl();
            case LOGIN:
                return new LoginDAOImpl();
            case WATER_MAINTENANCE:
                return new WaterMaintenanceDAOImpl();
            case PAYMENT:
                return new PaymentDAOImpl();
            default:
                return null;
        }
    }
}