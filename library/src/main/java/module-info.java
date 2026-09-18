module com.github.jjbcoding.trellis {
    requires java.sql;
    requires java.desktop;
    exports com.github.jjbcoding.trellis.exceptions;
    exports com.github.jjbcoding.trellis.service;
    exports com.github.jjbcoding.trellis.service.annotations;
    exports com.github.jjbcoding.trellis.service.contributions;
    exports com.github.jjbcoding.trellis.service.contributions.defaults.title;
    exports com.github.jjbcoding.trellis.service.displays;
    exports com.github.jjbcoding.trellis.service.displays.defaults.swing;
    exports com.github.jjbcoding.trellis.service.disposal.object;
    exports com.github.jjbcoding.trellis.service.disposal.processor;
    exports com.github.jjbcoding.trellis.service.disposal.processor.defaults;
    exports com.github.jjbcoding.trellis.service.providers;
    exports com.github.jjbcoding.trellis.service.providers.defaults;
    exports com.github.jjbcoding.trellis.service.readers;
    exports com.github.jjbcoding.trellis.service.requests;
    exports com.github.jjbcoding.trellis.service.rules;
    exports com.github.jjbcoding.trellis.service.rules.defaults;
}