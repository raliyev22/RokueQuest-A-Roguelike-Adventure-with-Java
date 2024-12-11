package main.view;

import java.util.ArrayList;
import main.model.*;
import main.utils.*;

public class PlayModeViewGrid {
    protected final int ROW = 8;
    protected final int COLUMN = 8;
    protected final int tileWidth = 10;
    protected final int tileHeight = 10;
    protected final int bottomLeftXCoordinate = 15;
    protected final int bottomLeftYCoordinate = 15;
    
    protected Grid playModeGrid;
    protected Hero player;
    protected ArrayList<Monster> monsters;


    public PlayModeViewGrid() {
        playModeGrid = new Grid(ROW, COLUMN, tileWidth, tileHeight, bottomLeftXCoordinate, bottomLeftYCoordinate);
        player = new Hero(200, 200, 15, null);
    }

    public void createMonster(int xCoordinate, int yCoordinate) {
        Monster monster = new WizardMonster(xCoordinate,yCoordinate);
        monsters.add(monster);
        playModeGrid.changeTileWithIndex(monster.getX(),monster.getY(),monster.getType());
    }
    public String toString() {
        return playModeGrid.toString();
    }
    public static void main(String[] args) {
        PlayModeViewGrid playGrid = new PlayModeViewGrid();
        playGrid.createMonster(3, 5);
        System.out.println(playGrid.toString());
    }
}
