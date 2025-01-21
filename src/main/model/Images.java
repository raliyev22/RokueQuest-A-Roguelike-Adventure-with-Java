package main.model;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Images extends Application {
	// Images that are usually used only once, these do not have a char associated with them.
	public static final Image IMAGE_UI_CHEST = new Image("/rokue-like_assets/Build_Mode_Chest_Full_View.png");
	public static final Image IMAGE_INVENTORY = new Image("/rokue-like_assets/Inventory.png");
	
	// public static final Image IMAGE_DOORSOPEN = new Image("/rokue-like_assets/DoorOpenWithoutWalls_32_32.png");
	// public static final Image IMAGE_DOORSOPEN_x2 = new Image("/rokue-like_assets/DoorOpenWithoutWalls_x2_64_64.png");
	// public static final Image IMAGE_DOORSCLOSED = new Image("/rokue-like_assets/DoorWithoutWalls_32_32.png");
	// public static final Image IMAGE_DOORSCLOSED_x2 = new Image("/rokue-like_assets/DoorWithoutWalls_x2_64_64.png");
	// public static final Image IMAGE_DOORSCLOSEDWALLS = new Image("/rokue-like_assets/DoorWithWalls_64_32.png");
	// public static final Image IMAGE_DOORSCLOSEDWALLS_x2 = new Image("/rokue-like_assets/DoorWithWalls_x2_128_64.png");
	
	public static final Image IMAGE_EXITBUTTON_x2 = new Image("/rokue-like_assets/ExitButton_x2_32_32.png");
	public static final Image IMAGE_EXITBUTTON_x4 = new Image("/rokue-like_assets/ExitButton_x4_64_64.png");
	public static final Image IMAGE_PAUSEBUTTON_x2 = new Image("/rokue-like_assets/PauseButton_x2_32_32.png");
	public static final Image IMAGE_PAUSEBUTTON_x4 = new Image("/rokue-like_assets/PauseButton_x4_64_64.png");
	public static final Image IMAGE_SAVEBUTTON_x2 = new Image("/rokue-like_assets/SaveButton_x2_32_32.png");
	public static final Image IMAGE_SAVEBUTTON_x4 = new Image("/rokue-like_assets/SaveButton_x4_64_64.png");
	public static final Image IMAGE_PLAYBUTTON_x2 = new Image("/rokue-like_assets/PlayButton_x2_32_32.png");
	public static final Image IMAGE_PLAYBUTTON_x4 = new Image("/rokue-like_assets/PlayButton_x4_64_64.png");
	
	public static final Image IMAGE_HEART_x2 = new Image("/rokue-like_assets/Heart_x2_32_32.png");
	public static final Image IMAGE_HEART_x4 = new Image("/rokue-like_assets/Heart_x4_64_64.png");
	
	public static final Image IMAGE_SMEARED = new Image("/rokue-like_assets/SmearedTile_32_32.png");
	public static final Image IMAGE_SMEARED_x2 = new Image("/rokue-like_assets/SmearedTile_x2_64_64.png");

	public static final Image IMAGE_BACKGROUNDPNG = new Image("/rokue-like_assets/rokue33.png");
	public static final Image IMAGE_BACKGROUNDJPG = new Image("/rokue-like_assets/rokue33.jpg");

	public static final Image IMAGE_WALLS_X4 = new Image("/rokue-like_assets/Walls_x4_736_680.png");
	public static final Image IMAGE_EARTHHALL_TOP_X4 = new Image("/rokue-like_assets/Walls_Earth_x4_680_399_Top_new.png");
	public static final Image IMAGE_EARTHHALL_CLOSEDOOR_BOTTOM_X4 = new Image("/rokue-like_assets/Walls_Earth_Close_Door_x4_680_400_Bottom.png");
	public static final Image IMAGE_EARTHHALL_OPENDOOR_BOTTOM_X4 = new Image("/rokue-like_assets/Walls_Earth_Open_Door_x4_680_400_Bottom.png");
	public static final Image IMAGE_AIRHALL_TOP_X4 = new Image("/rokue-like_assets/Walls_Air_x4_680_399_Top_new.png");
	public static final Image IMAGE_AIRHALL_CLOSEDOOR_BOTTOM_X4 = new Image("/rokue-like_assets/Walls_Air_Close_Door_x4_680_400_Bottom.png");
	public static final Image IMAGE_AIRHALL_OPENDOOR_BOTTOM_X4 = new Image("/rokue-like_assets/Walls_Air_Open_Door_x4_680_400_Bottom.png");
	public static final Image IMAGE_WATERHALL_TOP_X4 = new Image("/rokue-like_assets/Walls_Water_x4_680_399_Top.png");
	public static final Image IMAGE_WATERHALL_CLOSEDOOR_BOTTOM_X4 = new Image("/rokue-like_assets/Walls_Water_Close_Door_x4_680_400_Bottom.png");
	public static final Image IMAGE_WATERHALL_OPENDOOR_BOTTOM_X4 = new Image("/rokue-like_assets/Walls_Water_Open_Door_x4_680_400_Bottom.png");
	public static final Image IMAGE_FIREHALL_TOP_X4 = new Image("/rokue-like_assets/Walls_Fire_x4_680_399_Top_new.png");
	public static final Image IMAGE_FIREHALL_CLOSEDOOR_BOTTOM_X4 = new Image("/rokue-like_assets/Walls_Fire_Close_Door_x4_680_400_Bottom.png");
	public static final Image IMAGE_FIREHALL_OPENDOOR_BOTTOM_X4 = new Image("/rokue-like_assets/Walls_Fire_Open_Door_x4_680_400_Bottom.png");
    
    public static final Image IMAGE_PLAYERLEFTTAKINGDAMAGE_x4 = new Image("/rokue-like_assets/PlayerLeftTakingDamage_x4_64_64.png");
    public static final Image IMAGE_PLAYERRIGHTTAKINGDAMAGE_x4 = new Image("/rokue-like_assets/PlayerRightTakingDamage_x4_64_64.png");
	
	
	
	// Images that are used more than once, associated with a char.
	// In alphabetical order.
	public final static Image IMAGE_ALLURE_X2 = new Image("/rokue-like_assets/Allure_x2_32_32.png");
	public static final Image IMAGE_ARCHER_X2 = new Image("/rokue-like_assets/Archer_x2_32_32.png");
	public static final Image IMAGE_BLOCK_X2 = new Image("/rokue-like_assets/Block_x2_32_40.png");
	public static final Image IMAGE_BOX_x2 = new Image("/rokue-like_assets/Box_x2_32_42.png");
	public static final Image IMAGE_BOXONBOX_x2 = new Image("/rokue-like_assets/BoxOnTopOfBox_x2_32_64.png");
	public static final Image IMAGE_CHESTCLOSED_x2 = new Image("/rokue-like_assets/Chest_Closed_x2_32_32.png");
	public static final Image IMAGE_CHESTOPEN_x2 = new Image("/rokue-like_assets/Chest_Open_x2_32_32.png");
	public static final Image IMAGE_CHESTHEARTCLOSED_x2 = new Image("/rokue-like_assets/ChestHeart_x2_32_32.png");
	// public static final Image IMAGE_CHESTHEARTOPEN_x2 = new Image("/rokue-like_assets/ChestOpenWithHeart_x2_32_54.png");
	public static final Image IMAGE_HEART_ENCH_X2 = new Image("/rokue-like_assets/Heart_Enchantment_x2_32_32.png");
    public static final Image IMAGE_CLOAK_x2 = new Image("/rokue-like_assets/Cloak_x2_32_32.png");
    public static final Image IMAGE_CLOCK_x2 = new Image("/rokue-like_assets/Clock_x2_60_60.png");
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
	public static final Image IMAGE_BLUEELIXIR_x2= new Image("/rokue-like_assets/Blue_Elixir_x2_32_32.png");
	
	// All of the assets above, in times two pixels.
	public static final Image IMAGE_ALLURE_x4 = new Image("/rokue-like_assets/Allure_x4_64_64.png");
	public static final Image IMAGE_ARCHER_x4 = new Image("/rokue-like_assets/Archer_x4_64_64.png");
	public static final Image IMAGE_BLOCK_x4 = new Image("/rokue-like_assets/Block_x4_64_80.png");
	public static final Image IMAGE_BOX_x4 = new Image("/rokue-like_assets/Box_x4_64_84.png");
	public static final Image IMAGE_BOXONBOX_x4 = new Image("/rokue-like_assets/BoxOnTopOfBox_x4_64_128.png");
	public static final Image IMAGE_CHESTCLOSED_x4 = new Image("/rokue-like_assets/Chest_Closed_x4_64_64.png");
	public static final Image IMAGE_CHESTOPEN_x4 = new Image("/rokue-like_assets/Chest_Open_x4_64_64.png");
	public static final Image IMAGE_CHESTHEARTCLOSED_x4 = new Image("/rokue-like_assets/ChestHeart_x4_64_64.png");
	// public static final Image IMAGE_CHESTHEARTOPEN_x4 = new Image("/rokue-like_assets/ChestOpenWithHeart_x4_64_108.png");
	public static final Image IMAGE_HEART_ENCH_X4 = new Image("/rokue-like_assets/Heart_Enchantment_x4_64_64.png");
    public static final Image IMAGE_CLOAK_x4 = new Image("/rokue-like_assets/Cloak_x4_64_64.png");
    public static final Image IMAGE_CLOCK_x4 = new Image("/rokue-like_assets/Clock_x2_60_60.png");
	public static final Image IMAGE_CUBE_x4 = new Image("/rokue-like_assets/Cube_x4_64_64.png");
	public static final Image IMAGE_FIGHTER_x4 = new Image("/rokue-like_assets/Fighter_x4_64_80.png");
	public static final Image IMAGE_LADDER_x4 = new Image("/rokue-like_assets/TileWithLadder_x4_64_64.png");
	public static final Image IMAGE_PILLAR_x4 = new Image("/rokue-like_assets/Pillar_x4_64_128.png");
	public static final Image IMAGE_PLAYERLEFT_x4 = new Image("/rokue-like_assets/PlayerLeft_x4_64_64.png");
	public static final Image IMAGE_PLAYERRIGHT_x4 = new Image("/rokue-like_assets/PlayerRight_x4_64_64.png");
	public static final Image IMAGE_REVEAL_x4 = new Image("/rokue-like_assets/Reveal_x4_64_64.png");
	public static final Image IMAGE_SKULL_x4 = new Image("/rokue-like_assets/Skull_x4_64_64.png");
	public static final Image IMAGE_TILE_x4 = new Image("/rokue-like_assets/Tile_x4_64_64.png");
	public static final Image IMAGE_WIZARD_x4 = new Image("/rokue-like_assets/Wizard_x4_64_64.png");
    public static final Image IMAGE_BLUEELIXIR_x4= new Image("/rokue-like_assets/Blue_Elixir_x4_64_64.png");

	public static final Image IMAGE_TRANSPARENT = new Image("/rokue-like_assets/Transparent.png");


	public static Image convertEnchantmentToImage(Enchantment.Type type) {
		switch (type) {
			case EXTRA_TIME:
				return IMAGE_TRANSPARENT; // Replace with actual image
			case REVEAL:
				return IMAGE_REVEAL_x4;
			case CLOAK_OF_PROTECTION:
				return IMAGE_CLOAK_x4;
			case LURING_GEM:
				return IMAGE_ALLURE_x4;
			case EXTRA_LIFE:
				return IMAGE_HEART_ENCH_X4;
			default:
				return null;
		}
	}
	
	
	//Takes an Image and finds the char associated with it.
	public static char convertImageToChar(Image img) {
		// Cannot use switch statements (image is not a primitive type) so idk what to do.
		// One option is to use the hashCode of all the images.
		// Other option is to use the toString method.
		// Never mind, found a workaround:
		int hashCode = img.hashCode();
		
		for (char c = 'a'; c <= 'z'; c++) {
			if (convertCharToImage(c) != null) {
				if (convertCharToImage(c).hashCode() == hashCode) {
					return c;
				}
			}
		}

		for (char c = 'A'; c <= 'Z'; c++) {
			if (convertCharToImage(c) != null) {
				if (convertCharToImage(c).hashCode() == hashCode) {
					return c;
				}
			}
		}


		return '%'; // just an error character.
	}
	
	// Takes a char and finds the image associated with it. Upper case means x2, lower case means x4.
	public static Image convertCharToImage(char c) {
		// Unless someone wants to change chars to TileType that I conveniently created just for this purpose, it returns a char.
		// Although if you want to change it, be careful about x2 and x4, please.
		// You could also use ints, like 1 = archerx2, -1 = archerx4, 2 = box etc.
		switch (c) {
			case 'A' -> {
				return IMAGE_ARCHER_X2;
			}
			case 'B' -> {
				return IMAGE_BOX_x2;
			}
			case 'C' -> {
				return IMAGE_CHESTCLOSED_x2;
			}
			case 'D' -> {
				return IMAGE_BOXONBOX_x2;
			}
			case 'E' -> {
				return IMAGE_TILE_x2;
			}
			case 'F' -> {
				return IMAGE_FIGHTER_x2;
			}
			case 'G' -> { 
				return IMAGE_CUBE_x2;
			}
			case 'H' -> {
				return IMAGE_CHESTHEARTCLOSED_x2;
			}
			case 'J' -> {
				return IMAGE_BLOCK_X2;
			}
			case 'K' -> {
				return IMAGE_CHESTOPEN_x2;
			}
			case 'L' -> {
				return IMAGE_PLAYERLEFT_x2;
			}
			case 'M' -> {
				return IMAGE_HEART_ENCH_X2;
			}
			case 'N' -> {
				return IMAGE_ALLURE_X2;
			}
			case 'O' -> {
				return IMAGE_CLOAK_x2;
			}
			case 'P' -> {
				return IMAGE_PILLAR_x2;
			}
			case 'Q' -> {
				return IMAGE_REVEAL_x2;
			}
			case 'R' -> {
				return IMAGE_PLAYERRIGHT_x2;
			}
			case 'S' -> {
				return IMAGE_SKULL_x2;
			}
			case 'T' -> {
				return IMAGE_LADDER_x2;
			}
			case 'W' -> {
				return IMAGE_WIZARD_x2;
			}
            case 'V' -> {
				return IMAGE_BLUEELIXIR_x2;
			}
            case 'X' -> {
				return IMAGE_CLOCK_x2;
			}
			// As of writing this, I U V X Y Z are unused. Also Turkish characters, in case we need more.
			
			// Here are the same chars, but lower case. These return x4 images.
			case 'a' -> {
				return IMAGE_ARCHER_x4;
			}
			case 'b' -> {
				return IMAGE_BOX_x4;
			}
			case 'c' -> {
				return IMAGE_CHESTCLOSED_x4;
			}
			case 'd' -> {
				return IMAGE_BOXONBOX_x4;
			}
			case 'e' -> {
				return IMAGE_TILE_x4;
			}
			case 'f' -> {
				return IMAGE_FIGHTER_x4;
			}
			case 'g' -> { 
				return IMAGE_CUBE_x4;
			}
			case 'h' -> {
				return IMAGE_CHESTHEARTCLOSED_x4;
			}
			case 'j' -> {
				return IMAGE_BLOCK_x4;
			}
			case 'k' -> {
				return IMAGE_CHESTOPEN_x4;
			}
			case 'l' -> {
				return IMAGE_PLAYERLEFT_x4;
			}
			case 'm' -> {
				return IMAGE_HEART_ENCH_X4;
			}
			case 'n' -> {
				return IMAGE_ALLURE_x4;
			}
			case 'o' -> {
				return IMAGE_CLOAK_x4;
			}
			case 'p' -> {
				return IMAGE_PILLAR_x4;
			}
			case 'q' -> {
				return IMAGE_REVEAL_x4;
			}
			case 'r' -> {
				return IMAGE_PLAYERRIGHT_x4;
			}
			case 's' -> {
				return IMAGE_SKULL_x4;
			}
			case 't' -> {
				return IMAGE_LADDER_x4;
			}
			case 'w' -> {
				return IMAGE_WIZARD_x4;
			}
            case 'v' -> {
				return IMAGE_BLUEELIXIR_x4;
			}
            case 'x' -> {
				return IMAGE_CLOCK_x4;
			}
		}
		
		return null;
	}
	
	public static void main(String[] args) {
		for (char c = 'A'; c <= 'W'; c++) {
			if (c == 'I' || c == 'U' || c == 'V') {
				continue;
			}
			System.out.println(c);
			System.out.println(convertCharToImage(c).hashCode());
			System.out.println(convertImageToChar(convertCharToImage(c)));
			System.out.println("------------------------------------------");
		}
		
		for (char c = 'a'; c <= 'w'; c++) {
			if (c == 'i' || c == 'u' || c == 'v') {
				continue;
			}
			System.out.println(c);
			System.out.println(convertCharToImage(c).hashCode());
			System.out.println(convertImageToChar(convertCharToImage(c)));
			System.out.println("------------------------------------------");
		}
	}

	@Override
	public void start(Stage arg0) throws Exception {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'start'");
	}
}
