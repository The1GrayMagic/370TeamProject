package habitPop.controller;

import habitPop.model.DataBaseInterface.DatabaseManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.Node;
import javafx.stage.Stage;

public class NewHabitController {


    @FXML
    private TextField habitNameField;
    @FXML
    private TextField locationField;
    @FXML
    private TextField notesField;

    @FXML
    private Spinner<Integer> defaultHour;
    @FXML
    private Spinner<Integer> minuteSpinner;
    @FXML
    private ComboBox<String> defaultTime;

    @FXML
    private ToggleGroup priorityGroup;

    @FXML
    private void cancel(ActionEvent event) {

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML private void addHabit(ActionEvent event) {


        String name = habitNameField.getText();
        String location = locationField.getText();
        String notes = notesField.getText();

        int hour = defaultHour.getValue();
        int minute = minuteSpinner.getValue();
        String ampm = defaultTime.getValue();

        RadioButton selectedPriority =
                (RadioButton) priorityGroup.getSelectedToggle();

        String priority = selectedPriority.getText();


        System.out.println("Name: " + name);
        System.out.println("Location: " + location);
        System.out.println("Notes: " + notes);
        System.out.println("Hour: " + hour);
        System.out.println("Minute: " + minute);
        System.out.println("AM/PM: " + ampm);
        System.out.println("Priority: " + priority);

    }


    @FXML
    private void initialize() {

        defaultHour.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(
                1,   // minimum
                12,  // maximum
                1    // starting value
        ));

        minuteSpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(
                        0,   // minimum
                        59,  // maximum
                        0    // starting value
                )
        );

        defaultTime.getItems().addAll(
                "AM",
                "PM"
        );
        defaultTime.setValue("AM");
        defaultTime.setValue("AM");
    }




}
