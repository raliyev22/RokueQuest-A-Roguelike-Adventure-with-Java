package test;

import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.image.Image;
import main.utils.Grid;
import main.utils.Tile;
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
            drawHall();
            showGrid(grid,scale);
        }
        else if(scale==2){
            drawHall2X();
            showGrid(grid,scale);

        }
        
        
    }

    private void drawHall() {
        // Add borders and walls

        // Top-left corner border
        Rectangle upBorder1 = new Rectangle(0, 0, (upBorderWidth + 1), upBorderHeight);
        upBorder1.setFill(new ImagePattern(UP_BORDER));
        getChildren().add(upBorder1);

        // Bottom-left corner border
        Rectangle downBorder1 = new Rectangle(0, upBorderHeight + sideBorderCount * sideBorderHeight + sideBorder1Height, bottomBorderWidth + 1, bottomBorderHeight);
        downBorder1.setFill(new ImagePattern(BOTTOM_BORDER));
        getChildren().add(downBorder1);

        // Top-right corner border
        Rectangle upBorder2 = new Rectangle(upBorderWidth + blockCount * blockWidth, 0, upBorderWidth + 1, upBorderHeight);
        upBorder2.setFill(new ImagePattern(UP_BORDER));
        getChildren().add(upBorder2);

        // Bottom-right corner border
        Rectangle downBorder2 = new Rectangle(bottomBorderWidth + blockCount * blockWidth, upBorderHeight + sideBorderCount * sideBorderHeight + sideBorder1Height, bottomBorderWidth + 1, bottomBorderHeight);
        downBorder2.setFill(new ImagePattern(BOTTOM_BORDER));
        getChildren().add(downBorder2);

        // Top wall
        for (int i = 0; i < blockCount; i++) {
            Rectangle topWall = new Rectangle(upBorderWidth + i * blockWidth, 0, blockWidth + 1, blockHeight);
            topWall.setFill(new ImagePattern(BLOCK_IMAGE));
            getChildren().add(topWall);
        }

        // Bottom wall
        for (int i = 0; i < blockCount; i++) {
            Rectangle bottomWall = new Rectangle(bottomBorderWidth + i * blockWidth, 6 + upBorderHeight + sideBorderCount * sideBorderHeight + sideBorder1Height, blockWidth + 1, blockHeight);
            bottomWall.setFill(new ImagePattern(BLOCK_IMAGE));
            getChildren().add(bottomWall);
        }

        // Left wall
        for (int i = 0; i < sideBorderCount; i++) {
            Rectangle leftWall = new Rectangle(0, upBorderHeight + i * sideBorderHeight, sideBorderWidth + 1, sideBorderHeight);
            leftWall.setFill(new ImagePattern(SIDE_BORDER));
            getChildren().add(leftWall);
        }

        // Right wall
        for (int i = 0; i < sideBorderCount; i++) {
            Rectangle rightWall = new Rectangle(sideBorderWidth + blockWidth * blockCount, upBorderHeight + i * sideBorderHeight, sideBorderWidth + 1, sideBorderHeight);
            rightWall.setFill(new ImagePattern(SIDE_BORDER));
            getChildren().add(rightWall);
        }

        // Additional bottom side borders
        Rectangle sideBorder1 = new Rectangle(sideBorderWidth + blockWidth * blockCount, upBorderHeight + sideBorderCount * sideBorderHeight, sideBorderWidth + 1, sideBorder1Height);
        sideBorder1.setFill(new ImagePattern(SIDE_BORDER1));
        getChildren().add(sideBorder1);

        Rectangle sideBorder2 = new Rectangle(0, upBorderHeight + sideBorderCount * sideBorderHeight, sideBorderWidth + 1, sideBorder1Height);
        sideBorder2.setFill(new ImagePattern(SIDE_BORDER1));
        getChildren().add(sideBorder2);

        // Set preferred size for the pane
        setPrefSize(upBorderWidth * 2 + blockCount * blockWidth, upBorderHeight + bottomBorderHeight + sideBorderCount * sideBorderHeight + sideBorder1Height);
    }


    private void drawHall2X() {
        // Add borders and walls

        // Top-left corner border
        Rectangle upBorder1 = new Rectangle(0, 0, (upBorderWidth )*2+1, upBorderHeight*2);
        upBorder1.setFill(new ImagePattern(UP_BORDER_2X));
        getChildren().add(upBorder1);

        // Bottom-left corner border
        Rectangle downBorder1 = new Rectangle(0, upBorderHeight*2 + sideBorderCount * sideBorderHeight*2 + sideBorder1Height*2, (bottomBorderWidth)*2+1, bottomBorderHeight*2);
        downBorder1.setFill(new ImagePattern(BOTTOM_BORDER_2X));
        getChildren().add(downBorder1);

        // Top-right corner border
        Rectangle upBorder2 = new Rectangle(upBorderWidth*2 + blockCount * blockWidth*2, 0, (upBorderWidth)*2+1, upBorderHeight*2);
        upBorder2.setFill(new ImagePattern(UP_BORDER_2X));
        getChildren().add(upBorder2);

        // Bottom-right corner border
        Rectangle downBorder2 = new Rectangle(bottomBorderWidth*2 + blockCount * blockWidth*2, upBorderHeight*2 + sideBorderCount * sideBorderHeight*2 + sideBorder1Height*2, (bottomBorderWidth)*2+1, bottomBorderHeight*2);
        downBorder2.setFill(new ImagePattern(BOTTOM_BORDER_2X));
        getChildren().add(downBorder2);

        // Top wall
        for (int i = 0; i < blockCount; i++) {
            Rectangle topWall = new Rectangle(upBorderWidth*2 + i * blockWidth*2, 0, (blockWidth)*2+1, blockHeight*2);
            topWall.setFill(new ImagePattern(BLOCK_IMAGE_2X));
            getChildren().add(topWall);
        }

        // Bottom wall
        for (int i = 0; i < blockCount; i++) {
            Rectangle bottomWall = new Rectangle(bottomBorderWidth*2 + i * blockWidth*2,  12+ upBorderHeight*2 + sideBorderCount * sideBorderHeight*2 + sideBorder1Height*2, (blockWidth)*2+1, blockHeight*2);
            bottomWall.setFill(new ImagePattern(BLOCK_IMAGE_2X));
            getChildren().add(bottomWall);
        }

        // Left wall
        for (int i = 0; i < sideBorderCount; i++) {
            Rectangle leftWall = new Rectangle(0, upBorderHeight*2 + i * sideBorderHeight*2, (sideBorderWidth)*2+1, sideBorderHeight*2);
            leftWall.setFill(new ImagePattern(SIDE_BORDER_2X));
            getChildren().add(leftWall);
        }

        // Right wall
        for (int i = 0; i < sideBorderCount; i++) {
            Rectangle rightWall = new Rectangle(sideBorderWidth*2 + blockWidth * blockCount*2, upBorderHeight*2 + i * sideBorderHeight*2, (sideBorderWidth )*2+1, sideBorderHeight*2);
            rightWall.setFill(new ImagePattern(SIDE_BORDER_2X));
            getChildren().add(rightWall);
        }

        // Additional bottom side borders
        Rectangle sideBorder1 = new Rectangle(sideBorderWidth*2 + blockWidth * blockCount*2, upBorderHeight*2 + sideBorderCount * sideBorderHeight*2, (sideBorderWidth)*2+1, sideBorder1Height*2);
        sideBorder1.setFill(new ImagePattern(SIDE_BORDER1_2X));
        getChildren().add(sideBorder1);

        Rectangle sideBorder2 = new Rectangle(0, upBorderHeight*2 + sideBorderCount * sideBorderHeight*2, (sideBorderWidth)*2+1, sideBorder1Height*2);
        sideBorder2.setFill(new ImagePattern(SIDE_BORDER1_2X));
        getChildren().add(sideBorder2);

        // Set preferred size for the pane
        setPrefSize(upBorderWidth * 2*2 + blockCount * blockWidth*2, upBorderHeight*2 + bottomBorderHeight*2 + sideBorderCount * sideBorderHeight*2 + sideBorder1Height*2);
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
}
