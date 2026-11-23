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

    // Retrieve habits, schedules, and completion history
    List<Habit> habits = databaseManager.getAllHabitsWithHistory();

    if (habits == null) {
        habits = new java.util.ArrayList<>();
    }

    appState.setHabits(habits);

    // Clear previous grid
    weeklyProgressGrid.getChildren().clear();

    // Configure grid spacing
    weeklyProgressGrid.getColumnConstraints().clear();
    weeklyProgressGrid.getRowConstraints().clear();
    weeklyProgressGrid.setGridLinesVisible(false);
    weeklyProgressGrid.setHgap(5);
    weeklyProgressGrid.setVgap(5);

    LocalDate today = LocalDate.now();

    // Find Monday of the current week
    LocalDate currentMonday = today.with(
        java.time.temporal.TemporalAdjusters
            .previousOrSame(java.time.DayOfWeek.MONDAY)
    );

    // Start 11 weeks before current week
    LocalDate startDate = currentMonday.minusWeeks(11);

    double todayProgress = 0;

    // Generate 12 weeks (84 days)
    for (int week = 0; week < 12; week++) {

        for (int day = 0; day < 7; day++) {

            LocalDate date = startDate.plusDays(week * 7L + day);

            Region square = new Region();
            square.setMinSize(8, 8);
            square.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

            GridPane.setHgrow(square, javafx.scene.layout.Priority.ALWAYS);
            GridPane.setVgrow(square, javafx.scene.layout.Priority.ALWAYS);
           // square.setMinSize(25, 25);
           // square.setPrefSize(25, 25);
           // square.setMaxSize(25, 25);

            // Hide future dates
            if (date.isAfter(today)) {
                square.setStyle("-fx-background-color: transparent;");
                weeklyProgressGrid.add(square, week, day);
                continue;
            }

            int totalTasks = 0;
            int completedTasks = 0;

            // Calculate completion for this date
            for (Habit habit : habits) {

                List<java.time.LocalTime> times =
                    habit.getTimesOfWeek().get(date.getDayOfWeek());

                if (times == null) {
                    continue;
                }

                for (java.time.LocalTime time : times) {

                    totalTasks++;

                    // Find history for this date
                    for (DayStatus status : habit.getHistory()) {

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

            // Calculate completion percentage
            double percentage = totalTasks == 0
                ? 0
                : (double) completedTasks / totalTasks;

            if (date.equals(today)) {
                todayProgress = percentage;
            }

            // Choose square color
            String color;

            if (totalTasks == 0 || percentage == 0) {
                color = "#EEEEEE";
            } else if (percentage <= 0.25) {
                color = "#D9F0E2";
            } else if (percentage <= 0.50) {
                color = "#A8DDBB";
            } else if (percentage <= 0.75) {
                color = "#63BE8B";
            } else {
                color = "#238B57";
            }

            square.setStyle(
                "-fx-background-color: " + color + ";" +
                "-fx-background-radius: 4;"
            );

            // Display information when hovering
            Tooltip.install(square, new Tooltip(
                date + "\n" +
                completedTasks + " / " + totalTasks +
                " habits completed\n" +
                Math.round(percentage * 100) + "%"
            ));

            // Add square to grid
            weeklyProgressGrid.add(square, week, day);
        }
    }

    // Update today's circular progress indicator
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
