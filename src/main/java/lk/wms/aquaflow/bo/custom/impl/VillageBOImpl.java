package lk.wms.aquaflow.bo.custom.impl;

import lk.wms.aquaflow.bo.custom.VillageBO;
import lk.wms.aquaflow.dto.VillageDTO;

import java.util.List;

public class VillageBOImpl implements VillageBO {
    @Override
    public boolean addVillage(VillageDTO villageDTO) {
        return false;
    }

    @Override
    public List<VillageDTO> searchVillage(String villageId) {
        return List.of();
    }

    @Override
    public boolean updateVillage(VillageDTO villageDTO) {
        return false;
    }

    @Override
    public boolean deleteVillage(String villageId) {
        return false;
    }

    @Override
    public boolean existVillage(String villageId) {
        return false;
    }

    @Override
    public String generateVillageId() {
        return "";
    }

    @Override
    public VillageDTO getVillageById(String villageId) {
        return null;
    }

    @Override
    public String getVillageIdByName(String villageName) {
        return "";
    }

    @Override
    public List<VillageDTO> getAllVillages() {
        return List.of();
    }
}
