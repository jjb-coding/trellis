package com.github.jjbcoding.trellis.exceptions;

/**
 * Thrown if a method on {@link com.github.jjbcoding.trellis.service.AppService}
 * is called that requires it to have been configured. If a method requires
 * {@link com.github.jjbcoding.trellis.service.AppService} to be configured and running,
 * {@link ServiceNotRunningException} will be thrown instead.
 */
public class ServiceNotConfiguredException extends RuntimeException {
    /**
     * Constructs a ServiceNotConfiguredException instance.
     */
    public ServiceNotConfiguredException() {
        super();
    }
}
