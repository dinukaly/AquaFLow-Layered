package lk.wms.aquaflow.bo.custom;

import lk.wms.aquaflow.bo.SuperBO;
import lk.wms.aquaflow.dto.HouseholdDTO;
import lk.wms.aquaflow.dto.custom.HouseholdWithVillageDTO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface HouseholdsBO extends SuperBO {
    public boolean addHousehold(HouseholdDTO householdDTO) throws SQLException, ClassNotFoundException;
    public List<HouseholdWithVillageDTO> searchHouseholds(String householdId);
    public boolean updateHousehold(HouseholdDTO householdDTO) throws SQLException, ClassNotFoundException;
    public boolean deleteHousehold(String householdId) throws SQLException, ClassNotFoundException;
    public boolean existHousehold(String householdId);
    public String generateHouseholdId() throws SQLException, ClassNotFoundException;
    public ArrayList<HouseholdWithVillageDTO> getAllHouseholdWithVillage() throws SQLException, ClassNotFoundException;
    public ArrayList<HouseholdDTO> getAllHouseholds() throws SQLException, ClassNotFoundException;
    public HouseholdDTO getHouseholdById(String householdId);
}
