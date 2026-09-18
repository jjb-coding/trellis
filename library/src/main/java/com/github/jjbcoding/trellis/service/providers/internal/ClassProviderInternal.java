package com.github.jjbcoding.trellis.service.providers.internal;

import com.github.jjbcoding.trellis.service.providers.ClassProvider;

import java.util.ArrayList;
import java.util.List;

public abstract class ClassProviderInternal<T> {
    // ----- DYNAMIC
    // *** FIELDS
    Class<T> targetClass;
    List<Class<? extends T>> cache;

    // *** CONSTRUCTORS
    public ClassProviderInternal(Class<T> targetClass) {
        this.targetClass = targetClass;
    }

    // *** METHODS
    // ** PUBLIC
    /**
     * Returns the class type or supertype.
     * @return  The class type or supertype
     */
    public Class<T> getTargetClass() {
        return targetClass;
    }

    // ** PACKAGE-PRIVATE
    @SuppressWarnings("unchecked")
    List<Class<? extends T>> getClasses() {
        if (cache != null)
            return cache;

        cache = new ArrayList<>();
        for (Class<?> cls : produceClasses())
            if (targetClass.isAssignableFrom(cls))
                cache.add((Class<? extends T>)cls);
        return cache;
    }

    // ----- ABSTRACT
    /**
     * Produces a list of classes. Each item on this list will be
     * filtered to ensure it is of the assignable from the target
     * class, and cached: this method will only be invoked once
     * regardless of how many times {@link ClassProvider#getClasses()}
     * is called.
     * @return  The list of classes
     */
    public abstract List<Class<?>> produceClasses();
}
