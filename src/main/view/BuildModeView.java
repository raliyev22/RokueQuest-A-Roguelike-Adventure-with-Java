package main.view;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import java.util.ArrayList;

public class BuildModeView extends Application {

    static final Image CHEST_IMAGE = new Image("/rokue-like_assets/Build_Mode_Chest_Full_View.png");
    static final Image OBJECT1_IMAGE = new Image("/rokue-like_assets/Pillar_16_43.png");
    static final Image OBJECT2_IMAGE = new Image("/rokue-like_assets/TileWithLadder_16_16.png");
    static final Image OBJECT3_IMAGE = new Image("/rokue-like_assets/Box_16_21.png");
    static final Image OBJECT4_IMAGE = new Image("/rokue-like_assets/BoxOnTopOfBox_16_32.png");
    static final Image OBJECT5_IMAGE = new Image("/rokue-like_assets/Cube_8_14.png");
    static final Image OBJECT6_IMAGE = new Image("/rokue-like_assets/Skull_6_6.png");
    static final Image OBJECT7_IMAGE = new Image("/rokue-like_assets/Chest_Closed_16_14.png");

    static final double GRID_X_OFFSET = 0;
    static final double GRID_Y_OFFSET = 0;

    ArrayList<Grid> grids = new ArrayList<>(); // List to store grids

    @Override
    public void start(Stage stage) {
        Pane root = new Pane();

        // Scene setup
        Scene scene = new Scene(root, 1280, 720);

        // Add chest
        Rectangle chest = new Rectangle(1105, 0, 175, 720);
        chest.setFill(new ImagePattern(CHEST_IMAGE));
        root.getChildren().add(chest);

        // Create grids based on provided image boundaries
        grids.add(new Grid(40, 40, 300, 300));   // Top-Left Grid
        grids.add(new Grid(400, 40, 300, 300));  // Top-Right Grid
        grids.add(new Grid(40, 400, 300, 300));  // Bottom-Left Grid
        grids.add(new Grid(400, 400, 300, 300)); // Bottom-Right Grid

        // Draw grids for visual feedback
        grids.forEach(grid -> root.getChildren().add(grid.getGridOutline()));

        // Add draggable objects
        Rectangle object1 = createDraggableObject(1172.5, 100, OBJECT1_IMAGE, root, 40, 75);
        Rectangle object2 = createDraggableObject(1172.5, 200, OBJECT2_IMAGE, root, 40, 50);
        Rectangle object3 = createDraggableObject(1172.5, 275, OBJECT3_IMAGE, root, 40, 50);
        Rectangle object4 = createDraggableObject(1172.5, 350, OBJECT4_IMAGE, root, 40 ,75);
        Rectangle object5 = createDraggableObject(1172.5, 450, OBJECT5_IMAGE, root, 40, 50);
        Rectangle object6 = createDraggableObject(1180, 525, OBJECT6_IMAGE, root, 25, 25);
        Rectangle object7 = createDraggableObject(1172.5, 575, OBJECT7_IMAGE, root, 40, 50);


        root.getChildren().addAll(object1, object2, object3, object4, object5, object6, object7);

        stage.setScene(scene);
        stage.show();
    }

    // Create draggable object logic
    private Rectangle createDraggableObject(double x, double y, Image image, Pane root, double width, double height) {
        Rectangle object = new Rectangle(x, y, width, height);
        object.setFill(new ImagePattern(image));
        
        double[] startPos = new double[2];
    
        object.setOnMousePressed(event -> {
            // If this is the original inventory object, create a new clone
            if (object.getUserData() == null) {
                Rectangle clone = createDraggableObject(x, y, image, root, width, height);
                root.getChildren().add(clone);
                object.setUserData("inventory");
            }
            
            startPos[0] = object.getX();
            startPos[1] = object.getY();
            object.setUserData(new double[]{event.getSceneX(), event.getSceneY()});
        });
    
        object.setOnMouseDragged(event -> {
            double[] start = (double[]) object.getUserData();
            object.setX(object.getX() + event.getSceneX() - start[0]);
            object.setY(object.getY() + event.getSceneY() - start[1]);
            object.setUserData(new double[]{event.getSceneX(), event.getSceneY()});
        });
    
        object.setOnMouseReleased(event -> {
            boolean insideGrid = false;
            for (Grid grid : grids) {
                if (grid.contains(object.getX(), object.getY(), object.getWidth(), object.getHeight())) {
                    insideGrid = true;
                    break;
                }
            }
    
            if (!insideGrid) {
                // Return to inventory if outside all grids
                object.setX(startPos[0]);
                object.setY(startPos[1]);
            } else {
                System.out.printf("Object placed at X: %.2f, Y: %.2f%n", object.getX(), object.getY());
            }
        });
    
        return object;
    }
    
    // Grid class to define grid areas
    static class Grid {
        double x, y, width, height;

        Grid(double x, double y, double width, double height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        boolean contains(double objX, double objY, double objWidth, double objHeight) {
            return objX >= x && objX + objWidth <= x + width &&
                   objY >= y && objY + objHeight <= y + height;
        }

        Rectangle getGridOutline() {
            Rectangle rect = new Rectangle(x, y, width, height);
            rect.setFill(null);
            rect.setStroke(javafx.scene.paint.Color.RED);
            rect.setStrokeWidth(2);
            return rect;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
