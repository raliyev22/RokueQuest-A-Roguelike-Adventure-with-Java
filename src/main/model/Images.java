package main.model;

import javafx.application.Application;
import javafx.scene.image.Image;

public class Images extends Application {
	// Images that are usually used only once, these do not have a char associated with them.
	public static final Image IMAGE_UI_CHEST = new Image("/rokue-like_assets/Build_Mode_Chest_Full_View.png");
	public static final Image IMAGE_INVENTORY = new Image("/rokue-like_assets/Inventory.png");
	
	public static final Image IMAGE_DOORSOPEN = new Image("/rokue-like_assets/DoorOpenWithoutWalls_32_32.png");
	public static final Image IMAGE_DOORSOPEN_x2 = new Image("/rokue-like_assets/DoorOpenWithoutWalls_x2_64_64.png");
	public static final Image IMAGE_DOORSCLOSED = new Image("/rokue-like_assets/DoorWithoutWalls_32_32.png");
	public static final Image IMAGE_DOORSCLOSED_x2 = new Image("/rokue-like_assets/DoorWithoutWalls_x2_64_64.png");
	public static final Image IMAGE_DOORSCLOSEDWALLS = new Image("/rokue-like_assets/DoorWithWalls_64_32.png");
	public static final Image IMAGE_DOORSCLOSEDWALLS_x2 = new Image("/rokue-like_assets/DoorWithWalls_x2_128_64.png");
	
	public static final Image IMAGE_EXITBUTTON_x2 = new Image("/rokue-like_assets/ExitButton_x2_32_32.png");
	public static final Image IMAGE_EXITBUTTON_x4 = new Image("/rokue-like_assets/ExitButton_x4_64_64.png");
	public static final Image IMAGE_PAUSEBUTTON_x2 = new Image("/rokue-like_assets/PauseButton_x2_32_32.png");
	public static final Image IMAGE_PAUSEBUTTON_x4 = new Image("/rokue-like_assets/PauseButton_x4_64_64.png");
	public static final Image IMAGE_PLAYBUTTON_x2 = new Image("/rokue-like_assets/PlayButton_x2_32_32.png");
	public static final Image IMAGE_PLAYBUTTON_x4 = new Image("/rokue-like_assets/PlayButton_x4_64_64.png");
	
	public static final Image IMAGE_HEART_x2 = new Image("/rokue-like_assets/Heart_x2_32_32.png");
	public static final Image IMAGE_HEART_x4 = new Image("/rokue-like_assets/Heart_x4_64_64.png");

	public static final Image IMAGE_SMEARED = new Image("/rokue-like_assets/SmearedTile_32_32.png");
	public static final Image IMAGE_SMEARED_x2 = new Image("/rokue-like_assets/SmearedTile_x2_64_64.png");
	
	
	
	
	// Images that are used more than once, associated with a char.
	// In alphabetical order.
	public static final Image IMAGE_ALLURE_X2 = new Image("/rokue-like_assets/Allure_x2_32_32.png");
	public static final Image IMAGE_ARCHER_X2 = new Image("/rokue-like_assets/Archer_x2_32_32.png");
	public static final Image IMAGE_BLOCK_X2 = new Image("/rokue-like_assets/Block_x2_32_40.png");
	public static final Image IMAGE_BOX_x2 = new Image("/rokue-like_assets/Box_x2_32_42.png");
	public static final Image IMAGE_BOXONBOX_x2 = new Image("/rokue-like_assets/BoxOnTopOfBox_x2_32_64.png");
	public static final Image IMAGE_CHESTCLOSED_x2 = new Image("/rokue-like_assets/Chest_Closed_x2_32_32.png");
	public static final Image IMAGE_CHESTOPEN_x2 = new Image("/rokue-like_assets/Chest_Open_x2_32_32.png");
	public static final Image IMAGE_CHESTHEART_x2 = new Image("/rokue-like_assets/ChestHeart_x2_32_32.png");
	public static final Image IMAGE_CHESTHEARTOPEN_x2 = new Image("/rokue-like_assets/ChestOpenWithHeart_x2_32_54.png");
	public static final Image IMAGE_CLOAK_x2 = new Image("/rokue-like_assets/Cloak_x2_32_32.png");
	public static final Image IMAGE_CUBE_x2 = new Image("/rokue-like_assets/Cube_x2_32_32.png");
	public static final Image IMAGE_FIGHTER_x2 = new Image("/rokue-like_assets/Fighter_x2_32_40.png");
	public static final Image IMAGE_PILLAR_x2 = new Image("/rokue-like_assets/Pillar_x2_32_64.png");
	public static final Image IMAGE_PLAYERLEFT_x2 = new Image("/rokue-like_assets/PlayerLeft_x2_32_32.png");
	public static final Image IMAGE_PLAYERRIGHT_x2 = new Image("/rokue-like_assets/PlayerRight_x2_32_32.png");
	public static final Image IMAGE_REVEAL_x2 = new Image("/rokue-like_assets/Reveal_x2_32_32.png");
	public static final Image IMAGE_SKULL_x2 = new Image("/rokue-like_assets/Skull_x2_32_32.png");
	public static final Image IMAGE_TILE_x2 = new Image("/rokue-like_assets/Tile_x2_32_32.png");
	public static final Image IMAGE_LADDER_x2 = new Image("/rokue-like_assets/TileWithLadder_x2_32_32.png");
	public static final Image IMAGE_WIZARD_x2 = new Image("/rokue-like_assets/Wizard_x2_32_32.png");

	public static final Image IMAGE_ALLURE_x4 = new Image("/rokue-like_assets/Allure_x4_64_64.png");
	public static final Image IMAGE_ARCHER_x4 = new Image("/rokue-like_assets/Archer_x4_64_64.png");
	public static final Image IMAGE_BLOCK_x4 = new Image("/rokue-like_assets/Block_x4_64_80.png");
	public static final Image IMAGE_BOX_x4 = new Image("/rokue-like_assets/Box_x4_64_84.png");
	public static final Image IMAGE_BOXONBOX_x4 = new Image("/rokue-like_assets/BoxOnTopOfBox_x4_64_128.png");
	public static final Image IMAGE_CHESTCLOSED_x4 = new Image("/rokue-like_assets/Chest_Closed_x4_64_64.png");
	public static final Image IMAGE_CHESTOPEN_x4 = new Image("/rokue-like_assets/Chest_Open_x4_64_64.png");
	public static final Image IMAGE_CHESTHEART_x4 = new Image("/rokue-like_assets/ChestHeart_x4_64_64.png");
	public static final Image IMAGE_CHESTHEARTOPEN_x4 = new Image("/rokue-like_assets/ChestOpenWithHeart_x4_64_108.png");
	public static final Image IMAGE_CLOAK_x4 = new Image("/rokue-like_assets/Cloak_x4_64_64.png");
	public static final Image IMAGE_CUBE_x4 = new Image("/rokue-like_assets/Cube_x4_64_64.png");
	public static final Image IMAGE_FIGHTER_x4 = new Image("/rokue-like_assets/Fighter_x4_64_80.png");
	public static final Image IMAGE_PILLAR_x4 = new Image("/rokue-like_assets/Pillar_x4_64_128.png");
	public static final Image IMAGE_PLAYERLEFT_x4 = new Image("/rokue-like_assets/PlayerLeft_x4_64_64.png");
	public static final Image IMAGE_PLAYERRIGHT_x4 = new Image("/rokue-like_assets/PlayerRight_x4_64_64.png");
	public static final Image IMAGE_REVEAL_x4 = new Image("/rokue-like_assets/Reveal_x4_64_64.png");
	public static final Image IMAGE_SKULL_x4 = new Image("/rokue-like_assets/Skull_x4_64_64.png");
	public static final Image IMAGE_TILE_x4 = new Image("/rokue-like_assets/Tile_x4_64_64.png");
	public static final Image IMAGE_LADDER_x4 = new Image("/rokue-like_assets/TileWithLadder_x4_64_64.png");
	public static final Image IMAGE_WIZARD_x4 = new Image("/rokue-like_assets/Wizard_x4_64_64.png");
	
	
	public static char convertImageToChar(Image img) {
		return 'E';
	}
	
	public static void main(String[] args) {
		System.out.println(convertImageToChar(IMAGE_UI_CHEST));
	}
}
