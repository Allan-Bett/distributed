package tasks;

import interfaces.Task;
import model.FruitPrice;
import data.FruitPriceDatabase;

/**
 * DeleteFruitPrice - Task implementation for deleting fruit-price entities
 * Implements the Task interface and the execute() method to delete a fruit-price from the database
 */
public class DeleteFruitPrice implements Task<String> {
    private static final long serialVersionUID = 1L;
    
    private final String fruitName;

    /**
     * Constructor for DeleteFruitPrice task
     * @param fruitName The name of the fruit to delete
     */
    public DeleteFruitPrice(String fruitName) {
        this.fruitName = fruitName;
    }

    /**
     * Execute the delete fruit price task
     * Deletes a fruit-price entity from the fruit-price table
     * @return Success or failure message
     */
    @Override
    public String execute() {
        try {
            // Validate input
            if (fruitName == null || fruitName.trim().isEmpty()) {
                return "ERROR: Fruit name cannot be empty";
            }

            // Get database instance
            FruitPriceDatabase database = FruitPriceDatabase.getInstance();
            
            // Check if fruit exists first
            FruitPrice existingFruit = database.getFruitPrice(fruitName);
            if (existingFruit == null) {
                String message = String.format("ERROR: Fruit '%s' does not exist in the database", fruitName);
                System.out.println(message);
                return message;
            }
            
            // Store the price for logging before deletion
            double price = existingFruit.getPricePerUnit();
            
            // Delete the fruit price
            boolean success = database.deleteFruitPrice(fruitName);
            
            if (success) {
                String message = String.format("SUCCESS: Deleted fruit '%s' (was $%.2f per unit) from the database", 
                    fruitName, price);
                System.out.println(message);
                return message;
            } else {
                String message = String.format("ERROR: Failed to delete fruit '%s' from the database", fruitName);
                System.out.println(message);
                return message;
            }
            
        } catch (Exception e) {
            String errorMessage = "ERROR: Failed to delete fruit price - " + e.getMessage();
            System.err.println(errorMessage);
            return errorMessage;
        }
    }

    /**
     * Get the fruit name being deleted
     * @return The fruit name
     */
    public String getFruitName() {
        return fruitName;
    }

    @Override
    public String toString() {
        return String.format("DeleteFruitPrice{fruitName='%s'}", fruitName);
    }
} 