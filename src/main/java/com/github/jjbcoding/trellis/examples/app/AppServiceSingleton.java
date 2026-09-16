package com.github.jjbcoding.trellis.examples.app;

import com.github.jjbcoding.trellis.service.AppService;

public enum AppServiceSingleton {
    // ----- MEMBERS
    INSTANCE
    ;
    // ----- DYNAMIC
    // *** FIELDS
    final AppService appService;

    // *** CONSTRUCTORS
    AppServiceSingleton() {
        appService = new AppService();
    }

    // *** METHODS
    // ** PUBLIC
    // Getters
    public AppService get() {
        return appService;
    }
}
