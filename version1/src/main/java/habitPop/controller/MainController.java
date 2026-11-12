package habitPop.controller;

import habitPop.model.AppStatus;
import habitPop.model.DataBaseInterface.DatabaseManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class MainController {

    private final DatabaseManager databaseManager = new DatabaseManager();
    private final AppStatus appState = new AppStatus();

    @FXML
    private AnchorPane pageContainer;


    private void loadPage(String fileName) {

        try {

            FXMLLoader loader = new FXMLLoader(
                            getClass().getResource("/habitpop/" + fileName)
                    );

            Parent page = loader.load();

            Object controller = loader.getController();

            if (controller instanceof HomeController homeController) {

                homeController.setAppState(appState);
                homeController.setDatabaseManager(databaseManager);

            }

            if (controller instanceof LocationController locationController) {

                locationController.setAppState(appState);
                locationController.setDatabaseManager(databaseManager);

            }

            if (controller instanceof PriorityController priorityController) {

                priorityController.setAppState(appState);
                priorityController.setDatabaseManager(databaseManager);

            }

            pageContainer.getChildren().setAll(page);

            // Make loaded page fill the AnchorPane
            AnchorPane.setTopAnchor(page, 0.0);
            AnchorPane.setBottomAnchor(page, 0.0);
            AnchorPane.setLeftAnchor(page, 0.0);
            AnchorPane.setRightAnchor(page, 0.0);

        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    @FXML
    private void showHome() {
        loadPage("Home.fxml");
    }

    @FXML
    private void showLocation() {
        loadPage("Location.fxml");
    }

    @FXML
    private void showPriority() {
        loadPage("Priority.fxml");
    }

    @FXML
    private void showSettings() {
        loadPage("Settings.fxml");
    }



    @FXML
    private void viewHabit() {

        loadPage("View_habit.fxml");

        /*
         * TODO later:
         *
         * Get which habit the user clicked.
         *
         * Get its ID.
         *
         * Give that Habit object / ID to the
         * ViewHabitController.
         */
    }

    @FXML
    private void initialize() {
        showHome();
    }

}