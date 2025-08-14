package lk.wms.aquaflow.bo.custom;

import lk.wms.aquaflow.bo.SuperBO;
import lk.wms.aquaflow.dto.HouseholdDTO;

import java.util.ArrayList;
import java.util.List;

public interface HouseholdsBO extends SuperBO {
    public boolean addHousehold(HouseholdDTO householdDTO);
    public List<HouseholdDTO> searchHouseholds(String householdId);
    public boolean updateHousehold(HouseholdDTO householdDTO);
    public boolean deleteHousehold(String householdId);
    public boolean existHousehold(String householdId);
    public String generateHouseholdId();
    public ArrayList<HouseholdDTO> getAllHouseholds();
    public HouseholdDTO getHouseholdById(String householdId);
}
