package com.github.jjbcoding.trellis.service.disposal.object;

import com.github.jjbcoding.trellis.service.Injectable;
import com.github.jjbcoding.trellis.service.Node;

import java.util.List;

public class NodeRegistration {
    // ----- DYNAMIC
    // *** FIELDS
    Node node;
    Node parent;
    List<Node> children;
    List<Injectable> injectables;
    int r;

    // *** CONSTRUCTORS
    public NodeRegistration(Node node, List<Node> children, Node parent, List<Injectable> injectables) {
        this.node = node;
        this.children = children;
        this.parent = parent;
        this.injectables = injectables;
    }

    // *** METHODS
    // Conversion
    public NodeDescription toDescription() {
        return new NodeDescription(node, injectables);
    }

    // Getters
    /**
     * Gets the Node.
     * @return              The Node
     */
    @SuppressWarnings("unused")
    public Node getNode() {
        return node;
    }

    /**
     * Gets the parent Node.
     * @return              The parent Node
     */
    @SuppressWarnings("unused")
    public Node getParent() {
        return parent;
    }

    /**
     * Gets the list of Injectables.
     * @return              The list of Injectables
     */
    @SuppressWarnings("unused")
    public List<Injectable> getInjectables() {
        return injectables;
    }

    /**
     * Gets the list of children Nodes.
     * @return              The list of children Nodes
     */
    @SuppressWarnings("unused")
    public List<Node> getChildren() {
        return children;
    }

    /**
     * Gets the rank.
     * @return              The rank
     */
    @SuppressWarnings("unused")
    public int getR() {
        return r;
    }

    // Setters
    /**
     * Sets the Node.
     * @param node          The node
     */
    @SuppressWarnings("unused")
    public void setNode(Node node) {
        this.node = node;
    }

    /**
     * Sets the parent Node.
     * @param parent        The parent Node
     */
    @SuppressWarnings("unused")
    public void setParent(Node parent) {
        this.parent = parent;
    }

    /**
     * Sets the list of Injectables.
     * @param injectables   The list of Injectables
     */
    @SuppressWarnings("unused")
    public void setInjectables(List<Injectable> injectables) {
        this.injectables = injectables;
    }

    /**
     * Sets the list of children Nodes.
     * @param children      The list of children Nodes.
     */
    @SuppressWarnings("unused")
    public void setChildren(List<Node> children) {
        this.children = children;
    }

    /**
     * Sets the rank.
     * @param r             The rank
     */
    @SuppressWarnings("unused")
    public void setR(int r) {
        this.r = r;
    }
}
