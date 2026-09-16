package com.github.jjbcoding.trellis.service;

import com.github.jjbcoding.trellis.service.annotations.Expects;
import com.github.jjbcoding.trellis.service.annotations.Parent;
import com.github.jjbcoding.trellis.service.annotations.Supplies;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Classifies the data obtained from scanning a class whose superclass is Node.
 */
class NodeConfiguration extends Configuration<Node> {
    // ----- DYNAMIC
    // *** FIELDS
    // From Annotations
    Class<? extends Node> parentClass;
    List<Class<? extends Injectable>> expects;
    List<Class<? extends Injectable>> provides;
    // Routing
    HashSet<Class<? extends Node>> descendantClasses;
    HashSet<NodeConfiguration> childrenConfigurations;

    // *** CONSTRUCTORS
    public NodeConfiguration(Class<? extends Node> cls) {
        // * Super
        super(cls, true);

        // * Initialise descendants & children
        descendantClasses = new HashSet<>();
        childrenConfigurations = new HashSet<>();

        // * Annotations
        // Expects
        Expects expectsAnnotation = cls.getAnnotation(Expects.class);
        if (expectsAnnotation != null) {
            expects = new ArrayList<>();
            Class<? extends Injectable>[] expectsArray = expectsAnnotation.value();
            for (int i = 0; i < expectsArray.length; i++)
                expects.add(expectsArray[i]);
        }

        // Provides
        Supplies providesAnnotation = cls.getAnnotation(Supplies.class);
        if (providesAnnotation != null) {
            provides = new ArrayList<>();
            Class<? extends Injectable>[] providesArray = providesAnnotation.value();
            for (int i = 0; i < providesArray.length; i++)
                provides.add(providesArray[i]);
        }

        // Parent
        Parent parentAnnotation = cls.getAnnotation(Parent.class);
        if (parentAnnotation == null)
            parentClass = null;
        else
            parentClass = parentAnnotation.value();
    }

    // *** METHODS
    // ** PUBLIC

    // ** PACKAGE-PRIVATE
    // * Provides / Expects Interface
    // Query
    boolean expectsAnything() {
        return expects != null;
    }

    boolean providesAnything() {
        return provides != null;
    }

    // Getters / Clone
    HashSet<Class<? extends Injectable>> expects() {
        return new HashSet<>(expects);
    }

    HashSet<Class<? extends Injectable>> provides() {
        return new HashSet<>(provides);
    }


    Object execute(AppService appContainer, Node parent, InjectableBag bag) {
        Object[] values = new Object[3];
        values[0] = appContainer;
        values[1] = parent;
        values[2] = bag;

        Object ret;
        try {
            ret = constructor.newInstance(values);
        } catch (Exception e) {
            throw new RuntimeException("APP:runtime: Couldn't instantiate " + thisCls.getSimpleName(), e.getCause());
        }
        return ret;
    }

    // ** PACKAGE-PRIVATE
    boolean isLeaf() {
        return childrenConfigurations.isEmpty();
    }
}
