//package oldFiles;
//
//import java.util.List;
//
//import javafx.application.Application;
//import javafx.scene.Scene;
//import javafx.scene.image.Image;
//import javafx.scene.layout.Pane;
//import javafx.scene.paint.Color;
//import javafx.scene.paint.ImagePattern;
//import javafx.scene.shape.Rectangle;
//import javafx.stage.Stage;
//import main.model.Images;
//import main.model.MonsterType;
//import main.utils.Grid;
//import main.utils.Tile;
//import test.TiledHall;
//import main.controller.PlayModeController;
//
//public class PlayModeOtherView extends Application {
//	protected final int tileSize = 64;
//	protected final int SCENE_WIDTH = 1400;
//	protected final int SCENE_HEIGHT = 800;
//	protected final int BLOCK_COUNT = 20;
//	protected final int SIDE_BORDER_COUNT = 15;
//	protected final int HALL_POSITION_X = PlayModeController.topLeftXCoordinate;
//	protected final int HALL_POSITION_Y = PlayModeController.topLeftYCoordinate;
//
//	protected final Image tileImage = Images.IMAGE_TILE_x4;
//
//	protected TiledHall hall = null;
//
//	protected PlayModeController playModeController =
//	new PlayModeController(Grid earthHall, Grid airHall, Grid waterHall, Grid fireHall);
//
//	public void start(Stage primaryStage) {
//		Pane pane = new Pane();
//
//		Scene scene = new Scene(pane, SCENE_WIDTH, SCENE_HEIGHT);
//		ImagePattern backgroundPattern = new ImagePattern(tileImage);
//		scene.setFill(backgroundPattern);
//		primaryStage.setTitle("Tiled Hall Example");
//		primaryStage.setScene(scene);
//		primaryStage.show();
//
//		hall = new TiledHall(BLOCK_COUNT, SIDE_BORDER_COUNT, playModeController.getPlayModeGrid());
//		setHallPosition(hall, HALL_POSITION_X, HALL_POSITION_Y);
//
//		pane.getChildren().add(hall);
//		drawHall();
//		System.out.println(playModeController.getPlayModeGrid());
//		playModeController.getPlayModeGrid().changeTileWithIndex(2, 4, 'E');
//		playModeController.createMonster(5, 8, MonsterType.WIZARD);
//		drawHall();
//		System.out.println(playModeController.getPlayModeGrid());
//	}
//
//	private boolean isWalkable(char c) {
//
//	}
//
//	private void drawEmptyHall() {
//	}
//
//	private void drawHall() {
//		List<Tile> tiles = playModeController.playModeGrid.getTileMap();
//		for (Tile tile : tiles) {
//			char tileType = tile.getTileType();
//			char lowerCaseLetter = Character.toLowerCase(tileType);
//			Image image = Images.convertCharToImage(lowerCaseLetter);
//
//			if (image != null) {
//				if (tileType == 'P') {
//					drawTallItem(hall, tile, image);
//				} else if (tileType == 'D') {
//					drawTallItem(hall, tile, image);
//				} else if (tileType == 'E') { // For debugging purposes, print empty tile as fighter
//					image = Images.convertCharToImage('f');
//					drawNormalItem(hall, tile, image);
//				} else {
//					drawNormalItem(hall, tile, image);
//				}
//			}
//		}
//	}
//
//	private void drawNormalItem(Pane hall, Tile tile, Image image) {
//		Rectangle normalItem =
//		new Rectangle(tile.getLeftSide() * 2 - 10, tile.getTopSide() * 2 - 43, tileSize,tileSize);
//		if (image != null) {
//			normalItem.setFill(new ImagePattern(image));
//		} else {
//			normalItem.setFill(Color.GRAY);
//		}
//		hall.getChildren().add(normalItem);
//	}
//
//	private void drawTallItem(Pane hall, Tile tile, Image image) {
//		Rectangle tallItem =
//		new Rectangle(tile.getLeftSide() * 2 - 10, tile.getTopSide() * 2 - 107, tileSize, tileSize * 2);
//		tallItem.setFill(new ImagePattern(image));
//		hall.getChildren().add(tallItem);
//	}
//
//	private void setHallPosition(TiledHall hall, int x, int y) {
//		hall.setLayoutX(x);
//		hall.setLayoutY(y);
//		hall.getGrid().setTopLeftXCordinate(hall.getGrid().topLeftXCoordinate + x);
//		hall.getGrid().setTopLeftYCordinate(hall.getGrid().topLeftYCoordinate + y);
//	}
//
//	public static void main(String[] args) {
//		launch(args);
//	}
//}
