package com.github.jjbcoding.trellis.service;

import com.github.jjbcoding.trellis.exceptions.OperationException;
import com.github.jjbcoding.trellis.exceptions.ResolutionFailure;
import com.github.jjbcoding.trellis.exceptions.ResolutionTermination;
import com.github.jjbcoding.trellis.service.contributions.Contribution;
import com.github.jjbcoding.trellis.service.contributions.ContributionAggregator;
import com.github.jjbcoding.trellis.service.contributions.DigestComponent;

import java.util.*;
import java.util.function.Predicate;

public class Digest {
    // *** FIELDS
    // Injections
    AppService _appService;
    List<Contribution> contributions;
    List<DigestComponent> components;

    // *** CONSTRUCTORS
    public Digest(AppService _appService) {
        this._appService = _appService;
        contributions = new ArrayList<>();
    }

    // *** METHODS
    // ** PUBLIC
    // Contributions
    public void addContribution(Contribution contribution) {
        contributions.add(contribution);
    }

    // Aggregation
    public void process(ContributionAggregator aggregator) {
        components = aggregator.process(contributions);
    }

    // Components
    public boolean isEmpty() {
        return components.isEmpty();
    }

    // Retrieval / Consume / Error
    /**
     * Returns and removes a component in the Digest by constant.
     * @param componentConstant         The constant
     * @return                          The DigestComponent
     * @throws ResolutionTermination    if there is no such DigestComponent
     * @throws OperationException       if the constant is invalid
     */
    @SuppressWarnings("unused")
    public DigestComponent consume(Enum<?> componentConstant) {
        return consume(_appService.constToClassDigCom(componentConstant));
    }

    /**
     * Returns and removes a component in the Digest by class.
     * @param componentClass            The class
     * @return                          The DigestComponent
     * @throws ResolutionTermination    if there is no such DigestComponent
     */
    @SuppressWarnings("unused")
    public DigestComponent consume(Class<? extends DigestComponent> componentClass) {
        Iterator<DigestComponent> it = components.iterator();
        while (it.hasNext()) {
            DigestComponent component = it.next();
            if (component.getClass().equals(componentClass)) {
                it.remove();
                return component;
            }
        }
        throw new ResolutionTermination("app:digest: Attempted to consume DigestComponent [" + componentClass.getSimpleName() + "] which was not in Digest");
    }

    /**
     * Returns and removes one component in the Digest, based on the filter.
     * @param filter                    The filter
     * @return                          The DigestComponent
     * @throws ResolutionTermination    if there is no such DigestComponent
     */
    @SuppressWarnings("unused")
    public DigestComponent consumeOne(Predicate<DigestComponent> filter) {
        DigestComponent component = tryConsumeOne(filter);
        if (component == null)
            throw new ResolutionTermination("app:digest: Attempted to consume DigestComponent by predicate; none matched");
        return component;
    }

    /**
     * Returns and removes one or more components in the Digest, based on the filter.
     * @param filter                    The filter
     * @return                          The list of DigestComponents
     * @throws ResolutionTermination    if no DigestComponents matched
     */
    @SuppressWarnings("unused")
    public List<DigestComponent> consume(Predicate<DigestComponent> filter) {
        List<DigestComponent> list = tryConsume(filter);
        if (list.isEmpty())
            throw new ResolutionTermination("app:digest: Attempted to consume DigestComponents by predicate; none matched");
        return list;
    }

    // Retrieval / Consume / Try
    /**
     * Returns and removes a component in the Digest by constant, or null
     * if there is no such DigestComponent.
     * @param componentConstant         The constant
     * @return                          The DigestComponent
     * @throws OperationException       if the constant is invalid
     */
    @SuppressWarnings("unused")
    public DigestComponent tryConsume(Enum<?> componentConstant) {
        return tryConsume(_appService.constToClassDigCom(componentConstant));
    }

    /**
     * Returns and removes a component in the Digest by class, or null
     * if there is no such DigestComponent.
     * @param componentClass            The class
     * @return                          The DigestComponent
     */
    @SuppressWarnings("unused")
    public DigestComponent tryConsume(Class<? extends DigestComponent> componentClass) {
        Iterator<DigestComponent> it = components.iterator();
        while (it.hasNext()) {
            DigestComponent component = it.next();
            if (component.getClass().equals(componentClass)) {
                it.remove();
                return component;
            }
        }
        return null;
    }

    /**
     * Returns and removes one component in the Digest, based on the filter,
     * or an empty list if there is no such DigestComponent.
     * @param filter                    The filter
     * @return                          The DigestComponent
     */
    @SuppressWarnings("unused")
    public DigestComponent tryConsumeOne(Predicate<DigestComponent> filter) {
        Iterator<DigestComponent> it = components.iterator();
        while (it.hasNext()) {
            DigestComponent component = it.next();
            if (filter.test(component)) {
                it.remove();
                return component;
            }
        }
        return null;
    }

    /**
     * Returns and removes one or more components in the Digest, based on the filter,
     * or an empty list if there is no such DigestComponent.
     * @param filter                    The filter
     * @return                          The list of DigestComponents
     */
    @SuppressWarnings("unused")
    public List<DigestComponent> tryConsume(Predicate<DigestComponent> filter) {
        List<DigestComponent> list = new ArrayList<>();
        Iterator<DigestComponent> it = components.iterator();
        while (it.hasNext()) {
            DigestComponent component = it.next();
            if (filter.test(component)) {
                it.remove();
                list.add(component);
            }
        }
        return list;
    }

    // Retrieval / Fetch / Error
    /**
     * Returns a component in the Digest by constant.
     * @param componentConstant         The constant
     * @return                          The DigestComponent
     * @throws ResolutionTermination    if there is no such DigestComponent
     * @throws OperationException       if the constant is invalid
     */
    @SuppressWarnings("unused")
    public DigestComponent fetch(Enum<?> componentConstant) {
        return fetch(_appService.constToClassDigCom(componentConstant));
    }


    /**
     * Returns a component in the Digest by class.
     * @param componentClass            The class
     * @return                          The DigestComponent
     * @throws ResolutionTermination    if there is no such DigestComponent
     */
    @SuppressWarnings("unused")
    public DigestComponent fetch(Class<? extends DigestComponent> componentClass) {
        for (DigestComponent component : components)
            if (component.getClass().equals(componentClass))
                return component;
        throw new ResolutionFailure("");
    }


    /**
     * Returns one component in the Digest, based on the filter.
     * @param filter                    The filter
     * @return                          The DigestComponent
     * @throws ResolutionTermination    if there is no such DigestComponent
     */
    @SuppressWarnings("unused")
    public DigestComponent fetchOne(Predicate<DigestComponent> filter) {
        for (DigestComponent component : components)
            if (filter.test(component))
                return component;
        throw new ResolutionFailure("");
    }

    /**
     * Returns one or more components in the Digest, based on the filter.
     * @param filter                    The filter
     * @return                          The list of DigestComponents
     * @throws ResolutionTermination    if no DigestComponents matched
     */
    @SuppressWarnings("unused")
    public List<DigestComponent> fetch(Predicate<DigestComponent> filter) {
        List<DigestComponent> list = new ArrayList<>();
        for (DigestComponent component : components)
            if (filter.test(component))
                list.add(component);
        if (list.isEmpty())
            throw new ResolutionFailure("");
        return list;
    }

    // Retrieval / Fetch / Try
    /**
     * Returns a component in the Digest by constant, or null
     * if there is no such DigestComponent.
     * @param componentConstant         The constant
     * @return                          The DigestComponent
     * @throws OperationException       if the constant is invalid
     */
    @SuppressWarnings("unused")
    public DigestComponent tryFetch(Enum<?> componentConstant) {
        return tryFetch(_appService.constToClassDigCom(componentConstant));
    }

    /**
     * Returns a component in the Digest by class, or null
     * if there is no such DigestComponent.
     * @param componentClass            The class
     * @return                          The DigestComponent
     */
    @SuppressWarnings("unused")
    public DigestComponent tryFetch(Class<? extends DigestComponent> componentClass) {
        for (DigestComponent component : components)
            if (component.getClass().equals(componentClass))
                return component;
        return null;
    }

    /**
     * Returns one component in the Digest, based on the filter,
     * or an empty list if there is no such DigestComponent.
     * @param filter                    The filter
     * @return                          The DigestComponent
     */
    @SuppressWarnings("unused")
    public DigestComponent tryFetchOne(Predicate<DigestComponent> filter) {
        for (DigestComponent component : components)
            if (filter.test(component))
                return component;
        return null;
    }

    /**
     * Returns one or more components in the Digest, based on the filter,
     * or an empty list if there is no such DigestComponent.
     * @param filter                    The filter
     * @return                          The list of DigestComponents
     */
    @SuppressWarnings("unused")
    public List<DigestComponent> tryFetch(Predicate<DigestComponent> filter) {
        List<DigestComponent> list = new ArrayList<>();
        for (DigestComponent component : components)
            if (filter.test(component))
                list.add(component);
        return list;
    }
}
