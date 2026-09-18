package com.github.jjbcoding.trellis.service.contributions.defaults.title;

import com.github.jjbcoding.trellis.exceptions.OperationException;
import com.github.jjbcoding.trellis.service.contributions.Contribution;
import com.github.jjbcoding.trellis.service.contributions.ContributionAggregator;
import com.github.jjbcoding.trellis.service.contributions.DigestComponent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A ContributionAggregator that concatenates title parts from each Node,
 * using a configurable delimiter.
 */
public class TitleContributionAggregator
    extends ContributionAggregator {
    // ----- DYNAMIC
    // *** FIELDS
    String delimeterString;

    // *** CONSTRUCTORS
    @SuppressWarnings("unused")
    public TitleContributionAggregator(String delimeterString) {
        this.delimeterString = delimeterString;
    }

    // *** METHODS
    // ** PUBLIC
    @Override
    public List<DigestComponent> process(List<Contribution> contributions) {
        StringBuilder stringBuilder = new StringBuilder();
        Iterator<Contribution> iterator = contributions.iterator();

        while (iterator.hasNext()) {
            Contribution contribution = iterator.next();
            if (contribution == null)
                continue;

            if (!(contribution instanceof TitleContribution titleContribution))
                throw new OperationException("");
            stringBuilder.append(titleContribution.title);

            if (iterator.hasNext())
                stringBuilder.append(delimeterString);
        }
        TitleDigestComponent digest = new TitleDigestComponent(stringBuilder.toString());
        List<DigestComponent> digestComponents = new ArrayList<>();
        digestComponents.add(digest);
        return digestComponents;
    }
}
