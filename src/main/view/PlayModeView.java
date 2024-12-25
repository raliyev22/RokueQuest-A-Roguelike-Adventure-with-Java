// PlayModeView.java
package main.view;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.util.Pair;
import main.model.Hero;
import main.model.Images;
import main.utils.Grid;
import main.utils.Tile;
import test.TiledHall;

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
        // for (int i = 0; i < 10; i++){
        //     for(int j = 0; j < 9 ; j++){
        //         grid.changeTileWithIndex(i, j, 'a');
        //         Rectangle clone = new Rectangle(64,64);
        //         clone.setFill(new ImagePattern(Images.IMAGE_BOX_x4));
        //         clone.setX(i*64);
        //         clone.setY(j*64);
        //         pane.getChildren().add(clone);
        //     }
        // }


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

        System.out.println(grid);
        System.out.println();

        }

    
    private void showGrid(Grid grid) {
    // Calculate grid parameters
    Image image;

    // Draw the grid tiles
    for (Tile tile : grid.getTileMap()) {
        image = Images.convertCharToImage(Character.toLowerCase('a'));
        if (tile.getTileType() == 'R'){
            image = Images.convertCharToImage(Character.toLowerCase('a'));
        }
        
        Rectangle tileRect = new Rectangle(tile.getLeftSide(), (tile.getTopSide()-1), 64+1, 64+1);
        tileRect.setFill(new ImagePattern(image)); // Assign tile image
        pane.getChildren().add(tileRect);
        }
        
    }
        
    // public void setHeroDirection(String direction) {
    //     if (direction.equals("LEFT")) {
    //         hero.setFill(new ImagePattern(Images.IMAGE_PLAYERLEFT_x4));
    //     } else if (direction.equals("RIGHT")) {
    //         hero.setFill(new ImagePattern(Images.IMAGE_PLAYERRIGHT_x4));
    //     }
    // }
    // public void initializeHero() {
    //     hero = new Rectangle(64, 64);
    //     hero.setFill(new ImagePattern(Images.IMAGE_PLAYERRIGHT_x4));
    //     Random rand = new Random();
    //     int randomX = rand.nextInt(9);
    //     int randomY = rand.nextInt( 8);
    //     // hero.setX(randomX * 64);
    //     // hero.setY(randomY * 64);
    //     hero.setX(0);
    //     hero.setY(0);
    //     pane.getChildren().add(hero);
    // }
    

    public void updateHeroPosition(double x, double y) {
        heroView.setX(x);
        heroView.setY(y);
    }

    public Pane getPane() {
        return pane;
    }

    // public Rectangle getHero() {
    //     return hero;
    // }

    public Grid getGrid() {
        return grid;
    }
}