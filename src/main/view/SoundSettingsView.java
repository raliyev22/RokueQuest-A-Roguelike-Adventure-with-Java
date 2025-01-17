package main.view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import main.Main;
import main.utils.SoundEffects;

import java.util.HashMap;

public class SoundSettingsView extends Application {

    private HashMap<String, Slider> soundSliders;
    private ToggleButton muteToggle;
    private Button backButton;
    private Button resetButton;
    private SoundEffects soundPlayer;

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.soundPlayer = SoundEffects.getInstance(); // SoundEffects singleton instance
        showSoundSettingsMenu();
    }

    private void showSoundSettingsMenu() {
        // Root layout with background image
        VBox root = new VBox(20);
        root.setPadding(new Insets(15));
        root.setAlignment(Pos.TOP_CENTER);

        BackgroundImage backgroundImage = new BackgroundImage(
                new Image("/rokue-like_assets/rokue33.png", 600, 400, false, true),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT
        );
        root.setBackground(new Background(backgroundImage));

        // Title Label
        Label titleLabel = new Label("Sound Settings");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: white;");
        titleLabel.setAlignment(Pos.CENTER);

        // Create sound sliders for each sound effect
        soundSliders = new HashMap<>();
        GridPane sliderGrid = new GridPane();
        sliderGrid.setPadding(new Insets(10));
        sliderGrid.setHgap(10);
        sliderGrid.setVgap(10);
        sliderGrid.setAlignment(Pos.CENTER);

        // Add sliders with labels
        addSliderToGrid("Background Music", "background", -80, 6, soundPlayer.getVolume("background"), sliderGrid, 0);
        addSliderToGrid("Step", "step", -80, 6, soundPlayer.getVolume("step"), sliderGrid, 1);
        addSliderToGrid("Winning Sound", "gameWinner", -80, 6, soundPlayer.getVolume("gameWinner"), sliderGrid, 2);
        addSliderToGrid("Losing Sound", "gameLoser", -80, 6, soundPlayer.getVolume("gameLoser"), sliderGrid, 3);
        addSliderToGrid("Wizard", "wizard", -80, 6, soundPlayer.getVolume("wizard"), sliderGrid, 4);
        addSliderToGrid("Archer", "archer", -80, 6, soundPlayer.getVolume("archer"), sliderGrid, 5);
        addSliderToGrid("Fighter", "fighter", -80, 6, soundPlayer.getVolume("fighter"), sliderGrid, 6);
        addSliderToGrid("Sparkle", "sparkle", -80, 6, soundPlayer.getVolume("sparkle"), sliderGrid, 7);

        // Mute Toggle Button
        muteToggle = new ToggleButton("Mute All");
        muteToggle.setStyle("-fx-background-color: #555; -fx-text-fill: white;");
        muteToggle.setPrefWidth(150);
        muteToggle.setOnAction(e -> {
            soundPlayer.playSoundEffectInThread("menuButtons");
            boolean muted = muteToggle.isSelected();
            if (muted) {
                muteAll();
            } else {
                unmuteAll();
            }
        });

        // Reset Button
        resetButton = new Button("Reset to Default");
        resetButton.setStyle("-fx-background-color: #444; -fx-text-fill: white;");
        resetButton.setPrefWidth(150); 
        resetButton.setOnAction(e -> resetToDefault());

        // Back Button
        backButton = new Button("Back");
        backButton.setStyle("-fx-background-color: #444; -fx-text-fill: white;");
        backButton.setPrefWidth(150);
        backButton.setOnAction(e -> {
            soundPlayer.playSoundEffectInThread("menuButtons");
            Main mainPage = new Main();
            mainPage.start(primaryStage);
        });

        // Buttons Box
        HBox buttonsBox = new HBox(15, muteToggle, resetButton, backButton);
        buttonsBox.setAlignment(Pos.CENTER);
        buttonsBox.setTranslateY(-15);

        // Add everything to the root
        root.getChildren().addAll(titleLabel, sliderGrid, buttonsBox);

        // Create and set the scene
        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
    }

    private void addSliderToGrid(String labelText, String key, double min, double max, double defaultValue, GridPane grid, int row) {
        Label label = new Label(labelText);
        label.setStyle("-fx-text-fill: white;");

        Slider slider = createSlider(min, max, defaultValue);
        slider.setId(key);
        soundSliders.put(key, slider);

        grid.add(label, 0, row);
        grid.add(slider, 1, row);
    }

    private Slider createSlider(double min, double max, double defaultValue) {
        Slider slider = new Slider(min, max, defaultValue);
        slider.setShowTickMarks(true);
        slider.setShowTickLabels(false);
        slider.setMajorTickUnit(10);
        slider.setBlockIncrement(5);
        slider.setPrefWidth(200);
        slider.setStyle("-fx-control-inner-background: #555;");

        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                float volume = newValue.floatValue();
                String id = slider.getId();
                if (id != null) {
                    soundPlayer.setVolume(id, volume);
                }
            }
        });

        return slider;
    }

    private void resetToDefault() {
        soundPlayer.playSoundEffectInThread("menuButtons");

        soundSliders.get("background").setValue(-20);
        soundSliders.get("step").setValue(-10);
        soundSliders.get("gameWinner").setValue(-15);
        soundSliders.get("gameLoser").setValue(-15);
        soundSliders.get("archer").setValue(-5);
        soundSliders.get("fighter").setValue(-5);
        soundSliders.get("wizard").setValue(-10);
        soundSliders.get("sparkle").setValue(-5);

        if (muteToggle.isSelected()) {
            muteToggle.setSelected(false);
        }
    }

    private void muteAll() {
        muteToggle.setText("Unmute All");
        soundSliders.values().forEach(slider -> {
            slider.setDisable(true);
            soundPlayer.setVolume(slider.getId(), -80f);
        });
    }
    
    private void unmuteAll() {
        muteToggle.setText("Mute All");
        soundSliders.values().forEach(slider -> slider.setDisable(false));
        applyVolumeSettings();
    }

    private void applyVolumeSettings() {
        soundSliders.forEach((key, slider) -> {
            float volume = (float) slider.getValue();
            soundPlayer.setVolume(key, volume);
        });
    }
}
