package lk.wms.aquaflow.bo.custom.impl;

import lk.wms.aquaflow.bo.custom.InventoryBO;
import lk.wms.aquaflow.dao.DAOFactory;
import lk.wms.aquaflow.dao.QueryDAO;
import lk.wms.aquaflow.dao.custom.InventoryDAO;
import lk.wms.aquaflow.dto.InventoryDTO;
import lk.wms.aquaflow.dto.custom.InventoryWithSupplierNameDTO;
import lk.wms.aquaflow.entity.Inventory;
import lk.wms.aquaflow.entity.custom.InventoryWithSupplierName;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class InventoryBOImpl implements InventoryBO {
    private final InventoryDAO inventoryDAO = (InventoryDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.INVENTORY);
    private final QueryDAO queryDAO = (QueryDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.QUERY);
    @Override
    public boolean addInventory(InventoryDTO inventoryDTO) throws SQLException, ClassNotFoundException {
        return inventoryDAO.add(
                new Inventory(
                        inventoryDTO.getInventoryId(),
                        inventoryDTO.getType(),
                        inventoryDTO.getQuantity(),
                        inventoryDTO.getUnitPrice(),
                        inventoryDTO.getSupplierId()
                )
        );
    }

    @Override
    public InventoryDTO searchInventory(String inventoryId) {
        return null;
    }

    @Override
    public boolean updateInventory(InventoryDTO inventoryDTO) throws SQLException, ClassNotFoundException {
        return inventoryDAO.update(
                new Inventory(
                        inventoryDTO.getInventoryId(),
                        inventoryDTO.getType(),
                        inventoryDTO.getQuantity(),
                        inventoryDTO.getUnitPrice(),
                        inventoryDTO.getSupplierId()
                )
        );
    }

    @Override
    public boolean deleteInventory(String inventoryId) throws SQLException, ClassNotFoundException {
        return inventoryDAO.delete(inventoryId);
    }

    @Override
    public boolean existInventory(String inventoryId) {
        return false;
    }

    @Override
    public String generateInventoryId() throws SQLException, ClassNotFoundException {
        return inventoryDAO.generateNewId();
    }

    @Override
    public InventoryWithSupplierNameDTO getInventoryByIdWithSupplierName(String inventoryId) throws SQLException, ClassNotFoundException {
        InventoryWithSupplierName inventoryWithSupplierName= queryDAO.getInventoryById(inventoryId);
        return new InventoryWithSupplierNameDTO(
                inventoryWithSupplierName.getInventoryId(),
                inventoryWithSupplierName.getType(),
                inventoryWithSupplierName.getQuantity(),
                inventoryWithSupplierName.getUnitPrice(),
                inventoryWithSupplierName.getSupplierId(),
                inventoryWithSupplierName.getSupplierName()
        );
    }

    @Override
    public InventoryDTO getInventoryById(String inventoryId) throws SQLException, ClassNotFoundException {
        Inventory inventory = inventoryDAO.getInventoryById(inventoryId);
        return new InventoryDTO(
                inventory.getInventoryId(),
                inventory.getType(),
                inventory.getQuantity(),
                inventory.getUnitPrice(),
                inventory.getSupplierId()
        );
    }

    @Override
    public List<InventoryWithSupplierNameDTO> getAllInventories() throws SQLException, ClassNotFoundException {
        List<InventoryWithSupplierName> all = queryDAO.getAllInventory();
        return all.stream().map(inventoryWithSupplierName -> new InventoryWithSupplierNameDTO(
                inventoryWithSupplierName.getInventoryId(),
                inventoryWithSupplierName.getType(),
                inventoryWithSupplierName.getQuantity(),
                inventoryWithSupplierName.getUnitPrice(),
                inventoryWithSupplierName.getSupplierId(),
                inventoryWithSupplierName.getSupplierName()
        )).collect(Collectors.toList());
    }

    @Override
    public List<InventoryDTO> getAvailableInventoryItems() throws SQLException, ClassNotFoundException {
        List<Inventory> inventories = inventoryDAO.getAvailableItems();
        return inventories.stream().map(inventory -> new InventoryDTO(
                inventory.getInventoryId(),
                inventory.getType(),
                inventory.getQuantity(),
                inventory.getUnitPrice(),
                inventory.getSupplierId()
        )).collect(Collectors.toList());
    }
}