package com.github.jjbcoding.trellis.service.disposal.object;

import com.github.jjbcoding.trellis.service.Node;
import com.github.jjbcoding.trellis.service.disposal.object.internal.DisposalObjectInternal;

import java.util.ArrayList;
import java.util.List;

/**
 * A DisposalObject accumulates Nodes and Injectables that are to be disposed of.
 * Its purpose is to be passed to an object implementing {@link com.github.jjbcoding.trellis.service.disposal.processor.IDisposalProcessor}.
 * @see com.github.jjbcoding.trellis.service.disposal.processor.IDisposalProcessor
 */
public class DisposalObject
    extends DisposalObjectInternal {
    // ----- DYNAMIC
    // *** CONSTRUCTORS
    /**
     * Constructs a DisposalObject instance.
     */
    public DisposalObject() {
        super();
    }

    // *** METHODS
    /**
     * Returns registered nodes by rank - in the manner of a breadth-first search -
     * in ascending order, from root to leaf.
     * @return      A list of ranks in order, each a list of NodeDescriptions
     */
    @SuppressWarnings("unused")
    public List<List<NodeDescription>> byRankAscending() {
        List<List<NodeDescription>> nodeDescriptions = new ArrayList<>();

        for (NodeRegistration registration : nodes) {
            while (nodeDescriptions.size() <= registration.getR())
                nodeDescriptions.add(new ArrayList<>());

            nodeDescriptions.get(registration.r).add(registration.toDescription());
        }

        return nodeDescriptions;
    }

    /**
     * Returns registered nodes by rank - in the manner of a breadth-first search -
     * in descending order, from leaf to root.
     * @return      A list of ranks in reverse order, each a list of NodeDescriptions
     */
    @SuppressWarnings("unused")
    public List<List<NodeDescription>> byRankDescending() {
        return byRankAscending().reversed();
    }

    /**
     * Returns registered nodes by lineage - in the manner of a depth-first search -
     * in ascending order, from root to leaf.
     * @return      A list of NodeDescriptions
     */
    @SuppressWarnings("unused")
    public List<NodeDescription> byLineageAscending() {
        NodeRegistration rootRegistration = map.get(root);

        List<NodeDescription> nodeDescriptions = new ArrayList<>();
        preorderR(rootRegistration, nodeDescriptions);

        return nodeDescriptions;
    }

    /**
     * Returns registered nodes by lineage - in the manner of a depth-first search -
     * in descending order, from leaf to root.
     * @return      A list of NodeDescriptions
     */
    @SuppressWarnings("unused")
    public List<NodeDescription> byLineageDescending() {
        NodeRegistration rootRegistration = map.get(root);

        List<NodeDescription> nodeDescriptions = new ArrayList<>();
        postorderR(rootRegistration, nodeDescriptions);

        return nodeDescriptions;
    }

    // ** PRIVATE
    // Graph / Traversal
    private void preorderR(NodeRegistration registration, List<NodeDescription> nodeDescriptions) {
        nodeDescriptions.add(registration.toDescription());

        for (Node child : registration.getChildren())
            preorderR(map.get(child), nodeDescriptions);
    }

    private void postorderR(NodeRegistration registration, List<NodeDescription> nodeDescriptions) {
        for (Node child : registration.getChildren())
            postorderR(map.get(child), nodeDescriptions);

        nodeDescriptions.add(registration.toDescription());
    }
}
