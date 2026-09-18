package com.github.jjbcoding.trellis.service;

import com.github.jjbcoding.trellis.exceptions.*;
import com.github.jjbcoding.trellis.service.disposal.object.DisposalObject;
import com.github.jjbcoding.trellis.service.disposal.object.internal.DisposalObjectProxy;
import com.github.jjbcoding.trellis.service.disposal.processor.IDisposalProcessor;
import com.github.jjbcoding.trellis.service.contributions.ContributionAggregator;
import com.github.jjbcoding.trellis.service.contributions.DigestComponent;
import com.github.jjbcoding.trellis.service.providers.*;
import com.github.jjbcoding.trellis.service.providers.internal.ClassProviderProxy;
import com.github.jjbcoding.trellis.service.readers.EnumReaderPair;
import com.github.jjbcoding.trellis.service.readers.MapPair;
import com.github.jjbcoding.trellis.service.requests.Request;
import com.github.jjbcoding.trellis.service.rules.IRule;

import java.util.*;

/**
 * The AppService facilitates Requests, queries of the Node tree,
 * and start and closure. It is intended to be hosted by any dependency
 * injection framework as a singleton.
 * It must be configured through use of the
 * {@link AppService#configure(AppServiceConfigurationBuilder)} method
 * before any methods can be called. Some methods cannot be called
 * unless there is a running Node tree, which can be launched by a
 * start method and closed by the close method.
 * @see AppServiceConfigurationBuilder
 */
public class AppService {
	// *** FIELDS
	// Maps
	HashMap<Class<? extends Node>,NodeConfiguration> classToNodeConfigurationMap;
	HashMap<Class<? extends Injectable>,InjectableConfiguration> classToInjectableConfigurationMap;

	// Configuration
	boolean configured;

	// Configuration / Providers
	ClassProvider<Node> nodesClassProvider;
	// Configuration / Rules
	IRule<Node> nodesRule;
	IRule<Injectable> injectablesRule;
	// Startup / Configuration
	NodeLocator startupRoot, startupState;
	InjectableBag startupBag;
	// Startup / State
	Node root;
	// Reader / Injectable
	Class<? extends Enum<?>> injectableEnum;
	Map<Enum<?>,Class<? extends Injectable>> injectableEnumToInjectableMap;
	Map<Class<? extends Injectable>,Enum<?>> injectableToInjectableEnumMap;
	// Reader / State
	Class<? extends Enum<?>> stateEnum;
	Map<Enum<?>,Class<? extends Node>> stateEnumToNodeMap;
	Map<Class<? extends Node>,Enum<?>> nodeToStateEnumMap;
	// Reader / Digest
	Class<? extends Enum<?>> digestComponentEnum;
	Map<Enum<?>,Class<? extends DigestComponent>> digestComponentEnumToDigestComponentMap;
	Map<Class<? extends DigestComponent>,Enum<?>> digestComponentToDigestComponentEnumMap;
	// Contributions
	ContributionAggregator aggregator;
	// Disposal
	IDisposalProcessor disposalProcessor;
	// Flags
	boolean digestMustBeExhausted, requestMustBeSatisfied, injectableBagMustBeExhausted;

	// *** CONSTRUCTORS
	/**
	 * Constructs an AppService instance.
	 */
	public AppService() {
		// * Initialise
		nodesClassProvider = null;
		// Configuration
		configured = false;
	}

	// *** METHODS
	// ** PUBLIC
	// Build
	/**
	 * Builds the service.
	 * @param configuration			The configuration object
	 * @throws BuilderException		if the builder is misconfigured
	 * @throws ImplementerException	if any {@link com.github.jjbcoding.trellis.service.readers.IReader#getAssociation(Enum)} invocation throws an exception
	 */
	@SuppressWarnings("unused")
	public void configure(AppServiceConfigurationBuilder configuration) {
		// * Configuration
		// Finalise
		configuration.finalise();

		// * Clone
		// Provider
		nodesClassProvider = configuration.nodesClassProvider;
		// Rules
		nodesRule = configuration.nodesRule;
		injectablesRule = configuration.injectablesRule;
		// Startup
		startupRoot = configuration.startupRoot;
		startupState = configuration.startupState;
		// Contributions
		aggregator = configuration.aggregator;
		// Disposal
		disposalProcessor = configuration.disposalProcessor;
		// Flags
		digestMustBeExhausted = configuration.digestMustBeExhausted;
		requestMustBeSatisfied = configuration.requestMustBeSatisfied;
		injectableBagMustBeExhausted = configuration.injectableBagMustBeExhausted;

		// * Extract Enums
		// Injectables
		{
			EnumReaderPair<Injectable,? extends Enum<?>> injectableReaderPair = configuration.injectablesReaderPair;
			if (injectableReaderPair != null) {
				MapPair<Injectable> pair = injectableReaderPair.toMap();
				injectableEnumToInjectableMap = pair.getEnumToClass();
				injectableToInjectableEnumMap = pair.getClassToEnum();

				injectableEnum = injectableReaderPair.getEnumClass();
			}
		}

		// States
		{
			EnumReaderPair<Node,? extends Enum<?>> stateReaderPair = configuration.stateReaderPair;
			if (stateReaderPair != null) {
				MapPair<Node> pair = stateReaderPair.toMap();
				stateEnumToNodeMap = pair.getEnumToClass();
				nodeToStateEnumMap = pair.getClassToEnum();

				stateEnum = stateReaderPair.getEnumClass();
			}
		}

		// Digest
		{
			EnumReaderPair<DigestComponent,? extends Enum<?>> digestComponentReaderPair = configuration.digestComponentReaderPair;
			if (digestComponentReaderPair != null) {
				MapPair<DigestComponent> pair = digestComponentReaderPair.toMap();
				digestComponentEnumToDigestComponentMap = pair.getEnumToClass();
				digestComponentToDigestComponentEnumMap = pair.getClassToEnum();

				digestComponentEnum = digestComponentReaderPair.getEnumClass();
			}
		}

		// * Build
		// Pull from Node provider
		List<Class<? extends Node>> nodesProvidedClasses = ClassProviderProxy.getClasses(nodesClassProvider);

		// Initialise registries & sets
		classToNodeConfigurationMap = new HashMap<>();
		HashSet<NodeConfiguration> nodeConfigurations = new HashSet<>();
		HashSet<NodeConfiguration> nodeConfigurationLeaves = new HashSet<>();
		HashSet<Class<? extends Node>> nodesClasses = new HashSet<>(nodesProvidedClasses);

		classToInjectableConfigurationMap = new HashMap<>();
		HashSet<Class<? extends Injectable>> injectableClasses = new HashSet<>();

		// Visit nodes
		while (!nodesClasses.isEmpty()) {
			// * Loop
			// Get current
			Class<? extends Node> nodeClass = nodesClasses.iterator().next();
			nodesClasses.remove(nodeClass);

			// Skip if already mapped
			if (classToNodeConfigurationMap.containsKey(nodeClass))
				continue;

			// * Validation & Build
			// VALIDATE: Is of type Node?
			if (!Node.class.isAssignableFrom(nodeClass))
				throw new RuntimeException("APP:init[" + nodeClass.getSimpleName() + "]: Is not of type Node.");

			// Build configuration
			NodeConfiguration nodeConfiguration = new NodeConfiguration(nodeClass);

			// * Register
			// Discover linked node classes
			if (nodeConfiguration.parentClass != null)
				nodesClasses.add(nodeConfiguration.parentClass);

			// Insert into map & sets
			classToNodeConfigurationMap.put(nodeClass, nodeConfiguration);
			nodeConfigurations.add(nodeConfiguration);
		}

		// Link parents to children
		for (NodeConfiguration nodeConfiguration : nodeConfigurations) {
			NodeConfiguration parent = classToNodeConfigurationMap.get(nodeConfiguration.parentClass);
			if (parent != null)
				parent.childrenConfigurations.add(nodeConfiguration);
		}

		// Identify leaves
		for (NodeConfiguration nodeConfiguration : nodeConfigurations)
			if (nodeConfiguration.isLeaf())
				nodeConfigurationLeaves.add(nodeConfiguration);

		// For each leaf in node structure: fill routing
		for (NodeConfiguration nodeConfiguration : nodeConfigurationLeaves) {
			NodeConfiguration current = nodeConfiguration;
			while (current.parentClass != null) {
				// Fetch parent
				NodeConfiguration parent = classToNodeConfigurationMap.get(current.parentClass);
				if (parent == null)
					throw new RuntimeException("APP:init[" + current.parentClass.getSimpleName() + "]: Is not a scanned class.");

				// Push class into routing
				parent.descendantClasses.add(nodeConfiguration.thisCls);

				// Iterate
				current = parent;
			}
		}

		// Discover injectables from nodes - Expects & Provides
		for (NodeConfiguration nodeConfiguration : nodeConfigurations) {
			if (nodeConfiguration.providesAnything())
            	injectableClasses.addAll(nodeConfiguration.provides);
			if (nodeConfiguration.expectsAnything())
            	injectableClasses.addAll(nodeConfiguration.expects);
		}

		// Scan discovered injectables
		for (Class<? extends Injectable> injectableClass : injectableClasses) {
			classToInjectableConfigurationMap.put(injectableClass, new InjectableConfiguration(injectableClass));
		}

		// * Validation
		if (nodesRule != null && !(nodesRule.isValid(classToNodeConfigurationMap.keySet())))
			throw new BuilderException("");
		if (injectablesRule != null && !(injectablesRule.isValid(classToInjectableConfigurationMap.keySet())))
			throw new BuilderException("");

		// Finish
		configured = true;
	}

	// Factory / InjectableBag
	/**
	 * Creates an empty InjectableBag.
	 * @return						The InjectableBag
	 */
	@SuppressWarnings("unused")
	public InjectableBag createBag() {
		return new InjectableBag(this);
	}

	/**
	 * Creates an InjectableBag with one element, specified by the constant.
	 * @param injectableConstant	The Injectable constant
	 * @return						The InjectableBag
	 * @throws OperationException	if the constant is not valid
	 */
	@SuppressWarnings("unused")
	public InjectableBag createBagFromConstant(Enum<?> injectableConstant) {
		InjectableBag bag = new InjectableBag(this);
		bag.create(injectableConstant);
		return bag;
	}

	/**
	 * Creates an InjectableBag with one element, specified by the class.
	 * @param injectableClass		The Injectable class
	 * @return						The InjectableBag
	 */
	@SuppressWarnings("unused")
	public InjectableBag createBagFromClass(Class<? extends Injectable> injectableClass) {
		InjectableBag bag = new InjectableBag(this);
		bag.create(injectableClass);
		return bag;
	}

	/**
	 * Creates an InjectableBag with many elements, specified by constants.
	 * @param injectableConstants	The array of Injectable constants
	 * @return						The InjectableBag
	 * @throws OperationException	if any of the constants are not valid
	 */
	@SuppressWarnings("unused")
	public InjectableBag createBagFromConstants(Enum<?>[] injectableConstants) {
		InjectableBag bag = new InjectableBag(this);
		bag.create(injectableConstants);
		return bag;
	}

	/**
	 * Creates an InjectableBag with many elements, specified by classes.
	 * @param injectableClasses		The array of Injectable classes
	 * @return	The InjectableBag	The InjectableBag
	 */
	@SuppressWarnings("unused")
	public InjectableBag createBagFromClasses(Class<? extends Injectable>[] injectableClasses) {
		InjectableBag bag = new InjectableBag(this);
		bag.create(injectableClasses);
		return bag;
	}

	/**
	 * Creates an InjectableBag with many elements, specified by constants.
	 * @param injectableConstants	The list of Injectable constants
	 * @return						The InjectableBag
	 * @throws OperationException	if any of the constants are not valid
	 */
	@SuppressWarnings("unused")
	public InjectableBag createBagFromConstants(Iterable<Enum<?>> injectableConstants) {
		InjectableBag bag = new InjectableBag(this);
		bag.createFromConstants(injectableConstants);
		return bag;
	}

	/**
	 * Creates an InjectableBag with many elements, specified by classes.
	 * @param injectableClasses		The list of Injectable classes
	 * @return	The InjectableBag	The InjectableBag
	 */
	@SuppressWarnings("unused")
	public InjectableBag createBagFromClasses(Iterable<Class<? extends Injectable>> injectableClasses) {
		InjectableBag bag = new InjectableBag(this);
		bag.createFromClasses(injectableClasses);
		return bag;
	}

	// Query
	/**
	 * Determines if the service is running; that is, if it has active
	 * nodes. Calling {@link AppService#start()} or any start method will
	 * make this true; calling {@link AppService#close()} will make this false.
	 * @return			Whether it is running
	 */
	@SuppressWarnings("unused")
	public boolean isRunning() {
		return root != null;
	}

	/**
	 * Determines if the service has been configured. Calling the {@link AppService#configure(AppServiceConfigurationBuilder)}
	 * method will make this true.
	 * @return			Whether it is configured
	 */
	@SuppressWarnings("unused")
	public boolean isConfigured() {
		return configured;
	}

	// Post-Configured / Start / Bag
	/**
	 * Attaches an InjectableBag to use by default when starting the
	 * service. If null, this clears any preexisting attachment.
	 * This can only be called after {@link AppService#configured}.
	 * @param bag		The InjectableBag
	 * @throws ServiceNotConfiguredException if the service has not yet been configured
	 */
	@SuppressWarnings("unused")
	public void attachStartupBag(InjectableBag bag) {
		checkConfigured();

		startupBag = bag;
	}

	// Post-Configuration / Start / Launch
	/**
	 * Launches the service.
	 * Will fail if a startup state has not been configured.
	 * If a startup Bag has been attached, it will be used.
	 * This can only be called after {@link AppService#configured}.
	 * @return		The root node
	 * @throws ServiceNotConfiguredException	if the service has not yet been configured
	 * @throws OperationException				if already started
	 */
	@SuppressWarnings("unused")
	public Node start() {
		return start(startupBag);
	}

	/**
	 * Launches the service.
	 * If a startup InjectableBag has been attached, it will be used.
	 * This can only be called after {@link AppService#configured}.
	 * @param stateConstant		The starting state, by constant
	 * @return					The root node
	 * @throws ServiceNotConfiguredException	if the service has not yet been configured
	 * @throws OperationException				if already started
	 */
	@SuppressWarnings("unused")
	public Node start(Enum<?> stateConstant) {
		return start(stateConstant, startupBag);
	}

	/**
	 * Launches the service.
	 * If a startup InjectableBag has been attached, it will be used.
	 * This can only be called after {@link AppService#configured}.
	 * @param stateClass		The starting state, by class
	 * @return					The root node
	 * @throws ServiceNotConfiguredException	if the service has not yet been configured
	 * @throws OperationException				if already started
	 */
	@SuppressWarnings("unused")
	public Node start(Class<? extends Node> stateClass) {
		return start(stateClass, startupBag);
	}

	/**
	 * Launches the service.
	 * Will fail if a startup state has not been configured.
	 * Will ignore any startup InjectableBag that has been attached.
	 * This can only be called after {@link AppService#configured}.
	 * @param bag				The InjectableBag to use
	 * @return					The root node
	 * @throws ServiceNotConfiguredException	if the service has not yet been configured
	 * @throws OperationException				if already started
	 */
	@SuppressWarnings("unused")
	public Node start(InjectableBag bag) {
		if (startupState == null)
			throw new OperationException("app:start: start was invoked without a state; no default state was configured.");
		return start(nodeDescriptionToClass(startupState), bag);
	}

	/**
	 * Launches the service.
	 * Will ignore any startup InjectableBag that has been attached.
	 * This can only be called after {@link AppService#configured}.
	 * @param stateConstant		The starting state, by constant
	 * @param bag				The InjectableBag to use
	 * @return					The root node
	 * @throws ServiceNotConfiguredException	if the service has not yet been configured
	 * @throws OperationException				if already started
	 */
	@SuppressWarnings("unused")
	public Node start(Enum<?> stateConstant, InjectableBag bag) {
		return start(constToClassNode(stateConstant), bag);
	}

	/**
	 * Launches the service.
	 * Will ignore any startup InjectableBag that has been attached.
	 * This can only be called after {@link AppService#configured}.
	 * @param stateClass		The starting state, by class
	 * @param bag				The InjectableBag to use
	 * @return					The root node
	 * @throws ServiceNotConfiguredException	if the service has not yet been configured
	 * @throws OperationException				if already started
	 */
	@SuppressWarnings("unused")
	public Node start(Class<? extends Node> stateClass, InjectableBag bag) {
		checkConfigured();
		if (isRunning())
			throw new OperationException("app:service: Start was called, but Nodes are already launched");

		Class<? extends Node> startupRootClass = nodeDescriptionToClass(startupRoot);

		root = launchNode(null, startupRootClass, bag);
		root.setState(stateClass);
		return root;
	}

	// When Running / Close
	/**
	 * Closes all Nodes using the configured or default DisposalProcessor.
	 * @throws ResolutionFailure    		if any close invocations on any Node or Injectable throw an exception,
	 * 										or the DisposalProcessor threw an exception
	 * @throws ServiceNotRunningException 	if the service is not running
	 * @see IDisposalProcessor
	 */
	@SuppressWarnings("unused")
	public void close() {
		checkRunning();

		try {
			root.dispose();
		}
		catch (ResolutionTermination e) {
			throw new ResolutionFailure("app:close: Disposal failed", e);
		}
		root = null;
	}

	/**
	 * Closes all Nodes using the configured or default DisposalProcessor, if running.
	 * @throws ResolutionTermination 		if any close invocations on any Node or Injectable throw an error
	 */
	@SuppressWarnings("unused")
	public void tryClose() {
		if (!isRunning())
			return;

		root.dispose();
		root = null;
	}

	// When Running / Request
	/**
	 * Sends a request from root to leaf.
	 * This can only be called after {@link AppService#configured}.
	 * @param request						The request instance
	 * @throws ResolutionFailure 			if the request is not caught, and requestMustBeSatisfied is true
	 * @throws ServiceNotRunningException 	if the service is not running
	 */
	@SuppressWarnings("unused")
	public void request(Request request) {
		checkRunning();

		try {
			root.sendRequestR(request);
		}
		catch (ResolutionTermination e) {
			throw new ResolutionFailure("app:resolution:request: Request failed from AppService", e);
		}
	}

	/**
	 * Sends a request from root to leaf, if running.
	 * This can only be called after {@link AppService#configured}.
	 * @param request						The request instance
	 * @throws ResolutionFailure 			if the request is not caught, and requestMustBeSatisfied is true
	 */
	@SuppressWarnings("unused")
	public void tryRequest(Request request) {
		if (!isRunning())
			return;

		try {
			root.sendRequestR(request);
		}
		catch (ResolutionTermination e) {
			throw new ResolutionFailure("app:resolution:request: Request failed from AppService", e);
		}
	}

	// When Running / Contributions
	/**
	 * Forces a recompute of contributions.
	 * @throws ServiceNotRunningException 	if the service is not running
	 */
	@SuppressWarnings("unused")
	public void forceUpdateContributions() {
		checkRunning();

		contributionsChanged();
	}

	/**
	 * Forces a recompute of contributions, if running.
	 */
	@SuppressWarnings("unused")
	public void tryForceUpdateContributions() {
		if (!isRunning())
			return;

		contributionsChanged();
	}

	// When Running / Node / Get Leaf
	/**
	 * Returns the leaf node.
	 * @return								The Node
	 * @throws ServiceNotRunningException 	if the service is not running
	 */
	@SuppressWarnings("unused")
	public Node getLeaf() {
		checkRunning();

		return root;
	}

	/**
	 * Returns the leaf node, or null if not running.
	 * @return								The Node
	 */
	@SuppressWarnings("unused")
	public Node tryGetLeaf() {
		if (!isRunning())
			return null;

		return root;
	}

	// When Running / Node / Get Root
	/**
	 * Returns the root node.
	 * @return								The Node
	 * @throws ServiceNotRunningException 	if the service is not running
	 */
	@SuppressWarnings("unused")
	public Node getRoot() {
		checkRunning();

		return root.getLeaf();
	}

	/**
	 * Returns the root node, or null if not running.
	 * @return								The Node
	 */
	@SuppressWarnings("unused")
	public Node tryGetRoot() {
		if (!isRunning())
			return null;

		return root.getLeaf();
	}

	// When Running / Node / Get State
	/**
	 * Returns the current state. Can return null if the current state
	 * is not in the state enum.
	 * @return								The state by constant
	 * @throws ServiceNotRunningException 	if the service is not running
	 * @throws OperationException			if no state reader is configured
	 */
	@SuppressWarnings("unused")
	public Enum<?> getState() {
		checkRunning();

		return toConstant(getLeaf());
	}

	/**
	 * Returns the current state. Will return null if the current state
	 * is not in the state enum, no state reader is configured, or the
	 * service is not running.
	 * @return								The state by constant
	 * @throws ServiceNotRunningException 	if the service is not running
	 * @throws OperationException			if no state reader is configured
	 */
	@SuppressWarnings("unused")
	public Enum<?> tryGetState() {
		if (!isRunning())
			return null;

		Enum<?> constant;
		try {
			constant = toConstant(getLeaf());
		}
		catch (OperationException e) {
			return null;
		}
		return constant;
	}

	// When Running / Node / Get All Active
	/**
	 * Returns all Nodes that are active.
	 * @return								The list of Nodes
	 * @throws ServiceNotRunningException 	if the service is not running
	 */
	@SuppressWarnings("unused")
	public List<Node> getAllActiveNodes() {
		checkRunning();

		List<Node> nodes = new ArrayList<>();
		root.findActiveNodes(nodes);
		return nodes;
	}

	/**
	 * Returns all Nodes that are active, if running, otherwise an empty list.
	 * @return								The list of Nodes
	 */
	@SuppressWarnings("unused")
	public List<Node> tryGetAllActiveNodes() {
		List<Node> nodes = new ArrayList<>();
		if (!isRunning())
			return nodes;

		root.findActiveNodes(nodes);
		return nodes;
	}

	// When Running / Node / Get All Instantiated
	/**
	 * Returns all Nodes that are instantiated. This differs from {@link AppService#getAllActiveNodes()}
	 * in that it includes the branches of MultiNodes, which are persisted even when not active.
	 * @return								The list of nodes
	 * @throws ServiceNotRunningException 	if the service is not running
	 */
	@SuppressWarnings("unused")
	public List<Node> getAllInstantiatedNodes() {
		checkRunning();

		List<Node> nodes = new ArrayList<>();
		root.findInstantiatedNodes(nodes);
		return nodes;
	}

	/**
	 * Returns all Nodes that are instantiated. This differs from {@link AppService#getAllActiveNodes()}
	 * in that it includes the branches of MultiNodes, which are persisted even when not active. If not
	 * running, returns an empty list.
	 * @return								The list of nodes
	 */
	@SuppressWarnings("unused")
	public List<Node> tryGetAllInstantiatedNodes() {
		List<Node> nodes = new ArrayList<>();
		if (!isRunning())
			return nodes;

		root.findInstantiatedNodes(nodes);
		return nodes;
	}

	// Inverse Constant Resolution / Node
	/**
	 * Finds the constant associated with a Node, else null.
	 * @param node								The Node
	 * @return									The constant; may be null
	 * @throws ServiceNotConfiguredException 	if the service has not been configured
	 * @throws OperationException 				if node is null, or the state reader has not been configured
	 */
	@SuppressWarnings("unused")
	public Enum<?> toConstant(Node node) {
		if (node == null)
			throw new OperationException("app:query: Provided Node was null");

		return nodeClassToConstant(node.getClass());
	}

	/**
	 * Finds the constant associated with a Node class, else null.
	 * @param nodeClass							The Node class
	 * @return									The constant; may be null
	 * @throws ServiceNotConfiguredException 	if the service has not been configured
	 * @throws OperationException 				if nodeClass is null, or the state reader has not been configured
	 */
	@SuppressWarnings("unused")
	public Enum<?> nodeClassToConstant(Class<? extends Node> nodeClass) {
		if (nodeClass == null)
			throw new OperationException("app:query: Provided Node class was null");
		checkConfigured();

		if (nodeToStateEnumMap == null)
			throw new OperationException("app:query: A state constant was requested, but no state reader is configured");

        return nodeToStateEnumMap.get(nodeClass);
	}

	// Inverse Constant Resolution / Injectable
	/**
	 * Finds the constant associated with an Injectable, else null.
	 * @param injectable						The Injectable
	 * @return									The constant; may be null
	 * @throws ServiceNotConfiguredException 	if the service has not been configured
	 * @throws OperationException 				if injectable is null, or the state reader has not been configured
	 */
	@SuppressWarnings("unused")
	public Enum<?> toConstant(Injectable injectable) {
		if (injectable == null)
			throw new OperationException("app:query: Provided Injectable was null");

		return injectableClassToConstant(injectable.getClass());
	}

	/**
	 * Finds the constant associated with an Injectable class, else null.
	 * @param injectableClass					The Injectable class
	 * @return									The constant; may be null
	 * @throws ServiceNotConfiguredException 	if the service has not been configured
	 * @throws OperationException 				if injectableClass is null, or the state reader has not been configured
	 */
	@SuppressWarnings("unused")
	public Enum<?> injectableClassToConstant(Class<? extends Injectable> injectableClass) {
		if (injectableClass == null)
			throw new OperationException("app:query: Provided Injectable class was null");
		checkConfigured();

		if (injectableToInjectableEnumMap == null)
			throw new OperationException("app:query: An Injectable constant was requested, but no Injectable reader is configured");

		return injectableToInjectableEnumMap.get(injectableClass);
	}

	// Inverse Constant Resolution / DigestComponent
	/**
	 * Finds the constant associated with a DigestComponent, else null.
	 * @param digestComponent					The DigestComponent
	 * @return									The constant; may be null
	 * @throws ServiceNotConfiguredException 	if the service has not been configured
	 * @throws OperationException 				if digestComponent is null, or the state reader has not been configured
	 */
	@SuppressWarnings("unused")
	public Enum<?> toConstant(DigestComponent digestComponent) {
		if (digestComponent == null)
			throw new OperationException("app:query: Provided DigestComponent was null");

		return this.digestComponentClassToConstant(digestComponent.getClass());
	}

	/**
	 * Finds the constant associated with a DigestComponent class, else null.
	 * @param digestComponentClass				The DigestComponent class
	 * @return									The constant; may be null
	 * @throws ServiceNotConfiguredException 	if the service has not been configured
	 * @throws OperationException 				if digestComponentClass is null, or the state reader has not been configured
	 */
	@SuppressWarnings("unused")
	public Enum<?> digestComponentClassToConstant(Class<? extends DigestComponent> digestComponentClass) {
		if (digestComponentClass == null)
			throw new OperationException("app:query: Provided DigestComponent class was null");
		checkConfigured();

		if (digestComponentToDigestComponentEnumMap == null)
			throw new OperationException("app:query: A DigestComponent constant was requested, but no DigestComponent reader is configured");

		return digestComponentToDigestComponentEnumMap.get(digestComponentClass);
	}

	// ** PACKAGE-PRIVATE
	// Constant & Configuration Resolution / Node
	Class<? extends Node> nodeDescriptionToClass(NodeLocator nodeDescription) {
		return nodeDescription.isConstant() ? constToClassNode(nodeDescription.nodeConstant) : nodeDescription.nodeClass;
	}

	NodeConfiguration clsToConfigNode(Class<? extends Node> nodeClass) {
		return classToNodeConfigurationMap.get(nodeClass);
	}

	NodeConfiguration objToConfig(Node node) {
		return clsToConfigNode(node.getClass());
	}

	Class<? extends Node> constToClassNode(Enum<?> constant) {
		if (stateEnum == null)
			throw new OperationException("app:enum: A state constant was used, but no state reader is configured");

		if (constant.getClass() != stateEnum)
			throw new OperationException("app:enum: A state constant was used, but of a different class than was configured");

		Class<? extends Node> nodeClass = stateEnumToNodeMap.get(constant);
		if (nodeClass == null)
			throw new OperationException("app:enum: A state constant was used from the configured class, but was not recognised");

		return nodeClass;
	}

	// Constant & Configuration Resolution / Injectable
	InjectableConfiguration clsToConfigInj(Class<? extends Injectable> injectableClass) {
		return classToInjectableConfigurationMap.get(injectableClass);
	}

	Class<? extends Injectable> constToClassInj(Enum<?> constant) {
		if (injectableEnum == null)
			throw new OperationException("app:enum: An Injectable constant was used, but no Injectable reader is configured.");

		if (constant.getClass() != injectableEnum)
			throw new OperationException("app:enum: An Injectable constant was used, but of a different class than was configured.");

		Class<? extends Injectable> injectableClass = injectableEnumToInjectableMap.get(constant);
		if (injectableClass == null)
			throw new OperationException("app:enum: An Injectable constant was used from the configured class, but was not recognised.");

		return injectableClass;
	}

	List<Class<? extends Injectable>> constItToClassItInj(Iterable<Enum<?>> constantList) {
		ArrayList<Class<? extends Injectable>> list = new ArrayList<>();
		for (Enum<?> constant : constantList)
			list.add(constToClassInj(constant));
		return list;
	}

	// Constant & Configuration Resolution / Digest Component
    Class<? extends DigestComponent> constToClassDigCom(Enum<?> constant) {
		if (digestComponentEnum == null)
			throw new OperationException("app:enum: A Digest Component constant was used, but no Digest Component reader is configured.");

		if (constant.getClass() != digestComponentEnum)
			throw new OperationException("app:enum: A Digest Component constant was used, but of a different class than was configured.");

		Class<? extends DigestComponent> digestComponentClass = digestComponentEnumToDigestComponentMap.get(constant);
		if (digestComponentClass == null)
			throw new OperationException("app:enum: A Digest Component constant was used from the configured class, but was not recognised.");

		return digestComponentClass;
	}

	// Resolution / Decision
	Class<? extends Node> decide(Node node, Class<? extends Node> targetClass) {
		return decide(node.getClass(), targetClass);
	}

	Class<? extends Node> decide(Class<? extends Node> nodeClass, Class<? extends Node> targetClass) {
		checkRunning();

		// If state is state, then it resolves - STOP
		if (nodeClass == targetClass)
			return nodeClass;

		// VALIDATE: all Nodes should have been scanned
		NodeConfiguration nodeConfiguration = classToNodeConfigurationMap.get(nodeClass);
		if (nodeConfiguration == null)
			throw new ResolutionTermination("app:resolution:navigation: Couldn't find configuration for " + nodeClass.getSimpleName());
		
		// If descendants don't contain state, then BACK
		if (!nodeConfiguration.descendantClasses.contains(targetClass))
			return null;
			
		// Search immediate children to see which contains the state
		for (NodeConfiguration child : nodeConfiguration.childrenConfigurations)
			if (child.descendantClasses.contains(targetClass) || child.thisCls == targetClass)
				return child.thisCls;
			
		// Fall-through: should not be possible to reach this
		throw new ResolutionTermination("app:resolution:navigation: Failed on " + nodeClass.getSimpleName());
	}

	// Resolution / Contributions
	void contributionsChanged() {
		if (aggregator == null)
			return;

		Digest digest = new Digest(this);
		root.compileContributionsFromRoot(digest);

		try {
			digest.process(aggregator);
		}
		catch (Exception e) {
			throw new ResolutionTermination(
				"app:resolution:contribution: Implementer method threw an exception",
				"DigestProcessor::process",
				e);
		}

		root.sendDigestFromRoot(digest);
	}

	// Resolution / Disposal
	void processDisposal(DisposalObject disposalObject) {
		try {
			DisposalObjectProxy.finalise(disposalObject);
			disposalProcessor.consume(disposalObject);
		}
		catch (ImplementerException e) {
			throw new ResolutionTermination("app:resolution:dispose: Disposal failed", e);
		}
		catch (Exception e) {
			throw new ResolutionTermination(
				"app:resolution:dispose: Implementer method threw an exception",
				"DisposalProcessor::consume at " + disposalProcessor.getClass().getSimpleName(),
				e);
		}
	}

	// Launch
	Node launchNode(Node launcher, Class<? extends Node> nodeClass, InjectableBag bag) {
		NodeConfiguration configuration = clsToConfigNode(nodeClass);
		if (configuration == null)
			throw new ResolutionTermination("app:launch: Couldn't find discovered configuration for Node " + nodeClass.getSimpleName());

		Node node;
		try {
			node = (Node)configuration.execute(this, launcher, bag);
		}
		catch (ResolutionTermination e) {
			throw e;
		}
		catch (Exception e) {
			throw new ResolutionTermination(
					"app:launch: Implementer constructor threw an exception",
					"Node::ctor at " + nodeClass.getSimpleName(),
					e);
		}
		return node;
	}

	Injectable launchInjectable(Node launcher, Class<? extends Injectable> injectableClass) {
		InjectableConfiguration configuration = clsToConfigInj(injectableClass);
		if (configuration == null)
			throw new ResolutionTermination("app:launch: Couldn't find discovered configuration for Node " + injectableClass.getSimpleName());

		Injectable injectable;
		try {
			injectable = (Injectable)configuration.execute(this, launcher);
		}
		catch (ResolutionTermination e) {
			throw e;
		}
		catch (Exception e) {
			throw new ResolutionTermination(
					"app:launch: Implementer constructor threw an exception",
					"Injectable::ctor at " + injectableClass.getSimpleName(),
					e);
		}
		return injectable;
	}

	// Configuration
	private void checkConfigured() {
		if (!configured)
			throw new ServiceNotConfiguredException();
	}

	private void checkRunning() {
		if (root == null)
			throw new ServiceNotRunningException();
	}
}