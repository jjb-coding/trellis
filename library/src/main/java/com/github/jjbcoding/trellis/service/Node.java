package com.github.jjbcoding.trellis.service;

import com.github.jjbcoding.trellis.service.annotations.Supplies;
import com.github.jjbcoding.trellis.service.displays.IDisplay;
import com.github.jjbcoding.trellis.exceptions.*;
import com.github.jjbcoding.trellis.service.contributions.Contribution;
import com.github.jjbcoding.trellis.service.disposal.object.DisposalObject;
import com.github.jjbcoding.trellis.service.disposal.object.internal.DisposalObjectInternal;
import com.github.jjbcoding.trellis.service.disposal.object.internal.DisposalObjectProxy;
import com.github.jjbcoding.trellis.service.requests.Request;

import java.util.*;

/**
 * A Node is the scaffolding on which the application is built. Each node
 * hosts a partnered {@link IDisplay} element, and any number of Injectables.
 * If the node is discarded, its descendents and Injectables are also discarded, and
 * the change to the Node tree is reconciled with the {@link IDisplay} tree.
 * Any Node can request an Injectable, which will be sourced rom the nearest ancestor
 * that hosts it.
 * Nodes, like Injectables, are instantiated by the framework, not directly.
 */
public abstract class Node extends Base {
	// ----- CONTRACTUAL
	// *** USER-OVERRIDDEN
	/**
	 * To override. Supplies the contribution of this node.
	 * @return			The contribution.
	 */
	protected Contribution getContribution() {
		return null;
	}

	/**
	 * To override. Catches the contribution digest.
	 * @param digest	The digest.
	 */
	@SuppressWarnings("unused")
	protected void catchDigest(Digest digest) {}

	/**
	 * To override. Catches a request.
	 * @param request	The request.
	 * @return			Whether the request has been satisfied. If requestMustBeSatisfied is
	 * 					true, an error will be thrown if the request is not satisfied by the
	 * 					tree.
	 */
	@SuppressWarnings("unused")
	protected boolean catchRequest(Request request) {
		return false;
	}

	// *** LIBRARY-OVERRIDDEN
	// Internal Query
	List<Node> livingNodesToList() {
		List<Node> list = new ArrayList<>();
		list.add(child);
		return list;
	}

	List<Injectable> injectablesToList() {
		return new ArrayList<>(injectables.values());
	}

	// Node Manipulation
	Node nextNode(Class<? extends Node> nodeClass, InjectableBag bag) {
		return _appService.launchNode(this, nodeClass, bag);
	}

	void previousNode(Node node) {
		node.dispose();
	}

	// ----- DYNAMIC
	// *** FIELDS
	Node child;
	HashMap<Class<? extends Injectable>, Injectable> injectables;
	IDisplay display;

	// *** CONSTRUCTORS	
	/**
	 * Constructs a Node instance.
	 * @param _appService	The app service
	 * @param parent		The parent
	 * @param bag			The Injectable bag
	 */
	protected Node(AppService _appService, Node parent, InjectableBag bag) {
		// Super
		super(_appService, parent);
		
		// Injectables
		injectables = new HashMap<>();
		NodeConfiguration configuration = _appService.objToConfig(this);

		// Pull everything expected from InjectableBag
		if (configuration.expectsAnything() && bag != null) {
			HashSet<Class<? extends Injectable>> expectsSet = configuration.expects();
			Iterator<Map.Entry<Class<? extends Injectable>,Injectable>> it = bag.map.entrySet().iterator();
			while (it.hasNext()) {
				Injectable injectable = it.next().getValue();
				if (!expectsSet.contains(injectable.getClass()))
					continue;

				it.remove();
				injectable.assignParentLate(this);
				injectables.put(injectable.getClass(), injectable);
			}
			if (!expectsSet.isEmpty())
				throw new ResolutionTermination("app:resolution:state: Expected more Injectables in bag from " + this.getClass().getSimpleName());
		}
		
		// Launch everything provided locally
		if (configuration.providesAnything()) {
			for (Class<? extends Injectable> injectableClass : configuration.provides())
				injectables.put(injectableClass, _appService.launchInjectable(this, injectableClass));
		}
		
		// Initialise all injectables to allow them to interact
		for (Injectable injectable : injectables.values())
			try {
				injectable.initialise();
			}
			catch (Exception e) {
				throw new ImplementerException("Injectable::initialise for " + injectable.getClass().getSimpleName(), e);
			}
	}
	
	// *** METHODS
	// ** PUBLIC
	// Display
	/**
	 * Gets the display object.
	 * @return	The object implementing IDisplay
	 */
	@SuppressWarnings("unused")
	public IDisplay getDisplay() {
		return display;
	}

	/**
	 * Attaches a display object. If null, clears the attachment.
	 */
	@SuppressWarnings("unused")
	public void setDisplay(IDisplay display) {
		this.display = display;
	}

	/**
	 * Determines whether there is an attached display object.
	 * @return	Whether there is an attached display object
	 */
	@SuppressWarnings("unused")
	public boolean hasDisplay() {
		return display != null;
	}

	// Child
	/**
	 * Gets the child Node object. Returns null if this Node
	 * is a leaf.
	 * @return	The child Node
	 */
	@SuppressWarnings("unused")
	public Node getChildNode() {
		return child;
	}

	// Resolution / Contribution
	/**
	 * To be called whenever the Contribution in this Node changes
	 * in a manner that would affect the output of the configured
	 * DigestProcessor.
	 * @throws ResolutionTermination	if any DigestComponents were not consumed and digestMustBeExhausted is true
	 */
	@SuppressWarnings("unused")
	public void contributionChanged() {
		_appService.contributionsChanged();
	}

	// Resolution / Request
	/**
	 * Sends a request from this Node to leaf.
	 * @param request						The request instance
	 * @throws ResolutionFailure 			if the request is not caught, and requestMustBeSatisfied is true
	 */
	@SuppressWarnings("unused")
	public void request(Request request) {
		try {
			sendRequestR(request);
		}
		catch (ResolutionTermination e) {
			throw new ResolutionFailure("app:resolution:request: Request failed from Node " + this.getClass().getSimpleName(), e);
		}
	}

	// Navigation
	/**
	 * Gets the leaf.
	 * @return	The leaf Node
	 */
	@SuppressWarnings("unused")
	public Node getLeaf() {
		if (child != null)
			return child.getLeaf();
		else
			return this;
	}

	/**
	 * Gets the root.
	 * @return	The root Node
	 */
	@SuppressWarnings("unused")
	public Node getRoot() {
		return _appService.root;
	}

	/**
	 * Determines whether this is the leaf.
	 * @return	Whether this is the leaf
	 */
	@SuppressWarnings("unused")
	public boolean isLeaf() {
		return child == null;
	}

	/**
	 * Determines whether this is active.
	 * @return	Whether this is active
	 */
	@SuppressWarnings("unused")
	public boolean isActive() {
		return _appService.getAllActiveNodes().contains(this);
	}

	/**
	 * Determines whether the connected AppService has been closed.
	 * If this is the case, the Node is also closed, and no operations
	 * should be performed on it.
	 * @return	Whether this is active
	 */
	@SuppressWarnings("unused")
	public boolean isRunning() {
		return _appService.isRunning();
	}

	// ** PACKAGE-PRIVATE
	// Query
	void findActiveNodes(List<Node> nodes) {
		nodes.add(this);
		if (child != null)
			child.findActiveNodes(nodes);
	}

	void findInstantiatedNodes(List<Node> nodes) {
		nodes.add(this);
		List<Node> children = livingNodesToList();
		if (children == null)
			return;
		for (Node child : children)
			child.findInstantiatedNodes(nodes);
	}

	// Resolution / Contribution
	void compileContributionsFromRoot(Digest digest) {
		Contribution contribution;
		try {
			contribution = getContribution();
		}
		catch (Exception e) {
			throw new ResolutionTermination(
					"app:resolution:contribution: Method threw an exception",
					"Node::getContribution at " + this.getClass().getSimpleName(),
					e);
		}

		if (contribution != null)
			digest.addContribution(getContribution());

		if (child != null)
			child.compileContributionsFromRoot(digest);
	}

	void sendDigestFromRoot(Digest digest) {
		try {
			catchDigest(digest);
		}
		catch (Exception e) {
			throw new ResolutionTermination(
					"app:resolution:contribution: Method threw an exception",
					"Node::catchDigest at " + this.getClass().getSimpleName(),
					e);
		}

		if (!digest.isEmpty()) {
			if (child != null)
				child.sendDigestFromRoot(digest);
			else if (_appService.digestMustBeExhausted)
				throw new ResolutionTermination("app:resolution:digest: Digest was not exhausted, from Node " + this.getClass().getSimpleName());
		}
	}

	// Resolution / Request
	void sendRequestR(Request request) {
		if (child != null)
			child.sendRequestR(request);
		else {
			boolean satisfied;
			try {
				satisfied = catchRequest(request);
			}
			catch (Exception e) {
				throw new ResolutionTermination(
						"app:resolution:request: Threw an exception",
						"Node::catchRequest at " + this.getClass().getSimpleName(),
						e);
			}
			if (!satisfied && _appService.requestMustBeSatisfied)
				throw new ResolutionTermination("app:resolution:request: Request failed at Node " + this.getClass().getSimpleName());
		}
	}

	// Resolution / Disposal
	void dispose() {
		if (_appService.disposalProcessor == null)
			return;

		DisposalObject disposalObject = new DisposalObject();
		disposeR(disposalObject);

		_appService.processDisposal(disposalObject);
	}

	void disposeR(DisposalObjectInternal disposalObject) {
		List<Node> children = livingNodesToList();
		DisposalObjectProxy.register(disposalObject, this, children, injectablesToList());
		for (Node child : children)
			child.disposeR(disposalObject);
	}

	// Reorganization
	void swapChild(Class<? extends Node> nextNodeClass, InjectableBag bag) {
		Node next = nextNode(nextNodeClass, bag);
		previousNode(child);
		child = next;
		if (display != null)
			display.swapChild(child.display);
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
		if (localBag == bag)
			localBag = null;

		try {
			setStateR(state, bag);
		}
		catch (ResolutionTermination e) {
			throw new ResolutionFailure("app:resolution:state: Change in state failed from Node " + this.getClass().getSimpleName(), e);
		}
	}

	/**
	 * Sets the state, or leaf Node, by constant.
	 * Will use the local Injectable Bag, if configured.
	 * @param stateConstant			The constant to navigate to
	 * @throws OperationException 	if the constant is invalid
	 * @throws ResolutionFailure 	if the state cannot be reached
	 */
	@Override
	@SuppressWarnings("unused")
	public void setState(Enum<?> stateConstant) {
		setState(stateConstant, localBag);
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
		Class<? extends Node> target = _appService.constToClassNode(stateConstant);
		setState(target, bag);
	}

	@Override
	@SuppressWarnings("unused")
	void setStateR(Class<? extends Node> state, InjectableBag bag) {
		// Decide
		Class<? extends Node> nodeClass = _appService.decide(this, state);

		// BACK
		if (nodeClass == null) {
			sendStateToParent(state, bag);
		}
		// STOP
		else if (nodeClass == this.getClass()) {
			contributionChanged();
		}
		// LAUNCH & CONTINUE
		else {
			swapChild(nodeClass, bag);
			child.setState(state, bag);
		}
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
		Class<? extends Injectable> injectableClass = _appService.constToClassInj(injectableConstant);
		return inject(injectableClass);
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
			throw new ResolutionFailure("app:resolution:inject: Injection request failed from Node " + this.getClass().getSimpleName(), e);
		}
	}

	/*
	PRIVATE-LOCAL
	 */
	@Override
	Injectable injectR(Class<? extends Injectable> injectableClass) {
		Injectable injectable = injectables.get(injectableClass);
		if (injectable != null)
			return injectable;

		return sendInjectToParent(injectableClass);
	}
}
