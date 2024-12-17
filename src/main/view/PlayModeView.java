package main.view;

import java.util.List;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import main.model.*;
import test.TiledHall;

public class PlayModeView extends Application{

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int PLAYER_SIZE = 50;
    static final Image PLAYER_IMAGE = new Image("/rokue-like_assets/player.png");
    boolean gameOver = false;
    boolean upPressed, downPressed, leftPressed, rightPressed = false;
    private GraphicsContext gc;
    Hero player = new Hero(400,300, PLAYER_IMAGE);
    List<Monster> monsters;

@Override
public void start(Stage stage) {
    // Ana root pane
    Pane root = new Pane();

    // ===================== Arkaplan =====================
    Image backgroundImage = new Image("/rokue-like_assets/Tile_x4_64_64.png");
    Rectangle background = new Rectangle(800, 600); // Arkaplan boyutu
    background.setFill(new ImagePattern(backgroundImage));
    root.getChildren().add(background);

    TiledHall hall1 = new TiledHall(17, 12);
    hall1.setLayoutX(40);
    hall1.setLayoutY(20);
    root.getChildren().addAll(hall1);

    // ===================== Oyuncu (Player) =====================
    Rectangle player = new Rectangle(50, 50, 40, 40);
    ImagePattern playerPattern = new ImagePattern(PLAYER_IMAGE);
    player.setFill(playerPattern);
    player.setX(100);
    player.setY(100);
    root.getChildren().add(player);

    // ===================== UI Bileşenleri =====================
    VBox uiContainer = new VBox(10);
    uiContainer.setStyle("-fx-background-color: #3C2D3B; -fx-padding: 10;");
    uiContainer.setLayoutX(620);
    uiContainer.setLayoutY(20);
    uiContainer.setPrefWidth(150);
    uiContainer.setPrefHeight(500);

    // Zaman Göstergesi
    Label timeLabel = new Label("Time: 5 seconds");
    timeLabel.setTextFill(Color.WHITE);
    timeLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

    // Canlar (Lives)
    HBox heartsContainer = new HBox(5);
    Image heartImage = new Image("/rokue-like_assets/fighter.png");
    for (int i = 0; i < 3; i++) {
        ImageView heart = new ImageView(heartImage);
        heart.setFitWidth(30);
        heart.setFitHeight(30);
        heartsContainer.getChildren().add(heart);
    }

    // Büyük Inventory Görseli
    ImageView inventoryImage = new ImageView(new Image("/rokue-like_assets/Inventory.png"));
    inventoryImage.setFitWidth(150); // Daha büyük boyut
    inventoryImage.setFitHeight(200);

    Region spacer = new Region();
    spacer.setPrefHeight(150); // Boşluk mümkün olduğunca genişletilecek
    
    // UI bileşenlerini ekle
    uiContainer.getChildren().addAll(timeLabel, heartsContainer,  spacer, inventoryImage);
    root.getChildren().add(uiContainer);


    // ===================== Tuş Kontrolleri =====================
    Scene scene = new Scene(root, 800, 600);
    scene.setOnKeyPressed(event -> {
        switch (event.getCode()) {
            case UP, W -> upPressed = true;
            case DOWN, S -> downPressed = true;
            case LEFT, A -> leftPressed = true;
            case RIGHT, D -> rightPressed = true;
        }
    });

    scene.setOnKeyReleased(event -> {
        switch (event.getCode()) {
            case UP, W -> upPressed = false;
            case DOWN, S -> downPressed = false;
            case LEFT, A -> leftPressed = false;
            case RIGHT, D -> rightPressed = false;
        }
    });

    // ===================== Ana Oyun Döngüsü =====================
    AnimationTimer gameLoop = new AnimationTimer() {
        public void handle(long now) {
            double newX = player.getX();
            double newY = player.getY();

            if (upPressed) newY -= 2;
            if (downPressed) newY += 2;
            if (leftPressed) newX -= 2;
            if (rightPressed) newX += 2;

            if (newX >= 50 && newX + player.getWidth() <= 600) player.setX(newX);
            if (newY >= 58 && newY + player.getHeight() <= 530) player.setY(newY);
        }
    };
    
    gameLoop.start();

    // ===================== Sahne Ayarları =====================
    stage.setScene(scene);
    stage.setTitle("Play Mode");
    stage.show();
}



    public static void main(String[] args) {
        launch(args);
    }
}
