/**
 * The Swing module contains Java Swing-specific extensions.
 */
module com.github.jjbcoding.trellis.swing {
    requires java.sql;
    requires java.desktop;
    requires com.github.jjbcoding.trellis;

    exports com.github.jjbcoding.trellis.swing.displays;
}