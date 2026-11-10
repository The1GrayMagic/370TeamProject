package habitPop.controller;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ToggleGroup;

public class SettingsController {

    @FXML
    private ToggleGroup themeGroup;

    @FXML
    private ToggleGroup timeFormatGroup;

    @FXML
    private ToggleGroup weekStartGroup;

    @FXML
    private CheckBox enableNotificationsCheckBox;

    @FXML
    private CheckBox notificationSoundCheckBox;

    @FXML
    private CheckBox showCompletedCheckBox;

    @FXML
    private CheckBox showProgressGraphCheckBox;

    @FXML
    private ComboBox<String> defaultReminderTimeCombo;

    @FXML
    private ComboBox<String> defaultPageCombo;


    @FXML
    private void initialize() {

        defaultPageCombo.getItems().addAll(
                "Home",
                "Location",
                "Priority"
        );

        defaultPageCombo.setValue("Home");


        defaultReminderTimeCombo.getItems().addAll(
                "6:00 AM",
                "7:00 AM",
                "8:00 AM",
                "9:00 AM",
                "12:00 PM",
                "6:00 PM",
                "8:00 PM"
        );
    }


    @FXML
    private void saveSettings() {

        System.out.println("Save settings");

        // TODO:
        // save settings later
    }


    @FXML
    private void exportData() {

        System.out.println("Export data");
    }


    @FXML
    private void importData() {

        System.out.println("Import data");
    }


    @FXML
    private void clearHistory() {

        System.out.println("Clear history");
    }
}