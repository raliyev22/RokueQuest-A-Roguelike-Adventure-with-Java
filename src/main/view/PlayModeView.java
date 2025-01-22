package main.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
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
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.Main;
import main.controller.MonsterManager;
import main.controller.PlayModeController;
import main.model.*;
import main.utils.Grid;
import main.utils.SoundEffects;
import main.utils.Tile;

public class PlayModeView {
	protected Pane pane;
	protected Scene scene;
	protected int tileSize = 64;
	protected Grid grid;
	private HallType hallType;
	protected Rectangle heroView;
	protected double time;
  	protected List<Rectangle> monsterViews;
	protected Label timeLabel;
	private HBox heartsContainer;
	private Stage primaryStage;
	public Inventory inventory;
	public InventoryView inventoryView;
	public Rectangle topWalls;
	public Rectangle bottomWalls;
	private StackPane pauseOverlay;
	private StackPane exitOverlay;
	public Button pauseButton;
	private HBox buttonContainer;
	public Button sureExitButton;
	public Button exitButton;
	public Button cancelExitButton;
	public Button saveButton;
	SoundEffects soundPlayer = SoundEffects.getInstance();
	private Timeline countdown;
	private HashMap<Enchantment.Type, Label> bagLabels;
	private HashMap<Enchantment, Rectangle> enchantmentViews;

	protected final Image tileImage = Images.IMAGE_TILE_x4;

	public PlayModeView(Grid grid, double time, Stage primaryStage, HallType hallType) {
		this.grid = grid;
		this.time = time;
		this.hallType = hallType;
		this.pane = new Pane();
		this.primaryStage = primaryStage;

		// Reuse or create InventoryView
		if (inventoryView == null) {
			inventoryView = new InventoryView();
		}

		heroView = new Rectangle(64, 64);
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
	public void highlightTile(Tile tile, boolean highlight) {
		Rectangle rect = new Rectangle(40, 40);
		rect.setLayoutX(tile.getLeftSide());
		rect.setLayoutY(tile.getTopSide());
		rect.setFill(highlight ? Color.LIGHTBLUE : Color.TRANSPARENT);
		rect.setStroke(highlight ? Color.BLUE : null);
		rect.setId("highlight-" + tile.hashCode());

		if (highlight) {
			Platform.runLater(() -> pane.getChildren().add(rect)); // Ensure UI modification on the JavaFX thread
		} else {
			Platform.runLater(() -> pane.getChildren().removeIf(node ->
					node.getId() != null && node.getId().equals("highlight-" + tile.hashCode())
			)); // Remove on the JavaFX thread
		}
	}
	public void removeEnchantment(Enchantment enchantment) {
		pane.getChildren().removeIf(node -> node.getId() != null &&
				node.getId().equals("enchantment-" + enchantment.getType()));
	}
	public HashMap<Enchantment, Rectangle> getEnchantmentViews() {
		return enchantmentViews;
	}
	public void updateBagUI(HashMap<Enchantment.Type, Integer> enchantments) {
		for (Enchantment.Type type : bagLabels.keySet()) {
			int count = enchantments.getOrDefault(type, 0);
			bagLabels.get(type).setText(type.name() + ": " + count);
		}
	}
	public void refresh(Grid newGrid, double time, HallType hallType) {
		this.grid = newGrid;
		this.time = time;
		this.hallType = hallType;
		pane.getChildren().clear();
		initialize();
	}
	

	public void initialize() {
		Rectangle2D screenBounds = Screen.getPrimary().getBounds();

		if (scene == null) {
			scene = new Scene(pane, screenBounds.getWidth(), screenBounds.getHeight());
		}

        monsterViews = new ArrayList<>();
		enchantmentViews = new HashMap<Enchantment, Rectangle>();
		pane.setBackground(new Background(new BackgroundImage(
			tileImage,
			BackgroundRepeat.REPEAT,
			BackgroundRepeat.REPEAT,
			BackgroundPosition.DEFAULT,
			BackgroundSize.DEFAULT
		)));

        showTopWalls(grid);
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
		buttonContainer.setAlignment(javafx.geometry.Pos.CENTER);

		saveButton = new Button();

		exitButton = new Button();
		exitButton.setStyle("-fx-background-color: transparent;");     

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
		timeLabelContainer.setAlignment(javafx.geometry.Pos.CENTER); // Center align horizontally
		timeLabel = new Label("Time: " + (int)time);
		timeLabel.setStyle("-fx-font-size: 32px; -fx-text-fill: white; -fx-background-color: transparent;");
		timeLabelContainer.getChildren().add(timeLabel); // Add the label to the container

		heartsContainer = new HBox(5); // Kalpler arasındaki boşluk 5 px
		heartsContainer.setAlignment(javafx.geometry.Pos.CENTER); // Kalpleri ortala
		heartsContainer.setTranslateY(80);
		Rectangle heart1,heart2,heart3,heart4;
		heart1 = new Rectangle(32,32);
		heart2 = new Rectangle(32,32);
		heart3 = new Rectangle(32,32);
		heart1.setFill(new ImagePattern(Images.IMAGE_HEART_x4));
		heart2.setFill(new ImagePattern(Images.IMAGE_HEART_x4));
		heart3.setFill(new ImagePattern(Images.IMAGE_HEART_x4));
		heartsContainer.getChildren().addAll(heart1,heart2,heart3);

		inventoryView = new InventoryView();
		inventoryView.setTranslateY(100);
		uiContainer.getChildren().addAll(buttonContainer, timeLabelContainer, heartsContainer, inventoryView);

		pane.getChildren().add(uiContainer);

		initializePauseOverlay();
		initializeExitOverlay();
		showBottomWalls(grid, true);
	}
	public void addEnchantmentView(Enchantment enchantment, double x, double y) {
		// Skip rendering for EXTRA_TIME_ENCHANTMENT
		// if (enchantment.getType() == Enchantment.Type.EXTRA_TIME) {
		// 	enchantmentViews.put(enchantment, null); // Logical presence without rendering
		// 	return;
		// }

		Rectangle enchantmentView = new Rectangle(tileSize, tileSize);
		enchantmentView.setFill(new ImagePattern(enchantment.getImage()));
		enchantmentView.setX(x);
		enchantmentView.setY(y);
		enchantmentView.setId("enchantment-" + enchantment.hashCode());

		enchantmentViews.put(enchantment, enchantmentView);
		pane.getChildren().add(enchantmentView);
	}

	public void collectEnchantment(Enchantment enchantment, Inventory inventory) {
		if (!enchantmentViews.containsKey(enchantment)) {
			System.out.println("Attempted to collect a non-existent enchantment.");
			return; // Avoid duplicate processing
		}

		Platform.runLater(() -> {
			inventory.addEnchantment(enchantment.getType()); // Add the enchantment (only once)
			updateInventoryUI(inventory.getEnchantments()); // Update the inventory UI

			// Remove the enchantment view if it was rendered
			Rectangle enchantmentView = enchantmentViews.get(enchantment);
			if (enchantmentView != null) {
				pane.getChildren().remove(enchantmentView);
			}

			synchronized (enchantmentViews) {
				enchantmentViews.remove(enchantment); // Remove from the internal tracking
			}
		});
	}
	public void updateInventoryUI(List<Enchantment.Type> enchantments) {
		if (inventoryView != null) {
			inventoryView.updateInventory(enchantments);
		}
	}

	public void removeEnchantmentView(Enchantment enchantment) {
		Rectangle enchantmentView = enchantmentViews.remove(enchantment);
		if (enchantmentView != null) {
			pane.getChildren().remove(enchantmentView);
		}
	}


	public void updateEnchantmentPosition(Enchantment enchantment, double x, double y) {
		Rectangle enchantmentView = enchantmentViews.get(enchantment);
		if (enchantmentView != null) {
			enchantmentView.setX(x);
			enchantmentView.setY(y);
		}
	}
	public void highlightArea(int x, int y, int width, int height, boolean highlight) {
		String highlightId = "highlight-" + x + "-" + y;

		if (highlight) {
			Rectangle highlightRectangle = new Rectangle(x, y, width, height);
			highlightRectangle.setStroke(Color.GOLD); // Border color
			highlightRectangle.setFill(Color.TRANSPARENT); // Transparent fill
			highlightRectangle.setId(highlightId);

			// Add the rectangle to the pane if it doesn't already exist
			Platform.runLater(() -> {
				pane.getChildren().removeIf(node -> highlightId.equals(node.getId()));
				pane.getChildren().add(highlightRectangle);
			});
		} else {
			// Remove the rectangle by ID
			Platform.runLater(() -> pane.getChildren().removeIf(node -> highlightId.equals(node.getId())));
		}
	}

    private void initializePauseOverlay() {

        // Create a "Paused" text
        Label pauseText = new Label("Game Paused");
        pauseText.setFont(Font.font(36));
        pauseText.setTextFill(Color.WHITE);

        VBox overlayContent = new VBox(20, pauseText);
		overlayContent.setAlignment(javafx.geometry.Pos.CENTER);
		overlayContent.setTranslateY(400);
		overlayContent.setTranslateX(300);

        // Add elements to a stack pane
        pauseOverlay = new StackPane(overlayContent);
        pauseOverlay.setVisible(false);
        pane.getChildren().add(pauseOverlay);
    }

	public void initializeExitOverlay() {
		Rectangle background = new Rectangle();
        background.setFill(Color.rgb(0, 0, 0, 0.5));
        background.widthProperty().bind(pane.widthProperty());
        background.heightProperty().bind(pane.heightProperty());

        // Create a "Exit Game" text
        Label exitText = new Label("Exit Game?");
        exitText.setFont(Font.font(36));
        exitText.setTextFill(Color.WHITE);
        
		
		sureExitButton = new Button("Exit Game");

		sureExitButton.setStyle("-fx-font-size: 16px; -fx-padding: 10px;");
		
        sureExitButton.setOnMouseEntered(event -> {
			sureExitButton.setCursor(Cursor.HAND);
        });
		
        sureExitButton.setOnMouseExited(event -> {
			sureExitButton.setCursor(Cursor.DEFAULT);
        });
		
		
        cancelExitButton = new Button("Cancel");
		cancelExitButton.setStyle("-fx-font-size: 16px; -fx-padding: 10px;");
		
        cancelExitButton.setOnMouseEntered(event -> {
			cancelExitButton.setCursor(Cursor.HAND);
        });
		
        cancelExitButton.setOnMouseExited(event -> {
			cancelExitButton.setCursor(Cursor.DEFAULT);
        });
		
		VBox buttonContainer = new VBox(10, sureExitButton, cancelExitButton);
        buttonContainer.setAlignment(javafx.geometry.Pos.CENTER);


		VBox overlayContent = new VBox(20, exitText, buttonContainer);
		overlayContent.setAlignment(javafx.geometry.Pos.CENTER);


        // Add elements to a stack pane
        exitOverlay = new StackPane(background, overlayContent);
        exitOverlay.setVisible(false);
        pane.getChildren().add(exitOverlay);
    
	}
	
	public void showExitGame(){
		exitOverlay.setVisible(true);
	}

	public void hideExitGame(){
        exitOverlay.setVisible(false);
    }

	public void showPauseGame() {
        pauseOverlay.setVisible(true);
		pauseOverlay.toFront();

		// Setup for save button
		ImageView saveImageView = new ImageView(Images.IMAGE_SAVEBUTTON_x4);
		saveImageView.setFitHeight(40);
		saveImageView.setFitWidth(40);

		saveButton.setStyle("-fx-background-color: transparent;");

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

	public void showTopWalls(Grid grid) {
		int wallX = grid.topLeftXCoordinate - 20;
		int wallY = grid.topLeftYCoordinate - 109;

		int wallLengthX = 680;
		int wallLengthY = 399;

		ImagePattern wallImage = getTopWallImage(hallType);

		if (topWalls != null) {
			pane.getChildren().remove(topWalls);
		}

		topWalls = new Rectangle(wallX, wallY, wallLengthX, wallLengthY);
		topWalls.setFill(wallImage);
		pane.getChildren().add(topWalls);
	}
	
	public void showBottomWalls(Grid grid, Boolean doorClosed) {
		int wallX = grid.topLeftXCoordinate - 20;
		int wallY = grid.topLeftYCoordinate + 290;

		int wallLengthX = 680;
		int wallLengthY = 400;

		ImagePattern wallImage = getBottomWallImage(hallType, doorClosed);

		if (bottomWalls != null) {
			pane.getChildren().remove(bottomWalls);
		}

		bottomWalls = new Rectangle(wallX, wallY, wallLengthX, wallLengthY);
		bottomWalls.setFill(wallImage);
		bottomWalls.toFront();
		pane.getChildren().add(bottomWalls);
	}
	
	private ImagePattern getTopWallImage(HallType hallType) {
		return switch (hallType) {
			case EARTH -> new ImagePattern(Images.IMAGE_EARTHHALL_TOP_X4);
			case AIR -> new ImagePattern(Images.IMAGE_AIRHALL_TOP_X4);
			case WATER -> new ImagePattern(Images.IMAGE_WATERHALL_TOP_X4);
			case FIRE -> new ImagePattern(Images.IMAGE_FIREHALL_TOP_X4);
			default -> null;
		};
	}

	private ImagePattern getBottomWallImage(HallType hallType, boolean doorClosed) {
		return switch (hallType) {
			case EARTH -> doorClosed ? 
				new ImagePattern(Images.IMAGE_EARTHHALL_CLOSEDOOR_BOTTOM_X4) : 
				new ImagePattern(Images.IMAGE_EARTHHALL_OPENDOOR_BOTTOM_X4);
			case AIR -> doorClosed ? 
				new ImagePattern(Images.IMAGE_AIRHALL_CLOSEDOOR_BOTTOM_X4) : 
				new ImagePattern(Images.IMAGE_AIRHALL_OPENDOOR_BOTTOM_X4);
			case WATER -> doorClosed ? 
				new ImagePattern(Images.IMAGE_WATERHALL_CLOSEDOOR_BOTTOM_X4) : 
				new ImagePattern(Images.IMAGE_WATERHALL_OPENDOOR_BOTTOM_X4);
			case FIRE -> doorClosed ? 
				new ImagePattern(Images.IMAGE_FIREHALL_CLOSEDOOR_BOTTOM_X4) : 
				new ImagePattern(Images.IMAGE_FIREHALL_OPENDOOR_BOTTOM_X4);
			default -> null;
		};
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

		monster.monsterView = monsterView;
        
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
		monsterViews.remove(monsterView);
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
			headerLabel.setAlignment(javafx.geometry.Pos.CENTER);
			headerLabel.setMaxWidth(Double.MAX_VALUE);
	
			Label contentLabel = new Label(isWin 
				? "You have successfully completed the game!" 
				: "You failed to complete the game.");
			contentLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: white;");
	
			VBox contentBox = new VBox(10, headerLabel, contentLabel);
			contentBox.setAlignment(javafx.geometry.Pos.CENTER);
			contentBox.setStyle("-fx-background-color: transparent; -fx-padding: 10;");
	
			// Butonları elle oluştur ve ortala
			Button mainMenuButton = new Button("Return to Menu");
			Button exitButton = new Button("Exit the Game");
			mainMenuButton.setPrefWidth(150);
			exitButton.setPrefWidth(150);
			
			mainMenuButton.setOnAction(e -> {
				soundPlayer.pauseSoundEffect("gameWinner");
				soundPlayer.pauseSoundEffect("gameLoser");
				soundPlayer.playSoundEffect("menuButtons");
				Main mainPage = new Main();
				primaryStage.close();
				mainPage.start(primaryStage);
				alert.close();
			});
			
			exitButton.setOnAction(e -> {
				soundPlayer.playSoundEffect("menuButtons");
				System.exit(0);
			});
	
			HBox buttonBox = new HBox(20, mainMenuButton, exitButton);
			buttonBox.setAlignment(javafx.geometry.Pos.CENTER);
	
			VBox dialogContent = new VBox(20, contentBox, buttonBox);
			dialogContent.setAlignment(javafx.geometry.Pos.CENTER);
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

	public void showCountdownAndStart(Runnable onComplete) {
		Label countdownLabel = new Label("3");
		countdownLabel.setStyle("-fx-font-size: 72px; -fx-text-fill: white; -fx-font-weight: bold;");
		
		StackPane countdownPane = new StackPane(countdownLabel);
		countdownPane.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");
		countdownPane.setPrefSize(primaryStage.getWidth(), primaryStage.getHeight());
	
		pane.getChildren().add(countdownPane);
	
		countdown = new Timeline(
			new KeyFrame(Duration.seconds(0), e -> countdownLabel.setText("3")),
			new KeyFrame(Duration.seconds(1), e -> countdownLabel.setText("2")),
			new KeyFrame(Duration.seconds(2), e -> countdownLabel.setText("1")),
			new KeyFrame(Duration.seconds(3), e -> {
				pane.getChildren().remove(countdownPane);
				onComplete.run();
			})
		);
	
		countdown.play();
	}
	
}