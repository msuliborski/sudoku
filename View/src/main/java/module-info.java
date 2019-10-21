open module ViewProject {
    requires ModelProject;
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires java.logging;
    requires java.sql;
    exports pl.comp.view;
}