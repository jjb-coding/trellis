package com.github.jjbcoding.trellis.service;

import com.github.jjbcoding.trellis.exceptions.BuilderException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

/**
 * Base class for scanning AppBase types.
 */
class Configuration<T> {
    // ----- DYNAMIC
    // *** FIELDS
    // This class
    Class<? extends T> thisCls;
    // Constructor for the node
    final Constructor<?> constructor;

    // *** CONSTRUCTORS
    public Configuration(Class<? extends T> cls, boolean isNode) {
        // thisCls
        thisCls = cls;

        // Constructor
        Constructor<?>[] constructors = cls.getDeclaredConstructors();
        if (constructors.length != 1)
            throw new BuilderException("app:build:scan: [" + cls.getSimpleName() + "]: Must only have 1 constructor");
        constructor = constructors[0];

        // VALIDATE: public
        if (!(Modifier.isPublic(constructor.getModifiers())))
            throw new BuilderException("app:build:scan: [" + cls.getSimpleName() + "]: Constructor is not public.");

        // Scan constructor
        Type[] types = constructor.getGenericParameterTypes();

        // VALIDATE: number of parameters
        int expectation = isNode ? 3 : 2;
        if (types.length != expectation)
            throw new BuilderException("app:build:scan: [" + cls.getSimpleName() + "]: Constructor " + expectation + " parameters were expected");

        // VALIDATE: is first parameter AppContainer?
        Type appServiceType = types[0];
        if (!(appServiceType.equals(AppService.class)))
            throw new BuilderException("app:build:scan: [" + cls.getSimpleName() + "]: First constructor parameter is not AppService type");

        // VALIDATE: is second parameter class?
        Type parentType = types[1];
        if (!(parentType instanceof Class<?> parentCls))
            throw new BuilderException("app:build:scan: [" + cls.getSimpleName() + "]: Second constructor parameter is not a class");

        // VALIDATE: is second parameter parent?
        if (!(Base.class.isAssignableFrom(parentCls)))
            throw new BuilderException("app:build:scan: [" + cls.getSimpleName() + "]: Second constructor parameter is not a Node, Injectable, or subtype thereof");

        // VALIDATE: is third parameter class?
        if (isNode) {
            // VALIDATE: is third parameter InjectableBag?
            Type injectableBagType = types[2];
            if (!(injectableBagType.equals(InjectableBag.class)))
                throw new BuilderException("app:build:scan:node: [" + cls.getSimpleName() + "]: Third constructor parameter is not InjectableBag type");
        }
    }
}
