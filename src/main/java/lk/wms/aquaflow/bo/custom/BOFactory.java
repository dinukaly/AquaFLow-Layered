package lk.wms.aquaflow.bo.custom;

import lk.wms.aquaflow.bo.SuperBO;
import lk.wms.aquaflow.bo.custom.impl.*;

public class BOFactory {
    public static BOFactory boFactory;
    private BOFactory(){
    }

    public static BOFactory getBoFactory(){
        return (boFactory == null) ? boFactory = new BOFactory() : boFactory;
    }

    public enum BOTypes{
        OFFICER, VENDOR, HOUSEHOLD, CONSUMPTION, INVENTORY, DASHBOARD, LOGIN, VILLAGE, DISTRIBUTION, COMPLAINTS, WATER_ALLOCATION, WATER_SOURCE, BILLING, MAINTENANCE;
    }
    public SuperBO getBO(BOTypes types){
        switch (types){
            case OFFICER:
                return new OfficerBOImpl();

            case VILLAGE:
                return new VillageBOImpl();
            case DASHBOARD:
                return new DashboardBOImpl();
            case LOGIN:
                return new LoginBOImpl();
            case DISTRIBUTION:
                return new WaterDistributionBOImpl();
            case WATER_ALLOCATION:
                return new WaterAllocationBOImpl();
            case WATER_SOURCE:
                return new WaterSourceBOImpl();
            case BILLING:
                return new BillingBOImpl();
            case HOUSEHOLD:
                return new HouseholdBOImpl();
            case VENDOR:
                return new VendorBOImpl();
            case COMPLAINTS:
                return new ComplaintsBOImpl();
            case INVENTORY:
                return new InventoryBOImpl();
            case CONSUMPTION:
                return new ConsumptionsBOImpl();
            case MAINTENANCE:
                return new MaintenanceBOImpl();
            default:
                return null;
        }
    }
}
