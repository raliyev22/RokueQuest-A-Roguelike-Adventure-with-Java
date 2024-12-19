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
import main.utils.Grid;
import main.utils.Tile;

public class PlayTest extends Application {
    private int tileSize = 64;
    static final Image tileImage = new Image("/rokue-like_assets/Tile_x4_64_64.png");
    // static final Image Pillar_IMAGE = new Image("/rokue-like_assets/Pillar_x2_32_64.png");
    // static final Image Ladder_IMAGE = new Image("/rokue-like_assets/TileWithLadder_x2_32_32.png");
    static final Image Box_IMAGE = new Image("rokue-like_assets/Box_x4_64_84.png");
    // static final Image BoxOnBox_IMAGE = new Image("/rokue-like_assets/BoxOnTopOfBox_x2_32_64.png");
    // static final Image Cube_IMAGE = new Image("/rokue-like_assets/Cube_x2_32_32.png");
    // static final Image Skull_IMAGE = new Image("/rokue-like_assets/Skull_x2_32_32.png");
    // static final Image Chest_IMAGE = new Image("/rokue-like_assets/Chest_Closed_16_14.png");
    static final Image Player_IMAGE = new Image("/rokue-like_assets/player4x.png");
    boolean upPressed, downPressed, leftPressed, rightPressed = false;
    public void start(Stage primaryStage) {
        //THESE CODES FOR ONLY TEST PURPOSES, WHEN BUILDMODE IS AVAILABLE IT IS GOING TO BE PULLED FROM THAT//
        TiledHall hall = new TiledHall(20, 14, new Grid(10,9,64,64,10,40));
        // Create a pane
        Pane pane = new Pane();
        Grid grid = hall.getGrid();
        for (int i = 0; i < 20; i++){
            Random rand = new Random();
            int randomTileX = rand.nextInt(9);
            int randomTileY = rand.nextInt(9);
            grid.changeTileWithIndex(randomTileX, randomTileY, 'C');
            Rectangle clone = new Rectangle(64,64);
            clone.setFill(new ImagePattern(Box_IMAGE));
            clone.setX(randomTileX*64);
            clone.setY(randomTileY*64);
            hall.getChildren().add(clone);
        }

        Random rand = new Random();
        int randomX = rand.nextInt(9);
        int randomY = rand.nextInt(9);
        grid.changeTileWithIndex(randomX, randomY, 'H');
        Rectangle hero = new Rectangle(64,64);
        hero.setFill(new ImagePattern(Player_IMAGE));
        hero.setX(randomX*64);
        hero.setY(randomY*64);
        hall.getChildren().add(hero);
        System.out.println(grid.toString());

        // Create the background grid
        for (int a = 0; a < 1536; a += tileSize) {
            for (int b = 0; b < 800; b += tileSize) {
                Rectangle tideRectangle = new Rectangle(a, b, tileSize, tileSize);
                tideRectangle.setFill(new ImagePattern(tileImage));
                pane.getChildren().add(tideRectangle);
            }
        }
        setHallPosition(hall, 98, 4);
        pane.getChildren().add(hall);
        //=====================================================================================//
        Scene scene = new Scene(pane, 1536, 800);

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

                if (newX >= 0 && newX + hero.getWidth() <= 640){
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
    public static void main(String[] args) {
        launch(args);
    }
}
