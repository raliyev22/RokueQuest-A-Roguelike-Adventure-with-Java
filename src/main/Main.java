package main;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;



public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        // Main layout
        VBox mainMenu = new VBox(20);
        mainMenu.setAlignment(Pos.CENTER);
        mainMenu.setPadding(new javafx.geometry.Insets(30));

        // Title
        Text title = new Text("Rokue-Like Adventure");
        title.setFont(Font.font("Verdana", 30));
        title.setFill(Color.GOLD);

        // Buttons
        Button startButton = new Button("Start a New Game");
        startButton.setPrefWidth(200);

        Button helpButton = new Button("Help");
        helpButton.setPrefWidth(200);

        Button exitButton = new Button("Exit");
        exitButton.setPrefWidth(200);
        
        
        // Button Actions
        startButton.setOnAction(e -> startNewGame(primaryStage));
        helpButton.setOnAction(e -> showHelp());
        exitButton.setOnAction(e -> primaryStage.close());
        
        // Add elements to VBox
        mainMenu.getChildren().addAll(title, startButton, helpButton, exitButton);
        
        // Background styling
        StackPane root = new StackPane();
        root.getChildren().add(mainMenu);
        root.setBackground(new Background(new BackgroundFill(Color.DARKSLATEGRAY, CornerRadii.EMPTY, javafx.geometry.Insets.EMPTY)));
        
        // Add a background image (Optional)
        Image backgroundImage = new Image("file:/Users/Raul/Desktop/Rokue.jpeg");
        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setFitWidth(600);
        backgroundView.setFitHeight(400);
        backgroundView.setOpacity(0.3); // Dim the background for readability
        root.getChildren().add(0, backgroundView);
        
        // Create Scene
        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Rokue-Like Adventure");
        primaryStage.show();
    }

    private void startNewGame(Stage primaryStage) {
        // Transition to Building Mode or Game
        System.out.println("Start New Game Clicked!");
    }

    private void showHelp() {
        // Show Help Window
        System.out.println("Help Clicked!");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
