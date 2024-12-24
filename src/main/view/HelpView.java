package main.view;

import javafx.animation.*;
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
import main.Main;

public class HelpView extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        showMainHelpMenu();
    }

    private void showMainHelpMenu() {
        // Root layout
        StackPane root = new StackPane();

        // Add a background image
        Image backgroundImage = new Image("/rokue-like_assets/rokue33.png");
        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setFitWidth(600);
        backgroundView.setFitHeight(400);
        backgroundView.setPreserveRatio(true);
        root.getChildren().add(backgroundView);

        // Title
        Text title = new Text("Help Menu");
        title.setFont(Font.font("Verdana", 30));
        title.setFill(Color.GOLD);
        addTitleAnimation(title);

        // Buttons for sections
        Button gameplayButton = createStyledButton("Gameplay", this::showGameplaySection);
        Button objectivesButton = createStyledButton("Objectives", this::showObjectivesSection);
        Button monstersButton = createStyledButton("Monsters", this::showMonstersSection);
        Button enchantmentsButton = createStyledButton("Enchantments", this::showEnchantmentsSection);
        Button controlsButton = createStyledButton("Controls", this::showControlsSection);

        // Back button
        Button backButton = createStyledButton("Back to Main Menu", this::backToMainMenu);

        // VBox for buttons
        VBox buttonBox = new VBox(10, gameplayButton, objectivesButton, monstersButton, enchantmentsButton, controlsButton, backButton);
        buttonBox.setAlignment(Pos.CENTER);

        // Content box
        VBox contentBox = new VBox(20, title, buttonBox);
        contentBox.setAlignment(Pos.CENTER);

        root.getChildren().add(contentBox);

        // Scene
        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Help Menu");
        primaryStage.show();
    }

    private void showSection(String titleText, String contentText) {
        // Root layout
        StackPane root = new StackPane();

        // Add a background image
        Image backgroundImage = new Image("/rokue-like_assets/rokue33.png");
        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setFitWidth(600);
        backgroundView.setFitHeight(400);
        backgroundView.setPreserveRatio(true);
        root.getChildren().add(backgroundView);

        // Title
        Text title = new Text(titleText);
        title.setFont(Font.font("Verdana", 30));
        title.setFill(Color.GOLD);
        addTitleAnimation(title);

        // Content text
        Text content = new Text(contentText);
        content.setFont(Font.font("Arial", 16));
        content.setFill(Color.WHITE);
        content.setWrappingWidth(500);

        // Back button
        Button backButton = createStyledButton("Back to Help Menu", this::showMainHelpMenu);

        // VBox layout
        VBox layout = new VBox(20, title, content, backButton);
        layout.setAlignment(Pos.CENTER);

        root.getChildren().add(layout);

        // Scene
        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Button createStyledButton(String text, Runnable action) {
        Button button = new Button(text);
        button.setPrefWidth(200);
        button.setStyle(
            "-fx-background-color: #303843; " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 16px; " +
            "-fx-padding: 10px 20px; " +
            "-fx-background-radius: 10; " +
            "-fx-border-color: #FFFFFF; " +
            "-fx-border-width: 1px; " +
            "-fx-border-radius: 10;"
        );
        button.setOnMouseEntered(e -> button.setStyle(
            "-fx-background-color: #505A63; " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 16px; " +
            "-fx-padding: 10px 20px; " +
            "-fx-background-radius: 10; " +
            "-fx-border-color: #FFFFFF; " +
            "-fx-border-width: 1px; " +
            "-fx-border-radius: 10;"
        ));
        button.setOnMouseExited(e -> button.setStyle(
            "-fx-background-color: #303843; " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 16px; " +
            "-fx-padding: 10px 20px; " +
            "-fx-background-radius: 10; " +
            "-fx-border-color: #FFFFFF; " +
            "-fx-border-width: 1px; " +
            "-fx-border-radius: 10;"
        ));
        button.setOnAction(e -> action.run());
        return button;
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

    private void backToMainMenu() {
        Main mainPage = new Main();
        mainPage.start(primaryStage);
    }

    private void showGameplaySection() {
        showSection("Gameplay", "- Use arrow keys to move the hero around the dungeon.\n" +
                "- Click on objects near the hero to search for runes.\n" +
                "- Avoid monsters and collect enchantments to assist your journey.");
    }

    private void showObjectivesSection() {
        showSection("Objectives", "- Find the hidden rune in each hall to progress to the next hall.\n" +
                "- Complete all halls to win the game.");
    }

    private void showMonstersSection() {
        showSection("Monsters", "- Archer: Shoots arrows if the hero is within 4 squares.\n" +
                "- Fighter: Attempts to stab the hero when adjacent.\n" +
                "- Wizard: Teleports the rune randomly every 5 seconds.");
    }

    private void showEnchantmentsSection() {
        showSection("Enchantments", "- Extra Time: Adds 5 seconds to the timer.\n" +
                "- Reveal: Highlights a 4x4 area where the rune might be hidden.\n" +
                "- Cloak of Protection: Hides the hero from archer monsters.\n" +
                "- Luring Gem: Distracts fighter monsters by throwing a lure.\n" +
                "- Extra Life: Adds an extra life to the hero.");
    }

    private void showControlsSection() {
        showSection("Controls", "- R: Use the Reveal enchantment.\n" +
                "- P: Use the Cloak of Protection enchantment.\n" +
                "- B + A/D/W/S: Use the Luring Gem enchantment to throw it in a direction.");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
