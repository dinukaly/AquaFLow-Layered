package lk.wms.aquaflow.dao.custom;

import lk.wms.aquaflow.dao.CrudDAO;
import lk.wms.aquaflow.entity.Complaint;

public interface ComplaintDAO extends CrudDAO<Complaint> {
    boolean updateComplaintStatus(String complaintId, String status);
    Complaint getComplaintById(String complaintId);
    int getTotalComplaintsCount();
    int getSolvedComplaintsCount();
    int getScheduledComplaintsCount();
    int getNewComplaintsThisMonth();
}
