package main.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javafx.util.Pair;
import java.util.Set;
import java.util.HashSet;
import javafx.util.Duration;
import java.awt.image.BufferedImage;
import java.awt.Dialog;
import java.awt.Graphics2D;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import main.utils.Grid;
import main.utils.Tile;
import test.TiledHall;

import java.util.Random;

public class BuildModeView extends Application {

    private int tileSize = 32;
    private int blockHeight = 40;

    static final Image tileImage = new Image("/rokue-like_assets/Tile_x2_32_32.png");

    static final Image CHEST_IMAGE = new Image("/rokue-like_assets/Build_Mode_Chest_Full_View.png");
    static final Image Pillar_IMAGE = new Image("/rokue-like_assets/Pillar_x2_32_64.png");
    static final Image Ladder_IMAGE = new Image("/rokue-like_assets/TileWithLadder_x2_32_32.png");
    static final Image BoxOnBox_IMAGE = new Image("/rokue-like_assets/BoxOnTopOfBox_x2_32_64.png");
    static final Image Cube_IMAGE = new Image("/rokue-like_assets/Cube_x2_32_32.png");
    static final Image Skull_IMAGE = new Image("/rokue-like_assets/Skull_x2_32_32.png");

    static final Image BOX_IMAGE = new Image("/rokue-like_assets/Box_x2_32_42.png");
    
    static final Image CHEST = new Image("/rokue-like_assets/ChestHeart_x2_32_28.png");
    
    private HashMap<TiledHall,List<Tile>> tileMap = new HashMap<TiledHall,List<Tile>>();
    private List<Pair<Integer,Integer>> runeLocationList = new ArrayList<Pair<Integer,Integer>>();

    public void start(Stage primaryStage) {

        // Create a pane
        Pane pane = new Pane();

        // Create the background grid
        for (int a = 0; a < 1200; a += tileSize) {
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
        setHallPosition(hall1, 200, 50); // Adjust X and Y positions
        setHallPosition(hall2, 600, 50);
        setHallPosition(hall3, 200, 400);
        setHallPosition(hall4, 600, 400);

        System.out.println(hall4.getHeight());

        // Add TiledHalls to the pane
        pane.getChildren().addAll(hall1, hall2, hall3, hall4);

        // Add toolbox UI directly in Run
        addToolbox(pane, halls);

        //
        tileMap.put(hall1, new ArrayList<Tile>());
        tileMap.put(hall2, new ArrayList<Tile>());
        tileMap.put(hall3, new ArrayList<Tile>());
        tileMap.put(hall4, new ArrayList<Tile>());

        // Create a scene
        Scene scene = new Scene(pane, 1200, 800); // Example: Decrease width
        primaryStage.setTitle("Tiled Hall Example");
        primaryStage.setScene(scene);
        primaryStage.show();

        scene.widthProperty().addListener((obs, oldVal, newVal) ->
                primaryStage.setTitle("Width: " + newVal.intValue() + ", Height: " + (int) scene.getHeight())
        );
        scene.heightProperty().addListener((obs, oldVal, newVal) ->
                primaryStage.setTitle("Width: " + (int) scene.getWidth() + ", Height: " + newVal.intValue())
        );

        Button button = new Button("Finish");
        button.setStyle(
            "-fx-background-color: #303843; " + // Light brown color
            "-fx-text-fill: white; " +         // White text
            "-fx-font-size: 18px; " +         // Larger font size
            "-fx-padding: 10px 20px; " +      // Padding for better sizing
            "-fx-background-radius: 10; " +  // Rounded corners
            "-fx-border-color: #FFFFFF; " +   // Optional: add a border
            "-fx-border-width: 1px; " +       // Border thickness
            "-fx-border-radius: 10;"         // Rounded border to match background
        );
        
        // Place the button below the toolbox
        double toolboxX = 950; // Toolbox X coordinate
        double toolboxY = 0;   // Toolbox Y coordinate
        double toolboxHeight = 720; // Toolbox height
        
        button.setLayoutX(toolboxX + 12); // Center the button under the toolbox
        button.setLayoutY(toolboxY + toolboxHeight + 10); // Place it slightly below the toolbox
        pane.getChildren().add(button);

        button.setOnAction(event -> {
            List<Tile> earthHall = tileMap.get("earth");
            List<Tile> airHall = tileMap.get("air");
            List<Tile> waterHall = tileMap.get("water");
            List<Tile> fireHall = tileMap.get("fire");

            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Message Here...");
            alert.setHeaderText("Look, an Information Dialog");
            alert.setContentText("I have a great message for you!");
        
            // Check constraints for each hall
            if (earthHall == null || earthHall.size() < 6) {
                alert.setContentText("The earth hall must contain at least 6 objects.");
                alert.showAndWait();
                return;
            }
        
            if (airHall == null || airHall.size() < 9) {
                alert.setContentText("The air hall must contain at least 9 objects.");
                alert.showAndWait();
                return;
            }
        
            if (waterHall == null || waterHall.size() < 13) {
                alert.setContentText("The water hall must contain at least 13 objects.");
                alert.showAndWait();
                return;
            }
        
            if (fireHall == null || fireHall.size() < 17) {
                alert.setContentText("The fire hall must contain at least 17 objects.");
                alert.showAndWait();
                return;
            }


        





        });






        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.S) {
                for (TiledHall hall : halls) {
                    runeLocationList.add(getRuneLocatiom(hall));
                }
            } else if (event.getCode() == KeyCode.R) {
                // Uncomment this if useRevealEnchantment is implemented:
                useRevealEnchantment(runeLocationList.get(0), hall1);
            }
        });


    }

    private void setHallPosition(TiledHall hall, int x, int y) {
        hall.setLayoutX(x);
        hall.setLayoutY(y);
        hall.getGrid().setTopLeftXCordinate(hall.getGrid().topLeftXCoordinate + x);
        hall.getGrid().setTopLeftYCordinate(hall.getGrid().topLeftYCoordinate + y);
    }

    private void addToolbox(Pane root, List<TiledHall> halls) {
        // Define toolbox dimensions and positions relative to the screen
        // double toolboxX = 1105; // Absolute X position for the toolbox on the screen
        double toolboxX = 950;
        double toolboxY = 0;   // Absolute Y position for the toolbox on the screen
        double toolboxWidth = 150;
        double toolboxHeight = 720;

        // Add toolbox background
        Rectangle chest = new Rectangle(toolboxX, toolboxY, toolboxWidth, toolboxHeight);
        chest.setFill(new ImagePattern(CHEST_IMAGE));
        root.getChildren().add(chest);

        // Define absolute positions for draggable objects relative to the screen
        double objectStartX = toolboxX + 70; // Absolute X position for objects
        double[] positionsY = {
            toolboxY + 100,  toolboxY + 100+64+20, toolboxY + 184+32+20,
            toolboxY + 236+42+20, toolboxY + 362+20, toolboxY + 410+20,
            toolboxY + 462+20
        };

        // Create draggable objects (absolute positioning)
        createDraggableObject(objectStartX, positionsY[0], Pillar_IMAGE, root, 32, 64, halls, 'P');
        createDraggableObject(objectStartX, positionsY[1], Ladder_IMAGE, root, 32, 32, halls, 'L');

        createDraggableObject(objectStartX, positionsY[2], BOX_IMAGE, root, 32, 32, halls, 'b');
        createDraggableObject(objectStartX, positionsY[3], BoxOnBox_IMAGE, root, 32, 64, halls, 'B');
        createDraggableObject(objectStartX, positionsY[4], Cube_IMAGE, root, 32, 32, halls, 'c');
        createDraggableObject(objectStartX, positionsY[5], Skull_IMAGE, root, 32, 32, halls, 'S');

        createDraggableObject(objectStartX, positionsY[6], CHEST, root, 32, 32, halls, 'C');


        // createDraggableObject(objectStartX, positionsY[6], Chest_IMAGE, root, 32, 32, halls, 'C');
    }

    private void createDraggableObject(double x, double y, Image image, Pane root, double width, double height, List<TiledHall> halls, char tileType) {
        Rectangle object = new Rectangle(x, y, width, height);
        object.setFill(new ImagePattern(image));
    
        object.setOnMousePressed(event -> {
            // Create a new copy when dragging starts
            Rectangle clone = new Rectangle(width, height);
            clone.setFill(new ImagePattern(image));
            root.getChildren().add(clone);
            clone.setVisible(false);
    
            // Update the position of the clone in real time
            object.setOnMouseDragged(dragEvent -> {
                clone.setVisible(true);
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

                int adjustmentForBigObjects=0;
                boolean flag=false;
    
                // Adjust the Y-coordinate for tall objects (32x64)
                if (height == 64) {
                    flag=true;
                    adjustmentForBigObjects=32;
                    sceneY += adjustmentForBigObjects; // Align the bottom part with the grid
                }
    
                for (TiledHall hall : halls) {
                    Grid grid = hall.getGrid();
    
                    // Check if the adjusted position is within the grid
                    if (grid.coordinatesAreInGrid(sceneX, sceneY)) {
                        Tile targetTile = grid.findTileUsingCoordinates(sceneX, sceneY);
    
                        if (targetTile != null && targetTile.getTileType() == 'E') {
                            // Update the tile's type to match the dragged object
                            targetTile.changeTileType(tileType);


                            //add the target tile to the tileList
                            if(!tileMap.get(hall).contains(targetTile)){tileMap.get(hall).add(targetTile);}


                            if (flag){
                            Tile flagTile=grid.findTileUsingCoordinates(sceneX, sceneY-32);
                            if(flagTile!=null){
                                flagTile.changeTileType('X');
                            }
                            }
    
                            // Snap the clone to the target tile
                            clone.setX(targetTile.getLeftSide());
                            clone.setY(targetTile.getTopSide()-adjustmentForBigObjects);    
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
    
    
    //hides the rune in one of the objects
    private Pair<Integer,Integer> getRuneLocatiom(TiledHall hall){
        if(!tileMap.containsKey(hall) || tileMap.get(hall).size()==0){return null;}

        int length = tileMap.get(hall).size();
        Random random = new Random();
        int randomIndex = random.nextInt(length);

        Pair<Integer,Integer> myPair = new Pair<Integer,Integer>(tileMap.get(hall).get(randomIndex).getLeftSide(), tileMap.get(hall).get(randomIndex).getTopSide());

        return myPair;

        

    }



    

    private void useRevealEnchantment(Pair<Integer,Integer> location,TiledHall hall){
        Random random = new Random();

        List<Tile> tileMap = hall.getGrid().getTileMap();

        int size = tileMap.size();

        int BottomYBound = tileMap.get(0).getTopSide();
        int UpperYBound = tileMap.get(size-1).getBottomSide();

        int BottomXBound = tileMap.get(0).getLeftSide();
        int UpperXBound = tileMap.get(size-1).getRightSide();



        int yStartingPoint=Integer.MAX_VALUE;
        int xStartingPoint=Integer.MAX_VALUE;


        int verticalStep = random.nextInt(4);
        yStartingPoint = location.getValue()-verticalStep*tileSize;
        while(BottomYBound>yStartingPoint || yStartingPoint+4*tileSize>UpperYBound || yStartingPoint==Integer.MAX_VALUE){
            verticalStep = random.nextInt(4);
            yStartingPoint = location.getValue()-verticalStep*tileSize;
        }
        
        
        int horizontalStep = random.nextInt(4);
        xStartingPoint = location.getKey()-horizontalStep*tileSize;
        while(xStartingPoint<BottomXBound || xStartingPoint+4*tileSize>UpperXBound ||xStartingPoint==Integer.MAX_VALUE){
            horizontalStep = random.nextInt(4);
            xStartingPoint = location.getKey()-horizontalStep*tileSize;
        }

        highlightRuneLocation(xStartingPoint, yStartingPoint, hall);
        
    }
    public void highlightRuneLocation(int xPoint,int yPoint,TiledHall hall){

        Rectangle rectangle = new Rectangle(xPoint, yPoint, 4*tileSize, 4*tileSize);
        rectangle.setStroke(Color.WHITE); // Border color
        rectangle.setFill(Color.TRANSPARENT); // Transparent fill

        // Add the rectangle to the pane
        hall.getChildren().add(rectangle);

        // Create a PauseTransition to remove the rectangle after 10 seconds
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(event -> hall.getChildren().remove(rectangle));
        pause.play();
    }

    public static void main(String[] args) {
        launch(args);
    }
    


}
