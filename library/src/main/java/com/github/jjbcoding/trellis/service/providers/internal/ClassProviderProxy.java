package com.github.jjbcoding.trellis.service.providers.internal;

import java.util.List;

public class ClassProviderProxy {
    // ----- STATIC
    public static <T> List<Class<? extends T>> getClasses(ClassProviderInternal<T> classProvider) {
        return classProvider.getClasses();
    }
}
