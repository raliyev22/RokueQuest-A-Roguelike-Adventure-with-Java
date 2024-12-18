package main.view;

import java.awt.Image;
import java.util.ArrayList;
import main.model.*;
import main.utils.*;

public class PlayModeController {
    protected final int ROW = 8;
    protected final int COLUMN = 8;
    protected final int tileWidth = 10;
    protected final int tileHeight = 10;
    protected final int bottomLeftXCoordinate = 15;
    protected final int bottomLeftYCoordinate = 15;
	//public static final SecureRandom rng = new SecureRandom();

	protected static Grid earthHall;
	protected static Grid airHall;
	protected static Grid waterHall;
	protected static Grid fireHall;
    
    protected Grid playModeGrid;
    protected Hero player;
    protected ArrayList<Monster> monsters;
    protected int time;


    public PlayModeController() {
        playModeGrid = new Grid(ROW, COLUMN, tileWidth, tileHeight, bottomLeftXCoordinate, bottomLeftYCoordinate);
        player = initializeHero(2, 4, 10, null);
        monsters = new ArrayList<>();
    }
    
    public Hero initializeHero(int xCoordinate, int yCoordinate, int size, Image img) {
        Hero hero = new Hero(xCoordinate, yCoordinate, null);
        playModeGrid.changeTileWithIndex(hero.getPosX(), hero.getPosY(), hero.getCharType());
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
    
    public Grid updateGrid() {
        playModeGrid = new Grid(ROW, COLUMN, tileWidth, tileHeight, bottomLeftXCoordinate, bottomLeftYCoordinate);
        for (Monster monster : monsters) {
            playModeGrid.changeTileWithIndex(monster.getX(), monster.getY(), monster.getCharType());
        }
        playModeGrid.changeTileWithIndex(player.getPosX(), player.getPosY(), player.getCharType());
        return playModeGrid;
    }


    public String toString() {
        return playModeGrid.toString();
    }
    public static void main(String[] args) {
        PlayModeController playGrid = new PlayModeController();
        Hero player = playGrid.player;
        playGrid.createMonster(3, 5, MonsterType.ARCHER);
        playGrid.createMonster(8, 8, MonsterType.ARCHER);
        playGrid.createMonster(7, 6, MonsterType.ARCHER);
        System.out.println(playGrid.toString());
        player.move(Directions.NORTH);
        playGrid.updateGrid();
        System.out.println(playGrid.toString());
    }
}
