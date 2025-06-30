package model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Model class representing a transaction receipt
 * Contains transaction details, costs, and cashier information
 */
public class Receipt implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private List<PurchaseItem> items;
    private double totalCost;
    private double amountPaid;
    private double changeDue;
    private String cashierName;
    private LocalDateTime transactionTime;
    
    public Receipt() {
        this.transactionTime = LocalDateTime.now();
    }
    
    public Receipt(List<PurchaseItem> items, double totalCost, double amountPaid, String cashierName) {
        this.items = items;
        this.totalCost = totalCost;
        this.amountPaid = amountPaid;
        this.changeDue = amountPaid - totalCost;
        this.cashierName = cashierName;
        this.transactionTime = LocalDateTime.now();
    }
    
    // Getters and setters
    public List<PurchaseItem> getItems() {
        return items;
    }
    
    public void setItems(List<PurchaseItem> items) {
        this.items = items;
    }
    
    public double getTotalCost() {
        return totalCost;
    }
    
    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }
    
    public double getAmountPaid() {
        return amountPaid;
    }
    
    public void setAmountPaid(double amountPaid) {
        this.amountPaid = amountPaid;
        this.changeDue = amountPaid - this.totalCost;
    }
    
    public double getChangeDue() {
        return changeDue;
    }
    
    public String getCashierName() {
        return cashierName;
    }
    
    public void setCashierName(String cashierName) {
        this.cashierName = cashierName;
    }
    
    public LocalDateTime getTransactionTime() {
        return transactionTime;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== FRUIT SERVICE RECEIPT ===\n");
        sb.append("Date: ").append(transactionTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).append("\n");
        sb.append("Cashier: ").append(cashierName).append("\n");
        sb.append("============================\n");
        
        if (items != null) {
            for (PurchaseItem item : items) {
                sb.append(String.format("%-15s %5.2f x $%6.2f = $%8.2f\n", 
                    item.getFruitName(), item.getQuantity(), item.getPricePerUnit(), item.getTotalPrice()));
            }
        }
        
        sb.append("============================\n");
        sb.append(String.format("Total Cost:       $%8.2f\n", totalCost));
        sb.append(String.format("Amount Paid:      $%8.2f\n", amountPaid));
        sb.append(String.format("Change Due:       $%8.2f\n", changeDue));
        sb.append("============================\n");
        sb.append("Thank you for your purchase!");
        
        return sb.toString();
    }
} 