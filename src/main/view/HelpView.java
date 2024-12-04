package main.view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class HelpView {

    public Scene createHelpScene(Stage primaryStage, Scene mainMenuScene) {
        // Main layout for Help menu
        VBox helpLayout = new VBox(20);
        helpLayout.setAlignment(Pos.CENTER);
        helpLayout.setPadding(new javafx.geometry.Insets(30));

        // Help Title
        Text helpTitle = new Text("Help Menu");
        helpTitle.setFont(Font.font("Verdana", 24));
        helpTitle.setFill(Color.GOLD);

        // Help Content
        Text helpContent = new Text(
                "Welcome to Rokue-Like Adventure!\n\n" +
                        "1. Start a New Game: Begin your epic adventure.\n" +
                        "2. Controls: Use arrow keys to move, space to interact.\n" +
                        "3. Objective: Explore, survive, and conquer challenges.\n\n" +
                        "Good luck and have fun!"
        );
        helpContent.setFont(Font.font("Arial", 16));
        helpContent.setFill(Color.WHITE);
        helpContent.setWrappingWidth(400);

        // Back Button
        Button backButton = new Button("Back to Main Menu");
        backButton.setPrefWidth(200);
        backButton.setOnAction(e -> primaryStage.setScene(mainMenuScene));

        // Add elements to layout
        helpLayout.getChildren().addAll(helpTitle, helpContent, backButton);

        // Background styling
        StackPane root = new StackPane();
        root.getChildren().add(helpLayout);
        root.setBackground(new Background(new BackgroundFill(Color.DARKSLATEGRAY, CornerRadii.EMPTY, javafx.geometry.Insets.EMPTY)));

        return new Scene(root, 600, 400);
    }
}
