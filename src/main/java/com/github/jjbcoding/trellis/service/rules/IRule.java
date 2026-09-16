package com.github.jjbcoding.trellis.service.rules;

/**
 * Interface for validation rules for the APIService build process.
 */
public interface IRule<T> {
    /**
     * Applies the rule.
     * @param discoveredClasses The set of discovered classes attached to the rule
     * @return                  If it validates
     */
    boolean isValid(Iterable<Class<? extends T>> discoveredClasses);
}
