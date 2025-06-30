package client;

import interfaces.Compute;
import interfaces.Task;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.RemoteException;
import java.rmi.NotBoundException;

/**
 * FruitComputeTaskRegistry - Client-side task registry for the fruit service
 * Looks for the fruit compute engine, creates client tasks, and runs them on the compute engine
 */
public class FruitComputeTaskRegistry {
    private Compute computeEngine;
    private String serverHost;
    private int serverPort;

    /**
     * Constructor with default localhost and port 1099
     */
    public FruitComputeTaskRegistry() {
        this("localhost", 1099);
    }

    /**
     * Constructor with custom host and port
     * @param serverHost The hostname of the RMI server
     * @param serverPort The port of the RMI server
     */
    public FruitComputeTaskRegistry(String serverHost, int serverPort) {
        this.serverHost = serverHost;
        this.serverPort = serverPort;
    }

    /**
     * Connect to the remote compute engine
     * Looks up the FruitComputeEngine in the RMI registry
     * @return true if connection successful, false otherwise
     */
    public boolean connectToComputeEngine() {
        try {
            // Locate the RMI registry
            Registry registry = LocateRegistry.getRegistry(serverHost, serverPort);
            
            // Look up the compute engine
            computeEngine = (Compute) registry.lookup("FruitComputeEngine");
            
            System.out.println("Successfully connected to FruitComputeEngine at " + 
                             serverHost + ":" + serverPort);
            return true;
            
        } catch (RemoteException e) {
            System.err.println("Failed to connect to RMI registry: " + e.getMessage());
            return false;
        } catch (NotBoundException e) {
            System.err.println("FruitComputeEngine not found in registry: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("Unexpected error connecting to compute engine: " + e.getMessage());
            return false;
        }
    }

    /**
     * Execute a task on the remote compute engine
     * @param task The task to execute
     * @return The result of the task execution
     * @throws RemoteException if RMI communication fails
     * @throws IllegalStateException if not connected to compute engine
     */
    public <T> T executeTask(Task<T> task) throws RemoteException {
        if (computeEngine == null) {
            throw new IllegalStateException("Not connected to compute engine. Call connectToComputeEngine() first.");
        }
        
        try {
            System.out.println("Sending task to compute engine: " + task.getClass().getSimpleName());
            T result = computeEngine.executeTask(task);
            System.out.println("Task executed successfully");
            return result;
        } catch (RemoteException e) {
            System.err.println("Failed to execute task: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Check if connected to the compute engine
     * @return true if connected, false otherwise
     */
    public boolean isConnected() {
        return computeEngine != null;
    }

    /**
     * Disconnect from the compute engine
     */
    public void disconnect() {
        computeEngine = null;
        System.out.println("Disconnected from compute engine");
    }

    /**
     * Get the compute engine reference (for advanced usage)
     * @return The compute engine reference
     */
    public Compute getComputeEngine() {
        return computeEngine;
    }
} 