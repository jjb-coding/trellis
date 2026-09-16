package com.github.jjbcoding.trellis.exceptions;

/**
 * A ResolutionFailure is thrown when setState, inject, etc. resolutions
 * fail. This can be due to a variety of reasons.
 */
public class ResolutionFailure extends RuntimeException {
    /**
     * Constructs a ResolutionFailure instance.
     * @param message   The string message
     */
    public ResolutionFailure(String message) {
        super(message);
    }

    /**
     * Constructs a ResolutionFailure instance.
     * @param message   The string message
     * @param origin    The ResolutionTermination that is being caught
     */
    public ResolutionFailure(String message, ResolutionTermination origin) {
        super(message, origin);
    }
}
