package com.github.jjbcoding.trellis.service;

class NodeLocator {
    // ----- STATIC
    static Enum<?> getConstant(NodeLocator description) {
        if (description == null)
            return null;
        return description.nodeConstant;
    }

    // ----- DYNAMIC
    // *** FIELDS
    Class<? extends Node> nodeClass;
    Enum<?> nodeConstant;

    // *** CONSTRUCTORS
    NodeLocator(Class<? extends Node> nodeClass) {
        this.nodeClass = nodeClass;
        nodeConstant = null;
    }

    NodeLocator(Enum<?> nodeConstant) {
        nodeClass = null;
        this.nodeConstant = nodeConstant;
    }

    // *** METHODS
    boolean isConstant() {
        return nodeConstant != null;
    }
}
