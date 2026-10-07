module habitPop {
    requires javafx.controls;
    requires javafx.fxml;

    opens habitPop to javafx.fxml;
    exports habitPop;
}
