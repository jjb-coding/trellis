package com.github.jjbcoding.trellis.exceptions;

/**
 * An ImplementationException provides an idiomatic exception for use by user-implemented constructors and
 * methods that are invoked by the service: i.e. Node or Injectable constructors, {@link com.github.jjbcoding.trellis.service.Injectable#initialise()},
 * {@link com.github.jjbcoding.trellis.service.disposal.processor.IDisposalProcessor#consume(com.github.jjbcoding.trellis.service.disposal.object.DisposalObject)},
 * etc. In practice, such constructors and methods can throw any class of exception.
 */
public class ImplementationException extends RuntimeException {
    /**
     * Constructs a ImplementationException instance.
     * @param message   The string message
     */
    @SuppressWarnings("unused")
    public ImplementationException(String message) {
        super(message);
    }

    /**
     * Constructs a ImplementationException instance.
     * @param message   The string message
     * @param e         The exception that is being caught
     */
    @SuppressWarnings("unused")
    public ImplementationException(String message, Exception e) {
        super(message, e);
    }
}
