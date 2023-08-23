module applicationtoolkit {
    requires transitive javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.kohsuke.github.api;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires com.fasterxml.jackson.databind;
    requires commons.configuration;

    opens com.kenyahmis.applicationtoolkit to javafx.fxml;
    exports com.kenyahmis.applicationtoolkit;
    exports com.kenyahmis.applicationtoolkit.Services;
    opens com.kenyahmis.applicationtoolkit.Services to javafx.fxml;
    exports com.kenyahmis.applicationtoolkit.Task;
    opens com.kenyahmis.applicationtoolkit.Task to javafx.fxml;
    exports com.kenyahmis.applicationtoolkit.utils;
    opens com.kenyahmis.applicationtoolkit.utils to javafx.fxml;
    exports com.kenyahmis.applicationtoolkit.controllers;
    opens com.kenyahmis.applicationtoolkit.controllers to javafx.fxml;
}