package com.github.jjbcoding.trellis.service;

import com.github.jjbcoding.trellis.service.annotations.Expects;
import com.github.jjbcoding.trellis.service.annotations.Parent;
import com.github.jjbcoding.trellis.service.annotations.Supplies;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
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
    NodeConfiguration(Class<? extends Node> nodeClass) {
        // * Super
        super(nodeClass, true);

        // * Initialise descendants & children
        descendantClasses = new HashSet<>();
        childrenConfigurations = new HashSet<>();

        // * Annotations
        // Expects
        Expects expectsAnnotation = nodeClass.getAnnotation(Expects.class);
        if (expectsAnnotation != null)
            expects = new ArrayList<>(Arrays.asList(expectsAnnotation.value()));

        // Provides
        Supplies providesAnnotation = nodeClass.getAnnotation(Supplies.class);
        if (providesAnnotation != null) {
            provides = new ArrayList<>(Arrays.asList(providesAnnotation.value()));
        }

        // Parent
        Parent parentAnnotation = nodeClass.getAnnotation(Parent.class);
        if (parentAnnotation == null)
            parentClass = null;
        else
            parentClass = parentAnnotation.value();
    }

    // *** METHODS
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

    // Execution
    Object execute(AppService _appService, Node parent, InjectableBag bag) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        Object[] values = new Object[3];
        values[0] = _appService;
        values[1] = parent;
        values[2] = bag;

        return constructor.newInstance(values);
    }

    // Query
    boolean isLeaf() {
        return childrenConfigurations.isEmpty();
    }
}
