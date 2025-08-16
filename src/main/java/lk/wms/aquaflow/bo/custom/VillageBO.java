package lk.wms.aquaflow.bo.custom;

import lk.wms.aquaflow.bo.SuperBO;
import lk.wms.aquaflow.dto.VillageDTO;
import lk.wms.aquaflow.dto.custom.VillageWithOfficersDTO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface VillageBO extends SuperBO {
    public boolean addVillage(VillageDTO villageDTO) throws SQLException, ClassNotFoundException;
    public List<VillageWithOfficersDTO> searchVillage(String villageId);
    public boolean updateVillage(VillageDTO villageDTO) throws SQLException, ClassNotFoundException;
    public boolean deleteVillage(String villageId) throws SQLException, ClassNotFoundException;
    public boolean existVillage(String villageId);
    public String generateVillageId() throws SQLException, ClassNotFoundException;
    public VillageDTO getVillageById(String villageId);
    public String getVillageIdByName(String villageName);
    public ArrayList<VillageDTO> getAllVillages() throws SQLException, ClassNotFoundException;
    public List<VillageWithOfficersDTO> getVillageWithOfficers() throws SQLException, ClassNotFoundException;
    public List<VillageDTO> getVillageIdAndName() throws SQLException, ClassNotFoundException;
    List<String> getAllVillageNames() throws SQLException, ClassNotFoundException;
}