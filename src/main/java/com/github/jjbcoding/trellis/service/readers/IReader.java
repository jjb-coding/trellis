package com.github.jjbcoding.trellis.service.readers;

/**
 * An Enum reader receives each constant in an enum, and produces a Class
 * value. This may come from the constant's name, the invocation of a method
 * on the enum class, or some other computation.
 * @param <T>   The class type or supertype that the constant is associated with
 * @param <U>   The Enum class the constant is a member of
 */
public interface IReader<T,U extends Enum<U>> {
    Class<? extends T> getAssociation(U constant);
}
