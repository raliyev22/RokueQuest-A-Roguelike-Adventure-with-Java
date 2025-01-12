package oldFiles;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import java.util.List;
import java.util.ArrayList;


public class RunWithToolbox extends Application {

    private int tileSize = 32;
    static final Image tileImage = new Image("/rokue-like_assets/Tile_x2_32_32.png");

    static final Image CHEST_IMAGE = new Image("/rokue-like_assets/Build_Mode_Chest_Full_View.png");
    static final Image OBJECT1_IMAGE = new Image("/rokue-like_assets/Pillar_16_43.png");
    static final Image OBJECT2_IMAGE = new Image("/rokue-like_assets/TileWithLadder_16_16.png");
    static final Image OBJECT3_IMAGE = new Image("/rokue-like_assets/Box_16_21.png");
    static final Image OBJECT4_IMAGE = new Image("/rokue-like_assets/BoxOnTopOfBox_16_32.png");
    static final Image OBJECT5_IMAGE = new Image("/rokue-like_assets/Cube_8_14.png");
    static final Image OBJECT6_IMAGE = new Image("/rokue-like_assets/Skull_6_6.png");
    static final Image OBJECT7_IMAGE = new Image("/rokue-like_assets/Chest_Closed_16_14.png");

    ArrayList<TiledHall> halls = new ArrayList<>(); // List of TiledHalls

    @Override
    public void start(Stage primaryStage) {
        Pane root = new Pane();

        // -------------------- Background Grid --------------------
        for (int a = 0; a < 1536; a += tileSize) {
            for (int b = 0; b < 800; b += tileSize) {
                Rectangle tileRectangle = new Rectangle(a, b, tileSize, tileSize);
                tileRectangle.setFill(new ImagePattern(tileImage));
                root.getChildren().add(tileRectangle);
            }
        }

        // -------------------- TiledHalls --------------------
        TiledHall hall1 = new TiledHall(10, 8);
        TiledHall hall2 = new TiledHall(10, 8);
        TiledHall hall3 = new TiledHall(10, 8);
        TiledHall hall4 = new TiledHall(10, 8);

        hall1.setLayoutX(98);
        hall1.setLayoutY(74);
        hall2.setLayoutX(482);
        hall2.setLayoutY(74);
        hall3.setLayoutX(98);
        hall3.setLayoutY(490);
        hall4.setLayoutX(482);
        hall4.setLayoutY(490);

        root.getChildren().addAll(hall1, hall2, hall3, hall4);

        halls.add(hall1);
        halls.add(hall2);
        halls.add(hall3);
        halls.add(hall4);

        // Adding XButtong
        ImageView xButtonView = new ImageView(new Image("/rokue-like_assets/x_button.png"));
        xButtonView.setFitWidth(30);
        xButtonView.setFitHeight(29);

        Button xButton = new Button();
        xButton.setGraphic(xButtonView);
        xButton.setStyle("-fx-background-color: transparent;");
        xButton.setLayoutX(1175);
        xButton.setLayoutY(15);
        xButton.setOnAction(event -> primaryStage.close());
        // Creating Chest and Inserting Image to Chest
        Rectangle chest = new Rectangle(1105, 50, 175, 720);
        chest.setFill(new ImagePattern(CHEST_IMAGE));
        root.getChildren().add(chest);

        // Adding Draggable Objects to the chest
        // Add draggable objects to the toolbox
        Rectangle object1 = createDraggableObject(1175, 150, OBJECT1_IMAGE, root, 30, 75, halls);
        Rectangle object2 = createDraggableObject(1175, 250, OBJECT2_IMAGE, root, 30, 30, halls);
        Rectangle object3 = createDraggableObject(1175, 325, OBJECT3_IMAGE, root, 30, 40, halls);
        Rectangle object4 = createDraggableObject(1175, 400, OBJECT4_IMAGE, root, 30, 60, halls);
        Rectangle object5 = createDraggableObject(1175, 475, OBJECT5_IMAGE, root, 20, 30, halls);
        Rectangle object6 = createDraggableObject(1175, 525, OBJECT6_IMAGE, root, 15, 15, halls);
        Rectangle object7 = createDraggableObject(1175, 575, OBJECT7_IMAGE, root, 30, 40, halls);

        // Add all objects to the root
        root.getChildren().addAll(object1, object2, object3, object4, object5, object6, object7);


        
        Scene scene = new Scene(root, 1536, 800);
        primaryStage.setTitle("Tiled Hall with Toolbox");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // the way of creating draggable object
    private Rectangle createDraggableObject(double x, double y, Image image, Pane root, double width, double height, List<TiledHall> halls) {
        Rectangle object = new Rectangle(x, y, width, height);
        object.setFill(new ImagePattern(image));

        double[] startPos = new double[2];
        
        //Saves the object's initial position when dragging starts.
        object.setOnMousePressed(event -> {
            startPos[0] = object.getX();
            startPos[1] = object.getY();
            object.setUserData(new double[]{event.getSceneX(), event.getSceneY()});
        });
        //Updates the object's position as the mouse is dragged.
        object.setOnMouseDragged(event -> {
            double[] start = (double[]) object.getUserData();
            object.setX(object.getX() + event.getSceneX() - start[0]);
            object.setY(object.getY() + event.getSceneY() - start[1]);
            object.setUserData(new double[]{event.getSceneX(), event.getSceneY()});
        });
        //Checks if the object is dropped inside any of the TiledHall bounds.
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

    public static void main(String[] args) {
        launch(args);
    }
}
