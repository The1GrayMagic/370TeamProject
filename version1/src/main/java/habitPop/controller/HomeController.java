package habitPop.controller;

import habitPop.model.AppStatus;
import habitPop.model.DataBaseInterface.DatabaseManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class HomeController {

    @FXML
    private VBox homeRoot;

    @FXML
    private TableView<?> habitTable;
    private AppStatus appState;
    private DatabaseManager databaseManager;




    private void loadHomeContent(String s) {


    }


    @FXML
    private AnchorPane homeListContainer;

    @FXML
    private void showUpcoming() {
        loadHomeContent("UpcomingList.fxml");
    }



    @FXML
    private void showAll() {
        loadHomeContent("AllHabitsList.fxml");
    }
    @FXML
    private void addHabit() {

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/habitpop/New_habit.fxml")
            );

            Parent root = loader.load();

            Stage popup = new Stage();

            popup.setTitle("New Habit");

            popup.initModality(Modality.APPLICATION_MODAL);

            popup.initOwner(homeRoot.getScene().getWindow());
            popup.setScene(new Scene(root));
            popup.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void habitSelected() {

        /*
         * TODO:
         *
         * Get the selected row from habitTable.
         *
         * Example idea:
         *
         * Object selected =
         *      habitTable.getSelectionModel()
         *                .getSelectedItem();
         *
         * Later decide whether one click selects
         * and double click opens View_habit.fxml.
         */
    }
    public void setAppState(AppStatus appState) {
        this.appState = appState;
    }


    public void setDatabaseManager(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }
}
