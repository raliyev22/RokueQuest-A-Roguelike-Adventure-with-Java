package main.view;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
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
        backgroundView.setFitWidth(600);  // Set width to 600
        backgroundView.setFitHeight(400); // Set height to 400
        backgroundView.setPreserveRatio(true);
        backgroundView.setOpacity(1.0); // Fully visible
        root.getChildren().add(backgroundView); // Add background

        // Title
        Label titleLabel = new Label("Help Menu");
        titleLabel.setFont(new Font("Arial", 24));
        titleLabel.setStyle("-fx-text-fill: #FFFFFF;");
        titleLabel.setAlignment(Pos.CENTER);

        // Buttons for sections
        Button gameplayButton = createSectionButton("Gameplay", this::showGameplaySection);
        Button objectivesButton = createSectionButton("Objectives", this::showObjectivesSection);
        Button monstersButton = createSectionButton("Monsters", this::showMonstersSection);
        Button enchantmentsButton = createSectionButton("Enchantments", this::showEnchantmentsSection);
        Button controlsButton = createSectionButton("Controls", this::showControlsSection);

        VBox buttonBox = new VBox(10, gameplayButton, objectivesButton, monstersButton, enchantmentsButton, controlsButton);
        buttonBox.setAlignment(Pos.CENTER);

        // Back button
        Button backButton = createBackButton();

        // Assemble UI
        VBox contentBox = new VBox(20, titleLabel, buttonBox, backButton);
        contentBox.setAlignment(Pos.CENTER);
        root.getChildren().add(contentBox);

        // Scene
        Scene scene = new Scene(root, 600, 400); // Set size to 600x400
        primaryStage.setScene(scene);
        primaryStage.setTitle("Help Menu");
        primaryStage.show();
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

    private void showSection(String title, String contentText) {
        // Root layout
        StackPane root = new StackPane();

        // Add a background image
        Image backgroundImage = new Image("/rokue-like_assets/rokue33.png");
        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setFitWidth(600);  // Set width to 600
        backgroundView.setFitHeight(400); // Set height to 400
        backgroundView.setPreserveRatio(true);
        backgroundView.setOpacity(1.0); // Fully visible
        root.getChildren().add(backgroundView); // Add background

        // Title
        Label titleLabel = new Label(title);
        titleLabel.setFont(new Font("Arial", 24));
        titleLabel.setStyle("-fx-text-fill: #FFFFFF;");
        titleLabel.setAlignment(Pos.CENTER);

        // Content
        Text content = new Text(contentText);
        content.setFont(new Font("Arial", 14));
        content.setFill(javafx.scene.paint.Color.WHITE);
        content.setTextAlignment(TextAlignment.LEFT);
        content.wrappingWidthProperty().set(500);

        VBox contentBox = new VBox(10, content);
        contentBox.setAlignment(Pos.CENTER);

        // Back button
        Button backButton = new Button("Back to Help Menu");
        backButton.setStyle(
            "-fx-background-color: #303843; " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 16px; " +
            "-fx-padding: 10px 20px; " +
            "-fx-background-radius: 10;"
        );
        backButton.setOnAction(event -> showMainHelpMenu());

        // Assemble UI
        VBox layout = new VBox(20, titleLabel, contentBox, backButton);
        layout.setAlignment(Pos.CENTER);
        root.getChildren().add(layout);

        // Scene
        Scene scene = new Scene(root, 600, 400); // Set size to 600x400
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Button createSectionButton(String text, Runnable action) {
        Button button = new Button(text);
        button.setPrefWidth(200); // Set fixed width for uniform button size
        button.setStyle(
            "-fx-background-color: #505A63; " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 16px; " +
            "-fx-padding: 10px 20px; " +
            "-fx-background-radius: 10;"
        );
        button.setOnAction(event -> action.run());
        return button;
    }

    private Button createBackButton() {
        Button backButton = new Button("Back to Main Menu");
        backButton.setStyle(
            "-fx-background-color: #303843; " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 16px; " +
            "-fx-padding: 10px 20px; " +
            "-fx-background-radius: 10;"
        );
        backButton.setOnAction(event -> {
            Main mainPage = new Main();
            mainPage.start(primaryStage);
        });
        return backButton;
    }

    public static void main(String[] args) {
        launch(args);
    }
}