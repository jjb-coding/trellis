package com.github.jjbcoding.trellis.service.contributions.defaults.title;

import com.github.jjbcoding.trellis.service.contributions.Contribution;

/**
 * The part of a title that a Node contributes.
 */
public class TitleContribution
    extends Contribution {
    // ----- DYNAMIC
    // *** FIELDS
    String title;

    // *** CONSTRUCTORS
    public TitleContribution(String title) {
        this.title = title;
    }
}
