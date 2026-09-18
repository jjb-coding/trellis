package com.github.jjbcoding.trellis.service.disposal.object.internal;

import com.github.jjbcoding.trellis.exceptions.ResolutionTermination;
import com.github.jjbcoding.trellis.service.Injectable;
import com.github.jjbcoding.trellis.service.Node;
import com.github.jjbcoding.trellis.service.disposal.object.NodeRegistration;

import java.util.*;

/**
 * A DisposalObject provides NodeDescriptions in orders corresponding
 * to the method called, over a region of Nodes and associated Injectables
 * to be closed.
 */
public class DisposalObjectInternal {
    // ----- DYNAMIC
    // *** FIELDS
    // Node Registration
    protected final List<NodeRegistration> nodes;
    protected final Map<Node,NodeRegistration> map;
    // Computed
    protected NodeRegistration root;

    // *** CONSTRUCTORS
    public DisposalObjectInternal() {
        nodes = new ArrayList<>();
        map = new IdentityHashMap<>();
    }

    // *** METHODS
    // ** PACKAGE-PRIVATE
    // Registration
    void register(Node node, List<Node> children, List<Injectable> injectables) {
        NodeRegistration registration = new NodeRegistration(node, children, null, injectables);

        nodes.add(registration);
        map.put(node, registration);
    }

    // Finalisation
    void finalise() {
        // Do nothing if empty
        if (nodes.isEmpty())
            return;

        // Check
        if (root != null)
            throw new ResolutionTermination("app:disposal: Finalise was called, but the DisposalObject has already been finalised");

        // Graph tasks
        linkParents();
        computeRoot();
        computeRanks();
    }

    // ** PRIVATE
    // Graph / Traversal
    private void linkParents() {
        for (NodeRegistration registration : nodes)
            registration.setParent(null);

        for (NodeRegistration registration : nodes) {
            for (Node child : registration.getChildren()) {
                NodeRegistration childRegistration = map.get(child);

                if (childRegistration == null)
                    throw new ResolutionTermination("app:disposal: Error occurred.");

                if (childRegistration.getParent() != null)
                    throw new ResolutionTermination("app:disposal: Cycle detected.");

                childRegistration.setParent(registration.getNode());
            }
        }
    }

    private void computeRoot() {
        for (NodeRegistration registration : nodes)
            if (registration.getParent() == null)
                root = registration;

        if (root == null)
            throw new ResolutionTermination("app:disposal: Root node could not be found.");
    }

    private void computeRanks() {
        Queue<NodeRegistration> queue = new ArrayDeque<>();
        root.setR(0);
        queue.add(root);

        while (!queue.isEmpty()) {
            NodeRegistration current = queue.remove();

            for (Node child : current.getChildren()) {
                NodeRegistration childRegistration = map.get(child);
                childRegistration.setR(current.getR() + 1);
                queue.add(childRegistration);
            }
        }
    }
}

