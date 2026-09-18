package com.github.jjbcoding.trellis.exceptions;

/**
 * A ResolutionTermination is thrown at the point where a setState, inject, etc.
 * resolution fails, where it is subsequently wrapped in a ResolutionFailure exception.
 * It is never exposed externally. It may wrap CloseException or another exception type.
 */
public class ResolutionTermination extends RuntimeException {
    /**
     * Constructs a ResolutionTermination instance.
     * @param message   The string message
     */
    public ResolutionTermination(String message) {
        super(message);
    }

    /**
     * Constructs a ResolutionTermination instance.
     * @param message   The string message
     * @param e         The CloseException that is being caught
     */
    public ResolutionTermination(String message, String implementationMessage, Exception e) {
        super(message, new ImplementerException(implementationMessage, e));
    }

    /**
     * Constructs a ResolutionTermination instance.
     * @param message   The string message
     * @param e         The exception that is being caught
     */
    public ResolutionTermination(String message, Exception e) {
        super(message, e);
    }
}
