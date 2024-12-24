package main.view;

import java.util.ArrayList;
import java.util.List;
import javafx.util.Pair;

import javafx.application.Application;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import main.Main;
import main.model.HallType;
import main.model.Images;
import main.utils.Grid;
import main.utils.Tile;
import test.TiledHall;

public class PlayModeView2 extends Application{
    private List<TiledHall> hallList;

    public PlayModeView2(List<TiledHall> hallList){
        this.hallList = hallList;
    }

    public void start(Stage primaryStage) {
        Pane pane = new Pane();

        TiledHall hall1 = hallList.get(0);
        pane.getChildren().add(hall1);
        Pair<Integer,Integer> pair = new Pair<Integer,Integer>(10, 72);
        addHeroToHall(hall1, pair);

        System.out.println(hall1.getGrid().getTileMap().get(10));

        //hall1.setLayoutX(0);
        

        Scene scene = new Scene(pane, 1200, 800); // Example: Decrease width
        primaryStage.setScene(scene);
        //javafx.geometry.Rectangle2D screenBounds = javafx.stage.Screen.getPrimary().getVisualBounds();
        //primaryStage.setX((screenBounds.getWidth() - 1200) / 2); // Width of the scene
        //primaryStage.setY((screenBounds.getHeight() - 800) / 2); // Height of the scene
        primaryStage.show();

       
    }
    private void addHeroToHall(TiledHall hall,Pair<Integer,Integer> location){
        Rectangle hero = new Rectangle(location.getKey(),location.getValue(),32,32);
        hero.setFill(new ImagePattern(Images.convertCharToImage('R')));
        hall.getChildren().add(hero);

    }

    public static void main(String[] args) {
        launch(args);
    }

}
