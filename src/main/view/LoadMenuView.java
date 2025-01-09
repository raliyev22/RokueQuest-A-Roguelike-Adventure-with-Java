package main.view;

import java.io.File;
import java.io.IOException;
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
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

        initializeLoadButtons();

        VBox contentBox = new VBox(20,title,buttonBox);
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
        ArrayList<String> saves = new ArrayList<String>();
        try (Scanner scanner = new Scanner(file)){
            while (scanner.hasNextLine()){
                String line = scanner.nextLine();
                if(!line.isEmpty()){
                    saves.add(line);
                }
            }
        }catch (IOException e){
            System.out.println("An error occurred: " + e.getMessage());
        }

        for(int i=0;i<saves.size();i++){
            String save = saves.get(i);
            Button theButton = new Button(save);
            theButton.setOnAction(e -> {
                soundPlayer.playSoundEffect("menuButtons");
                // TODO: Load the game from the selected save file
                PlayModeController controller = new PlayModeController();
                controller.load(primaryStage, save);
                System.out.println("Loading save file: " + save);
            });
            buttonBox.getChildren().add(theButton);
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
}
