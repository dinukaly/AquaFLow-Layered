package lk.wms.aquaflow.bo.custom.impl;

import lk.wms.aquaflow.bo.custom.VillageBO;
import lk.wms.aquaflow.dao.DAOFactory;
import lk.wms.aquaflow.dao.QueryDAO;
import lk.wms.aquaflow.dao.custom.impl.QueryDAOImpl;
import lk.wms.aquaflow.dao.custom.impl.VillageDAOImpl;
import lk.wms.aquaflow.dto.VillageDTO;
import lk.wms.aquaflow.dto.custom.VillageWithOfficersDTO;
import lk.wms.aquaflow.entity.Village;
import lk.wms.aquaflow.entity.custom.VillageWithOfficers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VillageBOImpl implements VillageBO {
    private final VillageDAOImpl villageDAOImpl = (VillageDAOImpl) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.VILLAGE);
    private final QueryDAO queryDAO = (QueryDAOImpl) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.QUERY);
    @Override
    public boolean addVillage(VillageDTO villageDTO) throws SQLException, ClassNotFoundException {
        return villageDAOImpl.add(
                new Village(
                        villageDTO.getVillageId(),
                        villageDTO.getVillageName(),
                        villageDTO.getPopulation(),
                        villageDTO.getWaterRequirement(),
                        villageDTO.getArea(),
                        villageDTO.getDistrict(),
                        villageDTO.getOfficerId()
                )
        );
    }

    @Override
    public List<VillageWithOfficersDTO> searchVillage(String villageId) {
        ArrayList<VillageWithOfficers> villages = queryDAO.searchVillage(villageId);
        ArrayList<VillageWithOfficersDTO> villageDTOS = new ArrayList<>();
        for (VillageWithOfficers v : villages) {
            villageDTOS.add(
                    new VillageWithOfficersDTO(
                            v.getVillageId(),
                            v.getVillageName(),
                            v.getPopulation(),
                            v.getWaterRequirement(),
                            v.getArea(),
                            v.getDistrict(),
                            v.getOfficerId(),
                            v.getOfficerName()
                    )
            );
        }
        return villageDTOS;
    }

    @Override
    public boolean updateVillage(VillageDTO villageDTO) throws SQLException, ClassNotFoundException {
        return villageDAOImpl.update(
                new Village(
                        villageDTO.getVillageId(),
                        villageDTO.getVillageName(),
                        villageDTO.getPopulation(),
                        villageDTO.getWaterRequirement(),
                        villageDTO.getArea(),
                        villageDTO.getDistrict(),
                        villageDTO.getOfficerId()
                )
        );
    }

    @Override
    public boolean deleteVillage(String villageId) throws SQLException, ClassNotFoundException {
        return villageDAOImpl.delete(villageId);
    }

    @Override
    public boolean existVillage(String villageId) {
        return false;
    }

    @Override
    public String generateVillageId() throws SQLException, ClassNotFoundException {
        return villageDAOImpl.generateNewId();
    }

    @Override
    public VillageDTO getVillageById(String villageId) {
        try {
            Village village = villageDAOImpl.get(villageId);
            if (village != null) {
                return new VillageDTO(
                        village.getVillageId(),
                        village.getVillageName(),
                        village.getPopulation(),
                        village.getWaterRequirement(),
                        village.getArea(),
                        village.getDistrict(),
                        village.getOfficerId()
                );
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public String getVillageIdByName(String villageName) {
        return villageDAOImpl.getVillageIdByName(villageName);
    }

    @Override
    public ArrayList<VillageDTO> getAllVillages() throws SQLException, ClassNotFoundException {
        ArrayList<Village> villages = villageDAOImpl.getAll();
        ArrayList<VillageDTO> villageDTOS = new ArrayList<>();
        for (Village v : villages) {
            villageDTOS.add(
                    new VillageDTO(
                            v.getVillageId(),
                            v.getVillageName(),
                            v.getPopulation(),
                            v.getWaterRequirement(),
                            v.getArea(),
                            v.getDistrict(),
                            v.getOfficerId()
                    )
            );
        }
        return villageDTOS;
    }

    @Override
    public List<VillageWithOfficersDTO> getVillageWithOfficers() throws SQLException, ClassNotFoundException {
        ArrayList<VillageWithOfficers> village = queryDAO.getAllVillages();
        ArrayList<VillageWithOfficersDTO> villageDTOS = new ArrayList<>();
        for (VillageWithOfficers v : village) {
            villageDTOS.add(
                    new VillageWithOfficersDTO(
                            v.getVillageId(),
                            v.getVillageName(),
                            v.getPopulation(),
                            v.getWaterRequirement(),
                            v.getArea(),
                            v.getDistrict(),
                            v.getOfficerId(),
                            v.getOfficerName()
                    )
            );
        }
        return villageDTOS;
    }

    @Override
    public ArrayList<VillageDTO> getVillageIdAndName() throws SQLException, ClassNotFoundException {
       return null;
    }

    @Override
    public List<String> getAllVillageNames() throws SQLException, ClassNotFoundException {
        return villageDAOImpl.getAllVillageNames();
    }
}