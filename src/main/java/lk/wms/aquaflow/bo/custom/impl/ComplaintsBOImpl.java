package lk.wms.aquaflow.bo.custom.impl;

import lk.wms.aquaflow.bo.custom.ComplaintsBO;
import lk.wms.aquaflow.dto.ComplaintDTO;

import java.util.ArrayList;

public class ComplaintsBOImpl implements ComplaintsBO {

    @Override
    public String getNextComplaintId() {
        return "";
    }

    @Override
    public boolean addComplaint(ComplaintDTO complaintDTO) {
        return false;
    }

    @Override
    public boolean updateComplaint(ComplaintDTO complaintDTO) {
        return false;
    }

    @Override
    public boolean deleteComplaint(String complaintId) {
        return false;
    }

    @Override
    public boolean updateComplaintStatus(String complaintId, String status) {
        return false;
    }

    @Override
    public ComplaintDTO getComplaintById(String complaintId) {
        return null;
    }

    @Override
    public ArrayList<ComplaintDTO> getAllComplaints() {
        return null;
    }

    @Override
    public int getTotalComplaintsCount() {
        return 0;
    }

    @Override
    public int getSolvedComplaintsCount() {
        return 0;
    }

    @Override
    public int getScheduledComplaintsCount() {
        return 0;
    }

    @Override
    public int getNewComplaintsThisMonth() {
        return 0;
    }
}
