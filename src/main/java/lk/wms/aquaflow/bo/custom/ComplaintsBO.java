package lk.wms.aquaflow.bo.custom;

import lk.wms.aquaflow.bo.SuperBO;
import lk.wms.aquaflow.dto.ComplaintDTO;

import java.util.ArrayList;

public interface ComplaintsBO extends SuperBO {
    public String getNextComplaintId();
    public boolean addComplaint(ComplaintDTO complaintDTO);
    public boolean updateComplaint(ComplaintDTO complaintDTO);
    public boolean deleteComplaint(String complaintId);
    public boolean updateComplaintStatus(String complaintId, String status);
    public ComplaintDTO getComplaintById(String complaintId);
    public ArrayList<ComplaintDTO> getAllComplaints();
    public int getTotalComplaintsCount();
    public int getSolvedComplaintsCount();
    public int getScheduledComplaintsCount();
    public int getNewComplaintsThisMonth();
}
