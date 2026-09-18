package com.github.jjbcoding.trellis.exceptions;

/**
 * Thrown if a method on {@link com.github.jjbcoding.trellis.service.AppService}
 * is called that requires it to have active Nodes.
 * Methods on Nodes - i.e. inject - assume that the Node being used is connected to the Node tree,
 * and generally will not throw this error if that is not the case.
 */
public class ServiceNotRunningException extends RuntimeException {
    /**
     * Constructs a ServiceNotConfiguredException instance.
     */
    public ServiceNotRunningException() {
        super();
    }
}
