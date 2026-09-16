package com.github.jjbcoding.trellis.exceptions;

/**
 * An OperationException is thrown from invalid post-configuration calls:
 * when an invalid constant is provided, an invalid argument, or a start
 * method is called on {@link com.github.jjbcoding.trellis.service.AppService}
 * without the necessary configurations.
 */
public class OperationException extends RuntimeException {
    /**
     * Constructs a OperationException instance.
     * @param message   The string message
     */
    public OperationException(String message) {
        super(message);
    }
}