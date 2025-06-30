package engine;

import interfaces.Compute;
import interfaces.Task;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;

/**
 * FruitComputeEngine - Remote compute engine for processing fruit service tasks
 * Extends UnicastRemoteObject and implements the Compute interface
 * Creates an object in the RMI Registry and executes client tasks
 */
public class FruitComputeEngine extends UnicastRemoteObject implements Compute {
    private static final long serialVersionUID = 1L;

    /**
     * Constructor for FruitComputeEngine
     * @throws RemoteException if RMI initialization fails
     */
    public FruitComputeEngine() throws RemoteException {
        super();
    }

    /**
     * Executes a task on the compute engine
     * This method implements the core functionality to execute different types of client tasks
     * @param t The task to be executed
     * @return The result of the task execution
     * @throws RemoteException if RMI communication fails
     */
    @Override
    public <T> T executeTask(Task<T> t) throws RemoteException {
        System.out.println("Executing task: " + t.getClass().getSimpleName());
        try {
            T result = t.execute();
            System.out.println("Task completed successfully");
            return result;
        } catch (Exception e) {
            System.err.println("Error executing task: " + e.getMessage());
            throw new RemoteException("Task execution failed", e);
        }
    }

    /**
     * Main method to start the FruitComputeEngine server
     * Creates and binds the compute engine to the RMI registry
     */
    public static void main(String[] args) {
        try {
            // Create and export the compute engine
            FruitComputeEngine engine = new FruitComputeEngine();
            
            // Create or locate the RMI registry on port 1099 (default port)
            Registry registry;
            try {
                registry = LocateRegistry.createRegistry(1099);
                System.out.println("Created RMI registry on port 1099");
            } catch (RemoteException e) {
                // Registry might already exist, try to locate it
                registry = LocateRegistry.getRegistry(1099);
                System.out.println("Located existing RMI registry on port 1099");
            }
            
            // Bind the compute engine to the registry
            registry.rebind("FruitComputeEngine", engine);
            System.out.println("FruitComputeEngine server started successfully!");
            System.out.println("Server is ready to accept client requests...");
            System.out.println("Registry URL: rmi://localhost:1099/FruitComputeEngine");
            
            // Keep the server running
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("\nShutting down FruitComputeEngine server...");
            }));
            
        } catch (Exception e) {
            System.err.println("Failed to start FruitComputeEngine server: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
} 