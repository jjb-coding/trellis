/**
 * Example module that demonstrates the Trellis library.
 */
module com.github.jjbcoding.trellis.example {
    requires java.sql;
    requires java.desktop;
    requires com.github.jjbcoding.trellis;
    requires com.github.jjbcoding.trellis.swing;

    exports com.github.jjbcoding.trellis.example.app;
    exports com.github.jjbcoding.trellis.example.displays;
    exports com.github.jjbcoding.trellis.example.enums.injectables;
    exports com.github.jjbcoding.trellis.example.enums.states;
    exports com.github.jjbcoding.trellis.example.injectables.transfer;
    exports com.github.jjbcoding.trellis.example.injectables.controllers;
    exports com.github.jjbcoding.trellis.example.nodes;
    exports com.github.jjbcoding.trellis.example.requests;
}