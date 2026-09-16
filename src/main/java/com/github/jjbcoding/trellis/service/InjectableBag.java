package com.github.jjbcoding.trellis.service;

import com.github.jjbcoding.trellis.exceptions.ImplementerException;
import com.github.jjbcoding.trellis.exceptions.OperationException;

import java.util.*;

/**
 * An InjectableBag stores a series of pre-constructed Injectables. This
 * can be used to courier information from one configuration of the Node tree
 * to another, as it changes.
 */
public class InjectableBag {
    // *** FIELDS
    // Injections
    AppService _appService;
    // Entries
    public LinkedHashMap<Class<? extends Injectable>,Injectable> map;

    // *** CONSTRUCTORS
    InjectableBag(AppService _appService) {
        this._appService = _appService;
        this.map = new LinkedHashMap<>();
    }

    // *** METHODS
    // ** PUBLIC
    // Manipulation / Add
    /**
     * Adds an Injectable instance that has already been constructed.
     * @param instance              The instance
     * @throws OperationException   if there is already an instance of this class in the InjectableBag
     */
    @SuppressWarnings("unused")
    public void add(Injectable instance) {
        if (map.putIfAbsent(instance.getClass(), instance) != null)
            throw new OperationException("app:injectableBag: An attempt was made to create or add an Injectable, but there is already an instance");
    }

    // Manipulation / Remove
    /**
     * Removes an Injectable instance by constant.
     * @param injectableConstant    The constant
     * @throws OperationException   if the constant is invalid
     * @throws ImplementerException       if the Injectable implements AutoCloseable and its close method threw an error
     */
    @SuppressWarnings("unused")
    public void remove(Enum injectableConstant) {
        remove(_appService.constToClassInj(injectableConstant));
    }

    /**
     * Removes an Injectable instance by class.
     * @param injectableClass       The class
     * @throws ImplementerException       if the Injectable implements AutoCloseable and its close method threw an error
     */
    @SuppressWarnings("unused")
    public void remove(Class<? extends Injectable> injectableClass) {
        Injectable injectable = map.remove(injectableClass);
        if (injectable instanceof AutoCloseable autoCloseableInjectable) {
            try {
                autoCloseableInjectable.close();
            }
            catch (Exception e) {
                throw new ImplementerException("app:close: Failed to close injectable", e);
            }
        }
    }

    // Manipulation / Reset
    /**
     * Removes all Injectables, emptying the Injectable Bag.
     * @throws ImplementerException       if any of the Injectables implement AutoCloseable and their close method threw an error
     */
    @SuppressWarnings("unused")
    public void reset() {
        for (Class<? extends Injectable> injectableClass : map.keySet())
            remove(injectableClass);
    }

    // Manipulation / Get
    /**
     * Retrieves an Injectable from the Injectable Bag by constant.
     * Returns null if it has not been added or created.
     * @param injectableConstant    The constant
     * @return                      The Injectable, or null
     * @throws OperationException   if the constant is invalid
     */
    @SuppressWarnings("unused")
    public Injectable get(Enum<?> injectableConstant) {
        return get(_appService.constToClassInj(injectableConstant));

    }

    /**
     * Retrieves an Injectable from the Injectable Bag by constant.
     * Returns null if it has not been added or created.
     * @param injectableClass       The class
     * @return                      The Injectable, or null
     */
    @SuppressWarnings("unused")
    public Injectable get(Class<? extends Injectable> injectableClass) {
        return map.get(injectableClass);
    }

    // Manipulation / GetOrCreate
    /**
     * Retrieves an Injectable from the Injectable Bag by constant,
     * or creates one if one does not yet exist.
     * @param injectableConstant    The constant
     * @return                      The Injectable
     * @throws OperationException   if the constant is invalid
     */
    @SuppressWarnings("unused")
    public Injectable getOrCreate(Enum<?> injectableConstant) {
        return getOrCreate(_appService.constToClassInj(injectableConstant));
    }

    /**
     * Retrieves an Injectable from the Injectable Bag by constant,
     * or creates one if one does not yet exist.
     * @param injectableClass       The class
     * @return                      The Injectable
     * @throws OperationException   if the constant is invalid
     */
    @SuppressWarnings("unused")
    public Injectable getOrCreate(Class<? extends Injectable> injectableClass) {
        return map.computeIfAbsent(injectableClass, (k) -> _appService.launchInjectable(null, k));
    }

    // Manipulation / Create
    /**
     * Creates an Injectable by constant and puts it in the Injectable Bag.
     * @param injectableConstant    The constant
     * @throws OperationException   if the constant is invalid, or an instance of the Injectable already exists
     */
    @SuppressWarnings("unused")
    public void create(Enum<?> injectableConstant) {
        create(_appService.constToClassInj(injectableConstant));
    }

    /**
     * Creates an Injectable by class and puts it in the Injectable Bag.
     * @param injectableClass       The class
     * @throws OperationException   if an instance of the Injectable already exists
     */
    @SuppressWarnings("unused")
    public void create(Class<? extends Injectable> injectableClass) {
        if (map.putIfAbsent(injectableClass, _appService.launchInjectable(null, injectableClass)) != null)
            throw new OperationException("app:injectableBag: An attempt was made to create or add an Injectable, but there is already an instance");
    }

    /**
     * Creates multiple Injectables by constants and puts it in the Injectable Bag.
     * @param injectableConstants   The array of constants
     * @throws OperationException   if any constants are invalid, or an instance of an Injectable already exists
     */
    @SuppressWarnings("unused")
    public void create(Enum<?>[] injectableConstants) {
        createFromConstants(List.of(injectableConstants));
    }

    /**
     * Creates multiple Injectables by classes and puts it in the Injectable Bag.
     * @param injectableClasses     The array of classes
     * @throws OperationException   if an instance of an Injectable already exists
     */
    @SuppressWarnings("unused")
    public void create(Class<? extends Injectable>[] injectableClasses) {
        createFromClasses(List.of(injectableClasses));
    }

    /**
     * Creates multiple Injectables by constants and puts it in the Injectable Bag.
     * @param injectableConstants   The iterable of constants
     * @throws OperationException   if any constants are invalid, or an instance of an Injectable already exists
     */
    @SuppressWarnings("unused")
    public void createFromConstants(Iterable<Enum<?>> injectableConstants) {
        for (Enum<?> injectableConstant : injectableConstants)
            create(injectableConstant);
    }

    /**
     * Creates multiple Injectables by classes and puts it in the Injectable Bag.
     * @param injectableClasses     The iterable of classes
     * @throws OperationException   if an instance of an Injectable already exists
     */
    @SuppressWarnings("unused")
    public void createFromClasses(Iterable<Class<? extends Injectable>> injectableClasses) {
        for (Class<? extends Injectable> injectableClass : injectableClasses)
            create(injectableClass);
    }

    // Manipulation / Create / Try
    /**
     * Creates an Injectable by constant and puts it in the Injectable Bag.
     * @param injectableConstant    The constant
     * @throws OperationException   if the constant is invalid
     */
    @SuppressWarnings("unused")
    public void tryCreate(Enum<?> injectableConstant) {
        tryCreate(_appService.constToClassInj(injectableConstant));
    }

    /**
     * Creates an Injectable by class and puts it in the Injectable Bag.
     * @param injectableClass       The class
     */
    @SuppressWarnings("unused")
    public void tryCreate(Class<? extends Injectable> injectableClass) {
        map.putIfAbsent(injectableClass, _appService.launchInjectable(null, injectableClass));
    }

    /**
     * Creates multiple Injectables by constants and puts it in the Injectable Bag.
     * @param injectableConstants   The array of constants
     * @throws OperationException   if any constants are invalid
     */
    @SuppressWarnings("unused")
    public void tryCreate(Enum<?>[] injectableConstants) {
        tryCreateFromConstants(List.of(injectableConstants));
    }

    /**
     * Creates multiple Injectables by classes and puts it in the Injectable Bag.
     * @param injectableClasses     The array of classes
     */
    @SuppressWarnings("unused")
    public void tryCreate(Class<? extends Injectable>[] injectableClasses) {
        tryCreateFromClasses(List.of(injectableClasses));
    }

    /**
     * Creates multiple Injectables by constants and puts it in the Injectable Bag.
     * @param injectableConstants   The iterable of constants
     * @throws OperationException   if any constants are invalid
     */
    @SuppressWarnings("unused")
    public void tryCreateFromConstants(Iterable<Enum<?>> injectableConstants) {
        for (Enum<?> injectableConstant : injectableConstants)
            tryCreate(injectableConstant);
    }

    /**
     * Creates multiple Injectables by classes and puts it in the Injectable Bag.
     * @param injectableClasses     The iterable of classes
     */
    @SuppressWarnings("unused")
    public void tryCreateFromClasses(Iterable<Class<? extends Injectable>> injectableClasses) {
        for (Class<? extends Injectable> injectableClass : injectableClasses)
            tryCreate(injectableClass);
    }
}
