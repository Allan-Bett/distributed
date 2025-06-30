package data;

import model.FruitPrice;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.Collection;

/**
 * Simple in-memory database for storing fruit prices
 * Uses ConcurrentHashMap for thread-safe operations
 */
public class FruitPriceDatabase {
    private static FruitPriceDatabase instance;
    private final Map<String, FruitPrice> fruitPriceTable;
    
    private FruitPriceDatabase() {
        fruitPriceTable = new ConcurrentHashMap<>();
        // Initialize with some sample data
        initializeSampleData();
    }
    
    /**
     * Singleton pattern to ensure single instance of database
     * @return The database instance
     */
    public static synchronized FruitPriceDatabase getInstance() {
        if (instance == null) {
            instance = new FruitPriceDatabase();
        }
        return instance;
    }
    
    /**
     * Initialize the database with some sample fruit prices
     */
    private void initializeSampleData() {
        addFruitPrice(new FruitPrice("Apple", 2.50));
        addFruitPrice(new FruitPrice("Banana", 1.80));
        addFruitPrice(new FruitPrice("Orange", 3.20));
        addFruitPrice(new FruitPrice("Mango", 4.50));
        addFruitPrice(new FruitPrice("Grapes", 5.00));
    }
    
    /**
     * Add a new fruit-price entity to the database
     * @param fruitPrice The fruit price to add
     * @return true if successfully added, false if fruit already exists
     */
    public boolean addFruitPrice(FruitPrice fruitPrice) {
        if (fruitPrice == null || fruitPrice.getFruitName() == null) {
            return false;
        }
        String key = fruitPrice.getFruitName().toLowerCase();
        if (fruitPriceTable.containsKey(key)) {
            return false; // Fruit already exists
        }
        fruitPriceTable.put(key, fruitPrice);
        return true;
    }
    
    /**
     * Update an existing fruit-price entity in the database
     * @param fruitPrice The fruit price to update
     * @return true if successfully updated, false if fruit doesn't exist
     */
    public boolean updateFruitPrice(FruitPrice fruitPrice) {
        if (fruitPrice == null || fruitPrice.getFruitName() == null) {
            return false;
        }
        String key = fruitPrice.getFruitName().toLowerCase();
        if (!fruitPriceTable.containsKey(key)) {
            return false; // Fruit doesn't exist
        }
        fruitPriceTable.put(key, fruitPrice);
        return true;
    }
    
    /**
     * Delete a fruit-price entity from the database
     * @param fruitName The name of the fruit to delete
     * @return true if successfully deleted, false if fruit doesn't exist
     */
    public boolean deleteFruitPrice(String fruitName) {
        if (fruitName == null) {
            return false;
        }
        String key = fruitName.toLowerCase();
        return fruitPriceTable.remove(key) != null;
    }
    
    /**
     * Get the price of a specific fruit
     * @param fruitName The name of the fruit
     * @return The fruit price entity, or null if not found
     */
    public FruitPrice getFruitPrice(String fruitName) {
        if (fruitName == null) {
            return null;
        }
        String key = fruitName.toLowerCase();
        return fruitPriceTable.get(key);
    }
    
    /**
     * Get all fruit prices in the database
     * @return Collection of all fruit prices
     */
    public Collection<FruitPrice> getAllFruitPrices() {
        return fruitPriceTable.values();
    }
    
    /**
     * Check if a fruit exists in the database
     * @param fruitName The name of the fruit
     * @return true if fruit exists, false otherwise
     */
    public boolean containsFruit(String fruitName) {
        if (fruitName == null) {
            return false;
        }
        return fruitPriceTable.containsKey(fruitName.toLowerCase());
    }
    
    /**
     * Get the current size of the database
     * @return Number of fruits in the database
     */
    public int size() {
        return fruitPriceTable.size();
    }
} 