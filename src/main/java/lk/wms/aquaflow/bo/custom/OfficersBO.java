package lk.wms.aquaflow.bo.custom;

import lk.wms.aquaflow.bo.SuperBO;
import lk.wms.aquaflow.dto.OfficerDTO;

import java.util.List;

public interface OfficersBO extends SuperBO {
    public boolean addOfficer(OfficerDTO officerDTO);
    public OfficerDTO searchOfficer(String officerId);
    public boolean updateOfficer(OfficerDTO officerDTO);
    public boolean deleteOfficer(String officerId);
    public String getOfficerIdByName(String officerName);
    public String generateOfficerId();
    public List<OfficerDTO> getAllOfficers();
}
