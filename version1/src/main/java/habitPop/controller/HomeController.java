package habitPop.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import habitPop.model.AppStatus;
import habitPop.model.DataBaseInterface.DatabaseManager;
import habitPop.model.DayStatus;
import habitPop.model.Habit;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class HomeController {

    @FXML
    private VBox homeRoot;

    @FXML
    private TableView<?> habitTable;
    private AppStatus appState;
    private DatabaseManager databaseManager;
    private MainController mainController;


    @FXML
    private AnchorPane homeListContainer;

    @FXML
    private GridPane weeklyProgressGrid;

    @FXML
    private javafx.scene.control.ProgressIndicator completionProgress;


    private void loadHomeContent(String s) {


    }


    private void loadSubPage(String fileName) {

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/habitpop/" + fileName)
            );

            Parent page = loader.load();

            Object controller = loader.getController();

            if (controller instanceof HabitALLTableController allController) {

                allController.setAppState(appState);
                allController.setDatabaseManager(databaseManager);

            }

            if (controller instanceof UpcomingController upController) {

                upController.setAppState(appState);
                upController.setDatabaseManager(databaseManager);

            }

            homeListContainer.getChildren().setAll(page);

            // Make loaded page fill the AnchorPane
            AnchorPane.setTopAnchor(page, 0.0);
            AnchorPane.setBottomAnchor(page, 0.0);
            AnchorPane.setLeftAnchor(page, 0.0);
            AnchorPane.setRightAnchor(page, 0.0);

        } catch (IOException e) {

            e.printStackTrace();
        }
    }


private void updateProgressTracker() {

    if (appState == null || databaseManager == null
            || weeklyProgressGrid == null
            || completionProgress == null) {
        return;
    }

    List<Habit> habits = databaseManager.getAllHabitsWithHistory();

    if (habits == null) {
        habits = new java.util.ArrayList<>();
    }

    appState.setHabits(habits);

    weeklyProgressGrid.getChildren().clear();

    LocalDate today = LocalDate.now();

    double todayProgress = 0;

    // Display the previous 7 days
    for (int day = 0; day < 7; day++) {

        LocalDate date = today.minusDays(6 - day);

        int totalTasks = 0;
        int completedTasks = 0;

        // Check every habit
        for (Habit habit : habits) {

            List<java.time.LocalTime> times =
                    habit.getTimesOfWeek().get(date.getDayOfWeek());

            if (times == null) {
                continue;
            }

            for (java.time.LocalTime time : times) {

                totalTasks++;

                // Find completion history for this date
                List<DayStatus> history = habit.getHistory();

                if (history == null) {
                    continue;
                }

                for (DayStatus status : history) {

                    if (status.getDate().equals(date)) {

                        Boolean completed =
                                status.getCompletions().get(time);

                        if (Boolean.TRUE.equals(completed)) {
                            completedTasks++;
                        }

                        break;
                    }
                }
            }
        }

        // Calculate daily percentage
        double percentage = totalTasks == 0
                ? 0
                : (double) completedTasks / totalTasks;

        // Today's percentage is used for the circle
        if (date.equals(today)) {
            todayProgress = percentage;
        }

        // Convert percentage into 7 graph levels
        int filledCells = (int) Math.round(percentage * 7);

        // Create the weekly graph column
        for (int row = 0; row < 7; row++) {

            Region cell = new Region();

            cell.setMinSize(20, 20);
            cell.setPrefSize(100, 40);

            if (row >= 7 - filledCells && totalTasks > 0) {
                cell.setStyle(
                    "-fx-background-color: #66BB6A;" +
                    "-fx-background-radius: 5;"
                );
            } else {
                cell.setStyle(
                    "-fx-background-color: #EEEEEE;" +
                    "-fx-background-radius: 5;"
                );
            }

            Tooltip.install(cell, new Tooltip(
                    date + "\n" +
                    completedTasks + " / " + totalTasks +
                    " tasks completed\n" +
                    Math.round(percentage * 100) + "%"
            ));

            weeklyProgressGrid.add(cell, day, row);
        }
    }

    // Update the circular progress indicator
    completionProgress.setProgress(todayProgress);
}



    @FXML
    private void showUpcoming() {
        if (!appState.getHomePageUpcoming()){
            appState.SwitchPageUpcoming();
        }
        loadSubPage("HabitTableAll.fxml");
    }

    @FXML
    private void showAll() {
        if (appState.getHomePageUpcoming()){
            appState.SwitchPageUpcoming();
        }
        loadSubPage("HabitTableAll.fxml");
    }

    @FXML
    private void addHabit() {
        mainController.showNewHabit();
/*
        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/habitpop/New_habit.fxml")
            );

            Parent root = loader.load();

            NewHabitController controller = loader.getController();
            controller.setAppState(appState);
            controller.setDatabaseManager(databaseManager);


            Stage popup = new Stage();

            popup.initModality(Modality.APPLICATION_MODAL);
            popup.setTitle("New Habit");

            popup.initModality(Modality.APPLICATION_MODAL);

            popup.initOwner(homeRoot.getScene().getWindow());
            popup.setScene(new Scene(root));
            popup.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    public void setAppState(AppStatus appState) {
    this.appState = appState;
    Picker();
    updateProgressTracker();
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }
    public void setDatabaseManager(DatabaseManager databaseManager) {
    this.databaseManager = databaseManager;

    updateProgressTracker();
    }

    private void Picker(){
        if (appState.getHomePageUpcoming()){
            showUpcoming();
        }else {
            showAll();
        }

    }

    public void initialize() {
    }



}
