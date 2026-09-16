package com.github.jjbcoding.trellis.examples.enums.states;

import com.github.jjbcoding.trellis.service.Node;
import com.github.jjbcoding.trellis.service.readers.IReader;

public class StateReader
    implements IReader<Node, States> {
    @Override
    public Class<? extends Node> getAssociation(States constant) {
        return constant.getNodeClass();
    }
}
