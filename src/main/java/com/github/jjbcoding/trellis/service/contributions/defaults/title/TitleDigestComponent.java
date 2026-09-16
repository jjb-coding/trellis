package com.github.jjbcoding.trellis.service.contributions.defaults.title;

import com.github.jjbcoding.trellis.service.contributions.DigestComponent;

/**
 * The concatenated title.
 */
public class TitleDigestComponent
    extends DigestComponent {
    // ----- DYNAMIC
    // *** FIELDS
    final String title;

    // *** CONSTRUCTORS
    @SuppressWarnings("unused")
    public TitleDigestComponent(String title) {
        this.title = title;
    }

    // *** METHODS
    // ** PUBLIC
    // Getters
    public String getTitle() {
        return title;
    }
}
