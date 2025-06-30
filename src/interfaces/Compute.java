package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Remote interface for the Fruit Compute Engine
 * Defines the contract for executing tasks on the remote server
 */
public interface Compute extends Remote {
    /**
     * Executes a task on the remote compute engine
     * @param t The task to be executed
     * @return The result of the task execution
     * @throws RemoteException if RMI communication fails
     */
    <T> T executeTask(Task<T> t) throws RemoteException;
} 