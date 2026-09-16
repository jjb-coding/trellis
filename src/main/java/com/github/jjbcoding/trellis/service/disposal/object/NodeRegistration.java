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
    public Node getNode() {
        return node;
    }
    public Node getParent() {
        return parent;
    }
    public List<Injectable> getInjectables() {
        return injectables;
    }
    public List<Node> getChildren() {
        return children;
    }
    public int getR() {
        return r;
    }

    // Setters
    public void setNode(Node node) {
        this.node = node;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public void setInjectables(List<Injectable> injectables) {
        this.injectables = injectables;
    }

    public void setChildren(List<Node> children) {
        this.children = children;
    }

    public void setR(int r) {
        this.r = r;
    }
}
