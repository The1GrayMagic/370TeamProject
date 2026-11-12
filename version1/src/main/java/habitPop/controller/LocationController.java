package habitPop.controller;

import habitPop.model.AppStatus;
import habitPop.model.DataBaseInterface.DatabaseManager;

public class LocationController {

    private AppStatus appState;
    private DatabaseManager databaseManager;


    public void setAppState(AppStatus appState) {
        this.appState = appState;
    }


    public void setDatabaseManager(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }
}
