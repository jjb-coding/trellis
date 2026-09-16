package com.github.jjbcoding.trellis.service.disposal.object;

import com.github.jjbcoding.trellis.service.Injectable;
import com.github.jjbcoding.trellis.service.Node;

import java.util.List;

/**
 * A NodeDescription contains a Node and all the Injectables
 * it hosts.
 */
public class NodeDescription {
    // ----- DYNAMIC
    // *** FIELDS
    final Node node;
    final List<Injectable> injectables;

    // *** CONSTRUCTORS
    public NodeDescription(Node node, List<Injectable> injectables) {
        this.node = node;
        this.injectables = injectables;
    }

    // *** METHODS
    /**
     * Gets the Node.
     * @return  The Node
     */
    @SuppressWarnings("unused")
    public Node getNode() {
        return node;
    }

    /**
     * Gets the list of Injectables.
     * @return  The list of Injectables
     */
    @SuppressWarnings("unused")
    public List<Injectable> getInjectables() {
        return injectables;
    }
}
