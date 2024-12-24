package main.view;

import java.util.ArrayList;

import javafx.scene.image.Image;
import main.model.*;
import main.utils.*;

public class PlayModeController {
    protected final int ROW = 10;
    protected final int COLUMN = 9;
    protected final int tileWidth = 32;
    protected final int tileHeight = 32;
    protected static final int topLeftXCoordinate = 10;
    protected static final int topLeftYCoordinate = 40;
	//public static final SecureRandom rng = new SecureRandom();

	protected static Grid earthHall;
	protected static Grid airHall;
	protected static Grid waterHall;
	protected static Grid fireHall;
    
    protected Grid playModeGrid;
    protected Hero player;
    protected ArrayList<Monster> monsters;
	protected HallType hallType;

    protected int time;
	


    public PlayModeController() {
        playModeGrid = new Grid(ROW, COLUMN, tileWidth, tileHeight, topLeftXCoordinate, topLeftYCoordinate);
		hallType = HallType.EARTH;
		playModeGrid.copyTileMap(earthHall);


        player = initializeHero(2, 4, tileHeight, Images.IMAGE_PLAYERRIGHT_x4);
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
        playModeGrid.changeTileWithIndex(monster.getX(), monster.getY(), monster.getCharType());
        return monster;
    }
    
    public Grid updateGrid() {
        this.playModeGrid = new Grid(ROW, COLUMN, tileWidth, tileHeight, topLeftXCoordinate, topLeftYCoordinate);
        for (Monster monster : monsters) {
            this.playModeGrid.changeTileWithIndex(monster.getX(), monster.getY(), monster.getCharType());
        }
        this.playModeGrid.changeTileWithIndex(player.getPosX(), player.getPosY(), player.getCharType());
        return this.playModeGrid;
    }

	public Grid getPlayModeGrid() {
		return this.playModeGrid;
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
