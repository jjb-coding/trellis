package com.github.jjbcoding.trellis.service.providers;

import com.github.jjbcoding.trellis.service.providers.internal.ClassProviderInternal;

/**
 * Interface for class providers.
 * A class provider produces a list of classes on request.
 * @param <T>   The class type or supertype
 */
public abstract class ClassProvider<T>
        extends ClassProviderInternal<T> {
    // ----- DYNAMIC
    // *** CONSTRUCTORS
    public ClassProvider(Class<T> targetClass) {
        super(targetClass);
    }
}
