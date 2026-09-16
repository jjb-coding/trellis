package com.github.jjbcoding.trellis.exceptions;

import com.github.jjbcoding.trellis.service.Injectable;
import com.github.jjbcoding.trellis.service.disposal.object.internal.DisposalObjectInternal;

/**
 * An ImplementationException provides an idiomatic exception for use by user-implemented constructors and
 * methods that interact with the service, like Node or Injectable constructors, {@link Injectable#initialise()},
 * {@link com.github.jjbcoding.trellis.service.disposal.processor.IDisposalProcessor#consume(DisposalObjectInternal)},
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
