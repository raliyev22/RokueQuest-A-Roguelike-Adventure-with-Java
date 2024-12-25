// PlayModeView.java
package main.view;

import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import main.model.Images;
import main.utils.Grid;
import main.utils.Tile;

public class PlayModeView {
    private Pane pane;
    private int tileSize = 64;
    private Grid grid;
    Rectangle heroView;


    public PlayModeView(Grid grid) {
        this.grid = grid;
        this.pane = new Pane();
        heroView = new Rectangle(64,64);
        initialize();
    }

    private void initialize() {
        // Create the background grid
        for (int a = 0; a < 1536; a += tileSize) {
            for (int b = 0; b < 800; b += tileSize) {
                Rectangle tideRectangle = new Rectangle(a, b, tileSize, tileSize);
                tideRectangle.setFill(new ImagePattern(Images.IMAGE_TILE_x4));
                pane.getChildren().add(tideRectangle);
            }
        }

        showGrid(grid);
        heroView.setFill(new ImagePattern(Images.IMAGE_PLAYERRIGHT_x4));
        pane.getChildren().add(heroView);
        }

    
    private void showGrid(Grid grid) {
    Image image;
    // Draw the grid tiles
    for (Tile tile : grid.getTileMap()) {
        image = Images.convertCharToImage(Character.toLowerCase(tile.getTileType()));
        if (tile.getTileType() != 'E' && tile.getTileType() != 'R'){
            Rectangle tileRect = new Rectangle(tile.getLeftSide(), (tile.getTopSide()-1), 64+1, 64+1);
            tileRect.setFill(new ImagePattern(image));
            pane.getChildren().add(tileRect);
        }

        }
    }    

    public void updateHeroPosition(double x, double y) {
        heroView.setX(x);
        heroView.setY(y);
    }

    public Pane getPane() {
        return pane;
    }
}