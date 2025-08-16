package lk.wms.aquaflow.dao.custom.impl;

import lk.wms.aquaflow.dao.custom.WaterAllocationDAO;
import lk.wms.aquaflow.entity.SourceAllocation;
import lk.wms.aquaflow.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WaterAllocationDAOImpl implements WaterAllocationDAO {
    @Override
    public boolean add(SourceAllocation sourceAllocation) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO source_allocation VALUES (?,?,?,?,?)", sourceAllocation.getAllocationId(), sourceAllocation.getAllocationAmount(), sourceAllocation.getWatersourceId(), sourceAllocation.getVillageId(), sourceAllocation.getAllocationDate());
    }

    @Override
    public boolean update(SourceAllocation sourceAllocation) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE source_allocation SET allocation_amount=?, watersource_id=?, village_id=?, allocation_date=? WHERE allocation_id=?", sourceAllocation.getAllocationAmount(), sourceAllocation.getWatersourceId(), sourceAllocation.getVillageId(), sourceAllocation.getAllocationDate(), sourceAllocation.getAllocationId());
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM source_allocation WHERE allocation_id=?", id);
    }

    @Override
    public String generateNewId() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT allocation_id FROM source_allocation ORDER BY allocation_id DESC LIMIT 1");
        if (rst.next()) {
            String id = rst.getString(1);
            int newId = Integer.parseInt(id.replace("A", "")) + 1;
            return String.format("A%03d", newId);
        } else {
            return "A001";
        }
    }

    @Override
    public SourceAllocation get(String id) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT * FROM source_allocation WHERE allocation_id=?", id);
        if (rst.next()) {
            return new SourceAllocation(rst.getString(1), rst.getDouble(2), rst.getString(3), rst.getString(4), rst.getDate(5));
        }
        return null;
    }

    @Override
    public ArrayList<SourceAllocation> getAll() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT * FROM source_allocation");
        ArrayList<SourceAllocation> allAllocations = new ArrayList<>();
        while (rst.next()) {
            allAllocations.add(new SourceAllocation(rst.getString(1), rst.getDouble(2), rst.getString(3), rst.getString(4), rst.getDate(5)));
        }
        return allAllocations;
    }

    @Override
    public SourceAllocation search(String keyword) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT * FROM source_allocation WHERE allocation_id=?", keyword);
        if (rst.next()) {
            return new SourceAllocation(rst.getString(1), rst.getDouble(2), rst.getString(3), rst.getString(4), rst.getDate(5));
        }
        return null;
    }

    @Override
    public boolean addAllocation(SourceAllocation allocation) throws SQLException, ClassNotFoundException {
        return add(allocation);
    }

    @Override
    public boolean updateAllocation(SourceAllocation allocation) throws SQLException, ClassNotFoundException {
        return update(allocation);
    }

    @Override
    public String getWaterSourceIdForVillage(String villageId) {
        try {
            ResultSet rst = CrudUtil.execute("SELECT watersource_id FROM source_allocation WHERE village_id=?", villageId);
            if (rst.next()) {
                return rst.getString(1);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<SourceAllocation> getAllocationsByWaterSourceId(String waterSourceId) {
        try {
            ResultSet rst = CrudUtil.execute("SELECT * FROM source_allocation WHERE watersource_id=?", waterSourceId);
            List<SourceAllocation> allocations = new ArrayList<>();
            while (rst.next()) {
                allocations.add(new SourceAllocation(rst.getString(1), rst.getDouble(2), rst.getString(3), rst.getString(4), rst.getDate(5)));
            }
            return allocations;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}