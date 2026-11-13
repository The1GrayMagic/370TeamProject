package habitPop.controller;

import habitPop.model.AppStatus;
import habitPop.model.DataBaseInterface.DatabaseManager;
import habitPop.model.DayStatus;
import habitPop.model.Habit;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HabitALLTableController {

    private AppStatus appState;
    private DatabaseManager databaseManager;
    private MainController mainController;



    @FXML
    private TableView<Habit> habitTable;

    @FXML
    private TableColumn<Habit, String> nameColumn;

    @FXML
    private TableColumn<Habit, String> priorityColumn;

    @FXML
    private TableColumn<Habit, String> dateTimeColumn;

    @FXML
    private TableColumn<Habit, Boolean> completedColumn;

    public void setAppState(AppStatus appState) {
        this.appState = appState;
       // habitTable.setItems(appState.getHabits());
       // SetupColumes();
    }
    public void setDatabaseManager(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }




    private void SetupColumes(){

        LocalDate today = LocalDate.now();

        //Set the vale based on the key.
        //key is the data which is the habit list
        nameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        priorityColumn.setCellValueFactory(data -> {
            //get text based on which values it is
            String text = switch (data.getValue().getPriority()) {
                case 1 -> "Low";
                case 2 -> "Medium";
                case 3 -> "High";
                default -> "Unknown";
            };
            //return vaule, it is not a string becasue javafx need in a display way
            return new SimpleStringProperty(text);});

        dateTimeColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));

        dateTimeColumn.setCellValueFactory(data -> {
            //get data
            Habit habit = data.getValue();
            StringBuilder text = new StringBuilder();

            //get all value put into list
            for (DayOfWeek day : DayOfWeek.values()) {
                List<LocalTime> times = habit.getTimesOfWeek().get(day);
                if (times != null && !times.isEmpty()) {

                    text.append(day).append(": ");
                    for (LocalTime time : times) {
                        //formate time
                        text.append(time.format(DateTimeFormatter.ofPattern("h:mm a")));
                        text.append(" ");
                    }
                    text.append("\n");
                }
            }
            //retrun list and put into colume
            return new SimpleStringProperty(text.toString());
        });

        completedColumn.setCellFactory(CheckBoxTableCell.forTableColumn(completedColumn));
        completedColumn.setCellValueFactory(data -> {
                Habit habit = data.getValue(); //get habit
                 //get days of weeks
                //get today if completed
                DayStatus dayStatus = habit.getDay(today);

                boolean completed = dayStatus != null && dayStatus.getCompletions().containsValue(true);
            
                return new SimpleBooleanProperty(completed);});
    }
    @FXML
    private void initialize() {
        Map<DayOfWeek, List<LocalTime>> testSchedule = new HashMap<>();

        for (DayOfWeek day : DayOfWeek.values()) {
            testSchedule.put(day, new ArrayList<>());
        }

        testSchedule.get(DayOfWeek.MONDAY)
                .add(LocalTime.of(8, 0));

        Habit testHabit =
                new Habit(
                        "Workout",
                        "Test habit",
                        2,
                        testSchedule
                );

        habitTable.getItems().add(testHabit);

        SetupColumes();

        habitTable.setEditable(true);

        completedColumn.setCellFactory(
                CheckBoxTableCell.forTableColumn(completedColumn)
        );

        habitTable.setRowFactory(table -> {

            TableRow<Habit> row = new TableRow<>();

            row.setOnMouseClicked(event -> {

                if (!row.isEmpty() && event.getClickCount() == 2) {

                    Habit habit = row.getItem();

                    appState.setSelectedHabit(habit);

                    mainController.showViewHabit();
                }
            });

            return row;
        });
    }


}
