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

public class Run extends Application {

    private int tileSize = 32;
    private int blockHeight = 40;

    static final Image tileImage = new Image("/rokue-like_assets/Tile_x2_32_32.png");

    static final Image CHEST_IMAGE = new Image("/rokue-like_assets/Build_Mode_Chest_Full_View.png");
    static final Image Pillar_IMAGE = new Image("/rokue-like_assets/PillarWithTile_16_43.png");
    static final Image Ladder_IMAGE = new Image("/rokue-like_assets/TileWithLadder_16_16.png");
    static final Image Box_IMAGE = new Image("/rokue-like_assets/Box_16_21.png");
    static final Image BoxOnBox_IMAGE = new Image("/rokue-like_assets/BoxOnTopOfBox_16_32.png");
    static final Image Cube_IMAGE = new Image("/rokue-like_assets/Cube_8_14.png");
    static final Image Skull_IMAGE = new Image("/rokue-like_assets/Skull_6_6.png");
    static final Image Chest_IMAGE = new Image("/rokue-like_assets/Chest_Closed_16_14.png");

    public void start(Stage primaryStage) {

        // Create a pane
        Pane pane = new Pane();

        // Create the background grid
        for (int a = 0; a < 1536; a += tileSize) {
            for (int b = 0; b < 800; b += tileSize) {
                Rectangle tideRectangle = new Rectangle(a, b, tileSize, tileSize);
                tideRectangle.setFill(new ImagePattern(tileImage));
                pane.getChildren().add(tideRectangle);
            }
        }

        // Create 4 TiledHall instances with specific sizes
        TiledHall hall1 = new TiledHall(10, 7, new Grid(10, 9, 32, 32, 10, 40));
        TiledHall hall2 = new TiledHall(10, 7, new Grid(10, 9, 32, 32, 10, 40));
        TiledHall hall3 = new TiledHall(10, 7, new Grid(10, 9, 32, 32, 10, 40));
        TiledHall hall4 = new TiledHall(10, 7, new Grid(10, 9, 32, 32, 10, 40));

        ArrayList<TiledHall> halls = new ArrayList<>();
        halls.add(hall1);
        halls.add(hall2);
        halls.add(hall3);
        halls.add(hall4);

        // Set desired positions for each TiledHall
        setHallPosition(hall1, 98, 4);
        setHallPosition(hall2, 482, 4);
        setHallPosition(hall3, 98, 420);
        setHallPosition(hall4, 482, 420);

        // Add TiledHalls to the pane
        pane.getChildren().addAll(hall1, hall2, hall3, hall4);

        // Add toolbox UI directly in Run
        addToolbox(pane, halls);

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
    }

    private void setHallPosition(TiledHall hall, int x, int y) {
        hall.setLayoutX(x);
        hall.setLayoutY(y);
        hall.getGrid().setTopLeftXCordinate(hall.getGrid().topLeftXCoordinate + x);
        hall.getGrid().setTopLeftYCordinate(hall.getGrid().topLeftYCoordinate + y);
    }

    private void addToolbox(Pane root, List<TiledHall> halls) {
        // Define toolbox dimensions and positions relative to the screen
        double toolboxX = 1105; // Absolute X position for the toolbox on the screen
        double toolboxY = 50;   // Absolute Y position for the toolbox on the screen
        double toolboxWidth = 175;
        double toolboxHeight = 720;

        // Add toolbox background
        Rectangle chest = new Rectangle(toolboxX, toolboxY, toolboxWidth, toolboxHeight);
        chest.setFill(new ImagePattern(CHEST_IMAGE));
        root.getChildren().add(chest);

        // Define absolute positions for draggable objects relative to the screen
        double objectStartX = toolboxX + 70; // Absolute X position for objects
        double[] positionsY = {
            toolboxY + 50,  toolboxY + 150, toolboxY + 250,
            toolboxY + 350, toolboxY + 450, toolboxY + 550,
            toolboxY + 650
        };

        // Create draggable objects (absolute positioning)
        createDraggableObject(objectStartX, positionsY[0], Pillar_IMAGE, root, 30, 75, halls, 'P');
        createDraggableObject(objectStartX, positionsY[1], Ladder_IMAGE, root, 30, 30, halls, 'L');
        createDraggableObject(objectStartX, positionsY[2], Box_IMAGE, root, 30, 40, halls, 'b');
        createDraggableObject(objectStartX, positionsY[3], BoxOnBox_IMAGE, root, 30, 60, halls, 'B');
        createDraggableObject(objectStartX, positionsY[4], Cube_IMAGE, root, 20, 30, halls, 'c');
        createDraggableObject(objectStartX, positionsY[5], Skull_IMAGE, root, 15, 15, halls, 'S');
        createDraggableObject(objectStartX, positionsY[6], Chest_IMAGE, root, 30, 40, halls, 'C');
    }

    private void createDraggableObject(double x, double y, Image image, Pane root, double width, double height, List<TiledHall> halls, char tileType) {
        Rectangle object = new Rectangle(x, y, width, height);
        object.setFill(new ImagePattern(image));
    
        object.setOnMousePressed(event -> {
            // Create a new copy when dragging starts
            Rectangle clone = new Rectangle(width, height);
            clone.setFill(new ImagePattern(image));
            root.getChildren().add(clone);
    
            // Update the position of the clone in real time
            object.setOnMouseDragged(dragEvent -> {
                // Position the clone to follow the mouse cursor
                clone.setX(dragEvent.getSceneX() - width / 2); // Center the clone around the mouse
                clone.setY(dragEvent.getSceneY() - height / 2);
            });
    
            // Handle release for snapping or discarding
            object.setOnMouseReleased(releaseEvent -> {
                boolean snappedToTile = false;
    
                // Get the scene coordinates where the object was released
                double sceneX = releaseEvent.getSceneX();
                double sceneY = releaseEvent.getSceneY();
    
                for (TiledHall hall : halls) {
                    Grid grid = hall.getGrid();
    
                    // Check if the object is within the grid
                    if (grid.coordinatesAreInGrid(sceneX, sceneY)) {
                        Tile targetTile = grid.findTileUsingCoordinates(sceneX, sceneY);

                        if (targetTile.getTileType()=='E') {
                            // Update the tile's type to match the dragged object
                            grid.findTileUsingCoordinates(sceneX, sceneY).changeTileType(tileType);
    
                            // Snap the clone to the target tile
                            clone.setX(targetTile.getLeftSide());
                            clone.setY(targetTile.getTopSide());
                            hall.getChildren().add(clone);
    
                            snappedToTile = true;
                            System.out.printf("Object placed at tile: %s%n", targetTile);
                            System.out.println(grid.toString());
                            break;
                        }
                    }
                }
    
                // If not snapped to a grid, remove the clone
                if (!snappedToTile) {
                    root.getChildren().remove(clone);
                }
            });
        });
    
        root.getChildren().add(object);
    }
    
    

    public static void main(String[] args) {
        launch(args);
    }
}
