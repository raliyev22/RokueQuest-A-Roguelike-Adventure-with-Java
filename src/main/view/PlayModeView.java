package main.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javafx.application.Platform;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import main.Main;
import main.controller.MonsterManager;
import main.controller.PlayModeController;
import main.model.Enchantment;
import main.model.Images;
import main.model.Inventory;
import main.model.Monster;
import main.utils.Grid;
import main.utils.SoundEffects;
import main.utils.Tile;


public class PlayModeView {
	protected InventoryView inventoryView; // Add InventoryView
	private VBox uiContainer; // UI Containe
	protected Pane pane;
	protected Scene scene;
	protected int tileSize = 64;
	protected Grid grid;
	protected Rectangle heroView;
	protected double time;
  	protected List<Rectangle> monsterViews;
	protected Label timeLabel;
	private HBox heartsContainer;
	private Stage primaryStage;
	private StackPane pauseOverlay;
	public Button pauseButton;
	public Button resumeButton;
	private HBox buttonContainer;
	SoundEffects soundPlayer = SoundEffects.getInstance();
	private PlayModeController playModeController;
	private HashMap<Enchantment.Type, Label> bagLabels;
	private HashMap<Enchantment, Rectangle> enchantmentViews;

	protected final Image tileImage = Images.IMAGE_TILE_x4;

	public PlayModeView(Grid grid, double time, Stage primaryStage) {
		this.grid = grid;
		this.time = time;
		this.pane = new Pane();
		heroView = new Rectangle(64,64);
		this.primaryStage = primaryStage;
		this.playModeController = new PlayModeController();
		bagLabels = new HashMap<>();
		initializeBagUI();
		initialize();
	}
	private void initializeBagUI() {
		int offsetX = 20;
		int offsetY = 20;

		for (Enchantment.Type type : Enchantment.Type.values()) {
			Label label = new Label(type.name() + ": 0");
			label.setLayoutX(offsetX);
			label.setLayoutY(offsetY);
			offsetY += 30;

			bagLabels.put(type, label);
			pane.getChildren().add(label);
		}
	}

	public void updateBagUI(HashMap<Enchantment.Type, Integer> enchantments) {
		for (Enchantment.Type type : bagLabels.keySet()) {
			int count = enchantments.getOrDefault(type, 0);
			bagLabels.get(type).setText(type.name() + ": " + count);
		}
	}



	public void removeEnchantment(Enchantment enchantment) {
		pane.getChildren().removeIf(node -> node.getId() != null &&
				node.getId().equals("enchantment-" + enchantment.getType()));
	}
	public HashMap<Enchantment, Rectangle> getEnchantmentViews() {
		return enchantmentViews;
	}



	public void highlightTile(Tile tile, boolean highlight) {
		Rectangle rect = new Rectangle(40, 40);
		rect.setLayoutX(tile.getLeftSide());
		rect.setLayoutY(tile.getTopSide());
		rect.setFill(highlight ? Color.LIGHTBLUE : Color.TRANSPARENT);
		rect.setStroke(highlight ? Color.BLUE : null);
		rect.setId("highlight-" + tile.hashCode());

		if (highlight) {
			pane.getChildren().add(rect);
		} else {
			pane.getChildren().removeIf(node -> node.getId() != null && node.getId().equals("highlight-" + tile.hashCode()));
		}
	}


	public double getTimeRemaining() {
		// Return the current time remaining
		return time;
	}

	public int getRuneXCoordinate() {
		// Return the X coordinate of the rune
		return 0;
	}

	public int getRuneYCoordinate() {
		// Return the Y coordinate of the rune
		return 0;
	}
	public void refresh(Grid newGrid, double time) {
		this.grid = newGrid;
		this.time = time;
		pane.getChildren().clear();
		initialize();
	}


	public void updateInventoryView(Inventory inventory) {
		inventoryView.updateInventory(inventory); // Delegate to InventoryView
	}
	public void initialize() {
		if (scene == null) {
			scene = new Scene(pane);
		}

        monsterViews = new ArrayList<>();

        soundPlayer.addSoundEffect("blueButtons", "src/main/sounds/blueButtons.wav");
		enchantmentViews = new HashMap<Enchantment, Rectangle>();
		uiContainer = new VBox(10); // Create a vertical box for UI
		inventoryView = new InventoryView(); // Initialize InventoryView
		uiContainer.getChildren().add(inventoryView.getInventoryBox()); // Add InventoryView to UI

		pane.getChildren().add(uiContainer);
		pane.setBackground(new Background(new BackgroundImage(
			tileImage,
			BackgroundRepeat.REPEAT,
			BackgroundRepeat.REPEAT,
			BackgroundPosition.DEFAULT,
			BackgroundSize.DEFAULT
		)));

        showWalls(grid);
        heroView.setFill(new ImagePattern(Images.IMAGE_PLAYERRIGHT_x4));
        pane.getChildren().add(heroView);
        showGrid(grid);
        
        VBox uiContainer = new VBox(10);
        uiContainer.setStyle("-fx-background-color:#6f5459; -fx-padding: 10;");
        uiContainer.setLayoutX(800);
        uiContainer.setLayoutY(70);
        uiContainer.setPrefWidth(200);
        uiContainer.setPrefHeight(736);

		buttonContainer = new HBox(10);
		buttonContainer.setStyle("-fx-alignment: center;");

		Button exitButton = new Button();
		exitButton.setStyle("-fx-background-color: transparent;");

		exitButton.setOnAction(e -> {
            soundPlayer.playSoundEffectInThread("blueButtons");
        });      

		exitButton.setOnMouseEntered(event -> {
            exitButton.setCursor(Cursor.HAND);
        });
        
        exitButton.setOnMouseExited(event -> {
            exitButton.setCursor(Cursor.DEFAULT);
        });

		ImageView exitImageView = new javafx.scene.image.ImageView(Images.IMAGE_EXITBUTTON_x4);
		exitImageView.setFitWidth(40);
		exitImageView.setFitHeight(40);

		exitButton.setGraphic(exitImageView);
		exitButton.setPrefWidth(40);
		exitButton.setPrefHeight(40);

		pauseButton = new Button();
		pauseButton.setStyle("-fx-background-color: transparent;"); 

		pauseButton.setOnMouseEntered(event -> {
            pauseButton.setCursor(Cursor.HAND);
        });
        
        pauseButton.setOnMouseExited(event -> {
            pauseButton.setCursor(Cursor.DEFAULT);
        });

		ImageView pauseImageView = new javafx.scene.image.ImageView(Images.IMAGE_PAUSEBUTTON_x4);
		pauseImageView.setFitWidth(40);
		pauseImageView.setFitHeight(40);

		pauseButton.setGraphic(pauseImageView);
		pauseButton.setPrefWidth(40);
		pauseButton.setPrefHeight(40);

		buttonContainer.getChildren().addAll(exitButton, pauseButton);
         
    HBox timeLabelContainer = new HBox(); // Container for timeLabel
		timeLabelContainer.setStyle("-fx-alignment: center;");
    timeLabel = new Label("Time: " + time);
    timeLabel.setStyle("-fx-font-size: 32px; -fx-text-fill: white; -fx-background-color: transparent;");
    timeLabelContainer.getChildren().add(timeLabel); // Add the label to the container

    heartsContainer = new HBox(5); // Kalpler arasındaki boşluk 5 px
		heartsContainer.setStyle("-fx-alignment: center;");
    heartsContainer.setTranslateY(80);
    Rectangle heart1,heart2,heart3,heart4;
    heart1 = new Rectangle(32,32);
    heart2 = new Rectangle(32,32);
    heart3 = new Rectangle(32,32);
    heart4 = new Rectangle(32,32);
    heart1.setFill(new ImagePattern(Images.IMAGE_HEART_x4));
    heart2.setFill(new ImagePattern(Images.IMAGE_HEART_x4));
    heart3.setFill(new ImagePattern(Images.IMAGE_HEART_x4));
    heart4.setFill(new ImagePattern(Images.IMAGE_HEART_x4));
    heartsContainer.getChildren().addAll(heart1,heart2,heart3,heart4);

    Rectangle inventory = new Rectangle(200,400);
    inventory.setFill(new ImagePattern(Images.IMAGE_INVENTORY));
    inventory.setTranslateY(100);


    uiContainer.getChildren().addAll(buttonContainer,timeLabelContainer,heartsContainer,inventory);
    pane.getChildren().add(uiContainer);

		initializePauseOverlay();
	}

    private void initializePauseOverlay() {

        // Create a "Paused" text
        Label pauseText = new Label("Game Paused");
        pauseText.setFont(Font.font(36));
        pauseText.setTextFill(Color.WHITE);

        VBox overlayContent = new VBox(20, pauseText);
		overlayContent.setStyle("-fx-alignment: center;");
	  	  overlayContent.setTranslateY(400);
	    	overlayContent.setTranslateX(300);

        // Add elements to a stack pane
        pauseOverlay = new StackPane(overlayContent);
        pauseOverlay.setVisible(false);
        pane.getChildren().add(pauseOverlay);
    }
	public void removeMonsterView(Monster monster) {
		pane.getChildren().removeIf(node ->
				node.getId() != null && node.getId().equals("monster-" + monster.hashCode()));
	}
	public void showPauseGame() {
        pauseOverlay.setVisible(true);
		// Setup for save button
		ImageView saveImageView = new ImageView(Images.IMAGE_SAVEBUTTON_x4);
		saveImageView.setFitHeight(40);
		saveImageView.setFitWidth(40);

		Button saveButton = new Button();
		saveButton.setStyle("-fx-background-color: transparent;");

		saveButton.setOnAction(e -> {
            soundPlayer.playSoundEffectInThread("blueButtons");
			saveGame();
        });

		saveButton.setOnMouseEntered(event -> {
            saveButton.setCursor(Cursor.HAND);
        });
        
        saveButton.setOnMouseExited(event -> {
            saveButton.setCursor(Cursor.DEFAULT);
        });

		saveButton.setGraphic(saveImageView);
		saveButton.setPrefWidth(40);
		saveButton.setPrefHeight(40);
		//Added to first index of button container
		buttonContainer.getChildren().add(1, saveButton);
		//Changing pause button's image to play button image
		ImageView resumeImageView = new ImageView(Images.IMAGE_PLAYBUTTON_x4);
		resumeImageView.setFitHeight(40);
		resumeImageView.setFitWidth(40);

		pauseButton.setGraphic(resumeImageView);
		pauseButton.setPrefWidth(40);
		pauseButton.setPrefHeight(40);
    }

	public void addEnchantmentView(Enchantment enchantment, double x, double y) {
		Rectangle enchantmentView = new Rectangle(tileSize, tileSize);
		enchantmentView.setFill(new ImagePattern(enchantment.getImage()));
		enchantmentView.setX(x);
		enchantmentView.setY(y);
		enchantmentView.setId("enchantment-" + enchantment.hashCode());

		enchantmentViews.put(enchantment, enchantmentView);
		pane.getChildren().add(enchantmentView);
	}

	/**
	 * Removes an enchantment from the screen.
	 *
	 * @param enchantment The enchantment to remove.
	 */
	public void removeEnchantmentView(Enchantment enchantment) {
		Rectangle enchantmentView = enchantmentViews.remove(enchantment);
		if (enchantmentView != null) {
			pane.getChildren().remove(enchantmentView);
		}
	}

	/**
	 * Updates the position of an enchantment on the screen.
	 *
	 * @param enchantment The enchantment to update.
	 * @param x The new x-coordinate of the enchantment.
	 * @param y The new y-coordinate of the enchantment.
	 */
	public void updateEnchantmentPosition(Enchantment enchantment, double x, double y) {
		Rectangle enchantmentView = enchantmentViews.get(enchantment);
		if (enchantmentView != null) {
			enchantmentView.setX(x);
			enchantmentView.setY(y);
		}
	}

	public void hidePauseGame() {
        pauseOverlay.setVisible(false);
		//Removing save button from button container
		buttonContainer.getChildren().remove(1);
		//Changing pause button's image to pause button image
		ImageView pauseImageView = new ImageView(Images.IMAGE_PAUSEBUTTON_x4);
		pauseImageView.setFitWidth(40);
		pauseImageView.setFitHeight(40);
		
		pauseButton.setGraphic(pauseImageView);
		pauseButton.setPrefWidth(40);
		pauseButton.setPrefHeight(40);
    }

	public void saveGame() {
		System.out.println("Game Saved!");
	}
	
	
	private void showWalls(Grid grid) {
		int wallX = grid.topLeftXCoordinate - 20;
		int wallY = grid.topLeftYCoordinate - 80;

		int wallLengthX = 680;
		int wallLengthY = 736;
		
		Rectangle walls = new Rectangle(wallX, wallY, wallLengthX, wallLengthY);
		walls.setFill(new ImagePattern(Images.IMAGE_WALLS_X4));
		pane.getChildren().add(walls);
	}
	
	public void showGrid(Grid grid) {
		List<Tile> tiles = grid.getTileMap();
		for (Tile tile : tiles) {
			char tileType = tile.getTileType();

			if (!PlayModeController.isHallObjectTileType(tileType)) {
				continue;
			}

			char lowerCaseLetter = Character.toLowerCase(tileType);
			Image image = Images.convertCharToImage(lowerCaseLetter);
			
			if (image != null) {
				if (tileType == 'P') {
					drawTallItem(tile, image);
				} else if (tileType == 'D') {
					drawTallItem(tile, image);
				} else if (tileType == 'E') {
					continue;
				} else {
					drawNormalItem(tile, image);
				}
			}
		}
	}

    public void redrawTallItems() {
        List<Tile> tiles = grid.getTileMap();
        for (Tile tile : tiles) {
            char tileType = tile.getTileType();
            
            if (!PlayModeController.isHallObjectTileType(tileType)) {
                continue;
            }
            
            char lowerCaseLetter = Character.toLowerCase(tileType);
            Image image = Images.convertCharToImage(lowerCaseLetter);
            
            if (image != null) {
                if (tileType == 'P') {
                    drawTallItem(tile, image);
                } else if (tileType == 'D') {
                    drawTallItem(tile, image);
                }
            }
        }
    }



    public Rectangle createMonsterView(Monster monster) {
        Rectangle monsterView = new Rectangle(monster.currentX, monster.currentY, tileSize, tileSize);

		monsterView.setFill(new ImagePattern(MonsterManager.getMonsterImage(monster)));
		pane.getChildren().add(monsterView);
        
        monsterViews.add(monsterView);
        updateMonsterPosition(monsterView, monster.currentX, monster.currentY);
        
        return monsterView;
    }

	public void changeHeroSprite(Image img) {
		ImagePattern patt = new ImagePattern(img);
		heroView.setFill(patt);
	}
	
	private void drawNormalItem(Tile tile, Image image) {
		Rectangle normalItem = new Rectangle(tile.getLeftSide(), tile.getTopSide(), tileSize, tileSize);
		if (image != null) {
            ImagePattern patt = new ImagePattern(image);
			normalItem.setFill(patt);
		} else {
			normalItem.setFill(Color.GRAY);
		}
		pane.getChildren().add(normalItem);
	}
	
	private void drawTallItem(Tile tile, Image image) {
		Rectangle tallItem = 
		new Rectangle(tile.getLeftSide(), tile.getTopSide() - tileSize, tileSize, tileSize * 2);
		tallItem.setFill(new ImagePattern(image));
		pane.getChildren().add(tallItem);
	}
	
	public void updateHeroPosition(double x, double y) {
		heroView.setX(x);
		heroView.setY(y);
	}

    public void updateMonsterPosition(int monsterID, double x, double y) {
        Rectangle monsterView = monsterViews.get(monsterID);
        
        monsterView.setX(x);
        monsterView.setY(y);
    }

	public void updateMonsterPosition(Rectangle monsterView,double x, double y) {
		monsterView.setX(x);
		monsterView.setY(y);
		//System.out.println("monster moved");
	}

	public void addToPane(Rectangle monsterView){
		pane.getChildren().add(monsterView);
	}

	public void removeFromPane(Rectangle monsterView){
		pane.getChildren().remove(monsterView);
	}
	
	public Pane getPane() {
		return pane;
	}
	
	public Scene getScene() {
		return scene;
	}

	public double updateTime(double time){
		this.time = time;
		timeLabel.setText("Time: " + (int)time);
		return time;
	}

	public void updateHeroLife(int life) {
		// Example: Update hearts container based on remaining life
		heartsContainer.getChildren().clear();
		for (int i = 0; i < life; i++) {
			Rectangle heart = new Rectangle(32, 32);
			heart.setFill(new ImagePattern(Images.IMAGE_HEART_x4));
			heartsContainer.getChildren().add(heart);
		}
	}


	public void showGameOverPopup(boolean isWin) {
		Platform.runLater(() -> {
			Alert alert = new Alert(Alert.AlertType.NONE);
			alert.setTitle("Game Over");
	
			Label headerLabel = new Label(isWin ? "Congratulations!" : "Try Again");
			headerLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: white; -fx-font-weight: bold;");
			headerLabel.setStyle("-fx-alignment: center;");
			headerLabel.setMaxWidth(Double.MAX_VALUE);
	
			Label contentLabel = new Label(isWin 
				? "You have successfully completed the game!" 
				: "You failed to complete the game.");
			contentLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: white;");
	
			VBox contentBox = new VBox(10, headerLabel, contentLabel);
			contentBox.setStyle("-fx-alignment: center;");
			contentBox.setStyle("-fx-background-color: transparent; -fx-padding: 10;");
	
			// Butonları elle oluştur ve ortala
			Button mainMenuButton = new Button("Return to Menu");
			Button exitButton = new Button("Exit the Game");
			mainMenuButton.setPrefWidth(150);
			exitButton.setPrefWidth(150);
	
			mainMenuButton.setOnAction(e -> {
				
				Main mainPage = new Main();
				primaryStage.close();
				mainPage.start(primaryStage);
				alert.close();
			});
	
			exitButton.setOnAction(e -> {
				System.exit(0);
			});
	
			HBox buttonBox = new HBox(20, mainMenuButton, exitButton);
			buttonBox.setStyle("-fx-alignment: center;");
	
			VBox dialogContent = new VBox(20, contentBox, buttonBox);
			dialogContent.setStyle("-fx-alignment: center;");
			dialogContent.setStyle("-fx-background-color: #222; -fx-padding: 20;");
	
			DialogPane dialogPane = alert.getDialogPane();
			dialogPane.setContent(dialogContent);
			dialogPane.setStyle("-fx-background-color: #222;");
	
			Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
			alertStage.initStyle(javafx.stage.StageStyle.UNDECORATED);
			alertStage.setResizable(false);
			alertStage.setAlwaysOnTop(true);
	
			// Arkadaki ekranı kilitlemek için initOwner ile ana pencereyi belirle
			alertStage.initOwner(primaryStage); // Ana sahneyi sahip olarak belirle
			alertStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
	
			alert.showAndWait();
		});
	}
}