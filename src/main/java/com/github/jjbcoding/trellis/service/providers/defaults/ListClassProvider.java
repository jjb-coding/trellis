package com.github.jjbcoding.trellis.service.providers.defaults;

import com.github.jjbcoding.trellis.service.providers.ClassProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * A generic list-based class provider.
 */
public class ListClassProvider<T>
    extends ClassProvider<T> {
    // ----- DYNAMIC
    // *** FIELDS
    List<Class<?>> classes;

    // *** CONSTRUCTORS
    /**
     * Constructs a ListClassProvider instance.
     * @param classes       The classes
     * @param targetClass   The class type or supertype
     */
    @SuppressWarnings("unused")
    public ListClassProvider(List<Class<?>> classes, Class<T> targetClass) {
        super(targetClass);

        this.classes = new ArrayList<>(classes);
    }

    /**
     * Constructs a ListClassProvider instance.
     */
    @SuppressWarnings("unused")
    public ListClassProvider(Class<T> targetClass) {
        super(targetClass);
    }

    // *** METHODS
    // ** PUBLIC
    // * Addition
    /**
     * Adds a class to the list.
     * @param cls       The class
     */
    @SuppressWarnings("unused")
    public void add(Class<? extends T> cls) {
        classes.add(cls);
    }

    /**
     * Adds multiple classes to the list.
     * @param classes   The classes
     */
    @SuppressWarnings("unused")
    public void add(Iterable<Class<? extends T>> classes) {
        for (Class<? extends T> cls : classes)
            this.classes.add(cls);
    }

    // *** INTERFACE IMPLEMENTATIONS
    // * [ IClassProvider ]
    @Override
    public List<Class<?>> produceClasses() {
        return classes;
    }
}
