package habitPop.controller;

import habitPop.model.AppStatus;
import habitPop.model.DataBaseInterface.DatabaseManager;
import habitPop.model.Habit;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.Node;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class NewHabitController {
    private AppStatus appState;
    private DatabaseManager databaseManager;
    private MainController mainController;


    // ---------------- Schedule checkboxes ----------------

    @FXML
    private CheckBox mondayCheck;

    @FXML
    private CheckBox tuesdayCheck;

    @FXML
    private CheckBox wednesdayCheck;

    @FXML
    private CheckBox thursdayCheck;

    @FXML
    private CheckBox fridayCheck;

    @FXML
    private CheckBox saturdayCheck;

    @FXML
    private CheckBox sundayCheck;


    // ---------------- Time containers ----------------

    @FXML
    private FlowPane mondayTimes;

    @FXML
    private FlowPane tuesdayTimes;

    @FXML
    private FlowPane wednesdayTimes;

    @FXML
    private FlowPane thursdayTimes;

    @FXML
    private FlowPane fridayTimes;

    @FXML
    private FlowPane saturdayTimes;

    @FXML
    private FlowPane sundayTimes;

    // --- data
    @FXML
    private TextField habitNameField;
    @FXML
    private TextField locationField;
    @FXML
    private TextField notesField;
    @FXML
    private ToggleGroup priorityGroup;

    // Actual schedule data
    private final Map<DayOfWeek, List<LocalTime>> timesOfWeek = new HashMap<>();

    @FXML
    private void cancel(ActionEvent event) {
      // Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        //stage.close();
        mainController.showHome();

    }

    @FXML private void addHabit(ActionEvent event) {

        String name = habitNameField.getText();
        String location = locationField.getText();
        String notes = notesField.getText();

        RadioButton selectedPriority = (RadioButton) priorityGroup.getSelectedToggle();
        int priority = switch (selectedPriority.getText()) {
            case "Low" -> 1;
            case "Medium" -> 2;
            case "High" -> 3;
            default -> 1;
        };

        Habit habit = new Habit(name, notes, priority,timesOfWeek );

        boolean successful = databaseManager.addHabitAndLocation(habit,location) ;

        if (successful) {
            appState.addHabit(habit);
           // Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            //stage.close();
            mainController.showHome();
        }
    }


    //----------- add time
    private LocalTime openTimePicker() {

    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/habitpop/AddTime.fxml"));

        Parent root = loader.load();

        AddTimeController controller = loader.getController();

        Stage popup = new Stage();

        popup.setTitle("Add Time");

        popup.initModality(Modality.APPLICATION_MODAL);
        popup.initOwner(habitNameField.getScene().getWindow());

        popup.setScene(new Scene(root));
        popup.showAndWait();

        return controller.getSelectedTime();

    }
    catch (IOException e) {
        e.printStackTrace();
        return null;
    }
}

    private void addTime(DayOfWeek day, FlowPane container, LocalTime time) {
        timesOfWeek.get(day).add(time);
        addTimeChip(container, day, time);
    }

    private void addTimeChip(FlowPane container, DayOfWeek day, LocalTime time) {

        HBox chip = new HBox();

        chip.setSpacing(5);
        Label timeLabel = new Label(time.format(DateTimeFormatter.ofPattern("h:mm a")));
        Button removeButton = new Button("×");


        removeButton.setOnAction(event -> {
            // Remove from actual schedule data
            timesOfWeek.get(day).remove(time);
            // Remove visible chip
            container.getChildren().remove(chip);
        });

        chip.getChildren().addAll(timeLabel, removeButton);
        chip.getStyleClass().add("time-chip");
        container.getChildren().add(chip);
    }

    @FXML
    private void addMondayTime() {
        LocalTime time = openTimePicker();
        if (time == null) { return; }

        addTime(DayOfWeek.MONDAY, mondayTimes, time);
        mondayCheck.setSelected(true);
    }


    @FXML
    private void addTuesdayTime() {
        LocalTime time = openTimePicker();
        if (time == null) { return; }


        addTime(DayOfWeek.TUESDAY, tuesdayTimes, time);
        tuesdayCheck.setSelected(true);
    }


    @FXML
    private void addWednesdayTime() {
        LocalTime time = openTimePicker();
        if (time == null) { return; }

        addTime(DayOfWeek.WEDNESDAY, wednesdayTimes, time);
        wednesdayCheck.setSelected(true);
    }


    @FXML
    private void addThursdayTime() {
        LocalTime time = openTimePicker();
        if (time == null) { return; }


        addTime(DayOfWeek.THURSDAY, thursdayTimes, time );
        thursdayCheck.setSelected(true);
    }


    @FXML
    private void addFridayTime() {
        LocalTime time = openTimePicker();
        if (time == null) { return; }


        addTime(DayOfWeek.FRIDAY, fridayTimes, time);
        fridayCheck.setSelected(true);
    }


    @FXML
    private void addSaturdayTime() {
        LocalTime time = openTimePicker();
        if (time == null) { return; }

        addTime(DayOfWeek.SATURDAY, saturdayTimes, time);
        saturdayCheck.setSelected(true);
    }


    @FXML
    private void addSundayTime() {
        LocalTime time = openTimePicker();
        if (time == null) { return; }

        addTime(DayOfWeek.SUNDAY, sundayTimes, time);
        sundayCheck.setSelected(true);
    }



//----------- on Start


    @FXML
    private void initialize() {

        for (DayOfWeek day : DayOfWeek.values()) {
            timesOfWeek.put(day, new ArrayList<>());
        }
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void setAppState(AppStatus appState) {
        this.appState = appState;
    }


    public void setDatabaseManager(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }
}
