package lk.wms.aquaflow.bo.custom.impl;

import lk.wms.aquaflow.bo.custom.HouseholdsBO;
import lk.wms.aquaflow.dao.DAOFactory;
import lk.wms.aquaflow.dao.QueryDAO;
import lk.wms.aquaflow.dao.custom.HouseholdDAO;
import lk.wms.aquaflow.dao.custom.impl.HouseholdDAOImpl;
import lk.wms.aquaflow.dao.custom.impl.QueryDAOImpl;
import lk.wms.aquaflow.dto.HouseholdDTO;
import lk.wms.aquaflow.dto.custom.HouseholdWithVillageDTO;
import lk.wms.aquaflow.entity.Household;
import lk.wms.aquaflow.entity.custom.HouseholdWithVillage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HouseholdBOImpl implements HouseholdsBO{
    private final HouseholdDAO householdDAO = (HouseholdDAOImpl) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.HOUSEHOLD);
    private final QueryDAO queryDAO = (QueryDAOImpl) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.QUERY);
    @Override
    public boolean addHousehold(HouseholdDTO householdDTO) throws SQLException, ClassNotFoundException {
        return householdDAO.add(
                new Household(
                        householdDTO.getHouseId(),
                        householdDTO.getOwnerName(),
                        householdDTO.getAddress(),
                        householdDTO.getNoOfMembers(),
                        householdDTO.getEmail(),
                        householdDTO.getVillageId()
                )
        );
    }

    @Override
    public List<HouseholdWithVillageDTO> searchHouseholds(String householdId) {
        return List.of();
    }


    @Override
    public boolean updateHousehold(HouseholdDTO householdDTO) throws SQLException, ClassNotFoundException {
        return householdDAO.update(
                new Household(
                        householdDTO.getHouseId(),
                        householdDTO.getOwnerName(),
                        householdDTO.getAddress(),
                        householdDTO.getNoOfMembers(),
                        householdDTO.getEmail(),
                        householdDTO.getVillageId()
                )
        );
    }

    @Override
    public boolean deleteHousehold(String householdId) throws SQLException, ClassNotFoundException {
        return householdDAO.delete(householdId);
    }

    @Override
    public boolean existHousehold(String householdId) {
        return false;
    }

    @Override
    public String generateHouseholdId() throws SQLException, ClassNotFoundException {

        return householdDAO.generateNewId();
    }

    @Override
    public ArrayList<HouseholdWithVillageDTO> getAllHouseholdWithVillage() throws SQLException, ClassNotFoundException {
        ArrayList<HouseholdWithVillage> all = queryDAO.getAllHouseholdWithVillage();
        ArrayList<HouseholdWithVillageDTO> householdDTOS = new ArrayList<>();
        for (HouseholdWithVillage household : all) {
            householdDTOS.add(
                    new HouseholdWithVillageDTO(
                            household.getHouseId(),
                            household.getOwnerName(),
                            household.getAddress(),
                            household.getNoOfMembers(),
                            household.getEmail(),
                            household.getVillageId(),
                            household.getVillageName()
                    )
            );
        }
        return householdDTOS;
    }

    @Override
    public ArrayList<HouseholdDTO> getAllHouseholds() throws SQLException, ClassNotFoundException {
        ArrayList<Household> all = householdDAO.getAll();
        ArrayList<HouseholdDTO> householdDTOS = new ArrayList<>();
        for (Household household : all) {
            householdDTOS.add(
                    new HouseholdDTO(
                            household.getHouseId(),
                            household.getOwnerName(),
                            household.getAddress(),
                            household.getNoOfMembers(),
                            household.getEmail(),
                            household.getVillageId()
                    )
            );
        }
        return householdDTOS;
    }

    @Override
    public HouseholdDTO getHouseholdById(String householdId) {
        return null;
    }
}
