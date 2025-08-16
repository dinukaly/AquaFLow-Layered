package lk.wms.aquaflow.bo.custom;

import lk.wms.aquaflow.bo.SuperBO;
import lk.wms.aquaflow.dto.ComplaintDTO;
import lk.wms.aquaflow.dto.custom.ComplaintsWithOwnerEmailDTO;

import java.sql.SQLException;
import java.util.ArrayList;

public interface ComplaintsBO extends SuperBO {
    public String getNextComplaintId() throws SQLException, ClassNotFoundException;
    public boolean addComplaint(ComplaintDTO complaintDTO) throws SQLException, ClassNotFoundException;
    public boolean updateComplaint(ComplaintDTO complaintDTO) throws SQLException, ClassNotFoundException;
    public boolean deleteComplaint(String complaintId) throws SQLException, ClassNotFoundException;
    public boolean updateComplaintStatus(String complaintId, String status);
    public ComplaintDTO getComplaintById(String complaintId) throws SQLException, ClassNotFoundException;
    public ComplaintsWithOwnerEmailDTO getComplaintByIdWithEmail(String complaintId) throws SQLException, ClassNotFoundException;
    public ArrayList<ComplaintsWithOwnerEmailDTO> getAllComplaints() throws SQLException, ClassNotFoundException;
    public ArrayList<ComplaintsWithOwnerEmailDTO> getRecentComplaints() throws SQLException, ClassNotFoundException;
    public int getTotalComplaintsCount();
    public int getSolvedComplaintsCount();
    public int getScheduledComplaintsCount();
    public int getNewComplaintsThisMonth();
}
