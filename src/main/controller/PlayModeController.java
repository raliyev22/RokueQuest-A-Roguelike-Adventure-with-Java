package main.controller;

import java.security.SecureRandom;
import java.util.ArrayList;
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

	protected final Grid earthHall;
	protected final Grid airHall;
	protected final Grid waterHall;
	protected final Grid fireHall;
    
    protected Grid playModeGrid;
    protected Hero hero;
    protected ArrayList<Monster> monsters;
	protected HallType hallType;
	
    protected int time;
	


    public PlayModeController(Grid earthHall, Grid airHall, Grid waterHall, Grid fireHall) {
		this.earthHall = earthHall;
		this.airHall = airHall;
		this.waterHall = waterHall;
		this.fireHall = fireHall;

        playModeGrid = new Grid(ROW, COLUMN, tileWidth, tileHeight, topLeftXCoordinate, topLeftYCoordinate);
		hallType = HallType.EARTH;
		playModeGrid.copyTileMap(earthHall);

		Tile initialHeroTile = getRandomEmptyTile();
		int randomXCoordinate = playModeGrid.findXofTile(initialHeroTile);
		int randomYCoordinate = playModeGrid.findXofTile(initialHeroTile);
        hero = initializeHero(randomXCoordinate, randomYCoordinate);

        monsters = new ArrayList<>();
    }
    
    public Hero initializeHero(int xCoordinate, int yCoordinate) {
        Hero hero = new Hero(xCoordinate, yCoordinate);
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

	public Tile getRandomEmptyTile() {
		SecureRandom rng = new SecureRandom();
		ArrayList<Tile> emptyTiles = getEmptyTiles();

		int luckyTileInd = rng.nextInt(emptyTiles.size());

		return emptyTiles.get(luckyTileInd);
	}

	public ArrayList<Tile> getEmptyTiles() {
		ArrayList<Tile> emptyTiles = new ArrayList<>();

		for (Tile tile: playModeGrid.getTileMap()) {
			if (isEmptyTile(tile.getTileType())){
				emptyTiles.add(tile);
			}
		}

		return emptyTiles;
	}

	public boolean isEmptyTile(char c) {
		if (c == 'E' || c == 'e') {
			return true;
		}
		return false;
	}
    
    public Grid updateGrid() {
        this.playModeGrid = new Grid(ROW, COLUMN, tileWidth, tileHeight, topLeftXCoordinate, topLeftYCoordinate);
        for (Monster monster : monsters) {
            this.playModeGrid.changeTileWithIndex(monster.getX(), monster.getY(), monster.getCharType());
        }
        this.playModeGrid.changeTileWithIndex(hero.getPosX(), hero.getPosY(), hero.getCharType());
        return this.playModeGrid;
    }

	public Grid getPlayModeGrid() {
		return this.playModeGrid;
	}

    public static void main(String[] args) {
        PlayModeController playGrid = new PlayModeController(null,null,null,null);
        Hero hero = playGrid.hero;
        System.out.println(playGrid.playModeGrid.toString());
        hero.move(Directions.NORTH);
        playGrid.updateGrid();
        System.out.println(playGrid.playModeGrid.toString());
    }
}

