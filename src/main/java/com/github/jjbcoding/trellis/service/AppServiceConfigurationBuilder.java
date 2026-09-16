package com.github.jjbcoding.trellis.service;

import com.github.jjbcoding.trellis.exceptions.BuilderException;
import com.github.jjbcoding.trellis.exceptions.ResolutionFailure;
import com.github.jjbcoding.trellis.service.disposal.processor.defaults.DisposalOrder;
import com.github.jjbcoding.trellis.service.disposal.processor.IDisposalProcessor;
import com.github.jjbcoding.trellis.service.disposal.processor.defaults.TraversalType;
import com.github.jjbcoding.trellis.service.disposal.processor.defaults.TreeOrder;
import com.github.jjbcoding.trellis.service.disposal.processor.defaults.DefaultDisposalProcessor;
import com.github.jjbcoding.trellis.service.contributions.ContributionAggregator;
import com.github.jjbcoding.trellis.service.contributions.DigestComponent;
import com.github.jjbcoding.trellis.service.providers.*;
import com.github.jjbcoding.trellis.service.providers.defaults.EnumClassProvider;
import com.github.jjbcoding.trellis.service.providers.defaults.ListClassProvider;
import com.github.jjbcoding.trellis.service.providers.defaults.ReflectionClassProvider;
import com.github.jjbcoding.trellis.service.rules.defaults.Rule;
import com.github.jjbcoding.trellis.service.readers.EnumReaderPair;
import com.github.jjbcoding.trellis.service.readers.IReader;
import com.github.jjbcoding.trellis.service.rules.IRule;
import com.github.jjbcoding.trellis.service.rules.defaults.Relationship;

import java.util.ArrayList;
import java.util.List;

/**
 * The AppServiceConfigurationBuilder is used to configure AppService.
 * Before such an object is passed to {@link AppService#configure(AppServiceConfigurationBuilder)},
 * the {@link AppService} cannot be used.
 * It facilitates the configuration of validation rules on discovered Nodes and
 * Injectables, the initial Node provider, enum constant readers, startup,
 * various flags, and the DisposalProcessor and ContributionAggregator. At minimum,
 * an initial Nodes provider and startup state and root must be configured.
 */
public class AppServiceConfigurationBuilder {
    // ----- NESTED
    // ** PUBLIC
    public enum Domain {
        Nodes,
        Injectables
    }
    // ** PACKAGE-PRIVATE
    enum ProviderType {
        ClassProvider,
        FromEnum
    }

    // ----- DYNAMIC
    // *** FIELDS
    // Packages
    private String prefix;
    // Providers
    ProviderType nodesClassProviderType;
    ClassProvider<Node> nodesClassProvider;
    // Rules
    IRule<Node> nodesRule;
    IRule<Injectable> injectablesRule;
    // Reader / State
    EnumReaderPair<Node,? extends Enum<?>> stateReaderPair;
    // Reader / Injectable
    EnumReaderPair<Injectable,? extends Enum<?>> injectablesReaderPair;
    // Reader / Digest
    EnumReaderPair<DigestComponent,? extends Enum<?>> digestComponentReaderPair;
    // Startup
    NodeLocator startupRoot, startupState;
    // Contributions
    ContributionAggregator aggregator;
    // Disposal
    TreeOrder disposalProcessorTreeOrder;
    TraversalType disposalProcessorTraversalType;
    DisposalOrder disposalProcessorDisposalOrder;
    IDisposalProcessor disposalProcessor;
    // Flags
    boolean digestMustBeExhausted, requestMustBeSatisfied, injectableBagMustBeExhausted;

    // *** CONSTRUCTORS
    /**
     * Creates a new AppServiceConfigurationBuilder instance.
     * Passed to {@link AppService#configure(AppServiceConfigurationBuilder)}.
     */
    public AppServiceConfigurationBuilder() {
        // Null or Empty
        prefix = "";
        nodesClassProvider = null;
        nodesRule = null;
        injectablesRule = null;
        stateReaderPair = null;
        injectablesReaderPair = null;
        digestComponentReaderPair = null;
        startupRoot = startupState = null;
        aggregator = null;
        disposalProcessor = null;

        // Defaults
        nodesClassProviderType = ProviderType.FromEnum;
        digestMustBeExhausted = requestMustBeSatisfied = injectableBagMustBeExhausted = true;
        disposalProcessorTreeOrder = TreeOrder.Ascending;
        disposalProcessorTraversalType = TraversalType.Breadth;
        disposalProcessorDisposalOrder = DisposalOrder.NodeThenInjectables;
    }

    // *** METHODS
    // ** PUBLIC
    // Builder / Package
    /**
     * Sets the prefix for all packages used by class providers created
     * through this builder. Can be described in 'a.b', 'a/b' or 'a\b' format.
     * A bridging connective is not required. For example, 'a.b' as prefix and 'c' as suffix implies 'a.b.c'.
     * This must be configured before packages are specified to take effect.
     * @param prefix            The prefix
     * @return                  Self-returning
     * @throws BuilderException if base is null
     */
    @SuppressWarnings("unused")
    public AppServiceConfigurationBuilder setPackagePrefix(String prefix) {
        if (prefix == null)
            throw new BuilderException("app:builder: Provided prefix was null");

        this.prefix = format(prefix);

        return this;
    }

    // Builder / Disposal Processor / Custom
    /**
     * Sets a custom disposal processor. If a custom disposal processor is configured,
     * any configured parameters for the default disposal processor are ignored. Providing
     * null will clear this setting and cause the builder to use the default disposal
     * processor instead.
     * @param disposalProcessor The DisposalProcessor
     * @return                  Self-returning
     */
    @SuppressWarnings("unused")
    public AppServiceConfigurationBuilder setCustomDisposalProcessor(IDisposalProcessor disposalProcessor) {
        this.disposalProcessor = disposalProcessor;
        return this;
    }

    // Builder / Disposal Processor / Default
    /**
     * Sets the parameters of the default disposal processor.
     * @param treeOrder         Whether to visit nodes root to leaf or leaf to root
     * @param traversalType     Whether to visit nodes breadth-first or last; or depth-first or last
     * @param disposalOrder     Whether to Close injectables or the parent node first
     * @return                  Self-returning
     * @throws BuilderException if treeOrder, traversalType or disposalOrder are null
     */
    @SuppressWarnings("unused")
    public AppServiceConfigurationBuilder setDefaultDisposalProcessor(TreeOrder treeOrder, TraversalType traversalType, DisposalOrder disposalOrder) {
        paramTreeOrder(treeOrder);
        paramTraversalType(traversalType);
        paramDisposalOrder(disposalOrder);

        disposalProcessorTreeOrder = treeOrder;
        disposalProcessorTraversalType = traversalType;
        disposalProcessorDisposalOrder = disposalOrder;
        return this;
    }

    /**
     * Configures the default disposal processor to visit nodes root to leaf.
     * @return                  Self-returning
     */
    @SuppressWarnings("unused")
    public AppServiceConfigurationBuilder setDefaultDisposalProcessorAscending() {
        disposalProcessorTreeOrder = TreeOrder.Ascending;
        return this;
    }

    /**
     * Configures the default disposal processor to visit nodes leaf to root.
     * @return                  Self-returning
     */
    @SuppressWarnings("unused")
    public AppServiceConfigurationBuilder setDefaultDisposalProcessorDescending() {
        disposalProcessorTreeOrder = TreeOrder.Descending;
        return this;
    }

    /**
     * Configures the default disposal processor to visit nodes in breadth-first search,
     * organised by rank. If the TreeOrder is Descending, ranks will be visited
     * from leaf to root.
     * @return                  Self-returning
     */
    @SuppressWarnings("unused")
    public AppServiceConfigurationBuilder setDefaultDisposalProcessorByBreadth() {
        disposalProcessorTraversalType = TraversalType.Breadth;
        return this;
    }

    /**
     * Configures the default disposal processor to visit nodes in depth-first search.
     * If the TreeOrder is Descending, visitation is equivalent to depth-first search
     * reversed.
     * @return                  Self-returning
     */
    @SuppressWarnings("unused")
    public AppServiceConfigurationBuilder setDefaultDisposalProcessorByDepth() {
        disposalProcessorTraversalType = TraversalType.Depth;
        return this;
    }

    /**
     * Configures the default disposal processor to close injectables provided by
     * the node first, then the node.
     * @return                  Self-returning
     */
    @SuppressWarnings("unused")
    public AppServiceConfigurationBuilder setDefaultDisposalProcessorInjectablesThenNode() {
        disposalProcessorDisposalOrder = DisposalOrder.InjectablesThenNode;
        return this;
    }

    /**
     * Configures the default disposal processor to close the node first, then
     * injectables provided by the node.
     * @return                  Self-returning
     */
    @SuppressWarnings("unused")
    public AppServiceConfigurationBuilder setDefaultDisposalProcessorNodeThenInjectables() {
        disposalProcessorDisposalOrder = DisposalOrder.NodeThenInjectables;
        return this;
    }

    // Builder / Providers / Nodes
    /**
     * Configures Nodes to come from the state reader.
     * A state reader does not have to have been configured yet.
     * This is true by default.
     * @return                  Self-returning
     */
    @SuppressWarnings("unused")
    public AppServiceConfigurationBuilder setNodesFromReader() {
        nodesClassProviderType = ProviderType.FromEnum;
        return this;
    }

    /**
     * Configures Nodes to come from a package.
     * @param packageString     The package name, with prefix if configured
     * @return                  Self-returning
     * @throws BuilderException if packageString is null or empty
     */
    @SuppressWarnings("unused")
    public AppServiceConfigurationBuilder setNodesFromPackage(String packageString) {
        if (packageString == null || packageString.isEmpty())
            throw new BuilderException("app:builder: Provided package was null or empty");

        nodesClassProviderType = ProviderType.ClassProvider;
        nodesClassProvider = new ReflectionClassProvider<>(prepend(packageString), Node.class);
        return this;
    }

    /**
     * Configures Nodes to come from a list.
     * @param nodesList         The list of classes
     * @return                  Self-returning
     * @throws BuilderException if the list of classes is null or empty
     */
    @SuppressWarnings("unused")
    public AppServiceConfigurationBuilder setNodesFromList(List<Class<? extends Node>> nodesList) {
        if (nodesList == null || nodesList.isEmpty())
            throw new BuilderException("app:builder: Provided list was null or empty");

        nodesClassProviderType = ProviderType.ClassProvider;
        nodesClassProvider = new ListClassProvider<>(new ArrayList<>(nodesList), Node.class);
        return this;
    }

    /**
     * Configures Nodes to come from a custom class provider.
     * @param nodesProvider     The custom class provider
     * @return                  Self-returning
     * @throws BuilderException if the Nodes Provider is null
     */
    @SuppressWarnings("unused")
    public AppServiceConfigurationBuilder setCustomNodesProvider(ClassProvider<Node> nodesProvider) {
        if (nodesProvider == null)
            throw new BuilderException("app:builder: Provided node provider was null");

        nodesClassProviderType = ProviderType.ClassProvider;
        this.nodesClassProvider = nodesProvider;
        return this;
    }

    // Builder / Rules / Other Set / Package
    /**
     * Determines if discovered Nodes or Injectables are all in a specific package.
     * @param domain            Whether the rule validates Nodes or Injectables
     * @param packageString     The package name, with prefix if configured
     * @return                  Self-returning
     * @throws BuilderException if domain is null, or packageString is null or empty
     */
    @SuppressWarnings("unused")
    public AppServiceConfigurationBuilder validateRecordsIn(Domain domain, String packageString) {
        if (packageString == null || packageString.isEmpty())
            throw new BuilderException("app:builder: Provided package was null or empty");
        paramDomain(domain);

        if (domain == Domain.Nodes)
            nodesRule = new Rule<>(new ReflectionClassProvider<>(prepend(packageString), Node.class), Relationship.InProvider);
        else
            injectablesRule = new Rule<>(new ReflectionClassProvider<>(prepend(packageString), Injectable.class), Relationship.InProvider);
        return this;
    }

    /**
     * Determines if discovered Nodes or Injectables cover all the classes in a specific package.
     * @param domain            Whether the rule validates Nodes or Injectables
     * @param packageString     The package name, with prefix if configured
     * @return                  Self-returning
     * @throws BuilderException if domain is null, or packageString is null or empty
     */
    @SuppressWarnings("unused")
    public AppServiceConfigurationBuilder validateRecordsCover(Domain domain, String packageString) {
        if (packageString == null || packageString.isEmpty())
            throw new BuilderException("app:builder: Provided package was null or empty");
        paramDomain(domain);

        if (domain == Domain.Nodes)
            nodesRule = new Rule<>(new ReflectionClassProvider<>(prepend(packageString), Node.class), Relationship.CoverProvider);
        else
            injectablesRule = new Rule<>(new ReflectionClassProvider<>(prepend(packageString), Injectable.class), Relationship.CoverProvider);
        return this;
    }

    /**
     * Determines if discovered Nodes or Injectables are equal to a specific package.
     * @param domain            Whether the rule validates Nodes or Injectables
     * @param packageString     The package name, with prefix if configured
     * @return                  Self-returning
     * @throws BuilderException if domain is null, or packageString is null or empty
     */
    @SuppressWarnings("unused")
    public AppServiceConfigurationBuilder validateRecordsEqual(Domain domain, String packageString) {
        if (packageString == null || packageString.isEmpty())
            throw new BuilderException("app:builder: Provided package was null or empty");
        paramDomain(domain);

        if (domain == Domain.Nodes)
            nodesRule = new Rule<>(new ReflectionClassProvider<>(prepend(packageString), Node.class), Relationship.EqualProvider);
        else
            injectablesRule = new Rule<>(new ReflectionClassProvider<>(prepend(packageString), Injectable.class), Relationship.EqualProvider);
        return this;
    }

    // Builder / Rules / Other Set / Classes
    /**
     * Determines if discovered Nodes or Injectables are all in a specific list of classes.
     * @param domain            Whether the rule validates Nodes or Injectables
     * @param classes           The list of classes
     * @return                  Self-returning
     * @throws BuilderException if domain is null, or the list of classes is null or empty
     */
    @SuppressWarnings("unused")
    public AppServiceConfigurationBuilder validateRecordsIn(Domain domain, List<Class<?>> classes) {
        if (classes == null || classes.isEmpty())
            throw new BuilderException("app:builder: Provided list was null or empty");
        paramDomain(domain);

        if (domain == Domain.Nodes)
            nodesRule = new Rule<>(new ListClassProvider<>(classes, Node.class), Relationship.InProvider);
        else
            injectablesRule = new Rule<>(new ListClassProvider<>(classes, Injectable.class), Relationship.InProvider);
        return this;
    }

    /**
     * Determines if discovered Nodes or Injectables cover a specific list of classes.
     * @param domain            Whether the rule validates Nodes or Injectables
     * @param classes           The list of classes
     * @return                  Self-returning
     * @throws BuilderException if domain is null, or the list of classes is null or empty
     */
    @SuppressWarnings("unused")
    public AppServiceConfigurationBuilder validateRecordsCover(Domain domain, List<Class<?>> classes) {
        if (classes == null || classes.isEmpty())
            throw new BuilderException("app:builder: Provided list was null or empty");
        paramDomain(domain);

        if (domain == Domain.Nodes)
            nodesRule = new Rule<>(new ListClassProvider<>(classes, Node.class), Relationship.CoverProvider);
        else
            injectablesRule = new Rule<>(new ListClassProvider<>(classes, Injectable.class), Relationship.CoverProvider);
        return this;
    }

    /**
     * Determines if discovered Nodes or Injectables are equal to a specific list of classes.
     * @param domain            Whether the rule validates Nodes or Injectables
     * @param classes           The list of classes
     * @return                  Self-returning
     * @throws BuilderException if domain is null, or the list of classes is null or empty
     */
    @SuppressWarnings("unused")
    public AppServiceConfigurationBuilder validateRecordsEqual(Domain domain, List<Class<?>> classes) {
        if (classes == null || classes.isEmpty())
            throw new BuilderException("app:builder: Provided list was null or empty");
        paramDomain(domain);

        if (domain == Domain.Nodes)
            nodesRule = new Rule<>(new ListClassProvider<>(classes, Node.class), Relationship.EqualProvider);
        else
            injectablesRule = new Rule<>(new ListClassProvider<>(classes, Injectable.class), Relationship.EqualProvider);
        return this;
    }

    // Builder / Rules / Other Set / Custom Classes Provider
    /**
     * Determines if discovered Nodes or Injectables are all in a specific class provider.
     * @param domain            Whether the rule validates Nodes or Injectables
     * @param classes           The class provider
     * @return                  Self-returning
     * @throws BuilderException if domain is null, or the Class Provider is null
     */
    @SuppressWarnings({"unused", "unchecked"})
    public AppServiceConfigurationBuilder validateRecordsIn(Domain domain, ClassProvider<?> classes) {
        if (classes == null)
            throw new BuilderException("app:builder: Provided Class Provider was null");
        paramDomain(domain);

        if (domain == Domain.Nodes) {
            if (classes.getTargetClass() != Node.class)
                throw new BuilderException("app:build: A Class Provider was configured to validate Nodes that does not provide Nodes");
            nodesRule = new Rule<>((ClassProvider<Node>)classes, Relationship.InProvider);
        }
        else {
            if (classes.getTargetClass() != Injectable.class)
                throw new BuilderException("app:build: A Class Provider was configured to validate Injectables that does not provide Injectables");
            injectablesRule = new Rule<>((ClassProvider<Injectable>)classes, Relationship.InProvider);
        }
        return this;
    }

    /**
     * Determines if discovered Nodes or Injectables cover the contents of a specific class provider.
     * @param domain            Whether the rule validates Nodes or Injectables
     * @param classes           The class provider
     * @return                  Self-returning
     * @throws BuilderException if domain is null, or the Class Provider is null
     */
    @SuppressWarnings({"unused", "unchecked"})
    public AppServiceConfigurationBuilder validateRecordsCover(Domain domain, ClassProvider<?> classes) {
        if (classes == null)
            throw new BuilderException("app:builder: Provided Class Provider was null");
        paramDomain(domain);

        if (domain == Domain.Nodes) {
            if (classes.getTargetClass() != Node.class)
                throw new BuilderException("app:build: A Class Provider was configured to validate Nodes that does not provide Nodes");
            nodesRule = new Rule<>((ClassProvider<Node>)classes, Relationship.CoverProvider);
        }
        else {
            if (classes.getTargetClass() != Injectable.class)
                throw new BuilderException("app:build: A Class Provider was configured to validate Injectables that does not provide Injectables");
            injectablesRule = new Rule<>((ClassProvider<Injectable>)classes, Relationship.CoverProvider);
        }
        return this;
    }

    /**
     * Determines if discovered Nodes or Injectables are equal to the contents of a specific class provider.
     * @param domain            Whether the rule validates Nodes or Injectables
     * @param classes           The class provider
     * @return                  Self-returning
     * @throws BuilderException if domain is null, or the Class Provider is null
     */
    @SuppressWarnings({"unused", "unchecked"})
    public AppServiceConfigurationBuilder validateRecordsEqual(Domain domain, ClassProvider<?> classes) {
        if (classes == null)
            throw new BuilderException("app:builder: Provided Class Provider was null");
        paramDomain(domain);

        if (domain == Domain.Nodes) {
            if (classes.getTargetClass() != Node.class)
                throw new BuilderException("app:build: A Class Provider was configured to validate Nodes that does not provide Nodes");
            nodesRule = new Rule<>((ClassProvider<Node>)classes, Relationship.EqualProvider);
        }
        else {
            if (classes.getTargetClass() != Injectable.class)
                throw new BuilderException("app:build: A Class Provider was configured to validate Injectables that does not provide Injectables");
            injectablesRule = new Rule<>((ClassProvider<Injectable>)classes, Relationship.EqualProvider);
        }
        return this;
    }

    // Builder / Rules / Both / Enum
    /**
     * Determines if discovered Nodes or Injectables are all in what the Nodes or Injectable reader describes.
     * The appropriate reader must have already been configured.
     * @param domain    Whether the rule validates Nodes or Injectables
     * @return          Self-returning
     * @throws BuilderException if domain is null
     */
    @SuppressWarnings("unused")
    public AppServiceConfigurationBuilder validateItemsInEnum(Domain domain) {
        validateItemsEnum(domain, Relationship.InProvider);
        return this;
    }

    /**
     * Determines if discovered Nodes or Injectables cover what the Nodes or Injectable reader describes.
     * The appropriate reader must have already been configured.
     * @param domain    Whether the rule validates Nodes or Injectables
     * @return          Self-returning
     * @throws BuilderException if domain is null
     */
    @SuppressWarnings("unused")
    public AppServiceConfigurationBuilder validateItemsCoverEnum(Domain domain) {
        validateItemsEnum(domain, Relationship.CoverProvider);
        return this;
    }

    /**
     * Determines if discovered Nodes or Injectables equal what the Nodes or Injectable reader describes.
     * The appropriate reader must have already been configured.
     * @param domain    Whether the rule validates Nodes or Injectables
     * @return          Self-returning
     * @throws BuilderException if domain is null
     */
    @SuppressWarnings("unused")
    public AppServiceConfigurationBuilder validateItemsEqualEnum(Domain domain) {
        validateItemsEnum(domain, Relationship.EqualProvider);
        return this;
    }

    private void validateItemsEnum(Domain domain, Relationship type) {
        paramDomain(domain);
        if (domain == Domain.Nodes) {
            if (stateReaderPair == null)
                throw new BuilderException("app:builder: A validation rule using stateReader was configured, but stateReader has not been configured.");
            EnumClassProvider<Node> enumClassProvider = new EnumClassProvider<>(stateReaderPair, Node.class);
            nodesRule = new Rule<>(enumClassProvider, type);
        }
        else {
            if (injectablesRule == null)
                throw new BuilderException("app:builder: A validation rule using injectableReader was configured, but injectableReader has not been configured.");
            EnumClassProvider<Injectable> enumClassProvider = new EnumClassProvider<>(injectablesReaderPair, Injectable.class);
            injectablesRule = new Rule<>(enumClassProvider, type);
        }
    }

    // Builder / Rules / Custom Rule
    /**
     * Applies a rule to discovered Nodes. If null, clears any preexisting rule.
     * @param rule              The rule
     * @return                  Self-returning
     */
    @SuppressWarnings("unused")
    public AppServiceConfigurationBuilder validateNodesWith(IRule<Node> rule) {
        nodesRule = rule;
        return this;
    }

    /**
     * Applies a rule to discovered Injectables. If null, clears any preexisting rule.
     * @param rule      The rule
     * @return          Self-returning
     */
    @SuppressWarnings("unused")
    public AppServiceConfigurationBuilder validateInjectablesWith(IRule<Injectable> rule) {
        injectablesRule = rule;
        return this;
    }

    // Builder / Readers
    /**
     * Sets the state reader and the enum class it reads.
     * @param stateReader           The reader
     * @param stateEnum             The enum class
     * @return                      Self-returning
     * @param <T>                   The enum class
     * @throws BuilderException     if a startup root or state constant was configured that is not valid under the provided state enum,
     *                              or either parameter is null
     */
    @SuppressWarnings("unused")
    public <T extends Enum<T>> AppServiceConfigurationBuilder setStateReader(IReader<Node,T> stateReader, Class<T> stateEnum) {
        if (stateReader == null)
            throw new BuilderException("app:builder: stateReader was null");
        if (stateEnum == null)
            throw new BuilderException("app:builder: stateEnum was null");

        this.stateReaderPair = new EnumReaderPair<>(stateReader, stateEnum);
        validateStartupRoot();
        validateStartupState();
        return this;
    }

    /**
     * Sets the injectable reader and the enum class it reads.
     * @param injectableReader      The reader
     * @param injectableEnum        The enum class it reads
     * @return                      Self-returning
     * @param <T>                   The enum class
     * @throws BuilderException     if either parameter is null
     */
    @SuppressWarnings("unused")
    public <T extends Enum<T>> AppServiceConfigurationBuilder setInjectableReader(IReader<Injectable,T> injectableReader, Class<T> injectableEnum) {
        if (injectableReader == null)
            throw new BuilderException("app:builder: injectableReader was null");
        if (injectableEnum == null)
            throw new BuilderException("app:builder: injectableEnum was null");

        this.injectablesReaderPair = new EnumReaderPair<>(injectableReader, injectableEnum);
        return this;
    }

    /**
     * Sets the DigestComponent reader and the enum class it reads.
     * @param digestComponentReader The reader
     * @param digestComponentEnum   The enum class it reads
     * @return                      Self-returning
     * @param <T>                   The enum class
     * @throws BuilderException     if either parameter is null
     */
    @SuppressWarnings("unused")
    public <T extends Enum<T>> AppServiceConfigurationBuilder setDigestComponentReader(IReader<DigestComponent,T> digestComponentReader, Class<T> digestComponentEnum) {
        if (digestComponentReader == null)
            throw new BuilderException("app:builder: digestComponentReader was null");
        if (digestComponentEnum == null)
            throw new BuilderException("app:builder: digestComponentEnum was null");

        this.digestComponentReaderPair = new EnumReaderPair<>(digestComponentReader, digestComponentEnum);
        return this;
    }

    // Builder / Startup
    /**
     * Sets the root node used during startup. This must refer to a Node that is discoverable.
     * If null, this will clear any preexisting startup root.
     * @param nodeClass             The class of the node
     * @return                      Self-returning
     */
    @SuppressWarnings("unused")
    public AppServiceConfigurationBuilder setStartupRoot(Class<? extends Node> nodeClass) {
        startupRoot = new NodeLocator(nodeClass);
        validateStartupRoot();
        return this;
    }

    /**
     * Sets the root node used during startup. If null, this will clear any preexisting startup root.
     * A state enum reader does not have to have been configured yet.
     * @param nodeConstant          The constant associated with the node
     * @return                      Self-returning
     * @throws BuilderException     if a state reader has been configured and the constant is not valid
     */
    @SuppressWarnings("unused")
    public AppServiceConfigurationBuilder setStartupRoot(Enum<?> nodeConstant) {
        startupRoot = new NodeLocator(nodeConstant);
        validateStartupRoot();
        return this;
    }

    /**
     * Sets the initial state - leaf node - used during startup. This must refer to a Node
     * that is discoverable. If null, this will clear any preexisting startup state.
     * @param nodeClass             The class of the node
     * @return                      Self-returning
     */
    @SuppressWarnings("unused")
    public AppServiceConfigurationBuilder setStartupState(Class<? extends Node> nodeClass) {
        startupState = new NodeLocator(nodeClass);
        validateStartupState();
        return this;
    }

    /**
     * Sets the initial state - leaf node - used during startup. If null, this will clear
     * any preexisting startup state. A state enum reader does not have to have been configured yet.
     * @param nodeConstant          The constant associated with the node
     * @return                      Self-returning
     * @throws BuilderException     if a state reader has been configured and the constant is not valid
     */
    @SuppressWarnings("unused")
    public AppServiceConfigurationBuilder setStartupState(Enum<?> nodeConstant) {
        startupState = new NodeLocator(nodeConstant);
        validateStartupState();
        return this;
    }

    // Builder / Aggregator
    /**
     * Sets the contribution aggregator. If null, this will clear any preexisting aggregator.
     * @param aggregator    The aggregator
     * @return              Self-returning
     */
    @SuppressWarnings("unused")
    public AppServiceConfigurationBuilder setContributionAggregator(ContributionAggregator aggregator) {
        this.aggregator = aggregator;
        return this;
    }

    // Builder / Validation Flags
    /**
     * Determines whether all the components of a digest of contributions
     * must be consumed by the tree. If raised, a {@link ResolutionFailure}
     * will be thrown when this is not the case.
     * Defaults to true.
     * @param value     Whether to enforce this rule
     * @return          Self-returning
     */
    @SuppressWarnings("unused")
    public AppServiceConfigurationBuilder digestMustBeExhausted(boolean value) {
        digestMustBeExhausted = value;
        return this;
    }

    /**
     * Determines whether a request must be consumed by the tree. If raised,
     * a {@link ResolutionFailure} will be thrown when this is not the case.
     * Defaults to true.
     * @param value     Whether to enforce this rule
     * @return          Self-returning
     */
    @SuppressWarnings("unused")
    public AppServiceConfigurationBuilder requestMustBeSatisfied(boolean value) {
        requestMustBeSatisfied = value;
        return this;
    }

    /**
     * Determines whether all the injectables in a bag must be
     * consumed by the tree. If raised, a {@link ResolutionFailure}
     * will be thrown when this is not the case.
     * Defaults to true.
     * @param value     Whether to enforce this rule
     * @return          Self-returning
     */
    @SuppressWarnings("unused")
    public AppServiceConfigurationBuilder injectableBagMustBeExhausted(boolean value) {
        injectableBagMustBeExhausted = value;
        return this;
    }

    // ** PACKAGE-PRIVATE
    void finalise() {
        // NodesProvider = from Enum
        if (nodesClassProviderType == ProviderType.FromEnum) {
            if (stateReaderPair == null)
                throw new BuilderException("app:build: The Nodes Provider was configured to come from the state enumerable, but no state enumerable was configured.");
            nodesClassProvider = new EnumClassProvider<>(stateReaderPair, Node.class);
        }
        // Startup root
        if (startupRoot == null)
            throw new BuilderException("app:build: No root node was configured.");
        // Disposal
        if (disposalProcessor == null)
            disposalProcessor = new DefaultDisposalProcessor(disposalProcessorTreeOrder, disposalProcessorTraversalType, disposalProcessorDisposalOrder);
    }

    // ** PRIVATE
    // Parameter Validation
    private void paramDomain(Domain domain) {
        if (domain == null)
            throw new BuilderException("app:builder: Domain was null");
    }

    private void paramTreeOrder(TreeOrder treeOrder) {
        if (treeOrder == null)
            throw new BuilderException("app:builder: Provided TreeOrder was null");
    }

    private void paramTraversalType(TraversalType traversalType) {
        if (traversalType == null)
            throw new BuilderException("app:builder: Provided TraversalType was null");
    }

    private void paramDisposalOrder(DisposalOrder disposalOrder) {
        if (disposalOrder == null)
            throw new BuilderException("app:builder: Provided DisposalOrder was null");
    }

    // Validation
    private void validateStartupRoot() {
        Enum<?> constant = NodeLocator.getConstant(startupRoot);
        if (stateReaderPair == null || constant == null)
            return;
        if (constant.getClass() != stateReaderPair.getEnumClass())
            throw new BuilderException("app:builder: Startup root is provided as constant, but is not of the configured constant class.");
    }

    private void validateStartupState() {
        Enum<?> constant = NodeLocator.getConstant(startupState);
        if (stateReaderPair == null || constant == null)
            return;
        if (constant.getClass() != stateReaderPair.getEnumClass())
            throw new BuilderException("app:builder: Startup state is provided as constant, but is not of the configured constant class.");
    }

    // String / Packages
    private String prepend(String append) {
        if (append == null)
            append = "";
        append = format(append);
        if (prefix == null || prefix.isEmpty())
            return append;
        else
            return prefix + "." + append;
    }

    private String format(String path) {
        return path
                .replace('/','.')
                .replace('\\','.')
                .replace(':','.')
                .replaceAll("^\\.+|\\.+$", "");
    }
}
