package main.view;

import java.util.ArrayList;
import main.model.*;
import main.utils.*;

public class PlayModeViewGrid {
    protected final int ROW = 8;
    protected final int COLUMN = 8;
    protected final int tileWidth = 10;
    protected final int tileHeight = 10;
    
    protected Grid playModeGrid = new Grid(ROW,COLUMN, tileWidth, tileHeight,0,0);
    protected Hero player;
    protected ArrayList<Monster> monsters;


    public PlayModeViewGrid() {
        player = new Hero(400,300, PLAYER_SIZE, PLAYER_IMAGE);
        monsters = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Monster monster = new Monster(200 + i*100, 200 + i*100, MONSTER_SIZE, MONSTER_IMAGE);
            monsters.add(monster);
        }
        playModeGrid.changeTileWithIndex(2, 2, 'P');
        playModeGrid.changeTileWithIndex(3, 3, 'M');
        playModeGrid.changeTileWithIndex(4, 4, 'M');
        playModeGrid.changeTileWithIndex(5, 5, 'M');
        playModeGrid.changeTileWithIndex(6, 6, 'M');
    }
    public String toString() {
        return playModeGrid.toString();
    }
    public static void main(String[] args) {
        PlayModeViewGrid playGrid = new PlayModeViewGrid();
        System.out.println(playGrid.toString());
    }
}
