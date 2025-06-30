package tasks;

import interfaces.Task;
import model.FruitPrice;
import model.PurchaseItem;
import data.FruitPriceDatabase;

/**
 * CalFruitCost - Task implementation for calculating fruit cost
 * Implements the Task interface and the execute() method to query fruit price and calculate cost
 */
public class CalFruitCost implements Task<PurchaseItem> {
    private static final long serialVersionUID = 1L;
    
    private final String fruitName;
    private final double quantity;

    /**
     * Constructor for CalFruitCost task
     * @param fruitName The name of the fruit
     * @param quantity The quantity of fruit to calculate cost for
     */
    public CalFruitCost(String fruitName, double quantity) {
        this.fruitName = fruitName;
        this.quantity = quantity;
    }

    /**
     * Execute the calculate fruit cost task
     * Queries the fruit price and calculates the total cost based on quantity
     * @return PurchaseItem containing the calculated cost details, or null if fruit not found
     */
    @Override
    public PurchaseItem execute() {
        try {
            // Validate input
            if (fruitName == null || fruitName.trim().isEmpty()) {
                System.err.println("ERROR: Fruit name cannot be empty");
                return null;
            }
            
            if (quantity <= 0) {
                System.err.println("ERROR: Quantity must be greater than zero");
                return null;
            }

            // Get database instance and query fruit price
            FruitPriceDatabase database = FruitPriceDatabase.getInstance();
            FruitPrice fruitPrice = database.getFruitPrice(fruitName);
            
            if (fruitPrice == null) {
                String message = String.format("ERROR: Fruit '%s' not found in the database", fruitName);
                System.out.println(message);
                return null;
            }
            
            // Create purchase item with calculated cost
            PurchaseItem purchaseItem = new PurchaseItem(
                fruitPrice.getFruitName(),
                quantity,
                fruitPrice.getPricePerUnit()
            );
            
            String message = String.format("SUCCESS: Calculated cost for %.2f units of '%s' at $%.2f per unit = $%.2f", 
                quantity, fruitPrice.getFruitName(), fruitPrice.getPricePerUnit(), purchaseItem.getTotalPrice());
            System.out.println(message);
            
            return purchaseItem;
            
        } catch (Exception e) {
            String errorMessage = "ERROR: Failed to calculate fruit cost - " + e.getMessage();
            System.err.println(errorMessage);
            return null;
        }
    }

    /**
     * Get the fruit name for cost calculation
     * @return The fruit name
     */
    public String getFruitName() {
        return fruitName;
    }

    /**
     * Get the quantity for cost calculation
     * @return The quantity
     */
    public double getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return String.format("CalFruitCost{fruitName='%s', quantity=%.2f}", fruitName, quantity);
    }
} 