package tasks;

import interfaces.Task;
import model.Receipt;
import model.PurchaseItem;
import java.util.List;

/**
 * CalculateCost - Task implementation for creating transaction receipts
 * Implements the Task interface and the execute() method to create a receipt after purchases
 */
public class CalculateCost implements Task<Receipt> {
    private static final long serialVersionUID = 1L;
    
    private final List<PurchaseItem> purchaseItems;
    private final double amountPaid;
    private final String cashierName;

    /**
     * Constructor for CalculateCost task
     * @param purchaseItems List of items purchased
     * @param amountPaid The amount of money paid by the customer
     * @param cashierName The name of the cashier/clerk handling the transaction
     */
    public CalculateCost(List<PurchaseItem> purchaseItems, double amountPaid, String cashierName) {
        this.purchaseItems = purchaseItems;
        this.amountPaid = amountPaid;
        this.cashierName = cashierName;
    }

    /**
     * Execute the calculate cost task
     * Creates a receipt based on the purchases and logged-in cashier information
     * @return Receipt containing transaction details, or null if invalid input
     */
    @Override
    public Receipt execute() {
        try {
            // Validate input
            if (purchaseItems == null || purchaseItems.isEmpty()) {
                System.err.println("ERROR: No purchase items provided");
                return null;
            }
            
            if (amountPaid < 0) {
                System.err.println("ERROR: Amount paid cannot be negative");
                return null;
            }
            
            if (cashierName == null || cashierName.trim().isEmpty()) {
                System.err.println("ERROR: Cashier name cannot be empty");
                return null;
            }

            // Calculate total cost from all purchase items
            double totalCost = 0.0;
            for (PurchaseItem item : purchaseItems) {
                if (item != null) {
                    totalCost += item.getTotalPrice();
                }
            }
            
            // Validate payment amount
            if (amountPaid < totalCost) {
                String message = String.format("ERROR: Insufficient payment. Total cost: $%.2f, Amount paid: $%.2f", 
                    totalCost, amountPaid);
                System.out.println(message);
                return null;
            }
            
            // Create receipt
            Receipt receipt = new Receipt(purchaseItems, totalCost, amountPaid, cashierName.trim());
            
            System.out.println("SUCCESS: Receipt generated successfully");
            System.out.println("Transaction Summary:");
            System.out.println("- Items: " + purchaseItems.size());
            System.out.println("- Total Cost: $" + String.format("%.2f", totalCost));
            System.out.println("- Amount Paid: $" + String.format("%.2f", amountPaid));
            System.out.println("- Change Due: $" + String.format("%.2f", receipt.getChangeDue()));
            System.out.println("- Cashier: " + cashierName);
            
            return receipt;
            
        } catch (Exception e) {
            String errorMessage = "ERROR: Failed to calculate cost and generate receipt - " + e.getMessage();
            System.err.println(errorMessage);
            return null;
        }
    }

    /**
     * Get the purchase items
     * @return List of purchase items
     */
    public List<PurchaseItem> getPurchaseItems() {
        return purchaseItems;
    }

    /**
     * Get the amount paid
     * @return The amount paid
     */
    public double getAmountPaid() {
        return amountPaid;
    }

    /**
     * Get the cashier name
     * @return The cashier name
     */
    public String getCashierName() {
        return cashierName;
    }

    @Override
    public String toString() {
        return String.format("CalculateCost{items=%d, amountPaid=%.2f, cashier='%s'}", 
            purchaseItems != null ? purchaseItems.size() : 0, amountPaid, cashierName);
    }
} 