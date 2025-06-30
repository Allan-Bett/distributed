package tasks;

import interfaces.Task;
import model.FruitPrice;
import data.FruitPriceDatabase;

/**
 * UpdateFruitPrice - Task implementation for updating existing fruit-price entities
 * Implements the Task interface and the execute() method to update a fruit-price in the database
 */
public class UpdateFruitPrice implements Task<String> {
    private static final long serialVersionUID = 1L;
    
    private final FruitPrice fruitPrice;

    /**
     * Constructor for UpdateFruitPrice task
     * @param fruitPrice The fruit price entity to update
     */
    public UpdateFruitPrice(FruitPrice fruitPrice) {
        this.fruitPrice = fruitPrice;
    }

    /**
     * Constructor for UpdateFruitPrice task with separate parameters
     * @param fruitName The name of the fruit to update
     * @param newPricePerUnit The new price per unit for the fruit
     */
    public UpdateFruitPrice(String fruitName, double newPricePerUnit) {
        this.fruitPrice = new FruitPrice(fruitName, newPricePerUnit);
    }

    /**
     * Execute the update fruit price task
     * Updates an existing fruit-price entity in the fruit-price table
     * @return Success or failure message
     */
    @Override
    public String execute() {
        try {
            // Validate input
            if (fruitPrice == null) {
                return "ERROR: Fruit price object is null";
            }
            
            if (fruitPrice.getFruitName() == null || fruitPrice.getFruitName().trim().isEmpty()) {
                return "ERROR: Fruit name cannot be empty";
            }
            
            if (fruitPrice.getPricePerUnit() <= 0) {
                return "ERROR: Price per unit must be greater than zero";
            }

            // Get database instance and update fruit price
            FruitPriceDatabase database = FruitPriceDatabase.getInstance();
            
            // Check if fruit exists first
            FruitPrice existingFruit = database.getFruitPrice(fruitPrice.getFruitName());
            if (existingFruit == null) {
                String message = String.format("ERROR: Fruit '%s' does not exist in the database", 
                    fruitPrice.getFruitName());
                System.out.println(message);
                return message;
            }
            
            // Store old price for logging
            double oldPrice = existingFruit.getPricePerUnit();
            
            // Update the fruit price
            boolean success = database.updateFruitPrice(fruitPrice);
            
            if (success) {
                String message = String.format("SUCCESS: Updated fruit '%s' price from $%.2f to $%.2f per unit", 
                    fruitPrice.getFruitName(), oldPrice, fruitPrice.getPricePerUnit());
                System.out.println(message);
                return message;
            } else {
                String message = String.format("ERROR: Failed to update fruit '%s'", 
                    fruitPrice.getFruitName());
                System.out.println(message);
                return message;
            }
            
        } catch (Exception e) {
            String errorMessage = "ERROR: Failed to update fruit price - " + e.getMessage();
            System.err.println(errorMessage);
            return errorMessage;
        }
    }

    /**
     * Get the fruit price being updated
     * @return The fruit price entity
     */
    public FruitPrice getFruitPrice() {
        return fruitPrice;
    }

    @Override
    public String toString() {
        return String.format("UpdateFruitPrice{fruitName='%s', newPricePerUnit=%.2f}", 
            fruitPrice != null ? fruitPrice.getFruitName() : "null",
            fruitPrice != null ? fruitPrice.getPricePerUnit() : 0.0);
    }
} 