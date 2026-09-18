package com.github.jjbcoding.trellis.service.contributions;

import java.util.List;

/**
 * A ContributionAggregator consumes a list of Contributions and aggregates them
 * into one or more DigestComponents. It is the responsibility of the DigestProcessor
 * to convert the types of the Contribution objects.
 */
public abstract class ContributionAggregator {
    /**
     * Aggregates a list of Contributions into one or more DigestComponents.
     * @param contributions     The list of contributions
     */
    public abstract List<DigestComponent> process(List<Contribution> contributions);
}
