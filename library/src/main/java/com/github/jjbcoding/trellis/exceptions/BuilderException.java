package com.github.jjbcoding.trellis.exceptions;

import com.github.jjbcoding.trellis.service.AppServiceConfigurationBuilder;

/**
 * A BuilderException is thrown when a method on the {@link com.github.jjbcoding.trellis.service.AppServiceConfigurationBuilder}
 * is called incorrectly, or when {@link com.github.jjbcoding.trellis.service.AppService#configure(AppServiceConfigurationBuilder)}
 * is called with a misconfigured builder.
 */
public class BuilderException extends RuntimeException {
    /**
     * Constructs a BuilderException instance.
     * @param message   The string message
     */
    public BuilderException(String message) {
        super(message);
    }
}