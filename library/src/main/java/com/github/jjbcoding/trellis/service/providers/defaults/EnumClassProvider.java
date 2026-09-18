package com.github.jjbcoding.trellis.service.providers.defaults;

import com.github.jjbcoding.trellis.service.providers.ClassProvider;
import com.github.jjbcoding.trellis.service.readers.EnumReaderPair;
import com.github.jjbcoding.trellis.service.readers.IReader;

import java.util.ArrayList;
import java.util.List;

/**
 * Uses an Enum reader and Enum to produce a list of classes.
 * @param <T>   The class type or supertype
 */
public class EnumClassProvider<T>
        extends ClassProvider<T> {
    // ----- DYNAMIC
    // *** FIELDS
    EnumReaderPair<?,? extends Enum<?>> pair;

    // *** CONSTRUCTORS
    /**
     * Constructs an EnumClassProvider instance.
     * @param pair          The Enum reader and Enum pair
     * @param targetClass   The class type or supertype
     */
    public EnumClassProvider(EnumReaderPair<T,? extends Enum<?>> pair, Class<T> targetClass) {
        super(targetClass);

        this.pair = pair;
    }

    // *** INTERFACE IMPLEMENTATIONS
    // * [ IClassProvider ]
    public List<Class<?>> produceClasses() {
        return produceClassesI(pair);
    }

    /*
    PRIVATE-LOCAL
     */
    @SuppressWarnings("unchecked")
    private static <T, U extends Enum<U>> List<Class<?>> produceClassesI(EnumReaderPair<T, ? extends Enum<?>> pair) {
        return produceClassesJ((EnumReaderPair<T,U>)pair);
    }

    private static <T, U extends Enum<U>> List<Class<?>> produceClassesJ(EnumReaderPair<T, U> pair) {
        List<Class<?>> classes = new ArrayList<>();

        Class<U> enumClass = pair.getEnumClass();
        IReader<T, U> reader = pair.getReader();

        for (U constant : enumClass.getEnumConstants())
            classes.add(reader.getAssociation(constant));

        return classes;
    }
}
