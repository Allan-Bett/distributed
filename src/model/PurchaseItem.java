package model;

import java.io.Serializable;

/**
 * Model class representing an individual purchase item
 * Contains fruit details, quantity, and calculated total price
 */
public class PurchaseItem implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String fruitName;
    private double quantity;
    private double pricePerUnit;
    private double totalPrice;
    
    public PurchaseItem() {}
    
    public PurchaseItem(String fruitName, double quantity, double pricePerUnit) {
        this.fruitName = fruitName;
        this.quantity = quantity;
        this.pricePerUnit = pricePerUnit;
        this.totalPrice = quantity * pricePerUnit;
    }
    
    // Getters and setters
    public String getFruitName() {
        return fruitName;
    }
    
    public void setFruitName(String fruitName) {
        this.fruitName = fruitName;
    }
    
    public double getQuantity() {
        return quantity;
    }
    
    public void setQuantity(double quantity) {
        this.quantity = quantity;
        this.totalPrice = quantity * this.pricePerUnit;
    }
    
    public double getPricePerUnit() {
        return pricePerUnit;
    }
    
    public void setPricePerUnit(double pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
        this.totalPrice = this.quantity * pricePerUnit;
    }
    
    public double getTotalPrice() {
        return totalPrice;
    }
    
    @Override
    public String toString() {
        return String.format("PurchaseItem{fruitName='%s', quantity=%.2f, pricePerUnit=%.2f, totalPrice=%.2f}", 
            fruitName, quantity, pricePerUnit, totalPrice);
    }
} 