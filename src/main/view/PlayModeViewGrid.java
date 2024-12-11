package main.view;

import java.awt.Image;
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
        player = initializeHero(2, 4, 10, null);
        monsters = new ArrayList<>();
    }

    public Hero initializeHero(int xCoordinate, int yCoordinate, int size, Image img) {
        Hero hero = new Hero(xCoordinate, yCoordinate, size, null);
        playModeGrid.changeTileWithIndex(hero.getPosX(), hero.getPosY(), hero.getType());
        return hero;
    }

    public Monster createMonster(int xCoordinate, int yCoordinate, MonsterType type) {
        Monster monster = null;
        switch (type) {
            case MonsterType.FIGHTER -> {
                monster = new FighterMonster(xCoordinate,yCoordinate);
            }
            case MonsterType.ARCHER -> {
                monster = new ArcherMonster(xCoordinate,yCoordinate);
            }
            case MonsterType.WIZARD -> {
                monster = new WizardMonster(xCoordinate,yCoordinate);
            }
        }
        monsters.add(monster);
        playModeGrid.changeTileWithIndex(monster.getX(),monster.getY(),monster.getCharType());
        return monster;
    }
    public String toString() {
        return playModeGrid.toString();
    }
    public static void main(String[] args) {
        PlayModeViewGrid playGrid = new PlayModeViewGrid();
        playGrid.createMonster(3, 5, MonsterType.ARCHER);
        System.out.println(playGrid.toString());
    }
}
