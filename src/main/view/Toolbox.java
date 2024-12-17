package main.view;

import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.List;
import test.TiledHall;

public class Toolbox extends Pane {

    static final Image CHEST_IMAGE = new Image("/rokue-like_assets/Build_Mode_Chest_Full_View.png");
    static final Image OBJECT1_IMAGE = new Image("/rokue-like_assets/Pillar_16_43.png");
    static final Image OBJECT2_IMAGE = new Image("/rokue-like_assets/TileWithLadder_16_16.png");
    static final Image OBJECT3_IMAGE = new Image("/rokue-like_assets/Box_16_21.png");
    static final Image OBJECT4_IMAGE = new Image("/rokue-like_assets/BoxOnTopOfBox_16_32.png");
    static final Image OBJECT5_IMAGE = new Image("/rokue-like_assets/Cube_8_14.png");
    static final Image OBJECT6_IMAGE = new Image("/rokue-like_assets/Skull_6_6.png");
    static final Image OBJECT7_IMAGE = new Image("/rokue-like_assets/Chest_Closed_16_14.png");

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
        Rectangle object1 = createDraggableObject(startX, positionsY[0], OBJECT1_IMAGE, root, 30, 75, halls);
        Rectangle object2 = createDraggableObject(startX, positionsY[1], OBJECT2_IMAGE, root, 30, 30, halls);
        Rectangle object3 = createDraggableObject(startX, positionsY[2], OBJECT3_IMAGE, root, 30, 40, halls);
        Rectangle object4 = createDraggableObject(startX, positionsY[3], OBJECT4_IMAGE, root, 30, 60, halls);
        Rectangle object5 = createDraggableObject(startX, positionsY[4], OBJECT5_IMAGE, root, 20, 30, halls);
        Rectangle object6 = createDraggableObject(startX, positionsY[5], OBJECT6_IMAGE, root, 15, 15, halls);
        Rectangle object7 = createDraggableObject(startX, positionsY[6], OBJECT7_IMAGE, root, 30, 40, halls);

        getChildren().addAll(object1, object2, object3, object4, object5, object6, object7);
    }

    private Rectangle createDraggableObject(double x, double y, Image image, Pane root, double width, double height, List<TiledHall> halls) {
        Rectangle object = new Rectangle(x, y, width, height);
        object.setFill(new ImagePattern(image));

        double[] startPos = new double[2];

        object.setOnMousePressed(event -> {
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
            boolean insideHall = false;
            for (TiledHall hall : halls) {
                if (hall.getBoundsInParent().contains(object.getX(), object.getY(), object.getWidth(), object.getHeight())) {
                    insideHall = true;
                    System.out.printf("Object placed in hall at X: %.2f, Y: %.2f%n", object.getX(), object.getY());
                    break;
                }
            }
            if (!insideHall) {
                object.setX(startPos[0]);
                object.setY(startPos[1]);
            }
        });

        return object;
    }
}
