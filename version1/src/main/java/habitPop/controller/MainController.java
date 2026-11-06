package habitPop.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class MainController {

    @FXML
    private StackPane pageContainer;

    @FXML
    private VBox homePage;

    @FXML
    private void addHabit() {
        System.out.println("Add Habit clicked");
    }


    private void showLocationPage() {

        try {

            FXMLLoader loader =
                    new FXMLLoader(
                            getClass().getResource(
                                    "/habitpop/Location_tab.fxml"
                            )
                    );

            Parent page =
                    loader.load();

            pageContainer.getChildren().setAll(page);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }









}