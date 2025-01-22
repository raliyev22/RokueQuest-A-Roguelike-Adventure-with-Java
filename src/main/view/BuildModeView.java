package main.view;
import main.Main;
import main.model.HallType;
import main.model.Images;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import javafx.util.Pair;

import javafx.util.Duration;

import javafx.scene.paint.ImagePattern;

import javafx.scene.control.Alert;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import main.utils.DialogDesignUtils;
import main.utils.Grid;
import main.utils.SoundEffects;
import main.utils.Tile;
import main.utils.TiledHall;

import java.util.Random;

import main.controller.BuildModeController;
import main.controller.PlayModeController;

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
    static final Image Exit_Button = new Image("/rokue-like_assets/ExitButton_x2_32_32.png");
    
    private HashMap<TiledHall,List<Tile>> tileMap = new HashMap<TiledHall,List<Tile>>();
    private List<Pair<Integer,Integer>> runeLocationList = new ArrayList<Pair<Integer,Integer>>();
    public static HashMap<TiledHall, List<Tile>> sharedTileMap = new HashMap<>();
    public static HashMap<TiledHall, HallType> hallTypeMap = new HashMap<>(); // Yeni ekleme

    private List<TiledHall> halls;
    Pane pane;
    BuildModeController controller = new BuildModeController();


    SoundEffects soundPlayer = SoundEffects.getInstance();

    public void start(Stage primaryStage) {

        // Create a pane
        pane = new Pane();

        //Create the background grid
        for (int a = 0; a < 1200; a += tileSize) {
            for (int b = 0; b < 800; b += tileSize) {
                Rectangle tideRectangle = new Rectangle(a, b, tileSize, tileSize);
                tideRectangle.setFill(new ImagePattern(tileImage));
                pane.getChildren().add(tideRectangle);
            }
        }
        
        showTopWalls();

        // Create 4 TiledHall instances with specific sizes
        TiledHall hall1 = new TiledHall(10, 7, new Grid(10, 9, 32, 32, 10, 40),1);
        TiledHall hall2 = new TiledHall(10, 7, new Grid(10, 9, 32, 32, 10, 40),1);
        TiledHall hall3 = new TiledHall(10, 7, new Grid(10, 9, 32, 32, 10, 40),1);
        TiledHall hall4 = new TiledHall(10, 7, new Grid(10, 9, 32, 32, 10, 40),1);

        halls = new ArrayList<>();
        halls.add(hall1);
        halls.add(hall2);
        halls.add(hall3);
        halls.add(hall4);

        // Set desired positions for each TiledHall
        // Adjust the positions of the halls
        // Adjust the positions of the halls with vertical gaps
        setHallPosition(hall1, 150, 25);  // First row
        setHallPosition(hall2, 550, 25);  // First row
        setHallPosition(hall3, 150, 425); // Second row with vertical gap
        setHallPosition(hall4, 550, 425); // Second row with vertical gap

        // Add TiledHalls to the pane
        pane.getChildren().addAll(hall1, hall2, hall3, hall4);
        //pane.getChildren().add(hall1);
        showBottomWalls();

        // Add toolbox UI directly in Run
        addToolbox(pane, halls);

        //hashmap to that keeps count of objects in each hall
        addTiledHall(hall1, HallType.EARTH);
        addTiledHall(hall2, HallType.AIR);
        addTiledHall(hall3, HallType.WATER);
        addTiledHall(hall4, HallType.FIRE);
        // tileMap.put(hall1, new ArrayList<Tile>());
        // tileMap.put(hall2, new ArrayList<Tile>());
        // tileMap.put(hall3, new ArrayList<Tile>());
        // tileMap.put(hall4, new ArrayList<Tile>());

        // Create a scene
        Scene scene = new Scene(pane, 1200, 800); // Example: Decrease width
        primaryStage.setScene(scene);
        javafx.geometry.Rectangle2D screenBounds = javafx.stage.Screen.getPrimary().getVisualBounds();
        primaryStage.setX((screenBounds.getWidth() - 1200) / 2); // Width of the scene
        primaryStage.setY((screenBounds.getHeight() - 800) / 2); // Height of the scene
        primaryStage.show();

        // Place the button below the toolbox
        double toolboxX = 950; // Toolbox X coordinate
        double toolboxY = 20;   // Toolbox Y coordinate
        double toolboxWidth = 150; // Toolbox width
        double toolboxHeight = 720; // Toolbox height


        HBox buttonContainer = new HBox(10);
		buttonContainer.setAlignment(javafx.geometry.Pos.CENTER);
        buttonContainer.setLayoutX(toolboxX+18);
        buttonContainer.setLayoutY(toolboxHeight-30);


        //Exit button to exit the play mode
        Button exitButton = new Button();
		exitButton.setStyle("-fx-background-color: transparent;"); 

        Button minusButton = new Button();
        minusButton.setStyle("-fx-background-color: transparent;");

        ImageView minusButtonView = new ImageView(new Image("/rokue-like_assets/MinusButton_x4_64_64.png"));
        minusButtonView.setFitWidth(35);
        minusButtonView.setFitHeight(35);

        minusButton.setGraphic(minusButtonView);
        minusButton.setPrefWidth(35);
        minusButton.setPrefHeight(35);

        minusButton.setOnMouseEntered(event -> {
            minusButton.setCursor(Cursor.HAND);
        });

        minusButton.setOnMouseExited(event -> {
            minusButton.setCursor(Cursor.DEFAULT);
        });

        minusButton.setOnMouseClicked(event -> {
            clearHalls();
            soundPlayer.playSoundEffectInThread("blueButtons");
        });

        Button randomButton = new Button();
        randomButton.setStyle("-fx-background-color: transparent;");

        ImageView randomButtonView = new ImageView(new Image("/rokue-like_assets/RandomButton_x4_64_64.png"));
        randomButtonView.setFitWidth(35);
        randomButtonView.setFitHeight(35);

        randomButton.setGraphic(randomButtonView);
        randomButton.setPrefWidth(35);
        randomButton.setPrefHeight(35);

        // randomButton.setLayoutX(toolboxX+48);
        // randomButton.setLayoutY(toolboxHeight-75);
        
        randomButton.setOnMouseEntered(event -> {
            randomButton.setCursor(Cursor.HAND);
        });
        
        randomButton.setOnMouseExited(event -> {
            randomButton.setCursor(Cursor.DEFAULT);
        });

        randomButton.setOnMouseClicked(event -> {
            soundPlayer.playSoundEffectInThread("blueButtons");
            randomCreateObjects();
        });

        HBox buttonContainer2 = new HBox(10);
        buttonContainer2.setAlignment(javafx.geometry.Pos.CENTER);
        buttonContainer2.setLayoutX(toolboxX+18);
        buttonContainer2.setLayoutY(toolboxHeight-75);
        buttonContainer2.getChildren().addAll(randomButton, minusButton);
        pane.getChildren().add(buttonContainer2);

		ImageView exitButtonView = new javafx.scene.image.ImageView(Images.IMAGE_EXITBUTTON_x4);
		exitButtonView.setFitWidth(35);
		exitButtonView.setFitHeight(35);

		exitButton.setGraphic(exitButtonView);
		exitButton.setPrefWidth(35);
		exitButton.setPrefHeight(35);      

        exitButton.setOnMouseEntered(event -> {
            exitButton.setCursor(Cursor.HAND);
        });
        
        exitButton.setOnMouseExited(event -> {
            exitButton.setCursor(Cursor.DEFAULT);
        });

        exitButton.setOnMouseClicked(event -> {
            soundPlayer.playSoundEffectInThread("blueButtons");

            Main mainPage = new Main();
            javafx.geometry.Rectangle2D screenBounds1 = javafx.stage.Screen.getPrimary().getVisualBounds();
        
            // Set up the main stage in the center of the screen
            primaryStage.setX((screenBounds1.getWidth() - 600) / 2);
            primaryStage.setY((screenBounds1.getHeight() - 400) / 2);
        
            mainPage.start(primaryStage);
        });

        Button playButton = new Button();
		playButton.setStyle("-fx-background-color: transparent;"); 

		ImageView playButtonView = new javafx.scene.image.ImageView(Images.IMAGE_PLAYBUTTON_x4);
		playButtonView.setFitWidth(35);
		playButtonView.setFitHeight(35);

		playButton.setGraphic(playButtonView);
		playButton.setPrefWidth(35);
		playButton.setPrefHeight(35);     

        playButton.setOnMouseEntered(event -> {
            playButton.setCursor(Cursor.HAND);
        });
        
        playButton.setOnMouseExited(event -> {
            playButton.setCursor(Cursor.DEFAULT);
        });

        playButton.setOnMouseClicked(event -> {
            soundPlayer.playSoundEffectInThread("blueButtons");

            List<Tile> earthHall = tileMap.get(hall1);
            List<Tile> airHall = tileMap.get(hall2);
            List<Tile> waterHall = tileMap.get(hall3);
            List<Tile> fireHall = tileMap.get(hall4);
        
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Build Mode Validation");
            alert.setHeaderText("⚠️ Object Count Validation Failed");
        
            StringBuilder message = new StringBuilder();
            boolean hasError = false;
        
            if (earthHall == null || earthHall.size() < 0) {
                message.append("❌ Earth Hall: Requires at least 6 objects.\n");
                hasError = true;
            }
            if (airHall == null || airHall.size() < 0) {
                message.append("❌ Air Hall: Requires at least 9 objects.\n");
                hasError = true;
            }
            if (waterHall == null || waterHall.size() < 0) {
                message.append("❌ Water Hall: Requires at least 13 objects.\n");
                hasError = true;
            }
            if (fireHall == null || fireHall.size() < 0) {
                message.append("❌ Fire Hall: Requires at least 17 objects.\n");
                hasError = true;
            }
        
            if (!hasError) {
				PlayModeController.earthHall = hall1.getGrid();
				PlayModeController.airHall = hall2.getGrid();
				PlayModeController.waterHall = hall3.getGrid();
				PlayModeController.fireHall = hall4.getGrid();

                PlayModeController playModeController = new PlayModeController();
                playModeController.start(primaryStage);
                return;
                // PlayModeView2 view = new PlayModeView2(halls);
                // view.start(primaryStage);
                // return;
            }
        
        alert.setContentText(message.toString());
        DialogDesignUtils.styleDialog(alert);

        alert.showAndWait();
        });

    buttonContainer.getChildren().addAll(exitButton, playButton);
    pane.getChildren().add(buttonContainer);

    //Hide the rune in one of the objects for each hall
    scene.setOnKeyPressed(event -> {
        if (event.getCode() == KeyCode.S) {
            for (TiledHall hall : halls) {
                runeLocationList.add(getRuneLocatiom(hall));
            }
        } else if (event.getCode() == KeyCode.R) {
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
        double toolboxX = 950;
        double toolboxY = 25;   // Absolute Y position for the toolbox on the screen
        double toolboxWidth = 150;
        double toolboxHeight = 720;

        // Add toolbox background
        Rectangle chest = new Rectangle(toolboxX, toolboxY, toolboxWidth, toolboxHeight);
        chest.setFill(new ImagePattern(CHEST_IMAGE));
        root.getChildren().add(chest);

        // Define absolute positions for draggable objects relative to the screen
        double objectStartX = toolboxX + 60; // Absolute X position for objects
        double[] positionsY = {
            toolboxY + 100,  toolboxY + 100+64+20, toolboxY + 184+32+20,
            toolboxY + 236+42+20, toolboxY + 362+20, toolboxY + 410+20,
            toolboxY + 462+20,toolboxY + 514+20
        };

        // Create draggable objects (absolute positioning)
        createDraggableObject(objectStartX, positionsY[0], Pillar_IMAGE, root, 32, 64, halls, 'P');
        createDraggableObject(objectStartX, positionsY[1], Ladder_IMAGE, root, 32, 32, halls, 'T');

        createDraggableObject(objectStartX, positionsY[2], BOX_IMAGE, root, 32, 32, halls, 'B');
        createDraggableObject(objectStartX, positionsY[3], BoxOnBox_IMAGE, root, 32, 64, halls, 'D');
        createDraggableObject(objectStartX, positionsY[4], Cube_IMAGE, root, 32, 32, halls, 'G');
        createDraggableObject(objectStartX, positionsY[5], Skull_IMAGE, root, 32, 32, halls, 'S');

        createDraggableObject(objectStartX, positionsY[6], CHEST, root, 32, 32, halls, 'H');


        createDraggableObject(objectStartX, positionsY[7], Images.IMAGE_BLUEELIXIR_x2, root, 32, 32, halls, 'V');
    }

    private void clearHalls() {
        for (TiledHall hall : halls) {
            for (Tile tile : tileMap.get(hall)){
                tile.changeTileType('E');
            }
            hall.getChildren().clear();
        }
    }

    private void randomCreateObjects(){
        int i = 0;
        int neededObj;
        clearHalls();
        for (TiledHall hall : halls){
            if (i==0) {
                randomCreateObjectHelper(hall, 6);
                i+=1;
            }
            else if (i==1) {
                randomCreateObjectHelper(hall, 9);
                i+=1;
            }
            else if (i==2) {
                randomCreateObjectHelper(hall, 13);
                i+=1;
            }
            else if (i==3) {
                randomCreateObjectHelper(hall, 17);
                i+=1;
            }

            System.out.println(hall.getGrid().toString());
        }
    }

    private void randomCreateObjectHelper(TiledHall hall, int neededObj){
        Image img;
        int w;
        int h;
        char tileType;
        Random random = new Random();
        for (int j = 0; j < neededObj; j++){
            //First pick which object is going to be putted
            int objRand = random.nextInt(8);
            if (objRand == 0) {
                img = Pillar_IMAGE;
                w = 32;
                h = 64;
                tileType = 'P';
            }
            else if (objRand == 1){
                img = Ladder_IMAGE;
                w = 32;
                h = 32;
                tileType = 'T';
            }
            else if (objRand == 2){
                img = BOX_IMAGE;
                w = 32;
                h = 32;
                tileType = 'B';
            }
            else if (objRand == 3){
                img = BoxOnBox_IMAGE;
                w = 32;
                h = 64;
                tileType = 'D';
            }
            else if (objRand == 4){
                img = Cube_IMAGE;
                w = 32;
                h = 32;
                tileType = 'G';
            }
            else if (objRand == 5){
                img = Skull_IMAGE;
                w = 32;
                h = 32;
                tileType = 'S';
            }
            else if (objRand == 6) {
                img = Images.IMAGE_BLUEELIXIR_x2;
                w = 32;
                h = 32;
                tileType = 'V';
            } 
            else {
                img = CHEST;
                w = 32;
                h = 32;
                tileType = 'H';
            }
            Grid grid = hall.getGrid();
            Tile chosenTile = grid.getRandomEmptyTile();
            int x = chosenTile.getLeftSide();
            int y = chosenTile.getTopSide();
            Boolean flag = false;
            if (h==64) {
                y -= 32;
                flag = true;
            }



            if (chosenTile.getTileType() == 'E'){
                chosenTile.changeTileType(tileType);
                tileMap.get(hall).add(chosenTile);
                
                if (flag) {
                    Tile flagTile = grid.findTileUsingCoordinates(x + 16, y-16);
                    if (flagTile!=null) {
                        flagTile.changeTileType('!');
                    }
                }


                Rectangle targetRect = new Rectangle(w, h);
                targetRect.setFill(new ImagePattern(img));
                targetRect.setX(x);
                targetRect.setY(y); 
                hall.getChildren().add(targetRect);
            }
            else {
                j-=1;
            }
        }
    }
    

    
    private void createDraggableObject(double x, double y, Image image, Pane root, double width, double height, List<TiledHall> halls, char tileType) {
        Rectangle object = new Rectangle(x, y, width, height);
        object.setFill(new ImagePattern(image));
        
        object.setOnMouseClicked(event -> {
            Rectangle clone = new Rectangle(width, height);
            clone.setFill(new ImagePattern(image));
            root.getChildren().add(clone);
            clone.setX(event.getSceneX() - width  / 2); // Center the clone around the mouse
            clone.setY(event.getSceneY() - height / 2);

            
            clone.setOnMouseMoved(dragEvent -> {
                clone.setVisible(true);
                clone.setX(dragEvent.getSceneX() - width / 2); // Center the clone around the mouse
                clone.setY(dragEvent.getSceneY() - height / 2);
            });
            clone.setOnMouseExited(exitEvent -> {
                clone.setVisible(true);
                clone.setX(exitEvent.getSceneX() - width / 2); // Center the clone around the mouse
                clone.setY(exitEvent.getSceneY() - height / 2);
            });
            clone.setOnMouseClicked(e -> {
                boolean snappedToTile = false;
    
                // Get the scene coordinates where the object was released
                double sceneX = e.getSceneX();
                double sceneY = e.getSceneY();

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
                                flagTile.changeTileType('!');
                            }
                            }
    
                            // Snap the clone to the target tile
                            Rectangle targetRect = new Rectangle(width, height);
                            targetRect.setFill(new ImagePattern(image));
                            // root.getChildren().add(targetRect);
                            targetRect.setX(targetTile.getLeftSide());
                            targetRect.setY(targetTile.getTopSide()-adjustmentForBigObjects);    
                            hall.getChildren().add(targetRect);
    
                            snappedToTile = true;

                            soundPlayer.playSoundEffectInThread("putting");
                            break;
                        }
                        showBottomWalls();
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
    
    
    //Hides the rune in one of the objects
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

    public HashMap<TiledHall, List<Tile>> getTileMap() {
        return tileMap;
    }

    public void addTiledHall(TiledHall hall, HallType type) {
        tileMap.put(hall, new ArrayList<>());
        hallTypeMap.put(hall, type); // Tür eşlemesi
    }
    public boolean repOk() {
        // Check for null references in tileMap and hallTypeMap
        if (tileMap == null || hallTypeMap == null) {
            return false;
        }

        // Check that each TiledHall in tileMap exists in hallTypeMap
        for (TiledHall hall : tileMap.keySet()) {
            if (!hallTypeMap.containsKey(hall) || hall.getGrid() == null) {
                return false;
            }
        }

        // Check for unique object placement within each TiledHall
        for (TiledHall hall : tileMap.keySet()) {
            List<Tile> placedTiles = tileMap.get(hall);
            HashSet<Tile> uniqueTiles = new HashSet<>(placedTiles);
            if (uniqueTiles.size() != placedTiles.size()) {
                return false; // Duplicate objects in the same tile
            }

            // Ensure all objects are within the hall's grid boundaries
            for (Tile tile : placedTiles) {
                if (!hall.getGrid().coordinatesAreInGrid(tile.getLeftSide(), tile.getTopSide())) {
                    return false; // Object placed outside the hall
                }
            }
        }

        // All checks passed
        return true;
    }


	public void showTopWalls() {
		ImagePattern earthHallImage = new ImagePattern(Images.IMAGE_EARTHHALL_TOP_X4);
		ImagePattern airHallImage = new ImagePattern(Images.IMAGE_AIRHALL_TOP_X4);
		ImagePattern waterHallImage = new ImagePattern(Images.IMAGE_WATERHALL_TOP_X4);
		ImagePattern fireHallImage = new ImagePattern(Images.IMAGE_FIREHALL_TOP_X4);

		Rectangle earthWalls = new Rectangle(151, 12, 340, 200);
		Rectangle airWalls = new Rectangle(551, 12, 340, 200);
		Rectangle waterWalls = new Rectangle(151, 410, 340, 200);
		Rectangle fireWalls = new Rectangle(551, 410, 340, 200);

		earthWalls.setFill(earthHallImage);
		airWalls.setFill(airHallImage);
		waterWalls.setFill(waterHallImage);
		fireWalls.setFill(fireHallImage);

		pane.getChildren().add(earthWalls);
		pane.getChildren().add(airWalls);
		pane.getChildren().add(waterWalls);
		pane.getChildren().add(fireWalls);
	}

    public void showBottomWalls() {
		ImagePattern earthHallImage = new ImagePattern(Images.IMAGE_EARTHHALL_CLOSEDOOR_BOTTOM_X4);
		ImagePattern airHallImage = new ImagePattern(Images.IMAGE_AIRHALL_CLOSEDOOR_BOTTOM_X4);
		ImagePattern waterHallImage = new ImagePattern(Images.IMAGE_WATERHALL_CLOSEDOOR_BOTTOM_X4);
		ImagePattern fireHallImage = new ImagePattern(Images.IMAGE_FIREHALL_CLOSEDOOR_BOTTOM_X4);

		Rectangle earthWalls = new Rectangle(151, 212, 340, 200);
		Rectangle airWalls = new Rectangle(551, 212, 340, 200);
		Rectangle waterWalls = new Rectangle(151, 610, 340, 200);
		Rectangle fireWalls = new Rectangle(551, 610, 340, 200);

		earthWalls.setFill(earthHallImage);
		airWalls.setFill(airHallImage);
		waterWalls.setFill(waterHallImage);
		fireWalls.setFill(fireHallImage);

		pane.getChildren().add(earthWalls);
		pane.getChildren().add(airWalls);
		pane.getChildren().add(waterWalls);
		pane.getChildren().add(fireWalls);
	}
	
    
    public static void main(String[] args) {
        launch(args);
    }
    


}
