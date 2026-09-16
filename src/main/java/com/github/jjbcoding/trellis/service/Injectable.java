package com.github.jjbcoding.trellis.service;

import com.github.jjbcoding.trellis.service.annotations.Supplies;
import com.github.jjbcoding.trellis.exceptions.OperationException;
import com.github.jjbcoding.trellis.exceptions.ResolutionFailure;
import com.github.jjbcoding.trellis.exceptions.ResolutionTermination;

/**
 * An Injectable is an object that is hosted by a Node; its lifetime is scoped
 * to the Node it is hosted by.
 * Injectables can source other Injectables, even if they are hosted on the same node.
 * Inject requests must be carried out within their {@link Injectable#initialise()} call,
 * not their constructor, as requested injectables may not have been constructed yet.
 * Injectables can be created and put in an InjectableBag as part of a change of state; they
 * are subsequently absorbed by Nodes that Expect them. This allows state to be transferred
 * from one Node tree configuration to another.
 * Injectables, like Nodes, are instantiated by the framework, not directly.
 */
public abstract class Injectable extends Base {
	// ----- CONTRACTUAL
	/**
	 * To override. For post-constructor initialisation.
	 * This may be used for injectables that require other injectables.
	 */
	public void initialise() {}

	// ----- DYNAMIC
	// *** CONSTRUCTORS
	/**
	 * Constructs an Injectable instance.
	 * @param _appService	The app service
	 * @param parent		The parent
	 */
    protected Injectable(AppService _appService, Node parent) {
		super(_appService, parent);
	}

	// *** METHODS
	// ** PACKAGE-PRIVATE
	void assignParentLate(Node parent) {
		this.parent = parent;
	}

	// ----- INTERFACE IMPLEMENTATIONS
	// * [ Base ]
	// Resolution / State
	/**
	 * Sets the state, or leaf Node, by class.
	 * Will use the local Injectable Bag, if configured.
	 * @param state					The class to navigate to
	 * @throws ResolutionFailure 	if the state cannot be reached
	 */
	@Override
	@SuppressWarnings("unused")
	public void setState(Class<? extends Node> state) {
		setState(state, localBag);
	}

	/**
	 * Sets the state, or leaf Node, by class.
	 * Will ignore the local Injectable Bag, if it exists.
	 * @param state					The class to navigate to
	 * @param bag					The Injectable Bag to supply
	 * @throws ResolutionFailure 	if the state cannot be reached
	 */
	@Override
	@SuppressWarnings("unused")
	public void setState(Class<? extends Node> state, InjectableBag bag) {
		// Detach local bag
		if (localBag == bag)
			localBag = null;

		try {
			setStateR(state, bag);
		}
		catch (ResolutionTermination e) {
			throw new ResolutionFailure("app:resolution:state: Change in state failed from Injectable " + this.getClass().getSimpleName(), e);
		}
	}

	/**
	 * Sets the state, or leaf Node, by constant.
	 * Will use the local Injectable Bag, if configured.
	 * @param stateConstant			The constant to navigate to
	 * @throws OperationException   if the constant is invalid
	 * @throws ResolutionFailure 	if the state cannot be reached
	 */
	@Override
	@SuppressWarnings("unused")
	public void setState(Enum<?> stateConstant) {
		setState(_appService.constToClassNode(stateConstant), localBag);
	}

	/**
	 * Sets the state, or leaf Node, by constant.
	 * Will ignore the local Injectable Bag, if it exists.
	 * @param stateConstant			The constant to navigate to
	 * @param bag					The Injectable Bag to supply
	 * @throws OperationException 	if the constant is invalid
	 * @throws ResolutionFailure 	if the state cannot be reached
	 */
	@Override
	@SuppressWarnings("unused")
	public void setState(Enum<?> stateConstant, InjectableBag bag) {
		setState(_appService.constToClassNode(stateConstant), bag);
	}

	/*
	PRIVATE-LOCAL
	 */
	void setStateR(Class<? extends Node> state, InjectableBag bag) {
		sendStateToParent(state, bag);
	}


	// Resolution / Inject
	/**
	 * Sources an Injectable by constant, from an ancestor that Supplies it.
	 * @param injectableConstant	The constant
	 * @return						The Injectable
	 * @throws OperationException 	if the constant is invalid
	 * @throws ResolutionFailure 	if the Injectable is not supplied by any ancestor
	 * @see    Supplies
	 */
	@Override
	@SuppressWarnings("unused")
	public Injectable inject(Enum<?> injectableConstant) {
		return inject(_appService.constToClassInj(injectableConstant));
	}

	/**
	 * Sources an Injectable by constant, from an ancestor that Supplies it.
	 * @param injectableClass		The class
	 * @return						The Injectable
	 * @throws ResolutionFailure 	if the Injectable is not supplied by any ancestor
	 * @see    Supplies
	 */
	@Override
	@SuppressWarnings("unused")
	public Injectable inject(Class<? extends Injectable> injectableClass) {
		try {
			return injectR(injectableClass);
		}
		catch (ResolutionTermination e) {
			throw new ResolutionFailure("app:resolution:inject: Injection request failed from Injectable " + this.getClass().getSimpleName(), e);
		}
	}

	/*
	PRIVATE-LOCAL
	 */
	@Override
	Injectable injectR(Class<? extends Injectable> injectableClass) {
		return sendInjectToParent(injectableClass);
	}
}
