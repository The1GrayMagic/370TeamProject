package habitPop.controller;

import habitPop.model.AppStatus;
import habitPop.model.DataBaseInterface.DatabaseManager;

public class HabitALLTableController {

    private DatabaseManager databaseManager = new DatabaseManager();
    private AppStatus appState = new AppStatus();

    public void setAppState(AppStatus appState) {
        this.appState = appState;
    }
    public void setDatabaseManager(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }


    public void showAll() {
    }

    public void showUpcoming() {

    }
}
