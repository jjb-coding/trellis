package com.github.jjbcoding.trellis.service;

import com.github.jjbcoding.trellis.exceptions.ImplementerException;
import com.github.jjbcoding.trellis.exceptions.OperationException;
import com.github.jjbcoding.trellis.exceptions.ResolutionTermination;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class, common to Injectables and Nodes. Reflects status
 * in an application-wide way.
 */
abstract class Base {
	// ----- ABSTRACT
	// Resolution / State
	@SuppressWarnings("unused")
	public abstract void setState(Class<? extends Node> state);
	@SuppressWarnings("unused")
	public abstract void setState(Class<? extends Node> state, InjectableBag bag);

	@SuppressWarnings("unused")
	public abstract void setState(Enum<?> stateConstant);
	@SuppressWarnings("unused")
	public abstract void setState(Enum<?> stateConstant, InjectableBag bag);

	abstract void setStateR(Class<? extends Node> state, InjectableBag bag);

	// Resolution / Inject
	@SuppressWarnings("unused")
	public abstract Injectable inject(Class<? extends Injectable> injectableClass);
	@SuppressWarnings("unused")
	public abstract Injectable inject(Enum<?> injectableConstant);

	abstract Injectable injectR(Class<? extends Injectable> injectableClass);

	// ----- DYNAMIC
	// *** FIELDS
	// Injections
	AppService _appService;
	// Linkage
	Base parent;
	// Bag
	InjectableBag localBag;

	// *** CONSTRUCTORS
	/**
	 * Attaches the AppContainer and parent.
	 * @param _appService 	The AppService.
	 * @param parent		The immediate parent.
	 */
	Base(AppService _appService, Base parent) {
		this._appService = _appService;
		this.parent = parent;
		localBag = null;
	}

	// *** METHODS
	// ** PUBLIC
	// Getters
	public AppService getAppService() {
		return _appService;
	}

	// Local Bag / Proxy / Manipulation / Add
	/**
	 * Adds an Injectable instance that has already been constructed to the internal Injectable Bag.
	 * Will create the internal Injectable Bag if it does not already exist.
	 * @param instance              	The instance
	 * @throws OperationException   	if there is already an instance of this class in the InjectableBag
	 */
	@SuppressWarnings("unused")
	public void bagAdd(Injectable instance) {
		localLazy();
		localBag.add(instance);
	}

	// Local Bag / Proxy / Manipulation / Remove
	/**
	 * Removes an Injectable instance by constant from the internal Injectable Bag.
	 * Will create the internal Injectable Bag if it does not already exist.
	 * @param injectableConstant    	The constant
	 * @throws OperationException   	if the constant is invalid
	 * @throws ImplementerException     if the Injectable implements AutoCloseable and its close method threw an error
	 */
	@SuppressWarnings("unused")
	public void bagRemove(Enum<?> injectableConstant) {
		localLazy();
		localBag.remove(injectableConstant);
	}

	/**
	 * Removes an Injectable instance by class from the internal Injectable Bag.
	 * Will create the internal Injectable Bag if it does not already exist.
	 * @param injectableClass       	The class
	 * @throws ImplementerException     if the Injectable implements AutoCloseable and its close method threw an error
	 */
	@SuppressWarnings("unused")
	public void bagRemove(Class<? extends Injectable> injectableClass) {
		localLazy();
		localBag.remove(injectableClass);
	}

	// Local Bag / Proxy / Manipulation / Reset
	/**
	 * Removes all Injectables, emptying the internal Injectable Bag.
	 * Will create the internal Injectable Bag if it does not already exist.
	 * @throws ImplementerException       if any of the Injectables implement AutoCloseable and their close method threw an error
	 */
	@SuppressWarnings("unused")
	public void bagReset() {
		localLazy();
		localBag.reset();
	}

	// Local Bag / Proxy / Manipulation / Get
	/**
	 * Retrieves an Injectable from the internal Injectable Bag by constant.
	 * Will create the internal Injectable Bag if it does not already exist.
	 * Returns null if it has not been added or created.
	 * @param injectableConstant    The constant
	 * @return                      The Injectable, or null
	 * @throws OperationException   if the constant is invalid
	 */
	@SuppressWarnings("unused")
	public Injectable bagGet(Enum<?> injectableConstant) {
		localLazy();
		return localBag.get(injectableConstant);
	}

	/**
	 * Retrieves an Injectable from the internal Injectable Bag by constant.
	 * Will create the internal Injectable Bag if it does not already exist.
	 * Returns null if it has not been added or created.
	 * @param injectableClass       The class
	 * @return                      The Injectable, or null
	 */
	@SuppressWarnings("unused")
	public Injectable bagGet(Class<? extends Injectable> injectableClass) {
		localLazy();
		return localBag.get(injectableClass);
	}

	// Local Bag / Proxy / Manipulation / GetOrCreate
	/**
	 * Retrieves an Injectable from the internal Injectable Bag by constant,
	 * or creates one if one does not yet exist.
	 * Will create the internal Injectable Bag if it does not already exist.
	 * @param injectableConstant    The constant
	 * @return                      The Injectable
	 * @throws OperationException   if the constant is invalid
	 */
	@SuppressWarnings("unused")
	public Injectable bagGetOrCreate(Enum<?> injectableConstant) {
		localLazy();
		return localBag.getOrCreate(injectableConstant);
	}

	/**
	 * Retrieves an Injectable from the internal Injectable Bag by constant,
	 * or creates one if one does not yet exist.
	 * Will create the internal Injectable Bag if it does not already exist.
	 * @param injectableClass       The class
	 * @return                      The Injectable
	 * @throws OperationException   if the constant is invalid
	 */
	@SuppressWarnings("unused")
	public Injectable bagGetOrCreate(Class<? extends Injectable> injectableClass) {
		localLazy();
		return localBag.getOrCreate(injectableClass);
	}

	// Local Bag / Proxy / Manipulation / Create
	/**
	 * Creates an Injectable by constant and puts it in the internal Injectable Bag.
	 * Will create the internal Injectable Bag if it does not already exist.
	 * @param injectableConstant    The constant
	 * @throws OperationException   if the constant is invalid, or an instance of the Injectable already exists
	 */
	@SuppressWarnings("unused")
	public void bagCreate(Enum<?> injectableConstant) {
		localLazy();
		localBag.create(injectableConstant);
	}

	/**
	 * Creates an Injectable by class and puts it in the internal Injectable Bag.
	 * Will create the internal Injectable Bag if it does not already exist.
	 * @param injectableClass       The class
	 * @throws OperationException   if an instance of the Injectable already exists
	 */
	@SuppressWarnings("unused")
	public void bagCreate(Class<? extends Injectable> injectableClass) {
		localLazy();
		localBag.create(injectableClass);
	}

	/**
	 * Creates multiple Injectables by constants and puts it in the internal Injectable Bag.
	 * Will create the internal Injectable Bag if it does not already exist.
	 * @param injectableConstants   The array of constants
	 * @throws OperationException   if any constants are invalid, or an instance of an Injectable already exists
	 */
	@SuppressWarnings("unused")
	public void bagCreate(Enum<?>[] injectableConstants) {
		localLazy();
		localBag.create(injectableConstants);
	}

	/**
	 * Creates multiple Injectables by classes and puts it in the internal Injectable Bag.
	 * Will create the internal Injectable Bag if it does not already exist.
	 * @param injectableClasses     The array of classes
	 * @throws OperationException   if an instance of an Injectable already exists
	 */
	@SuppressWarnings("unused")
	public void bagCreate(Class<? extends Injectable>[] injectableClasses) {
		localLazy();
		localBag.create(injectableClasses);
	}

	/**
	 * Creates multiple Injectables by constants and puts it in the internal Injectable Bag.
	 * Will create the internal Injectable Bag if it does not already exist.
	 * @param injectableConstants   The iterable of constants
	 * @throws OperationException   if any constants are invalid, or an instance of an Injectable already exists
	 */
	@SuppressWarnings("unused")
	public void bagCreateFromConstants(Iterable<Enum<?>> injectableConstants) {
		localLazy();
		localBag.createFromConstants(injectableConstants);
	}

	/**
	 * Creates multiple Injectables by classes and puts it in the internal Injectable Bag.
	 * Will create the internal Injectable Bag if it does not already exist.
	 * @param injectableClasses     The iterable of classes
	 * @throws OperationException   if an instance of an Injectable already exists
	 */
	@SuppressWarnings("unused")
	public void bagCreateFromClasses(Iterable<Class<? extends Injectable>> injectableClasses) {
		localLazy();
		localBag.createFromClasses(injectableClasses);
	}

	// Local Bag / Proxy / Manipulation / Create / Try
	/**
	 * Creates an Injectable by constant and puts it in the internal Injectable Bag.
	 * Will create the internal Injectable Bag if it does not already exist.
	 * @param injectableConstant    The constant
	 * @throws OperationException   if the constant is invalid
	 */
	@SuppressWarnings("unused")
	public void bagTryCreate(Enum<?> injectableConstant) {
		localLazy();
		localBag.tryCreate(injectableConstant);
	}

	/**
	 * Creates an Injectable by class and puts it in the internal Injectable Bag.
	 * Will create the internal Injectable Bag if it does not already exist.
	 * @param injectableClass       The class
	 */
	@SuppressWarnings("unused")
	public void bagTryCreate(Class<? extends Injectable> injectableClass) {
		localLazy();
		localBag.tryCreate(injectableClass);
	}

	/**
	 * Creates multiple Injectables by constants and puts it in the internal Injectable Bag.
	 * Will create the internal Injectable Bag if it does not already exist.
	 * @param injectableConstants   The array of constants
	 * @throws OperationException   if any constants are invalid
	 */
	@SuppressWarnings("unused")
	public void bagTryCreate(Enum<?>[] injectableConstants) {
		localLazy();
		localBag.tryCreate(injectableConstants);
	}

	/**
	 * Creates multiple Injectables by classes and puts it in the internal Injectable Bag.
	 * Will create the internal Injectable Bag if it does not already exist.
	 * @param injectableClasses     The array of classes
	 */
	@SuppressWarnings("unused")
	public void bagTryCreate(Class<? extends Injectable>[] injectableClasses) {
		localLazy();
		localBag.tryCreate(injectableClasses);
	}

	/**
	 * Creates multiple Injectables by constants and puts it in the internal Injectable Bag.
	 * Will create the internal Injectable Bag if it does not already exist.
	 * @param injectableConstants   The iterable of constants
	 * @throws OperationException   if any constants are invalid
	 */
	@SuppressWarnings("unused")
	public void bagTryCreateFromConstants(Iterable<Enum<?>> injectableConstants) {
		localLazy();
		localBag.tryCreateFromConstants(injectableConstants);
	}

	/**
	 * Creates multiple Injectables by classes and puts it in the internal Injectable Bag.
	 * Will create the internal Injectable Bag if it does not already exist.
	 * @param injectableClasses     The iterable of classes
	 */
	@SuppressWarnings("unused")
	public void bagTryCreateFromClasses(Iterable<Class<? extends Injectable>> injectableClasses) {
		localLazy();
		localBag.tryCreateFromClasses(injectableClasses);
	}

	// AppService Proxy / Injectable Factory
	/**
	 * Creates an empty InjectableBag.
	 * @return						The InjectableBag
	 */
	@SuppressWarnings("unused")
	public InjectableBag createBag() {
		return _appService.createBag();
	}

	/**
	 * Creates an InjectableBag with one element, specified by the constant.
	 * @param injectableConstant	The Injectable constant
	 * @return						The InjectableBag
	 * @throws OperationException	if the constant is not valid
	 */
	@SuppressWarnings("unused")
	public InjectableBag createBagFromConstant(Enum<?> injectableConstant) {
		return _appService.createBagFromConstant(injectableConstant);
	}

	/**
	 * Creates an InjectableBag with one element, specified by the class.
	 * @param injectableClass		The Injectable class
	 * @return						The InjectableBag
	 */
	@SuppressWarnings("unused")
	public InjectableBag createBagFromClass(Class<? extends Injectable> injectableClass) {
		return _appService.createBagFromClass(injectableClass);
	}

	/**
	 * Creates an InjectableBag with many elements, specified by constants.
	 * @param injectableConstants	The array of Injectable constants
	 * @return						The InjectableBag
	 * @throws OperationException	if any of the constants are not valid
	 */
	@SuppressWarnings("unused")
	public InjectableBag createBagFromConstants(Iterable<Enum<?>> injectableConstants) {
		return _appService.createBagFromConstants(injectableConstants);
	}

	/**
	 * Creates an InjectableBag with many elements, specified by classes.
	 * @param injectableClasses		The array of Injectable classes
	 * @return	The InjectableBag	The InjectableBag
	 */
	@SuppressWarnings("unused")
	public InjectableBag createBagFromClasses(Iterable<Class<? extends Injectable>> injectableClasses) {
		return _appService.createBagFromClasses(injectableClasses);
	}


	// Resolution / Inject
	/**
	 * Sources an array of Injectables by constant.
	 * @param injectableConstantsArray		The array of constants
	 * @return								The list of Injectables
	 */
	@SuppressWarnings("unused")
	public List<Injectable> inject(Enum<?>[] injectableConstantsArray) {
		return injectConstants(List.of(injectableConstantsArray));
	}

	/**
	 * Sources an array of Injectables by class.
	 * @param injectableClassArray			The array of classes
	 * @return								The list of Injectables
	 */
	@SuppressWarnings("unused")
	public List<Injectable> inject(Class<? extends Injectable>[] injectableClassArray) {
		return injectClasses(List.of(injectableClassArray));
	}

	/**
	 * Sources an iterable of Injectables by constant.
	 * @param injectableConstantIterable	The iterable of constants
	 * @return								The list of Injectables
	 */
	@SuppressWarnings("unused")
	public List<Injectable> injectConstants(Iterable<Enum<?>> injectableConstantIterable) {
		return injectClasses(_appService.constItToClassItInj(injectableConstantIterable));
	}

	/**
	 * Sources an iterable of Injectables by classes.
	 * @param injectableClassIterable		The iterable of classes
	 * @return								The list of Injectables
	 */
	@SuppressWarnings("unused")
	public List<Injectable> injectClasses(Iterable<Class<? extends Injectable>> injectableClassIterable) {
		ArrayList<Injectable> injectables = new ArrayList<>();
		for (Class<? extends Injectable> injectableClass : injectableClassIterable)
			injectables.add(inject(injectableClass));
		return injectables;
	}

	// ** PACKAGE-PRIVATE
	// Resolution
	Injectable sendInjectToParent(Class<? extends Injectable> injectableClass) {
		if (parent == null)
			throw new ResolutionTermination("app:resolution:injection: Injection request failed at " + this.getClass().getSimpleName());
		return parent.injectR(injectableClass);
	}

	void sendStateToParent(Class<? extends Node> state, InjectableBag bag) {
		if (parent == null)
			throw new ResolutionTermination("app:resolution:state: Change in state failed at " + this.getClass().getSimpleName());
		parent.setStateR(state, bag);
	}

	// Local Bag
	void localLazy() {
		if (localBag == null)
			localBag = new InjectableBag(_appService);
	}
}
