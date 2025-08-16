package lk.wms.aquaflow.bo.custom.impl;

import lk.wms.aquaflow.bo.custom.ConsumptionsBO;
import lk.wms.aquaflow.dao.DAOFactory;
import lk.wms.aquaflow.dao.QueryDAO;
import lk.wms.aquaflow.dao.custom.ConsumptionDAO;
import lk.wms.aquaflow.dao.custom.impl.ConsumptionDAOImpl;
import lk.wms.aquaflow.dto.ConsumptionDTO;
import lk.wms.aquaflow.dto.custom.ConsumptionWithHouseVillageDTO;
import lk.wms.aquaflow.entity.Consumption;
import lk.wms.aquaflow.entity.custom.ConsumptionWithHouseVillage;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConsumptionsBOImpl implements ConsumptionsBO {
    private final ConsumptionDAO consumptionDAO = (ConsumptionDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.CONSUMPTION);
    private final QueryDAO queryDAO = (QueryDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.QUERY);
    @Override
    public boolean addConsumption(ConsumptionDTO consumptionDTO) throws SQLException, ClassNotFoundException {
       return consumptionDAO.add(
               new Consumption(
                       consumptionDTO.getConsumptionId(),
                       consumptionDTO.getAmountOfUnits(),
                       consumptionDTO.getStartDate(),
                       consumptionDTO.getEndDate(),
                       consumptionDTO.getHouseId()
               )
       );

    }

    @Override
    public ConsumptionDTO searchConsumption(String consumptionId) {
        return null;
    }

    @Override
    public boolean updateConsumption(ConsumptionDTO consumptionDTO) throws SQLException, ClassNotFoundException {
        return consumptionDAO.update(
                new Consumption(
                        consumptionDTO.getConsumptionId(),
                        consumptionDTO.getAmountOfUnits(),
                        consumptionDTO.getStartDate(),
                        consumptionDTO.getEndDate(),
                        consumptionDTO.getHouseId()
                )
        );
    }

    @Override
    public boolean deleteConsumption(String consumptionId) throws SQLException, ClassNotFoundException {
        return consumptionDAO.delete(consumptionId);
    }

    @Override
    public boolean existConsumption(String consumptionId) {
        return false;
    }

    @Override
    public String generateConsumptionId() throws SQLException, ClassNotFoundException {
        return consumptionDAO.generateNewId();
    }

    @Override
    public ConsumptionDTO getPreviousConsumption(String houseId, String currentDate) {
        //parse the current end date
        LocalDate endDate = LocalDate.parse(currentDate);

        //get all consumptions fo this household
        ArrayList<Consumption> consumptions = consumptionDAO.getConsumptionsByHouseId(houseId);
        //find the most recent consumption before the current end date
        for (Consumption consumption : consumptions) {
            LocalDate consumptionEndDate = LocalDate.parse(consumption.getEndDate());
            if (consumptionEndDate.isBefore(endDate)) {
                return new ConsumptionDTO(
                        consumption.getConsumptionId(),
                        consumption.getAmountOfUnits(),
                        consumption.getStartDate(),
                        consumption.getEndDate(),
                        consumption.getHouseId()
                );
            }
        }
        return null;
    }

    @Override
    public ConsumptionDTO getConsumptionById(String consumptionId) {
       Consumption consumption= consumptionDAO.getConsumptionById(consumptionId);
       return new ConsumptionDTO(
               consumption.getConsumptionId(),
               consumption.getAmountOfUnits(),
               consumption.getStartDate(),
               consumption.getEndDate(),
               consumption.getHouseId()
       );
    }

    @Override
    public Map<String, Number> getMonthlyConsumptionForYear(String year) {
        return consumptionDAO.getMonthlyConsumptionForYear(year);
    }

    @Override
    public Map<String, Number> getConsumptionByVillageForYear(String year) throws SQLException, ClassNotFoundException {
        return queryDAO.getConsumptionByVillageForYear(year);
    }

    @Override
    public ArrayList<ConsumptionDTO> getAllConsumptions() {
        return new ArrayList<>();
    }

    @Override
    public String getHouseholdIdFromBill(String billId) {
        return "";
    }

    @Override
    public List<ConsumptionWithHouseVillageDTO> getAllConsumptionDetails() throws SQLException, ClassNotFoundException {
        List<ConsumptionWithHouseVillage> list = queryDAO.getConsumptionDetails();
        List<ConsumptionWithHouseVillageDTO> dtos = new ArrayList<>();
        for (ConsumptionWithHouseVillage e : list) {
            ConsumptionWithHouseVillageDTO dto = new ConsumptionWithHouseVillageDTO(
                    e.getConsumptionId(),
                    e.getAmountOfUnits(),
                    Date.valueOf(e.getStartDate()),
                    Date.valueOf(e.getEndDate()),
                    e.getHouseId(),
                    e.getOwnerName(),
                    e.getVillageName()
            );

            String previousMonth = "0";
            String change = "0";

            try {
                ConsumptionDTO previousConsumption = getPreviousConsumption(dto.getHouseId(), dto.getEndDate().toString());
                if (previousConsumption != null) {
                    previousMonth = previousConsumption.getAmountOfUnits();

                    // Calculate change
                    int current = Integer.parseInt(dto.getAmountOfUnits());
                    int previous = Integer.parseInt(previousMonth);
                    int changeValue = current - previous;
                    change = String.valueOf(changeValue);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            dto.setPreviousMonth(previousMonth);
            dto.setChange(change);
            dtos.add(dto);
        }
        return dtos;
    }
}