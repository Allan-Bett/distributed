package servlets;

import client.FruitComputeTaskRegistry;
import tasks.*;
import model.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * FruitServiceServlet - Java servlet that communicates with RMI classes
 * Handles HTTP requests for fruit service operations and forwards them to the compute engine
 */
@WebServlet("/fruitservice/*")
public class FruitServiceServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private FruitComputeTaskRegistry taskRegistry;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        super.init();
        this.gson = new Gson();
        this.taskRegistry = new FruitComputeTaskRegistry();
        
        // Connect to the RMI server
        if (!taskRegistry.connectToComputeEngine()) {
            throw new ServletException("Failed to connect to RMI compute engine");
        }
        
        System.out.println("FruitServiceServlet initialized and connected to RMI server");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String pathInfo = request.getPathInfo();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                // Return API information
                JsonObject apiInfo = new JsonObject();
                apiInfo.addProperty("service", "Fruit Service Engine");
                apiInfo.addProperty("version", "1.0");
                apiInfo.addProperty("status", "running");
                apiInfo.addProperty("description", "RMI-based fruit price management system");
                out.print(gson.toJson(apiInfo));
                
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                JsonObject error = new JsonObject();
                error.addProperty("error", "Endpoint not found");
                error.addProperty("message", "Use POST requests for fruit operations");
                out.print(gson.toJson(error));
            }
            
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            JsonObject error = new JsonObject();
            error.addProperty("error", "Internal server error");
            error.addProperty("message", e.getMessage());
            out.print(gson.toJson(error));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String pathInfo = request.getPathInfo();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        try {
            // Read request body
            StringBuilder requestBody = new StringBuilder();
            String line;
            while ((line = request.getReader().readLine()) != null) {
                requestBody.append(line);
            }

            JsonObject requestJson = JsonParser.parseString(requestBody.toString()).getAsJsonObject();
            JsonObject responseJson = new JsonObject();

            switch (pathInfo) {
                case "/add":
                    responseJson = handleAddFruit(requestJson);
                    break;
                case "/update":
                    responseJson = handleUpdateFruit(requestJson);
                    break;
                case "/delete":
                    responseJson = handleDeleteFruit(requestJson);
                    break;
                case "/calculate":
                    responseJson = handleCalculateCost(requestJson);
                    break;
                case "/receipt":
                    responseJson = handleGenerateReceipt(requestJson);
                    break;
                default:
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    responseJson.addProperty("error", "Unknown operation");
                    responseJson.addProperty("message", "Valid operations: /add, /update, /delete, /calculate, /receipt");
            }

            out.print(gson.toJson(responseJson));

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            JsonObject error = new JsonObject();
            error.addProperty("error", "Request processing failed");
            error.addProperty("message", e.getMessage());
            out.print(gson.toJson(error));
        }
    }

    /**
     * Handle add fruit operation
     */
    private JsonObject handleAddFruit(JsonObject request) {
        JsonObject response = new JsonObject();
        
        try {
            String fruitName = request.get("fruitName").getAsString();
            double price = request.get("price").getAsDouble();

            AddFruitPrice task = new AddFruitPrice(fruitName, price);
            String result = taskRegistry.executeTask(task);

            response.addProperty("success", result.startsWith("SUCCESS"));
            response.addProperty("message", result);
            response.addProperty("operation", "add");
            response.addProperty("fruitName", fruitName);
            response.addProperty("price", price);

        } catch (Exception e) {
            response.addProperty("success", false);
            response.addProperty("error", "Add operation failed");
            response.addProperty("message", e.getMessage());
        }

        return response;
    }

    /**
     * Handle update fruit operation
     */
    private JsonObject handleUpdateFruit(JsonObject request) {
        JsonObject response = new JsonObject();
        
        try {
            String fruitName = request.get("fruitName").getAsString();
            double newPrice = request.get("price").getAsDouble();

            UpdateFruitPrice task = new UpdateFruitPrice(fruitName, newPrice);
            String result = taskRegistry.executeTask(task);

            response.addProperty("success", result.startsWith("SUCCESS"));
            response.addProperty("message", result);
            response.addProperty("operation", "update");
            response.addProperty("fruitName", fruitName);
            response.addProperty("newPrice", newPrice);

        } catch (Exception e) {
            response.addProperty("success", false);
            response.addProperty("error", "Update operation failed");
            response.addProperty("message", e.getMessage());
        }

        return response;
    }

    /**
     * Handle delete fruit operation
     */
    private JsonObject handleDeleteFruit(JsonObject request) {
        JsonObject response = new JsonObject();
        
        try {
            String fruitName = request.get("fruitName").getAsString();

            DeleteFruitPrice task = new DeleteFruitPrice(fruitName);
            String result = taskRegistry.executeTask(task);

            response.addProperty("success", result.startsWith("SUCCESS"));
            response.addProperty("message", result);
            response.addProperty("operation", "delete");
            response.addProperty("fruitName", fruitName);

        } catch (Exception e) {
            response.addProperty("success", false);
            response.addProperty("error", "Delete operation failed");
            response.addProperty("message", e.getMessage());
        }

        return response;
    }

    /**
     * Handle calculate cost operation
     */
    private JsonObject handleCalculateCost(JsonObject request) {
        JsonObject response = new JsonObject();
        
        try {
            String fruitName = request.get("fruitName").getAsString();
            double quantity = request.get("quantity").getAsDouble();

            CalFruitCost task = new CalFruitCost(fruitName, quantity);
            PurchaseItem result = taskRegistry.executeTask(task);

            if (result != null) {
                response.addProperty("success", true);
                response.addProperty("message", "Cost calculated successfully");
                response.addProperty("operation", "calculate");
                response.addProperty("fruitName", result.getFruitName());
                response.addProperty("quantity", result.getQuantity());
                response.addProperty("pricePerUnit", result.getPricePerUnit());
                response.addProperty("totalCost", result.getTotalPrice());
            } else {
                response.addProperty("success", false);
                response.addProperty("message", "Cost calculation failed - fruit not found");
            }

        } catch (Exception e) {
            response.addProperty("success", false);
            response.addProperty("error", "Calculate operation failed");
            response.addProperty("message", e.getMessage());
        }

        return response;
    }

    /**
     * Handle generate receipt operation
     */
    private JsonObject handleGenerateReceipt(JsonObject request) {
        JsonObject response = new JsonObject();
        
        try {
            double amountPaid = request.get("amountPaid").getAsDouble();
            String cashier = request.get("cashier").getAsString();
            
            // Parse items array
            List<PurchaseItem> items = new ArrayList<>();
            if (request.has("items") && request.get("items").isJsonArray()) {
                request.getAsJsonArray("items").forEach(element -> {
                    JsonObject itemJson = element.getAsJsonObject();
                    String fruitName = itemJson.get("fruitName").getAsString();
                    double quantity = itemJson.get("quantity").getAsDouble();
                    
                    try {
                        CalFruitCost costTask = new CalFruitCost(fruitName, quantity);
                        PurchaseItem item = taskRegistry.executeTask(costTask);
                        if (item != null) {
                            items.add(item);
                        }
                    } catch (Exception e) {
                        System.err.println("Error calculating cost for " + fruitName + ": " + e.getMessage());
                    }
                });
            }

            if (!items.isEmpty()) {
                CalculateCost receiptTask = new CalculateCost(items, amountPaid, cashier);
                Receipt receipt = taskRegistry.executeTask(receiptTask);

                if (receipt != null) {
                    response.addProperty("success", true);
                    response.addProperty("message", "Receipt generated successfully");
                    response.addProperty("operation", "receipt");
                    response.addProperty("receiptText", receipt.toString());
                    response.addProperty("totalCost", receipt.getTotalCost());
                    response.addProperty("amountPaid", receipt.getAmountPaid());
                    response.addProperty("changeDue", receipt.getChangeDue());
                    response.addProperty("cashier", receipt.getCashierName());
                } else {
                    response.addProperty("success", false);
                    response.addProperty("message", "Receipt generation failed");
                }
            } else {
                response.addProperty("success", false);
                response.addProperty("message", "No valid items provided");
            }

        } catch (Exception e) {
            response.addProperty("success", false);
            response.addProperty("error", "Receipt generation failed");
            response.addProperty("message", e.getMessage());
        }

        return response;
    }

    @Override
    public void destroy() {
        if (taskRegistry != null) {
            taskRegistry.disconnect();
        }
        super.destroy();
        System.out.println("FruitServiceServlet destroyed and disconnected from RMI server");
    }
} 