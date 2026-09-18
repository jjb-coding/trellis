package com.github.jjbcoding.trellis.example.enums.states;

import com.github.jjbcoding.trellis.example.nodes.*;
import com.github.jjbcoding.trellis.service.Node;

@SuppressWarnings("unused")
public enum States {
    // ----- MEMBERS
    MAKE_BOOKING        (null),
    UPDATE_DETAILS      (null),
    VIEW_BOOKINGS       (null),
    DASHBOARD           (DashboardNode.class),
    LOG_IN              (LogInNode.class)
    ;
    // ----- DYNAMIC
    // *** FIELDS
    final Class<? extends Node> nodeClass;

    // *** CONSTRUCTORS
    States(Class<? extends Node> nodeClass) {
        this.nodeClass = nodeClass;
    }

    // *** METHODS
    // ** PUBLIC
    // Getters
    public Class<? extends Node> getNodeClass() {
        return nodeClass;
    }
}
