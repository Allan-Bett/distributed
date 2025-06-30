package client;

import client.FruitComputeTaskRegistry;
import tasks.*;
import model.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

/**
 * FruitServiceClient - Main client program for the Fruit Service Engine
 * Demonstrates all five client tasks through RMI communication
 */
public class FruitServiceClient {
    private FruitComputeTaskRegistry taskRegistry;
    private Scanner scanner;
    private String currentCashier;

    public FruitServiceClient() {
        this.taskRegistry = new FruitComputeTaskRegistry();
        this.scanner = new Scanner(System.in);
        this.currentCashier = "DefaultCashier";
    }

    /**
     * Main method to run the client application
     */
    public static void main(String[] args) {
        FruitServiceClient client = new FruitServiceClient();
        client.run();
    }

    /**
     * Main client loop
     */
    public void run() {
        System.out.println("=== FRUIT SERVICE ENGINE CLIENT ===");
        System.out.println("Connecting to RMI server...");

        // Connect to the compute engine
        if (!taskRegistry.connectToComputeEngine()) {
            System.err.println("Failed to connect to compute engine. Make sure the server is running.");
            return;
        }

        // Login cashier
        loginCashier();

        // Main menu loop
        boolean running = true;
        while (running) {
            displayMenu();
            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    addFruitPrice();
                    break;
                case 2:
                    updateFruitPrice();
                    break;
                case 3:
                    deleteFruitPrice();
                    break;
                case 4:
                    calculateFruitCost();
                    break;
                case 5:
                    generateReceipt();
                    break;
                case 6:
                    runAllTasksDemo();
                    break;
                case 7:
                    changeCashier();
                    break;
                case 0:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
            
            if (running) {
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();
            }
        }

        System.out.println("Disconnecting from server...");
        taskRegistry.disconnect();
        System.out.println("Thank you for using Fruit Service Engine!");
    }

    /**
     * Display the main menu
     */
    private void displayMenu() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("           FRUIT SERVICE MENU");
        System.out.println("           Cashier: " + currentCashier);
        System.out.println("=".repeat(50));
        System.out.println("1. Add New Fruit Price");
        System.out.println("2. Update Fruit Price");
        System.out.println("3. Delete Fruit Price");
        System.out.println("4. Calculate Fruit Cost");
        System.out.println("5. Generate Receipt");
        System.out.println("6. Run All Tasks Demo");
        System.out.println("7. Change Cashier");
        System.out.println("0. Exit");
        System.out.println("=".repeat(50));
    }

    /**
     * Task 1: Add a new fruit-price entity
     */
    private void addFruitPrice() {
        System.out.println("\n--- ADD NEW FRUIT PRICE ---");
        String fruitName = getStringInput("Enter fruit name: ");
        double price = getDoubleInput("Enter price per unit: $");

        try {
            AddFruitPrice task = new AddFruitPrice(fruitName, price);
            String result = taskRegistry.executeTask(task);
            System.out.println(result);
        } catch (Exception e) {
            System.err.println("Error executing add task: " + e.getMessage());
        }
    }

    /**
     * Task 2: Update a fruit-price entity
     */
    private void updateFruitPrice() {
        System.out.println("\n--- UPDATE FRUIT PRICE ---");
        String fruitName = getStringInput("Enter fruit name to update: ");
        double newPrice = getDoubleInput("Enter new price per unit: $");

        try {
            UpdateFruitPrice task = new UpdateFruitPrice(fruitName, newPrice);
            String result = taskRegistry.executeTask(task);
            System.out.println(result);
        } catch (Exception e) {
            System.err.println("Error executing update task: " + e.getMessage());
        }
    }

    /**
     * Task 3: Delete a fruit-price entity
     */
    private void deleteFruitPrice() {
        System.out.println("\n--- DELETE FRUIT PRICE ---");
        String fruitName = getStringInput("Enter fruit name to delete: ");

        try {
            DeleteFruitPrice task = new DeleteFruitPrice(fruitName);
            String result = taskRegistry.executeTask(task);
            System.out.println(result);
        } catch (Exception e) {
            System.err.println("Error executing delete task: " + e.getMessage());
        }
    }

    /**
     * Task 4: Calculate fruit cost
     */
    private void calculateFruitCost() {
        System.out.println("\n--- CALCULATE FRUIT COST ---");
        String fruitName = getStringInput("Enter fruit name: ");
        double quantity = getDoubleInput("Enter quantity: ");

        try {
            CalFruitCost task = new CalFruitCost(fruitName, quantity);
            PurchaseItem result = taskRegistry.executeTask(task);
            
            if (result != null) {
                System.out.println("Cost Calculation Result:");
                System.out.println(result);
            } else {
                System.out.println("Failed to calculate cost. Check if fruit exists.");
            }
        } catch (Exception e) {
            System.err.println("Error executing cost calculation task: " + e.getMessage());
        }
    }

    /**
     * Task 5: Generate receipt
     */
    private void generateReceipt() {
        System.out.println("\n--- GENERATE RECEIPT ---");
        List<PurchaseItem> items = new ArrayList<>();
        
        // Collect purchase items
        boolean addingItems = true;
        while (addingItems) {
            String fruitName = getStringInput("Enter fruit name (or 'done' to finish): ");
            if ("done".equalsIgnoreCase(fruitName)) {
                addingItems = false;
                continue;
            }
            
            double quantity = getDoubleInput("Enter quantity for " + fruitName + ": ");
            
            try {
                // Calculate cost for this item
                CalFruitCost costTask = new CalFruitCost(fruitName, quantity);
                PurchaseItem item = taskRegistry.executeTask(costTask);
                
                if (item != null) {
                    items.add(item);
                    System.out.println("Added: " + item);
                } else {
                    System.out.println("Failed to add " + fruitName + ". Fruit may not exist.");
                }
            } catch (Exception e) {
                System.err.println("Error calculating cost for " + fruitName + ": " + e.getMessage());
            }
        }

        if (items.isEmpty()) {
            System.out.println("No items to process.");
            return;
        }

        // Calculate total and get payment
        double total = items.stream().mapToDouble(PurchaseItem::getTotalPrice).sum();
        System.out.printf("Total cost: $%.2f\n", total);
        
        double amountPaid = getDoubleInput("Enter amount paid: $");

        try {
            CalculateCost receiptTask = new CalculateCost(items, amountPaid, currentCashier);
            Receipt receipt = taskRegistry.executeTask(receiptTask);
            
            if (receipt != null) {
                System.out.println("\n" + receipt.toString());
            } else {
                System.out.println("Failed to generate receipt.");
            }
        } catch (Exception e) {
            System.err.println("Error generating receipt: " + e.getMessage());
        }
    }

    /**
     * Demonstrate all tasks with sample data
     */
    private void runAllTasksDemo() {
        System.out.println("\n--- RUNNING ALL TASKS DEMO ---");
        
        try {
            // Task 1: Add fruit
            System.out.println("\n1. Adding new fruit 'Pineapple'...");
            AddFruitPrice addTask = new AddFruitPrice("Pineapple", 6.50);
            String addResult = taskRegistry.executeTask(addTask);
            System.out.println("Result: " + addResult);

            // Task 2: Update fruit
            System.out.println("\n2. Updating 'Apple' price...");
            UpdateFruitPrice updateTask = new UpdateFruitPrice("Apple", 2.75);
            String updateResult = taskRegistry.executeTask(updateTask);
            System.out.println("Result: " + updateResult);

            // Task 4: Calculate cost
            System.out.println("\n3. Calculating cost for 3 apples...");
            CalFruitCost costTask = new CalFruitCost("Apple", 3.0);
            PurchaseItem costResult = taskRegistry.executeTask(costTask);
            if (costResult != null) {
                System.out.println("Cost Result: " + costResult);
            }

            // Task 5: Generate receipt
            System.out.println("\n4. Generating sample receipt...");
            List<PurchaseItem> sampleItems = new ArrayList<>();
            if (costResult != null) {
                sampleItems.add(costResult);
                sampleItems.add(new PurchaseItem("Banana", 2.0, 1.80));
            }
            
            if (!sampleItems.isEmpty()) {
                CalculateCost receiptTask = new CalculateCost(sampleItems, 20.00, currentCashier);
                Receipt receipt = taskRegistry.executeTask(receiptTask);
                if (receipt != null) {
                    System.out.println("\nSample Receipt:");
                    System.out.println(receipt.toString());
                }
            }

            // Task 3: Delete fruit (done last to preserve demo data)
            System.out.println("\n5. Deleting 'Pineapple'...");
            DeleteFruitPrice deleteTask = new DeleteFruitPrice("Pineapple");
            String deleteResult = taskRegistry.executeTask(deleteTask);
            System.out.println("Result: " + deleteResult);

        } catch (Exception e) {
            System.err.println("Error during demo: " + e.getMessage());
        }
    }

    /**
     * Login/change cashier
     */
    private void loginCashier() {
        currentCashier = getStringInput("Enter cashier name: ");
        System.out.println("Logged in as: " + currentCashier);
    }

    /**
     * Change current cashier
     */
    private void changeCashier() {
        System.out.println("\n--- CHANGE CASHIER ---");
        loginCashier();
    }

    /**
     * Get string input from user
     */
    private String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    /**
     * Get integer input from user
     */
    private int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                int value = Integer.parseInt(scanner.nextLine().trim());
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    /**
     * Get double input from user
     */
    private double getDoubleInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                double value = Double.parseDouble(scanner.nextLine().trim());
                if (value >= 0) {
                    return value;
                } else {
                    System.out.println("Please enter a non-negative number.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
} 