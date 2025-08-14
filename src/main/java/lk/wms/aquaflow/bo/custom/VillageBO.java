package lk.wms.aquaflow.bo.custom;

import lk.wms.aquaflow.bo.SuperBO;
import lk.wms.aquaflow.dto.VillageDTO;

import java.util.List;

public interface VillageBO extends SuperBO {
    public boolean addVillage(VillageDTO villageDTO);
    public List<VillageDTO> searchVillage(String villageId);
    public boolean updateVillage(VillageDTO villageDTO);
    public boolean deleteVillage(String villageId);
    public boolean existVillage(String villageId);
    public String generateVillageId();
    public VillageDTO getVillageById(String villageId);
    public String getVillageIdByName(String villageName);
    public List<VillageDTO> getAllVillages();
    public List<VillageDTO> getVillageIdAndName();
}
