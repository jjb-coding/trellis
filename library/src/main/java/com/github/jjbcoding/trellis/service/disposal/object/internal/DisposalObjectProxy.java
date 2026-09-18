package com.github.jjbcoding.trellis.service.disposal.object.internal;

import com.github.jjbcoding.trellis.service.Injectable;
import com.github.jjbcoding.trellis.service.Node;

import java.util.List;

public class DisposalObjectProxy {
    public static void register(DisposalObjectInternal object, Node node, List<Node> children, List<Injectable> injectables) {
        object.register(node, children, injectables);
    }
    public static void finalise(DisposalObjectInternal object) {
        object.finalise();
    }
}
