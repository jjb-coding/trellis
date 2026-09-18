package com.github.jjbcoding.trellis.service.providers.defaults;
import com.github.jjbcoding.trellis.exceptions.BuilderException;
import com.github.jjbcoding.trellis.service.providers.ClassProvider;
import com.github.jjbcoding.trellis.util.internal.ReflectionHelper;

import java.util.List;

/**
 * A class provider that locates all classes in a package.
 */
public class ReflectionClassProvider<T>
    extends ClassProvider<T> {
    // ----- DYNAMIC
    // *** FIELDS
    String inputPackage;

    // *** CONSTRUCTORS
    /**
     * Constructs a ReflectionClassProvider instance.
     * @param packageString The name of the package
     * @param targetClass   The class type or supertype
     */
    public ReflectionClassProvider(String packageString, Class<T> targetClass) {
        super(targetClass);
        if (packageString == null || packageString.isEmpty())
            throw new BuilderException("app:build: Package string was null or empty.");
        this.inputPackage = packageString;
    }

    // *** METHODS
    @Override
    public List<Class<?>> produceClasses() {
        List<Class<?>> inputs;
        try {
            inputs = ReflectionHelper.getClasses(inputPackage);
        } catch (Exception e) {
            throw new BuilderException("API:init: Couldn't get classes from package.");
        }
        return inputs;
    }
}
