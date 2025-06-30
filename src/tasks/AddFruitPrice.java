package tasks;

import interfaces.Task;
import model.FruitPrice;
import data.FruitPriceDatabase;

/**
 * AddFruitPrice - Task implementation for adding new fruit-price entities
 * Implements the Task interface and the execute() method to add a fruit-price to the database
 */
public class AddFruitPrice implements Task<String> {
    private static final long serialVersionUID = 1L;
    
    private final FruitPrice fruitPrice;

    /**
     * Constructor for AddFruitPrice task
     * @param fruitPrice The fruit price entity to add
     */
    public AddFruitPrice(FruitPrice fruitPrice) {
        this.fruitPrice = fruitPrice;
    }

    /**
     * Constructor for AddFruitPrice task with separate parameters
     * @param fruitName The name of the fruit
     * @param pricePerUnit The price per unit of the fruit
     */
    public AddFruitPrice(String fruitName, double pricePerUnit) {
        this.fruitPrice = new FruitPrice(fruitName, pricePerUnit);
    }

    /**
     * Execute the add fruit price task
     * Adds a new fruit-price entity to the fruit-price table
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

            // Get database instance and add fruit price
            FruitPriceDatabase database = FruitPriceDatabase.getInstance();
            boolean success = database.addFruitPrice(fruitPrice);
            
            if (success) {
                String message = String.format("SUCCESS: Added fruit '%s' with price $%.2f per unit", 
                    fruitPrice.getFruitName(), fruitPrice.getPricePerUnit());
                System.out.println(message);
                return message;
            } else {
                String message = String.format("ERROR: Fruit '%s' already exists in the database", 
                    fruitPrice.getFruitName());
                System.out.println(message);
                return message;
            }
            
        } catch (Exception e) {
            String errorMessage = "ERROR: Failed to add fruit price - " + e.getMessage();
            System.err.println(errorMessage);
            return errorMessage;
        }
    }

    /**
     * Get the fruit price being added
     * @return The fruit price entity
     */
    public FruitPrice getFruitPrice() {
        return fruitPrice;
    }

    @Override
    public String toString() {
        return String.format("AddFruitPrice{fruitName='%s', pricePerUnit=%.2f}", 
            fruitPrice != null ? fruitPrice.getFruitName() : "null",
            fruitPrice != null ? fruitPrice.getPricePerUnit() : 0.0);
    }
} 