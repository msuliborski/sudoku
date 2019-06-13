open module ModelProject {
    exports pl.comp.model;
    exports pl.comp.model.sudoku;
    exports pl.comp.model.solvers;
    exports pl.comp.model.dao;
    exports pl.comp.model.logs;
    exports pl.comp.model.exceptions;
    requires org.apache.commons.lang3;
    requires org.junit.jupiter.api;
//    requires sqlite.jdbc;
    requires java.sql;
    requires java.logging;
}
