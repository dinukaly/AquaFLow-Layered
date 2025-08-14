package lk.wms.aquaflow.view.tm;

import java.time.LocalDate;

public class InventoryEntry {
    private String type;
    private int quantity;
    private double unitPrice;
    private double totalCost;
    private LocalDate dateUsed;

    public InventoryEntry(String type, int quantity, double unitPrice, LocalDate dateUsed) {
        this.type = type;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.dateUsed = dateUsed;
        this.totalCost = quantity * unitPrice;
    }

    // Getters and Setters
    public String getType() {
        return type;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public LocalDate getDateUsed() {
        return dateUsed;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        this.totalCost = quantity * unitPrice;
    }
}
