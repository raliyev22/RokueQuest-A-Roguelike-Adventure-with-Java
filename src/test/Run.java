package test;

import java.util.ArrayList;
import java.util.List;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import main.utils.Grid;
import main.utils.Tile;
import main.view.Toolbox;

public class Run extends Application {

    private int tileSize = 32;
    private int blockHeight = 40;

    static final Image tileImage = new Image("/rokue-like_assets/Tile_x2_32_32.png");

    private int column=10;
    private int row = 9;

    public void start(Stage primaryStage) {

        // Create a pane
        Pane pane = new Pane();

        for(int a = 0;a<1536;a+=tileSize){
            for(int b = 0;b<800;b+=tileSize){
                Rectangle tideRectangle = new Rectangle(a,b, tileSize,tileSize);

                tideRectangle.setFill(new ImagePattern(tileImage));
                pane.getChildren().add(tideRectangle);
            }
        }



        // Create 4 TiledHall instances with specific sizes
        TiledHall hall1 = new TiledHall(10, 7); // Default size
        TiledHall hall2 = new TiledHall(10, 7);  // Smaller
        TiledHall hall3 = new TiledHall(10,7); // Default size
        TiledHall hall4 = new TiledHall(10, 7);  // Smaller

        ArrayList<TiledHall> halls = new ArrayList<TiledHall>();
        halls.add(hall1);
        halls.add(hall2);
        halls.add(hall3);
        halls.add(hall4);

        
        //TiledHall hall3 = new TiledHall(12, 10); // Larger
        //TiledHall hall4 = new TiledHall(7, 5);  // Smallest

        // Set desired positions for each TiledHall
        hall1.setLayoutX(98);   // X-coordinate for hall1
        hall1.setLayoutY(4);   // Y-coordinate for hall1
        hall1.getGrid().setTopLeftXCordinate(hall1.getGrid().topLeftXCoordinate + (int) hall1.getLayoutX());
        hall1.getGrid().setTopLeftYCordinate(hall1.getGrid().topLeftYCoordinate + (int) hall1.getLayoutY());


        hall2.setLayoutX(482);  // X-coordinate for hall2
        hall2.setLayoutY(4);   // Y-coordinate for hall2

        hall3.setLayoutX(98);  // X-coordinate for hall2
        hall3.setLayoutY(420);

        hall4.setLayoutX(482);  // X-coordinate for hall2
        hall4.setLayoutY(420);


        // Add TiledHalls to the pane
        pane.getChildren().addAll(hall1, hall2,hall3,hall4);

        Toolbox toolbox = new Toolbox(1105, 50, 175, 720, pane, halls);
        pane.getChildren().add(toolbox);

        // Create a scene
        Scene scene = new Scene(pane, 1536, 800);
        primaryStage.setTitle("Tiled Hall Example");
        primaryStage.setScene(scene);
        primaryStage.show();


        scene.widthProperty().addListener((obs, oldVal, newVal) -> 
            primaryStage.setTitle("Width: " + newVal.intValue() + ", Height: " + (int) scene.getHeight())
        );
        scene.heightProperty().addListener((obs, oldVal, newVal) -> 
            primaryStage.setTitle("Width: " + (int) scene.getWidth() + ", Height: " + newVal.intValue())
        );


        
        // Grid grid1 = new Grid(10,9,tileSize,tileSize,(int) (hall1.getLayoutX()+10),(int) (hall1.getLayoutY() + blockHeight));
        // List<Tile> tilemap1 = grid1.getTileMap();

        // int a = 0;
        // for (Tile tile : tilemap1) {

        //     Rectangle tideRectangle = new Rectangle(tile.getLeftSide()-98,tile.getTopSide()-4, tileSize,tileSize);
        //     tideRectangle.setFill(new ImagePattern(tileImage));
        //     hall1.getChildren().add(tideRectangle);
        //     a++;

        //     System.out.println(tile);

            

        // }
        // System.out.println(hall1.getHeight());
        // System.out.println(a);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
