package lk.wms.aquaflow.bo.custom.impl;

import lk.wms.aquaflow.bo.custom.OfficersBO;
import lk.wms.aquaflow.dto.OfficerDTO;

import java.util.List;

public class OfficerBOImpl implements OfficersBO {
    @Override
    public boolean addOfficer(OfficerDTO officerDTO) {
        return false;
    }

    @Override
    public OfficerDTO searchOfficer(String officerId) {
        return null;
    }

    @Override
    public boolean updateOfficer(OfficerDTO officerDTO) {
        return false;
    }

    @Override
    public boolean deleteOfficer(String officerId) {
        return false;
    }

    @Override
    public String getOfficerIdByName(String officerName) {
        return "";
    }


    @Override
    public String generateOfficerId() {
        return "";
    }

    @Override
    public List<OfficerDTO> getAllOfficers() {
        return List.of();
    }
}
