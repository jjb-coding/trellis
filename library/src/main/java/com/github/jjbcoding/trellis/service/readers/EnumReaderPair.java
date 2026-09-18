package com.github.jjbcoding.trellis.service.readers;

import com.github.jjbcoding.trellis.exceptions.ImplementerException;

import java.util.HashMap;
import java.util.Map;

/**
 * An EnumReaderPair relates an IReader to the Enum it reads.
 * @param <T>   The type or supertype of class objects the reader produces
 * @param <U>   The Enum class
 */
public class EnumReaderPair<T,U extends Enum<U>> {
    // *** FIELDS
    IReader<T,U> reader;
    Class<U> enumClass;

    // *** CONSTRUCTORS
    /**
     * Constructs an EnumReaderPair instance.
     * @param reader        The reader
     * @param enumClass     The associated Enum
     */
    public EnumReaderPair(IReader<T, U> reader, Class<U> enumClass) {
        this.reader = reader;
        this.enumClass = enumClass;
    }

    // *** METHODS
    // Getters
    /**
     * Gets the Enum class from this pair.
     * @return  The Enum class
     */
    public Class<U> getEnumClass() {
        return enumClass;
    }

    /**
     * Gets the Reader from this pair.
     * @return  The reader
     */
    public IReader<T,U> getReader() {
        return reader;
    }

    // Compile
    /**
     * Uses the Reader to extract values from the Enum,
     * and returns a {@link MapPair} of associations in
     * both directions.
     * @return  The MapPair
     * @see MapPair
     */
    public MapPair<T> toMap() {
        Map<Enum<?>,Class<? extends T>> enumToClass = new HashMap<>();
        Map<Class<? extends T>,Enum<?>> classToEnum = new HashMap<>();
        for (U constant : enumClass.getEnumConstants()) {
            Class<? extends T> association;
            try {
                association = reader.getAssociation(constant);
            }
            catch (Exception e) {
                throw new ImplementerException(
                        "IReader::getAssociation at " + reader.getClass().getSimpleName() + " with " + constant.name(),
                        e
                );
            }
            enumToClass.put(constant, association);
            classToEnum.put(association, constant);
        }
        return new MapPair<>(enumToClass, classToEnum);
    }

    // Copy
    /**
     * Copies the EnumStateReader.
     * @return  The copy
     */
    @SuppressWarnings("unused")
    public EnumReaderPair<T,U> copy() {
        return new EnumReaderPair<>(reader, enumClass);
    }
}
