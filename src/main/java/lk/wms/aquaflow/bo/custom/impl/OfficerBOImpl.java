package lk.wms.aquaflow.bo.custom.impl;

import lk.wms.aquaflow.bo.custom.OfficersBO;
import lk.wms.aquaflow.dao.DAOFactory;
import lk.wms.aquaflow.dao.custom.impl.OfficerDAOImpl;
import lk.wms.aquaflow.dto.OfficerDTO;
import lk.wms.aquaflow.entity.Officer;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OfficerBOImpl implements OfficersBO {
    private final OfficerDAOImpl officerDAOImpl = (OfficerDAOImpl) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.OFFICER);
    @Override
    public boolean addOfficer(OfficerDTO officerDTO) throws SQLException, ClassNotFoundException {
        return officerDAOImpl.add(
                new Officer(
                        officerDTO.getOfficerId(),
                        officerDTO.getName(),
                        officerDTO.getAddress(),
                        officerDTO.getEmail(),
                        officerDTO.getTelephone()
                )
        );
    }

    @Override
    public OfficerDTO searchOfficer(String officerId) {
        return null;
    }

    @Override
    public boolean updateOfficer(OfficerDTO officerDTO) throws SQLException, ClassNotFoundException {
        return officerDAOImpl.update(
                new Officer(
                        officerDTO.getOfficerId(),
                        officerDTO.getName(),
                        officerDTO.getAddress(),
                        officerDTO.getEmail(),
                        officerDTO.getTelephone()
                )
        );
    }

    @Override
    public boolean deleteOfficer(String officerId) throws SQLException, ClassNotFoundException {
        return officerDAOImpl.delete(officerId);
    }

    @Override
    public String getOfficerIdByName(String officerName) throws SQLException, ClassNotFoundException {
        return officerDAOImpl.getOfficerIdByName(officerName);
    }


    @Override
    public String generateOfficerId() throws SQLException, ClassNotFoundException {
        return officerDAOImpl.generateNewId();
    }

    @Override
    public ArrayList<OfficerDTO> getAllOfficers() throws SQLException, ClassNotFoundException {
        ArrayList<Officer> officers = officerDAOImpl.getAll();
        ArrayList<OfficerDTO> officerDTOs = new ArrayList<>();
        for (Officer officer : officers) {
            officerDTOs.add(
                    new OfficerDTO(
                            officer.getOfficerId(),
                            officer.getName(),
                            officer.getAddress(),
                            officer.getEmail(),
                            officer.getTelephone()
                    )
            );
        }
        return officerDTOs;
    }
}
