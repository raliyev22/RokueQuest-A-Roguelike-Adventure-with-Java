package main.model;

import javafx.scene.image.Image;

public class Images {
	static final Image IMAGE_TILE_x2 = new Image("/rokue-like_assets/Tile_x2_32_32.png");
    static final Image IMAGE_UI_CHEST_x2 = new Image("/rokue-like_assets/Build_Mode_Chest_Full_View.png");
    static final Image IMAGE_PILLAR_x2 = new Image("/rokue-like_assets/Pillar_x2_32_64.png");
    static final Image IMAGE_LADDER_x2 = new Image("/rokue-like_assets/TileWithLadder_x2_32_32.png");
    static final Image IMAGE_BOX_x2 = new Image("/rokue-like_assets/Box_16_21.png");
    static final Image IMAGE_BOXONBOX_x2 = new Image("/rokue-like_assets/BoxOnTopOfBox_x2_32_64.png");
    static final Image IMAGE_CUBE_x2 = new Image("/rokue-like_assets/Cube_x2_32_32.png");
    static final Image IMAGE_SKULL_x2 = new Image("/rokue-like_assets/Skull_x2_32_32.png");
    static final Image IMAGE_CHEST_x2 = new Image("/rokue-like_assets/Chest_Closed_16_14.png");


	public char convertImageToChar(Image img) {
		switch (img) {
			case TILE_IMAGE_x2:
				"E";
				break;
			default:
				throw new AssertionError();
		}
	}
}
