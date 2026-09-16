package com.github.jjbcoding.trellis.service.rules.defaults;


import com.github.jjbcoding.trellis.service.providers.ClassProvider;
import com.github.jjbcoding.trellis.service.providers.internal.ClassProviderProxy;
import com.github.jjbcoding.trellis.service.rules.IRule;

import java.util.HashSet;
import java.util.List;

/**
 * A validation rule that compares a discovered set of classes
 * against a provided one.
 */
public class Rule<T>
    implements IRule<T> {
    // ----- NESTED
    // ----- DYNAMIC
    // *** FIELDS
    ClassProvider<T> classProvider;
    Relationship type;

    // *** CONSTRUCTORS
    @SuppressWarnings("unused")
    /**
     * Constructs a rule.
     * @param classProvider     The provided classes
     * @param type              The type of rule
     */
    public Rule(ClassProvider<T> classProvider, Relationship type) {
        this.classProvider = classProvider;
        this.type = type;
    }

    // *** INTERFACE IMPLEMENTATIONS
    // * [ IValidationProvider ]
    @SuppressWarnings("unused")
    @Override
    public boolean isValid(Iterable<Class<? extends T>> discoveredClasses) {
        // Get
        List<Class<? extends T>> containerClasses = ClassProviderProxy.getClasses(classProvider);

        // Validate
        if (type == Relationship.InProvider || type == Relationship.EqualProvider) {
            HashSet<Class<?>> containerSet = new HashSet<>(containerClasses);
            for (Class<?> discoveredClass : discoveredClasses)
                if (!containerSet.contains(discoveredClass))
                    return false;
        }
        if (type == Relationship.CoverProvider || type == Relationship.EqualProvider) {
            HashSet<Class<?>> discoveredSet = new HashSet<>();
            for (Class<?> cls : discoveredClasses)
                discoveredSet.add(cls);

            for (Class<?> containerClass : containerClasses)
                if (!discoveredSet.contains(containerClass))
                    return false;
        }

        // Return
        return true;
    }
}
