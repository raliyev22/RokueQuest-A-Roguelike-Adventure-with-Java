package main;

import javafx.application.Application;
import javafx.stage.Stage;
import main.view.MainMenuView;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        MainMenuView menu = new MainMenuView();
        primaryStage.setFullScreen(false);
        menu.start(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);     
    }

}
