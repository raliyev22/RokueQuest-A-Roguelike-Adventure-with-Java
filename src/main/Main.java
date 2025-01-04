package main;

import javafx.animation.*;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.utils.SoundEffects;
import main.view.BuildModeView;
//import main.view.CustomAlertView;
import main.view.HelpView;

public class Main extends Application {
    SoundEffects soundPlayer = SoundEffects.getInstance();


    @Override
    public void start(Stage primaryStage) {
        // Main layout
        VBox mainMenu = new VBox(20);
        mainMenu.setAlignment(Pos.CENTER);
        mainMenu.setPadding(new javafx.geometry.Insets(30));

        // Title with animations
        Text title = new Text("Rokue-Like Adventure");
        title.setFont(Font.font("Verdana", 30));
        title.setFill(Color.GOLD);
        addTitleAnimation(title);

        // Buttons
        Button startButton = createStyledButton("Start a New Game");
        Button helpButton = createStyledButton("Help");
        Button exitButton = createStyledButton("Exit");

        // Button Actions
        startButton.setOnAction(e -> startNewGame(primaryStage));
        helpButton.setOnAction(e -> showHelp(primaryStage));
        exitButton.setOnAction(e -> showExitConfirmation(primaryStage));

        // Add elements to VBox
        mainMenu.getChildren().addAll(title, startButton, helpButton, exitButton);

        // Background styling
        StackPane root = new StackPane();

        // Add a background image
        Image backgroundImage = new Image("/rokue-like_assets/rokue33.png");
        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setFitWidth(600);  // Adjust the size as needed
        backgroundView.setFitHeight(400); // Adjust the size as needed
        backgroundView.setPreserveRatio(true); // Maintain the aspect ratio
        backgroundView.setOpacity(1.0); // Fully visible, no dimming
        root.getChildren().add(backgroundView); // Ensure it's at the bottom layer

        // Add the VBox menu as the second layer
        root.getChildren().add(mainMenu);

        // Create Scene
        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Rokue-Like Adventure");
        javafx.geometry.Rectangle2D screenBounds1 = javafx.stage.Screen.getPrimary().getVisualBounds();
        
        // Set up the main stage in the center of the screen
        primaryStage.setX((screenBounds1.getWidth() - 600) / 2); // Replace 600 with the width of the mainPage scene
        primaryStage.setY((screenBounds1.getHeight() - 400) / 2); // Replace 400 with the height of the mainPage scene
        primaryStage.setFullScreen(false);

        soundPlayer.addSoundEffect("background", "src/main/sounds/background.wav");
        soundPlayer.setVolume("background", -25);   
        soundPlayer.loopSoundEffect("background");

        primaryStage.show();
    }

    private void addTitleAnimation(Text title) {
        // Gradient Color Animation (Orange and Yellow tones)
        Timeline gradientAnimation = new Timeline(
            new KeyFrame(Duration.ZERO, new KeyValue(title.fillProperty(), Color.ORANGE)),
            new KeyFrame(Duration.seconds(1), new KeyValue(title.fillProperty(), Color.GOLD)),
            new KeyFrame(Duration.seconds(2), new KeyValue(title.fillProperty(), Color.YELLOW)),
            new KeyFrame(Duration.seconds(3), new KeyValue(title.fillProperty(), Color.ORANGE))
        );
        gradientAnimation.setCycleCount(Animation.INDEFINITE);
    
        // Glow Effect
        DropShadow glow = new DropShadow();
        glow.setColor(Color.ORANGE);
        glow.setRadius(20);
        title.setEffect(glow);
    
        Timeline glowAnimation = new Timeline(
            new KeyFrame(Duration.ZERO, new KeyValue(glow.colorProperty(), Color.ORANGE)),
            new KeyFrame(Duration.seconds(1), new KeyValue(glow.colorProperty(), Color.GOLD)),
            new KeyFrame(Duration.seconds(2), new KeyValue(glow.colorProperty(), Color.YELLOW)),
            new KeyFrame(Duration.seconds(3), new KeyValue(glow.colorProperty(), Color.ORANGE))
        );
        glowAnimation.setCycleCount(Animation.INDEFINITE);
    
        // Scale Animation
        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(1.5), title);
        scaleTransition.setFromX(1);
        scaleTransition.setToX(1.2);
        scaleTransition.setFromY(1);
        scaleTransition.setToY(1.2);
        scaleTransition.setCycleCount(Animation.INDEFINITE);
        scaleTransition.setAutoReverse(true);
    
        // Combine Animations
        ParallelTransition parallelTransition = new ParallelTransition(
            gradientAnimation,
            glowAnimation,
            scaleTransition
        );
    
        // Play all animations
        parallelTransition.play();
    }
    


    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.setPrefWidth(200);
        button.setStyle(
            "-fx-background-color: #303843; " + // Background color
            "-fx-text-fill: white; " +         // Text color
            "-fx-font-size: 18px; " +         // Font size
            "-fx-padding: 10px 20px; " +      // Padding for better sizing
            "-fx-background-radius: 10; " +  // Rounded corners
            "-fx-border-color: #FFFFFF; " +   // Border color
            "-fx-border-width: 1px; " +       // Border thickness
            "-fx-border-radius: 10;"         // Rounded border to match background
        );
        button.setOnMouseEntered(e -> button.setStyle(
            "-fx-background-color: #505A63; " + // Highlight background
            "-fx-text-fill: white; " +
            "-fx-font-size: 18px; " +
            "-fx-padding: 10px 20px; " +
            "-fx-background-radius: 10; " +
            "-fx-border-color: #FFFFFF; " +
            "-fx-border-width: 1px; " +
            "-fx-border-radius: 10;"
        ));
        button.setOnMouseExited(e -> button.setStyle(
            "-fx-background-color: #303843; " + // Restore original background
            "-fx-text-fill: white; " +
            "-fx-font-size: 18px; " +
            "-fx-padding: 10px 20px; " +
            "-fx-background-radius: 10; " +
            "-fx-border-color: #FFFFFF; " +
            "-fx-border-width: 1px; " +
            "-fx-border-radius: 10;"
        ));
        return button;
    }

    private void startNewGame(Stage primaryStage) {
        // Transition to the BuildModeView or Game
        BuildModeView buildModeView = new BuildModeView();
        buildModeView.start(primaryStage);
    }

    private void showHelp(Stage primaryStage) {
        // Show Help Window
        HelpView helpMenuView = new HelpView();
        helpMenuView.start(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);     
    }
    private void showExitConfirmation(Stage primaryStage) {
        System.exit(0);
        // CustomAlertView.showAlert(
        //     primaryStage,
        //     "Exit Confirmation",
        //     "Are you sure you want to exit the game?"
        // );
        // // Add logic for handling OK button action inside `CustomAlertView` if needed
        // CustomAlertView.showAlert(primaryStage, "Exit Confirmation", "Are you sure you want to exit?");
        // // Handle the OK button click inside CustomAlertView to close the application.
    }

}
