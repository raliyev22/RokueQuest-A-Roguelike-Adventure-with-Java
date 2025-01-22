package main.utils;

import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.image.Image;
import main.model.Images;

public class TiledHall extends Pane {

    private static final Image BLOCK_IMAGE = new Image("/rokue-like_assets/Block_x2_32_40.png");
    private static final Image BLOCK_IMAGE_2X = new Image("/rokue-like_assets/Block_x4_64_80.png");


    private static final Image SIDE_BORDER = new Image("/rokue-like_assets/BlockSideBorder_x2_10_36.png");
    private static final Image SIDE_BORDER_2X = new Image("/rokue-like_assets/BlockSideBorder_x4_20_72.png");

    private static final Image BOTTOM_BORDER = new Image("/rokue-like_assets/BlockBottomBorder_x2_10_46.png");
    private static final Image BOTTOM_BORDER_2X = new Image("/rokue-like_assets/BlockBottomBorder_x4_20_92.png");

    private static final Image UP_BORDER = new Image("/rokue-like_assets/BlockUpBorder_x2_10_48.png");
    private static final Image UP_BORDER_2X = new Image("/rokue-like_assets/BlockUpBorder_x4_20_96.png");

    private static final Image SIDE_BORDER1 = new Image("/rokue-like_assets/BlockSideBorder_x2_10_32.png");
    private static final Image SIDE_BORDER1_2X = new Image("/rokue-like_assets/BlockSideBorder_x4_20_44.png");

    //Objects
    static final Image Pillar_IMAGE = new Image("/rokue-like_assets/Pillar_16_43.png");
    static final Image Ladder_IMAGE = new Image("/rokue-like_assets/TileWithLadder_16_16.png");
    static final Image Box_IMAGE = new Image("/rokue-like_assets/Box_16_21.png");
    static final Image BoxOnBox_IMAGE = new Image("/rokue-like_assets/BoxOnTopOfBox_16_32.png");
    static final Image Cube_IMAGE = new Image("/rokue-like_assets/Cube_8_14.png");
    static final Image Skull_IMAGE = new Image("/rokue-like_assets/Skull_6_6.png");
    static final Image Chest_IMAGE = new Image("/rokue-like_assets/Chest_Closed_16_14.png");

    private static final Image Empty_IMAGE = new Image("/rokue-like_assets/Tile_x2_32_32.png");

    private final double blockCount;
    private final double sideBorderCount;

    private final double blockWidth = 32;
    private final double blockHeight = 40;

    private final double sideBorderWidth = 10;
    private final double sideBorderHeight = 36;

    private final double bottomBorderWidth = 10;
    private final double bottomBorderHeight = 46;

    private final double upBorderWidth = 10;
    private final double upBorderHeight = 48;

    private final double sideBorder1Height = 22;

    private int scale;

    private Grid grid; // Grid instance to hold the tiles

    public TiledHall(double blockCount, double sideBorderCount, Grid givenGrid,int scale) {
        this.blockCount = blockCount;
        this.sideBorderCount = sideBorderCount;
        this.scale = scale;
        this.grid=givenGrid;

        if(scale==1){
            // drawHall();
            showGrid(grid,scale);
        }
        else if(scale==2){
            // drawHall2X();
            showGrid(grid,scale);
        }
    }

    private void showGrid(Grid grid,int scale) {
        // Calculate grid parameters
        Image image;

        // Draw the grid tiles
        for (Tile tile : grid.getTileMap()) {
            image = Images.convertCharToImage(Character.toLowerCase(tile.getTileType()));
            
            Rectangle tileRect = new Rectangle(scale*tile.getLeftSide(), scale*(tile.getTopSide()-1), 32*scale+1, 32*scale+3);
            tileRect.setFill(new ImagePattern(image)); // Assign tile image
            getChildren().add(tileRect);
            }
        
            
        }
    

    public Grid getGrid() {
        return grid;
    }

    public void setGrid(Grid grid) {
        this.grid = grid;
    }
}
