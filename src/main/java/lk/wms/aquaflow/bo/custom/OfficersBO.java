package lk.wms.aquaflow.bo.custom;

import lk.wms.aquaflow.bo.SuperBO;
import lk.wms.aquaflow.dto.OfficerDTO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface OfficersBO extends SuperBO {
    public boolean addOfficer(OfficerDTO officerDTO) throws SQLException, ClassNotFoundException;
    public OfficerDTO searchOfficer(String officerId);
    public boolean updateOfficer(OfficerDTO officerDTO) throws SQLException, ClassNotFoundException;
    public boolean deleteOfficer(String officerId) throws SQLException, ClassNotFoundException;
    public String getOfficerIdByName(String officerName) throws SQLException, ClassNotFoundException;
    public String generateOfficerId() throws SQLException, ClassNotFoundException;
    public ArrayList<OfficerDTO> getAllOfficers() throws SQLException, ClassNotFoundException;
}
