// PlayModeView.java
package main.view;

import java.util.List;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
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
	
	protected final Image tileImage = Images.IMAGE_TILE_x4;
	
	public PlayModeView(Grid grid) {
		this.grid = grid;
		this.pane = new Pane();
		heroView = new Rectangle(64,64);
		initialize();
	}
	
	private void initialize() {
		scene = new Scene(pane);
		
		ImagePattern backgroundPattern = new ImagePattern(tileImage);
		scene.setFill(backgroundPattern);
		
		showWalls(grid);
		heroView.setFill(new ImagePattern(Images.IMAGE_PLAYERRIGHT_x4));
		pane.getChildren().add(heroView);
		showGrid(grid);
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