package com.github.jjbcoding.trellis.exceptions;

/**
 * An ImplementerException wraps any checked or unchecked exception when a
 * user-implemented constructor or method is called into, including {@link AutoCloseable#close()}
 * on implementing Nodes and Injectables.
 */
public class ImplementerException extends RuntimeException {
    /**
     * Constructs an ImplementationException instance.
     * @param message   The string message
     * @param e         The arbitrary exception
     */
    public ImplementerException(String message, Exception e) {
        super(message, e);
    }
}
