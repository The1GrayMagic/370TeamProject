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
    private MainController mainController;


    @FXML
    private AnchorPane homeListContainer;




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
        Picker(); // pick cant be in initialize becasue appStatus need to be passed which is after initializtion.
        //so pick once it gets the appstatus.

    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }
    public void setDatabaseManager(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
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
