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

    }

    public void createWizardMonster() {
        Monster monster = new WizardMonster(1,1);
        monsters.add(monster);
        playModeGrid.changeTileWithIndex(monster.getX(),monster.getY(),monster.getType());
    }
    public String toString() {
        return playModeGrid.toString();
    }
    public static void main(String[] args) {
        PlayModeViewGrid playGrid = new PlayModeViewGrid();
        System.out.println(playGrid.toString());
    }
}
