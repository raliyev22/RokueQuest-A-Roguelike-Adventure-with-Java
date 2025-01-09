package main.view;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.controller.PlayModeController;
import main.utils.SoundEffects;

public class LoadMenuView extends Application{
    private Stage primaryStage;
    private VBox buttonBox;
    SoundEffects soundPlayer = SoundEffects.getInstance();

    public void start(Stage primaryStage){
        this.primaryStage = primaryStage;
        soundPlayer.addSoundEffect("menuButtons", "src/main/sounds/menuButtons.wav");
        showLoadMenu();
    }

    private void showLoadMenu(){
        StackPane root = new StackPane();

        // Add a background image
        Image backgroundImage = new Image("/rokue-like_assets/rokue33.png");
        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setFitWidth(600);
        backgroundView.setFitHeight(400);
        backgroundView.setPreserveRatio(true);
        root.getChildren().add(backgroundView);

        // Title
        Text title = new Text("Load Menu");
        title.setFont(Font.font("Verdana", 30));
        title.setFill(Color.GOLD);
        addTitleAnimation(title);

        // Initialize the buttons for save files
        initializeLoadButtons();

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(buttonBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefViewportHeight(200);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        scrollPane.setStyle("-fx-background: transparent;-fx-background-color: transparent; -fx-border-color: transparent;");

        Button backButton = new Button("Back");
        backButton.setStyle("-fx-font-size: 16px; -fx-background-color: gold;");
        backButton.setOnAction(e -> {
            soundPlayer.playSoundEffect("menuButtons");
            // Logic to go back to the previous menu
            System.out.println("Back button clicked!");
            // Example: return to the main menu
            MainMenuView main = new MainMenuView();
            main.start(primaryStage);
    });

    // Main layout container
    VBox contentBox = new VBox(20, title, scrollPane,backButton);
    contentBox.setAlignment(Pos.CENTER);

    root.getChildren().add(contentBox);

    Scene scene = new Scene(root, 600, 400);
    primaryStage.setScene(scene);
    primaryStage.setTitle("Load Menu");
    primaryStage.show();
    }

    private void initializeLoadButtons(){

        buttonBox = new VBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        String filePath = "src/saveFiles/allSaveFiles.txt";
        File file = new File(filePath);
        ArrayList<String> saves = new ArrayList<>();

        // Load save files into the list
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (!line.isEmpty()) {
                    saves.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }

        // Create buttons for each save file
        for (String save : saves) {
            HBox saveRow = new HBox(10);
            saveRow.setAlignment(Pos.CENTER);

            // Load Button
            Button loadButton = new Button(save);
            loadButton.setOnAction(e -> {
                soundPlayer.playSoundEffect("menuButtons");
                // Load the game from the selected save file
                PlayModeController controller = new PlayModeController();
                controller.load(primaryStage, save);
                System.out.println("Loading save file: " + save);
            });

            // Delete Button
            Button deleteButton = new Button("Delete");
            deleteButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
            deleteButton.setOnAction(e -> {
                soundPlayer.playSoundEffect("menuButtons");
                // Delete the save file
                File saveFile = new File("src/saveFiles/" + save);
                if (saveFile.exists() && saveFile.delete()) {
                    System.out.println("Deleted save file: " + save);
                    buttonBox.getChildren().remove(saveRow); // Remove the row from the UI
                    // Optionally, update `allSaveFiles.txt` to remove the entry
                    updateSaveFileList(saves, save, filePath);
                } else {
                    System.out.println("Failed to delete save file: " + save);
                }
            });

            saveRow.getChildren().addAll(loadButton, deleteButton);
            buttonBox.getChildren().add(saveRow);
        }

    }


    private void addTitleAnimation(Text title) {
        Timeline gradientAnimation = new Timeline(
            new KeyFrame(Duration.ZERO, new KeyValue(title.fillProperty(), Color.ORANGE)),
            new KeyFrame(Duration.seconds(1), new KeyValue(title.fillProperty(), Color.GOLD)),
            new KeyFrame(Duration.seconds(2), new KeyValue(title.fillProperty(), Color.YELLOW)),
            new KeyFrame(Duration.seconds(3), new KeyValue(title.fillProperty(), Color.ORANGE))
        );
        gradientAnimation.setCycleCount(Animation.INDEFINITE);

        // Glow effect
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

        ParallelTransition animation = new ParallelTransition(gradientAnimation, glowAnimation);
        animation.play();
    }
    private void updateSaveFileList(ArrayList<String> saves, String saveToDelete, String filePath) {
        saves.remove(saveToDelete); // Remove from the list
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath, false))) {
            for (String save : saves) {
                writer.println(save);
            }
        } catch (IOException e) {
            System.out.println("An error occurred while updating the save file list: " + e.getMessage());
        }
    }
}

