package habitPop.controller;

import habitPop.model.AppStatus;
import habitPop.model.DataBaseInterface.DatabaseManager;

public class ViewHabitController {
    private AppStatus appState;
    private DatabaseManager databaseManager;
    private MainController mainController;













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