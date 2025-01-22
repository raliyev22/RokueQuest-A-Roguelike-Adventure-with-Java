package main.utils;

import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.image.Image;
import main.model.Images;

public class TiledHall extends Pane {

    private final double blockCount;
    private final double sideBorderCount;
    private int scale;

    private Grid grid; // Grid instance to hold the tiles

    public TiledHall(double blockCount, double sideBorderCount, Grid givenGrid,int scale) {
        this.blockCount = blockCount;
        this.sideBorderCount = sideBorderCount;
        this.scale = scale;
        this.grid=givenGrid;
        showGrid(grid,scale);
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
