package test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
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
        //THESE CODES FOR ONLY TEST PURPOSES, WHEN BUILDMODE IS AVAILABLE IT IS GOING TO BE PULLED FROM THAT//
        TiledHall hall = new TiledHall(20, 15, new Grid(10,9,64,64,10,40));
        // Create a pane
        Pane pane = new Pane();
        Grid grid = hall.getGrid();
        HashMap<TiledHall, List<Tile>> tileMap = BuildModeView.sharedTileMap;
        if (tileMap != null && !tileMap.isEmpty()) {
            TiledHall earthHall = null;

            for (TiledHall h : BuildModeView.hallTypeMap.keySet()) {
                if (BuildModeView.hallTypeMap.get(h) == HallType.EARTH) {
                    earthHall = h;
                    break;
                }
            }

            if (earthHall != null) {
                List<Tile> tiles = BuildModeView.sharedTileMap.get(earthHall);
                for (Tile tile : tiles) {
                    Image image = Images.convertCharToImage(tile.getTileType());

                    if (image != null) {
                        if (tile.getTileType() == 'P') { // Örneğin, uzun item (Pillar)
                            drawTallItem(hall, tile, image); // Uzun itemi çiz
                        }
                        else if(tile.getTileType() == 'B') { // Örneğin, uzun item (Pillar)
                                drawTallItem(hall, tile, image); // Uzun itemi çiz
                        }
                        else {
                            drawNormalItem(hall, tile, image); // Normal itemi çiz
                        }
                    }

                    // Rectangle rect = new Rectangle(tileSize, tileSize);
                    // rect.setX(tile.getLeftSide()*2-10);
                    // rect.setY(tile.getTopSide()*2-75);

                    // // Tile türüne göre görselleri ayarla
                    // Image image = Images.convertCharToImage(tile.getTileType());
                    // if (image != null) {
                    //     rect.setFill(new ImagePattern(image));
                    // } else {
                    //     rect.setFill(Color.GRAY);
                    // }

                    // hall.getChildren().add(rect);
                }
            }
        }

        Random rand = new Random();
        int randomX = rand.nextInt(9);;
        int randomY = rand.nextInt(2,10);
        Rectangle hero = new Rectangle(64,64);
        hero.setFill(new ImagePattern(Images.IMAGE_PLAYERRIGHT_x4));
        hero.setX(randomX*64+11);
        hero.setY(randomY*64-90);
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
        setHallPosition(hall, 100,40);
        pane.getChildren().add(hall);
        //=====================================================================================//
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

            long start = System.currentTimeMillis();
            long end = start + 500;

            public void handle(long now) {
                double newX = hero.getX();
                double newY = hero.getY();
                if (System.currentTimeMillis() > end){
                    end = System.currentTimeMillis() + 100;
                    if (upPressed) newY -= 64;
                    else if (downPressed) newY += 64;
                    else if (leftPressed) newX -= 64;
                    else if (rightPressed) newX += 64;
                    else end = 0;
                }
                
                if (newX >= 0 && newX + hero.getWidth() <= 700){
                    hero.setX(newX);
                }
                if (newY >= 0 && newY + hero.getHeight() <= 630){
                    hero.setY(newY);
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
    // Uzun item için iki karelik bir alan kaplayacak şekilde yerleştir
    Rectangle tallItem = new Rectangle(tile.getLeftSide() * 2 -10, tile.getTopSide() * 2 -107, tileSize, tileSize*2);
    tallItem.setFill(new ImagePattern(image));
    hall.getChildren().add(tallItem);
}

// Normal itemleri tek kareye yerleştir
private void drawNormalItem(Pane hall, Tile tile, Image image) {
    Rectangle normalItem = new Rectangle(tile.getLeftSide() * 2-10, tile.getTopSide() * 2-43, tileSize, tileSize);
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
