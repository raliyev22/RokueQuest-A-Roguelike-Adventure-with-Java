package main.view;

import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.List;
import test.TiledHall;
import main.utils.*;

public class Toolbox extends Pane {

    static final Image CHEST_IMAGE = new Image("/rokue-like_assets/Build_Mode_Chest_Full_View.png");
    static final Image Pillar_IMAGE = new Image("/rokue-like_assets/Pillar_16_43.png");
    static final Image Ladder_IMAGE = new Image("/rokue-like_assets/TileWithLadder_16_16.png");
    static final Image Box_IMAGE = new Image("/rokue-like_assets/Box_16_21.png");
    static final Image BoxOnBox_IMAGE = new Image("/rokue-like_assets/BoxOnTopOfBox_16_32.png");
    static final Image Cube_IMAGE = new Image("/rokue-like_assets/Cube_8_14.png");
    static final Image Skull_IMAGE = new Image("/rokue-like_assets/Skull_6_6.png");
    static final Image Chest_IMAGE = new Image("/rokue-like_assets/Chest_Closed_16_14.png");

    public Toolbox(double x, double y, double width, double height, Pane root, List<TiledHall> halls) {
        setLayoutX(x);
        setLayoutY(y);
        setPrefSize(width, height);

        // Add toolbox background (Chest image)
        Rectangle chest = new Rectangle(0, 0, width, height);
        chest.setFill(new ImagePattern(CHEST_IMAGE));
        getChildren().add(chest);

        // Adding draggable objects
        addDraggableObjects(root, halls);
    }

    private void addDraggableObjects(Pane root, List<TiledHall> halls) {
        // Define draggable objects' positions
        double startX = 70; // Relative position in the toolbox
        double[] positionsY = {100, 180, 260, 340, 420, 500, 580};

        // Create draggable objects
        Rectangle object1 = createDraggableObject(startX, positionsY[0], Pillar_IMAGE, root, 30, 75, halls,'P');
        Rectangle object2 = createDraggableObject(startX, positionsY[1], Ladder_IMAGE, root, 30, 30, halls,'L');
        Rectangle object3 = createDraggableObject(startX, positionsY[2], Box_IMAGE, root, 30, 40, halls,'b');
        Rectangle object4 = createDraggableObject(startX, positionsY[3], BoxOnBox_IMAGE, root, 30, 60, halls,'B');
        Rectangle object5 = createDraggableObject(startX, positionsY[4], Cube_IMAGE, root, 20, 30, halls,'c');
        Rectangle object6 = createDraggableObject(startX, positionsY[5], Skull_IMAGE, root, 15, 15, halls,'S');
        Rectangle object7 = createDraggableObject(startX, positionsY[6], CHEST_IMAGE, root, 30, 40, halls,'C');
        getChildren().addAll(object1, object2, object3, object4, object5, object6, object7);
    }

private Rectangle createDraggableObject(double x, double y, Image image, Pane root, double width, double height, List<TiledHall> halls, char c) {
    Rectangle object = new Rectangle(x, y, width, height);
    object.setFill(new ImagePattern(image));

    double[] startPos = new double[2]; // Save the initial position for resetting

    // Save starting position on press
    object.setOnMousePressed(event -> {
    // Save the start position relative to the scene
        startPos[0] = object.getLayoutX();
        startPos[1] = object.getLayoutY();
        object.setUserData(new double[]{event.getSceneX(), event.getSceneY()});
});

    // Update position while dragging
    object.setOnMouseDragged(event -> {
        double[] start = (double[]) object.getUserData();
    
        // Calculate new position relative to the scene
        double deltaX = event.getSceneX() - start[0];
        double deltaY = event.getSceneY() - start[1];
    
        object.setLayoutX(startPos[0] + deltaX);
        object.setLayoutY(startPos[1] + deltaY);
    });

    // On release, check if object is within any hall's grid and snap it to a tile
    object.setOnMouseReleased(event -> {
        boolean snappedToTile = false;
    
        // Convert object position to scene coordinates
        

        //System.out.printf("%f %f  PPOPSODPSDOPSOD",event.getSceneX(),event.getSceneY());
    
        for (TiledHall hall : halls) {
            // Convert hall's grid position to scene coordinates
            double hallLayoutX = hall.getLayoutX();
            double hallLayoutY = hall.getLayoutY();
            
            
            if (hall.getGrid().coordinatesAreInGrid(event.getSceneX() , event.getSceneY() )) {
                // Get the grid of the hall
                Grid grid = hall.getGrid();
                Tile targetTile = grid.findTileUsingCoordinates(event.getSceneX() , event.getSceneY());
                grid.changeTileWithIndex(targetTile.getLeftSide(), targetTile.getTopSide(), c);
    
                if (targetTile != null) {
                    // Snap the object to the tile
                    System.out.println(targetTile);
                    //find that tile and replace the tilemap and the image 
                    object.setLayoutX(hallLayoutX + targetTile.getLeftSide());
                    object.setLayoutY(hallLayoutY + targetTile.getTopSide());
                    snappedToTile = true;
    
                    System.out.printf("Object snapped to tile: X=%d, Y=%d%n", targetTile.getLeftSide(), targetTile.getTopSide());
                    break;
                }
                
            }
        }
    
        // Reset to starting position if not snapped to a tile
        if (!snappedToTile) {
            object.setLayoutX(startPos[0]);
            object.setLayoutY(startPos[1]);
        }
    });
    

    root.getChildren().add(object);
    return object;
}

}
