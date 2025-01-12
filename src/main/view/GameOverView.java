//package main.view;
//
//import javafx.scene.Scene;
//import javafx.scene.control.Button;
//import javafx.scene.layout.StackPane;
//import javafx.scene.text.Font;
//import javafx.scene.text.Text;
//import javafx.stage.Stage;
//
//public class GameOverView {
//    private final Scene scene;
//
//    public GameOverView(Stage primaryStage) {
//        StackPane root = new StackPane();
//
//        // Game Over message
//        Text gameOverText = new Text("GAME OVER");
//        gameOverText.setFont(Font.font("Arial", 50));
//        gameOverText.setStyle("-fx-fill: red;");
//
//        // Exit button
//        Button exitButton = new Button("Exit");
//        exitButton.setStyle("-fx-font-size: 20px;");
//        exitButton.setOnAction(e -> {
//            primaryStage.close(); // Close the application
//        });
//
//        root.getChildren().addAll(gameOverText, exitButton);
//        gameOverText.layoutXProperty().bind(StackPane.widthProperty().subtract(gameOverText.widthProperty()).divide(2));
//        gameOverText.layoutYProperty().bind(stackPane.heightProperty().subtract(gameOverText.heightProperty()).divide(2));
//
//// Position the exitButton manually at the bottom center
//        exitButton.layoutXProperty().bind(stackPane.widthProperty().subtract(exitButton.widthProperty()).divide(2));
//        exitButton.layoutYProperty().bind(stackPane.heightProperty().subtract(exitButton.heightProperty()).subtract(20));
//        exitButton.setTranslateY(-50); // Move the button up slightly
//
//        scene = new Scene(root, 800, 600); // Set appropriate size for the game over screen
//    }
//
//    public Scene getScene() {
//        return scene;
//    }
//}