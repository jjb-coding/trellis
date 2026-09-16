package com.github.jjbcoding.trellis.examples.enums.injectables;

import com.github.jjbcoding.trellis.service.Injectable;
import com.github.jjbcoding.trellis.service.readers.IReader;

@SuppressWarnings("unused")
public class InjectableReader
    implements IReader<Injectable, Injectables> {
    @Override
    public Class<? extends Injectable> getAssociation(Injectables constant) {
        return constant.getInjectableClass();
    }
}
