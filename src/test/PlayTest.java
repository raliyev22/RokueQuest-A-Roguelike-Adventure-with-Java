package test;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import main.model.HallType;
import main.model.Images;
import main.utils.Grid;
import main.utils.Tile;
import main.view.BuildModeView;

public class PlayTest extends Application {
    private int tileSize = 64;

    boolean upPressed, downPressed, leftPressed, rightPressed = false;

    public void start(Stage primaryStage) {
        // THESE CODES FOR ONLY TEST PURPOSES, WHEN BUILDMODE IS AVAILABLE IT IS GOING
        // TO BE PULLED FROM THAT//
        TiledHall hall = new TiledHall(10, 7, new Grid(10, 9, 32, 32, 10, 40),2);
        // Create a pane
        Pane pane = new Pane();
        Grid grid = hall.getGrid();
        HashMap<TiledHall, List<Tile>> tileMap = BuildModeView.sharedTileMap;
        // if (tileMap != null && !tileMap.isEmpty()) {
        //     TiledHall earthHall = null;

        //     for (TiledHall h : BuildModeView.hallTypeMap.keySet()) {
        //         if (BuildModeView.hallTypeMap.get(h) == HallType.EARTH) {
        //             earthHall = h;
        //             break;
        //         }
        //     }

        //     if (earthHall != null) {
        //         List<Tile> tiles = BuildModeView.sharedTileMap.get(earthHall);
        //         for (Tile tile : tiles) {
        //             Image image = Images.convertCharToImage(tile.getTileType());

        //             if (image != null) {
        //                 if (tile.getTileType() == 'P') {
        //                     drawTallItem(hall, tile, image);
        //                 } else if (tile.getTileType() == 'd') {
        //                     drawTallItem(hall, tile, image);
        //                 } else {
        //                     drawNormalItem(hall, tile, image);
        //                 }
        //             }
        //         }
        //     }
        // }

        Random rand = new Random();
        int randomX = rand.nextInt(9);
        int randomY = rand.nextInt(2, 10);
        Rectangle hero = new Rectangle(64, 64);
        hero.setFill(new ImagePattern(Images.IMAGE_PLAYERRIGHT_x4));
        hero.setX(randomX * 64 + 11);
        hero.setY(randomY * 64 - 90);
        hall.getChildren().add(hero);
        System.out.println(grid.toString());

        // Create the background grid
        for (int a = 0; a < 1536; a += tileSize) {
            for (int b = 0; b < 800; b += tileSize) {
                Rectangle tideRectangle = new Rectangle(a, b, tileSize, tileSize);
                tideRectangle.setFill(new ImagePattern(Images.IMAGE_TILE_x4));
                pane.getChildren().add(tideRectangle);
            }
        }
        setHallPosition(hall, 100, 40);
        pane.getChildren().add(hall);
        // =====================================================================================//
        Scene scene = new Scene(pane, 1536, 800);

        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case UP, W -> upPressed = true;
                case DOWN, S -> downPressed = true;
                case LEFT, A -> {
                    leftPressed = true;
                    hero.setFill(new ImagePattern(Images.IMAGE_PLAYERLEFT_x4)); // Sol görsel
                }
                case RIGHT, D -> {
                    rightPressed = true;
                    hero.setFill(new ImagePattern(Images.IMAGE_PLAYERRIGHT_x4)); // Sağ görsel
                }
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

        AnimationTimer gameLoop = new AnimationTimer() {
            private double targetX = hero.getX(); // Hedef X konumu
            private double targetY = hero.getY(); // Hedef Y konumu
            private final double speed = 5; // Kahramanın hızı (pikseller/frame)
            private boolean isMoving = false; // Kahramanın şu anda hareket edip etmediğini takip eder
        
            @Override
            public void handle(long now) {
                // Tuş basımı sırasında hedef konumu yalnızca kahraman duruyorsa güncelle
                if (!isMoving) {
                    if (upPressed && hero.getY() > 40) {
                        targetY = Math.max(hero.getY() - tileSize, 40); // Alt sınırı kontrol et
                        isMoving = true; // Hareket başladı
                    } else if (downPressed && hero.getY() < 615 - hero.getHeight()) {
                        targetY = Math.min(hero.getY() + tileSize, 615 - hero.getHeight());
                        isMoving = true;
                    } else if (leftPressed && hero.getX() > 12) {
                        targetX = Math.max(hero.getX() - tileSize, 12); // Sol sınırı kontrol et
                        isMoving = true;
                    } else if (rightPressed && hero.getX() < 650 - hero.getWidth()) {
                        targetX = Math.min(hero.getX() + tileSize, 650 - hero.getWidth());
                        isMoving = true;
                    }
                }
        
                // Akıcı hareket için kahramanın pozisyonunu güncelle
                double currentX = hero.getX();
                double currentY = hero.getY();
        
                if (currentX < targetX) {
                    hero.setX(Math.min(currentX + speed, targetX));
                } else if (currentX > targetX) {
                    hero.setX(Math.max(currentX - speed, targetX));
                }
        
                if (currentY < targetY) {
                    hero.setY(Math.min(currentY + speed, targetY));
                } else if (currentY > targetY) {
                    hero.setY(Math.max(currentY - speed, targetY));
                }
        
                // Kahraman hedef konumuna ulaştığında durumu sıfırla
                if (currentX == targetX && currentY == targetY) {
                    isMoving = false; // Hareket tamamlandı
                }
            }
        };
        
        

        gameLoop.start();

        primaryStage.setTitle("Play Example");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    private void setHallPosition(TiledHall hall, int x, int y) {
        hall.setLayoutX(x);
        hall.setLayoutY(y);
        hall.getGrid().setTopLeftXCordinate(hall.getGrid().topLeftXCoordinate + x);
        hall.getGrid().setTopLeftYCordinate(hall.getGrid().topLeftYCoordinate + y);
    }

    private void drawTallItem(Pane hall, Tile tile, Image image) {
        Rectangle tallItem = new Rectangle(tile.getLeftSide() * 2 - 10, tile.getTopSide() * 2 - 107, tileSize,
                tileSize * 2);
        tallItem.setFill(new ImagePattern(image));
        hall.getChildren().add(tallItem);
    }

    private void drawNormalItem(Pane hall, Tile tile, Image image) {
        Rectangle normalItem = new Rectangle(tile.getLeftSide() * 2 - 10, tile.getTopSide() * 2 - 43, tileSize,
                tileSize);
        if (image != null) {
            normalItem.setFill(new ImagePattern(image));
        } else {
            normalItem.setFill(Color.GRAY);
        }
        hall.getChildren().add(normalItem);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
