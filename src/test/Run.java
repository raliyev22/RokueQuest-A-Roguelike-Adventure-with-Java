package test;

import java.util.List;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import main.utils.Grid;
import main.utils.Tile;

public class Run extends Application {

    private int tileSize = 32;

    public void start(Stage primaryStage) {

        // Create a pane
        Pane pane = new Pane();

        // Create 4 TiledHall instances with specific sizes
        TiledHall hall1 = new TiledHall(10, 8); // Default size
        TiledHall hall2 = new TiledHall(8, 6);  // Smaller
        //TiledHall hall3 = new TiledHall(12, 10); // Larger
        //TiledHall hall4 = new TiledHall(7, 5);  // Smallest

        // Set desired positions for each TiledHall
        hall1.setLayoutX(50);   // X-coordinate for hall1
        hall1.setLayoutY(50);   // Y-coordinate for hall1

        hall2.setLayoutX(500);  // X-coordinate for hall2
        hall2.setLayoutY(50);   // Y-coordinate for hall2


        // Add TiledHalls to the pane
        pane.getChildren().addAll(hall1, hall2);

        // Create a scene
        Scene scene = new Scene(pane, 800, 800);
        primaryStage.setTitle("Tiled Hall Example");
        primaryStage.setScene(scene);
        primaryStage.show();


        //int result = (int) (hall1.getLayoutX() + hall1.getWidth());


        System.out.println(hall1.getHeight());
        
        Grid grid1 = new Grid(10,8,tileSize,tileSize,(int) (hall1.getLayoutX()+10),(int) (hall1.getLayoutY() + hall1.getHeight()-tileSize));
        List<Tile> tilemap1 = grid1.getTileMap();
        for (Tile tile : tilemap1) {
            // Perform actions with each tile

            //System.out.printf("");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
