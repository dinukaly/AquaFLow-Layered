package lk.wms.aquaflow.bo.custom.impl;

import lk.wms.aquaflow.bo.custom.HouseholdsBO;
import lk.wms.aquaflow.dto.HouseholdDTO;

import java.util.ArrayList;
import java.util.List;

public class HouseholdBOImpl implements HouseholdsBO{
    @Override
    public boolean addHousehold(HouseholdDTO householdDTO) {
        return false;
    }

    @Override
    public List<HouseholdDTO> searchHouseholds(String householdId) {
        return List.of();
    }


    @Override
    public boolean updateHousehold(HouseholdDTO householdDTO) {
        return false;
    }

    @Override
    public boolean deleteHousehold(String householdId) {
        return false;
    }

    @Override
    public boolean existHousehold(String householdId) {
        return false;
    }

    @Override
    public String generateHouseholdId() {
        return "";
    }

    @Override
    public ArrayList<HouseholdDTO> getAllHouseholds() {
        return new ArrayList<>();
    }

    @Override
    public HouseholdDTO getHouseholdById(String householdId) {
        return null;
    }
}
