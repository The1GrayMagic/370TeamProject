package habitPop.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.stage.Stage;

import java.time.LocalTime;

public class AddTimeController {

    @FXML
    private Spinner<Integer> hourSpinner;

    @FXML
    private Spinner<Integer> minuteSpinner;

    @FXML
    private ComboBox<String> ampmCombo;


    // null means the user cancelled
    private LocalTime selectedTime;


    @FXML
    private void initialize() {

        hourSpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(
                        1,
                        12,
                        8
                ));

        minuteSpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(
                        0,
                        59,
                        0
                ));

        ampmCombo.getItems().addAll("AM", "PM");

        ampmCombo.setValue("AM");
    }


    @FXML
    private void addTime(ActionEvent event) {

        // Commit typed spinner values
        hourSpinner.increment(0);
        minuteSpinner.increment(0);

        int hour = hourSpinner.getValue();
        int minute = minuteSpinner.getValue();
        String ampm = ampmCombo.getValue();


        // Convert to 24-hour time

        if (ampm.equals("PM") && hour != 12) { hour += 12; }
        if (ampm.equals("AM") && hour == 12) { hour = 0; }

        selectedTime = LocalTime.of(hour, minute);
        close(event);
    }


    @FXML
    private void cancel(ActionEvent event) {
        selectedTime = null;
        close(event);
    }


    private void close(ActionEvent event) {
        Stage stage =(Stage) ((Node) event.getSource()).getScene().getWindow();stage.close();
    }


    public LocalTime getSelectedTime() {
        return selectedTime;
    }
}