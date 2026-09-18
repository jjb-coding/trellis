package com.github.jjbcoding.trellis.service;

import java.lang.reflect.Constructor;
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
            throw new RuntimeException("APP:Node[" + cls.getSimpleName() + "]: Must only have 1 constructor.");
        constructor = constructors[0];

        // Scan constructor
        Type[] types = constructor.getGenericParameterTypes();

        // VALIDATE: number of parameters
        int expectation = isNode ? 3 : 2;
        if (types.length != expectation)
            throw new RuntimeException("APP:refl[" + cls.getSimpleName() + "]: " + expectation + " parameters were expected.");

        // VALIDATE: is first parameter AppContainer?
        Type appContainerType = types[0];
        if (!(appContainerType.equals(AppService.class)))
            throw new RuntimeException("APP:refl[" + cls.getSimpleName() + "]: First parameter is not AppContainer type.");

        // VALIDATE: is second parameter class?
        Type parentType = types[1];
        if (!(parentType instanceof Class<?> parentCls))
            throw new RuntimeException("APP:refl[" + cls.getSimpleName() + "]: Second parameter is not a class.");

        // VALIDATE: is second parameter parent?
        if (!(Base.class.isAssignableFrom(parentCls)))
            throw new RuntimeException("APP:refl[" + cls.getSimpleName() + "]: Second parameter is not a subtype of Base.");

        // VALIDATE: is third parameter class?
        if (isNode) {
            // VALIDATE: is third parameter ViewBag?
            Type viewBagType = types[2];
            if (!(viewBagType.equals(InjectableBag.class)))
                throw new RuntimeException("APP:refl[" + cls.getSimpleName() + "]: Third parameter is not InjectableBag type.");
        }
    }
}
