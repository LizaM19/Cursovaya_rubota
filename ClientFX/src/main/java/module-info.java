module com.example.clientfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.desktop;
    requires java.logging;
    requires aopalliance;
    requires slf4j.api;
    requires com.google.common;

    opens com.example.clientfx to javafx.fxml;
    exports com.example.clientfx;
}