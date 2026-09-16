package com.github.jjbcoding.trellis.service.disposal.processor.defaults;

/**
 * Whether Injectables or Nodes are closed first.
 * Used by {@link DefaultDisposalProcessor}.
 */
public enum DisposalOrder {
    InjectablesThenNode,
    NodeThenInjectables
}
