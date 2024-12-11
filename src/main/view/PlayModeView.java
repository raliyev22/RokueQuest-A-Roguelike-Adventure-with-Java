package main.view;

import java.util.List;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import main.model.*;

public class PlayModeView extends Application{

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int PLAYER_SIZE = 50;
    static final Image PLAYER_IMAGE = new Image("/rokue-like_assets/player.png");
    boolean gameOver = false;
    boolean upPressed, downPressed, leftPressed, rightPressed = false;
    private GraphicsContext gc;
    Hero player = new Hero(400,300, PLAYER_SIZE, PLAYER_IMAGE);
    List<Monster> monsters;



    @Override
    public void start(Stage stage){
        Pane root = new Pane();
        Rectangle player = new Rectangle(50,50,40,40);
        ImagePattern pattern = new ImagePattern(PLAYER_IMAGE);
        player.setFill(pattern);
        root.getChildren().add(player);
        Scene scene = new Scene(root, 500, 500);

        Rectangle enemy = new Rectangle(200, 200, 40, 40);
        enemy.setFill(Color.RED);
        root.getChildren().add(enemy);


        scene.setOnKeyPressed(event ->{
            switch (event.getCode()) {
                case UP,W->upPressed = true;
                case DOWN,S->downPressed = true;
                case LEFT,A->leftPressed = true;
                case RIGHT,D->rightPressed = true;
            }
        });

        scene.setOnKeyReleased(event -> {
            switch (event.getCode()) {
                case UP,W->upPressed = false;
                case DOWN,S->downPressed = false;
                case LEFT,A->leftPressed = false;
                case RIGHT,D->rightPressed = false;
            }
        });

        // scene.setOnKeyPressed(event -> {
        //     switch (event.getCode()) {
        //         case W->player.setY(player.getY() - 10); // Move up
        //         case S->player.setY(player.getY() + 10); // Move down
        //         case A->player.setX(player.getX() - 10); // Move left
        //         case D->player.setX(player.getX() + 10); // Move right
        //     }
        // });

        AnimationTimer gameLoop = new AnimationTimer() {
            public void handle(long now){
                if (player.getBoundsInParent().intersects(enemy.getBoundsInParent())){
                    System.out.println("ded");
                }
                if (upPressed){
                    player.setY(player.getY() - 2);
                }
                if (downPressed){
                    player.setY(player.getY() + 2);
                }
                if (leftPressed){
                    player.setX(player.getX() - 2);
                }
                if (rightPressed){
                    player.setX(player.getX() + 2);
                }
            }
        };
        gameLoop.start();

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
