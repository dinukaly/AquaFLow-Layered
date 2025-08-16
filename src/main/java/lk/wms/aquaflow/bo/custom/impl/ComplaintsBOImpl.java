package lk.wms.aquaflow.bo.custom.impl;

import lk.wms.aquaflow.bo.custom.ComplaintsBO;
import lk.wms.aquaflow.dao.DAOFactory;
import lk.wms.aquaflow.dao.QueryDAO;
import lk.wms.aquaflow.dao.custom.ComplaintDAO;
import lk.wms.aquaflow.dto.ComplaintDTO;
import lk.wms.aquaflow.dto.custom.ComplaintsWithOwnerEmailDTO;
import lk.wms.aquaflow.entity.Complaint;
import lk.wms.aquaflow.entity.custom.ComplaintWithOwnerEmail;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

public class ComplaintsBOImpl implements ComplaintsBO {
    private final ComplaintDAO complaintDAO = (ComplaintDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.COMPLAINTS);
    private final QueryDAO queryDAO = (QueryDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.QUERY);

    @Override
    public String getNextComplaintId() throws SQLException, ClassNotFoundException {
        return complaintDAO.generateNewId();
    }

    @Override
    public boolean addComplaint(ComplaintDTO complaintDTO) throws SQLException, ClassNotFoundException {
        return complaintDAO.add(new Complaint(
                complaintDTO.getComplaintId(),
                Date.valueOf(complaintDTO.getDate()),
                complaintDTO.getDescription(),
                complaintDTO.getStatus(),
                complaintDTO.getHouseId()
        ));
    }

    @Override
    public boolean updateComplaint(ComplaintDTO complaintDTO) throws SQLException, ClassNotFoundException {
        return complaintDAO.update(new Complaint(
                complaintDTO.getComplaintId(),
                Date.valueOf(complaintDTO.getDate()),
                complaintDTO.getDescription(),
                complaintDTO.getStatus(),
                complaintDTO.getHouseId()
        ));
    }

    @Override
    public boolean deleteComplaint(String complaintId) throws SQLException, ClassNotFoundException {
        return complaintDAO.delete(complaintId);
    }

    @Override
    public boolean updateComplaintStatus(String complaintId, String status) {
        return complaintDAO.updateComplaintStatus(complaintId, status);
    }

    @Override
    public ComplaintDTO getComplaintById(String complaintId) throws SQLException, ClassNotFoundException {
        Complaint complaint = queryDAO.getComplaintById(complaintId);
        return new ComplaintDTO(
                complaint.getComplaintId(),
                complaint.getDate().toString(),
                complaint.getDescription(),
                complaint.getStatus(),
                complaint.getHouseId()
        );
    }

    @Override
    public ComplaintsWithOwnerEmailDTO getComplaintByIdWithEmail(String complaintId) throws SQLException, ClassNotFoundException {
        ComplaintWithOwnerEmail complaint = queryDAO.getComplaintByIdWithEmail(complaintId);
        return new ComplaintsWithOwnerEmailDTO(
                complaint.getComplaintId(),
                complaint.getDate().toString(),
                complaint.getDescription(),
                complaint.getStatus(),
                complaint.getHouseId(),
                complaint.getOwnerEmail()
        );
    }

    @Override
    public ArrayList<ComplaintsWithOwnerEmailDTO> getAllComplaints() throws SQLException, ClassNotFoundException {
        ArrayList<ComplaintWithOwnerEmail> all = queryDAO.getAllComplaints();
        ArrayList<ComplaintsWithOwnerEmailDTO> allDTO = new ArrayList<>();
        for (ComplaintWithOwnerEmail complaint : all) {
            allDTO.add(new ComplaintsWithOwnerEmailDTO(
                    complaint.getComplaintId(),
                    complaint.getDate().toString(),
                    complaint.getDescription(),
                    complaint.getStatus(),
                    complaint.getHouseId(),
                    complaint.getOwnerEmail()
            ));
        }
        return allDTO;
    }

    @Override
    public ArrayList<ComplaintsWithOwnerEmailDTO> getRecentComplaints() throws SQLException, ClassNotFoundException {
        ArrayList<ComplaintWithOwnerEmail> complaints = queryDAO.getRecentComplaints();
        ArrayList<ComplaintsWithOwnerEmailDTO> complaintsWithOwnerEmailDTOS = new ArrayList<>();
        for (ComplaintWithOwnerEmail complaint : complaints) {
            complaintsWithOwnerEmailDTOS.add(new ComplaintsWithOwnerEmailDTO(
                    complaint.getComplaintId(),
                    complaint.getDate().toString(),
                    complaint.getDescription(),
                    complaint.getStatus(),
                    complaint.getHouseId(),
                    complaint.getOwnerEmail()
            ));
        }
        return complaintsWithOwnerEmailDTOS;
    }

    @Override
    public int getTotalComplaintsCount() {
        return complaintDAO.getTotalComplaintsCount();
    }

    @Override
    public int getSolvedComplaintsCount() {
        return complaintDAO.getSolvedComplaintsCount();
    }

    @Override
    public int getScheduledComplaintsCount() {
        return complaintDAO.getScheduledComplaintsCount();
    }

    @Override
    public int getNewComplaintsThisMonth() {
        return complaintDAO.getNewComplaintsThisMonth();
    }
}