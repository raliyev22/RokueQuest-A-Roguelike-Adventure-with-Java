package main.view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import test.TiledHall;

public class PlayModeOtherView extends Application {
	protected final int tileSize = 32;
	protected final int SCENE_WIDTH = 1400;
	protected final int SCENE_HEIGHT = 800;
	protected final int BLOCK_COUNT = 17;
	protected final int SIDE_BORDER_COUNT = 12;
	protected final int HALL_POSITION_X = 50;
	protected final int HALL_POSITION_Y = 50;

	static final Image tileImage = new Image("/rokue-like_assets/Tile_x4_64_64.png");

	protected PlayModeController playModeController = new PlayModeController();

	public void start(Stage primaryStage) {
		Pane pane = new Pane();

		Scene scene = new Scene(pane, SCENE_WIDTH, SCENE_HEIGHT);
		ImagePattern backgroundPattern = new ImagePattern(tileImage);
		scene.setFill(backgroundPattern);
        primaryStage.setTitle("Tiled Hall Example");
        primaryStage.setScene(scene);
        primaryStage.show();

		TiledHall currentHall = new TiledHall(BLOCK_COUNT, SIDE_BORDER_COUNT, playModeController.getPlayModeGrid());
		setHallPosition(currentHall, HALL_POSITION_X, HALL_POSITION_Y);

		pane.getChildren().add(currentHall);
	}

	private void setHallPosition(TiledHall hall, int x, int y) {
        hall.setLayoutX(x);
        hall.setLayoutY(y);
        hall.getGrid().setTopLeftXCordinate(hall.getGrid().topLeftXCoordinate + x);
        hall.getGrid().setTopLeftYCordinate(hall.getGrid().topLeftYCoordinate + y);
    }
	
	public static void main(String[] args) {
		launch(args);
	}
}
