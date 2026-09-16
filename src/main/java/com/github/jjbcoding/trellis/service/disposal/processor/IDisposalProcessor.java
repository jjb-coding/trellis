package com.github.jjbcoding.trellis.service.disposal.processor;

import com.github.jjbcoding.trellis.service.AppService;
import com.github.jjbcoding.trellis.service.disposal.object.DisposalObject;

/**
 * A configured DisposalProcessor's consume method is invoked whenever the
 * Node tree is reorganised, or {@link AppService#close()} is called, and
 * a series of Nodes and Injectables that are no longer active is generated.
 * A DisposalProcessor can be used to shut down the Node or Injectable, i.e.
 * by calling {@link AutoCloseable#close()}.
 */
public interface IDisposalProcessor {
    /**
     * Processes the contents of a DisposalObject according to implementation.
     * @param disposalObject    The DisposalObject
     */
    void consume(DisposalObject disposalObject);
}
