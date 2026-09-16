package com.github.jjbcoding.trellis.service.readers;

import java.util.Map;

/**
 * A MapPair is a bidirectional map of associations between the
 * constants of an Enum and the classes an Enum reader relates it to.
 * @param <T>   The type or supertype of class objects the reader produces
 */
public class MapPair<T> {
    // ----- DYNAMIC
    // *** FIELDS
    Map<Enum<?>,Class<? extends T>> enumToClass;
    Map<Class<? extends T>,Enum<?>> classToEnum;

    // *** CONSTRUCTORS
    MapPair(Map<Enum<?>,Class<? extends T>> enumToClass, Map<Class<? extends T>,Enum<?>> classToEnum) {
        this.enumToClass = enumToClass;
        this.classToEnum = classToEnum;
    }

    // *** METHODS
    // ** PUBLIC
    // Getters
    /**
     * Returns the map associating Enum constants with a class.
     * @return  The map
     */
    public Map<Enum<?>,Class<? extends T>> getEnumToClass() {
        return enumToClass;
    }

    /**
     * Returns the map associating a class with an Enum constant.
     * @return  The map
     */
    public Map<Class<? extends T>,Enum<?>> getClassToEnum() {
        return classToEnum;
    }
}
