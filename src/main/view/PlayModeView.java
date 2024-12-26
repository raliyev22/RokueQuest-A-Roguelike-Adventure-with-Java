// PlayModeView.java
package main.view;

import java.net.http.HttpRequest;
import java.util.List;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import main.controller.PlayModeController;
import main.model.Images;
import main.utils.Grid;
import main.utils.Tile;

public class PlayModeView {
	protected Pane pane;
	protected Scene scene;
	protected int tileSize = 64;
	protected Grid grid;
	protected Rectangle heroView;
	private VBox popupContainer; // Popup dialog container

	protected final Image tileImage = Images.IMAGE_TILE_x4;
	
	public PlayModeView(Grid grid) {
		this.grid = grid;
		this.pane = new Pane();
		heroView = new Rectangle(64,64);
		initialize();
	}

	public void refresh(Grid newGrid) {
		this.grid = newGrid;
		pane.getChildren().clear();
		initialize();
	}
	

	public void initialize() {
		if (scene == null) {
			scene = new Scene(pane);
		}
		
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

		HBox buttonContainer = new HBox(10);
		buttonContainer.setAlignment(javafx.geometry.Pos.CENTER);

		Button closeButton = new Button();
		closeButton.setStyle("-fx-background-color: transparent;");

		ImageView exitImageView = new javafx.scene.image.ImageView(Images.IMAGE_EXITBUTTON_x4);
		exitImageView.setFitWidth(40);
		exitImageView.setFitHeight(40);

		closeButton.setGraphic(exitImageView);
		closeButton.setPrefWidth(40);
		closeButton.setPrefHeight(40);

		Button pauseButton = new Button();
		pauseButton.setStyle("-fx-background-color: transparent;"); 

		ImageView pauseImageView = new javafx.scene.image.ImageView(Images.IMAGE_PAUSEBUTTON_x4);
		pauseImageView.setFitWidth(40);
		pauseImageView.setFitHeight(40);

		pauseButton.setGraphic(pauseImageView);
		pauseButton.setPrefWidth(40);
		pauseButton.setPrefHeight(40);

		buttonContainer.getChildren().addAll(closeButton, pauseButton);
        
	    HBox timeLabelContainer = new HBox(); // Container for timeLabel
    	timeLabelContainer.setAlignment(javafx.geometry.Pos.CENTER); // Center align horizontally
		Label timeLabel = new Label("Time:");
    	timeLabel.setStyle("-fx-font-size: 32px; -fx-text-fill: white; -fx-background-color: transparent;");
    	timeLabelContainer.getChildren().add(timeLabel); // Add the label to the container

		
        HBox heartsContainer = new HBox(5); // Kalpler arasındaki boşluk 5 px
		heartsContainer.setAlignment(javafx.geometry.Pos.CENTER); // Kalpleri ortala
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
	
	private void showGrid(Grid grid) {
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
					drawTallItem(pane, tile, image);
				} else if (tileType == 'D') {
					drawTallItem(pane, tile, image);
				} else if (tileType == 'E') {
					continue;
				} else {
					drawNormalItem(pane, tile, image);
				}
			}
		}
	}
	
	private void drawNormalItem(Pane pane, Tile tile, Image image) {
		Rectangle normalItem = 
		new Rectangle(tile.getLeftSide(), tile.getTopSide(), tileSize, tileSize);
		if (image != null) {
			normalItem.setFill(new ImagePattern(image));
		} else {
			normalItem.setFill(Color.GRAY);
		}
		pane.getChildren().add(normalItem);
	}
	
	private void drawTallItem(Pane pane, Tile tile, Image image) {
		Rectangle tallItem = 
		new Rectangle(tile.getLeftSide(), tile.getTopSide() - tileSize, tileSize, tileSize * 2);
		tallItem.setFill(new ImagePattern(image));
		pane.getChildren().add(tallItem);
	}
	
	public void updateHeroPosition(double x, double y) {
		heroView.setX(x);
		heroView.setY(y);
	}
	
	public Pane getPane() {
		return pane;
	}
	
	public Scene getScene() {
		return scene;
	}
}