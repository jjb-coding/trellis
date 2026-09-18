package com.github.jjbcoding.trellis.example.enums.injectables;

import com.github.jjbcoding.trellis.example.injectables.controllers.DashboardController;
import com.github.jjbcoding.trellis.example.injectables.controllers.ElementAccountBarController;
import com.github.jjbcoding.trellis.example.injectables.controllers.ElementProgressBarController;
import com.github.jjbcoding.trellis.example.injectables.transfer.LogInData;
import com.github.jjbcoding.trellis.example.injectables.transfer.ProgressBarPublisher;
import com.github.jjbcoding.trellis.example.injectables.controllers.LogInController;
import com.github.jjbcoding.trellis.service.Injectable;

@SuppressWarnings("unused")
public enum Injectables {
    // ----- MEMBERS
    LOGIN_DATA              (LogInData.class),
    PROGRESSBAR_PUBLISHER   (ProgressBarPublisher.class),

    DASHBOARD_CONTROLLER    (DashboardController.class),
    ACCOUNTBAR_CONTROLLER   (ElementAccountBarController.class),
    PROGRESSBAR_CONTROLLER  (ElementProgressBarController.class),
    LOGIN_CONTROLLER        (LogInController.class)
    ;
    // ----- DYNAMIC
    // *** FIELDS
    final Class<? extends Injectable> injectableClass;

    // *** CONSTRUCTORS
    Injectables(Class<? extends Injectable> injectableClass) {
        this.injectableClass = injectableClass;
    }

    // *** METHODS
    // ** PUBLIC
    // Getters
    public Class<? extends Injectable> getInjectableClass() {
        return injectableClass;
    }
}
