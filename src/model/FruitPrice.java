package model;

import java.io.Serializable;

/**
 * Model class representing a fruit-price entity
 * Contains fruit name and its price per unit
 */
public class FruitPrice implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String fruitName;
    private double pricePerUnit;
    
    public FruitPrice() {}
    
    public FruitPrice(String fruitName, double pricePerUnit) {
        this.fruitName = fruitName;
        this.pricePerUnit = pricePerUnit;
    }
    
    // Getters and setters
    public String getFruitName() {
        return fruitName;
    }
    
    public void setFruitName(String fruitName) {
        this.fruitName = fruitName;
    }
    
    public double getPricePerUnit() {
        return pricePerUnit;
    }
    
    public void setPricePerUnit(double pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }
    
    @Override
    public String toString() {
        return "FruitPrice{fruitName='" + fruitName + "', pricePerUnit=" + pricePerUnit + "}";
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        FruitPrice that = (FruitPrice) obj;
        return fruitName != null ? fruitName.equals(that.fruitName) : that.fruitName == null;
    }
    
    @Override
    public int hashCode() {
        return fruitName != null ? fruitName.hashCode() : 0;
    }
} 