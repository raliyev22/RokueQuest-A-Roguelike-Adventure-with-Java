package test;

import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.image.Image;

public class TiledHall extends Pane {

    private static final Image BLOCK_IMAGE = new Image("/rokue-like_assets/Block_x2_32_40.png");
    private static final Image SIDE_BORDER = new Image("/rokue-like_assets/BlockSideBorder_x2_10_36.png");
    private static final Image BOTTOM_BORDER = new Image("/rokue-like_assets/BlockBottomBorder_x2_10_46.png");
    private static final Image UP_BORDER = new Image("/rokue-like_assets/BlockUpBorder_x2_10_48.png");

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

    public TiledHall(double blockCount, double sideBorderCount) {
        this.blockCount = blockCount;
        this.sideBorderCount = sideBorderCount;
        drawHall();
    }

    private void drawHall() {
        // Add borders and walls

        // Top-left corner border
        Rectangle upBorder1 = new Rectangle(0, 0, upBorderWidth + 1, upBorderHeight);
        upBorder1.setFill(new ImagePattern(UP_BORDER));
        getChildren().add(upBorder1);

        // Bottom-left corner border
        Rectangle downBorder1 = new Rectangle(0, upBorderHeight + sideBorderCount * sideBorderHeight, bottomBorderWidth + 1, bottomBorderHeight);
        downBorder1.setFill(new ImagePattern(BOTTOM_BORDER));
        getChildren().add(downBorder1);

        // Top-right corner border
        Rectangle upBorder2 = new Rectangle(upBorderWidth + blockCount * blockWidth, 0, upBorderWidth, upBorderHeight);
        upBorder2.setFill(new ImagePattern(UP_BORDER));
        getChildren().add(upBorder2);

        // Bottom-right corner border
        Rectangle downBorder2 = new Rectangle(bottomBorderWidth + blockCount * blockWidth, upBorderHeight + sideBorderCount * sideBorderHeight, bottomBorderWidth, bottomBorderHeight);
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
            Rectangle bottomWall = new Rectangle(bottomBorderWidth + i * blockWidth, 6 + upBorderHeight + sideBorderCount * sideBorderHeight, blockWidth + 1, blockHeight);
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

        // Set preferred size for the pane
        setPrefSize(upBorderWidth * 2 + blockCount * blockWidth, upBorderHeight + bottomBorderHeight + sideBorderCount * sideBorderHeight);
    }
}
