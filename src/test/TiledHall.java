package test;

import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.image.Image;
import main.utils.Grid;
import main.utils.Tile;

public class TiledHall extends Pane {

    private static final Image BLOCK_IMAGE = new Image("/rokue-like_assets/Block_x2_32_40.png");
    private static final Image SIDE_BORDER = new Image("/rokue-like_assets/BlockSideBorder_x2_10_36.png");
    private static final Image BOTTOM_BORDER = new Image("/rokue-like_assets/BlockBottomBorder_x2_10_46.png");
    private static final Image UP_BORDER = new Image("/rokue-like_assets/BlockUpBorder_x2_10_48.png");
    private static final Image SIDE_BORDER1 = new Image("/rokue-like_assets/BlockSideBorder_x2_10_32.png");

    private static final Image TILE_IMAGE = new Image("/rokue-like_assets/Tile_x2_32_32.png");

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

    private Grid grid; // Grid instance to hold the tiles

    public TiledHall(double blockCount, double sideBorderCount) {
        this.blockCount = blockCount;
        this.sideBorderCount = sideBorderCount;
        drawHall();
        addGrid(); // Add the grid inside the hall
    }

    private void drawHall() {
        // Add borders and walls

        // Top-left corner border
        Rectangle upBorder1 = new Rectangle(0, 0, upBorderWidth + 1, upBorderHeight);
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

    private void addGrid() {
        // Calculate grid parameters
        int gridRows = 9; // Example row count
        int gridColumns = 10; // Example column count
        int tileSize = 32;

        int gridTopLeftX = (int) upBorderWidth;
        int gridTopLeftY = (int) blockHeight;

        // Initialize the grid
        grid = new Grid(gridColumns, gridRows, tileSize, tileSize, gridTopLeftX, gridTopLeftY);

        // Draw the grid tiles
        for (Tile tile : grid.getTileMap()) {
            Rectangle tileRect = new Rectangle(tile.getLeftSide(), tile.getTopSide(), tileSize, tileSize);
            tileRect.setFill(new ImagePattern(TILE_IMAGE)); // Assign tile image
            getChildren().add(tileRect);
        }
    }

    public Grid getGrid() {
        return grid;
    }
}
