package main.model;

import javafx.application.Application;
import javafx.scene.image.Image;

public class Images extends Application {
	public static final Image IMAGE_UI_CHEST = new Image("/rokue-like_assets/Build_Mode_Chest_Full_View.png");

	public static final Image IMAGE_ALLURE_X2 = new Image("/rokue-like_assets/Allure_x2_32_32.png");
	public static final Image IMAGE_ARCHER_X2 = new Image("/rokue-like_assets/Archer_x2_32_32.png");
	public static final Image IMAGE_BLOCK_X2 = new Image("/rokue-like_assets/Block_x2_32_40.png");
	public static final Image IMAGE_BOX_x2 = new Image("/rokue-like_assets/Box_x2_32_42.png");
	public static final Image IMAGE_BOXONBOX_x2 = new Image("/rokue-like_assets/BoxOnTopOfBox_x2_32_64.png");

	public static final Image IMAGE_TILE_x2 = new Image("/rokue-like_assets/Tile_x2_32_32.png");
    public static final Image IMAGE_PILLAR_x2 = new Image("/rokue-like_assets/Pillar_x2_32_64.png");
    public static final Image IMAGE_LADDER_x2 = new Image("/rokue-like_assets/TileWithLadder_x2_32_32.png");
    public static final Image IMAGE_CUBE_x2 = new Image("/rokue-like_assets/Cube_x2_32_32.png");
    public static final Image IMAGE_SKULL_x2 = new Image("/rokue-like_assets/Skull_x2_32_32.png");
    public static final Image IMAGE_CHEST_x2 = new Image("/rokue-like_assets/Chest_Closed_16_14.png");


	public static char convertImageToChar(Image img) {
		return 'E';
	}

	public static void main(String[] args) {
		System.out.println(convertImageToChar(IMAGE_UI_CHEST));
	}
}
