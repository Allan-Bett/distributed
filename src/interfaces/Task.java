package interfaces;

import java.io.Serializable;

/**
 * Interface for all tasks that can be executed on the compute engine
 * Tasks must be serializable to be sent over RMI
 * @param <T> The return type of the task execution
 */
public interface Task<T> extends Serializable {
    /**
     * Executes the task logic
     * @return The result of the task execution
     */
    T execute();
} 