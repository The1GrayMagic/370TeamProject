module version1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    // JavaFX needs access to Main
    exports habitPop;

    // FXML needs access to your controller
    opens habitPop.controller to javafx.fxml;
    exports habitPop.controller;

}