package main.controller;

import java.lang.classfile.instruction.ThrowInstruction;
import java.security.SecureRandom;
import java.util.ArrayList;

import javafx.util.Pair;
import main.model.*;
import main.utils.*;

public class PlayModeController {
	protected final int ROW = 10;
	protected final int COLUMN = 9;
	protected final int tileWidth = 32;
	protected final int tileHeight = 32;
	protected final int topLeftXCoordinate = 10;
	protected final int topLeftYCoordinate = 40;
	//public static final SecureRandom rng = new SecureRandom();
	
	protected final Grid earthHall;
	protected final Grid airHall;
	protected final Grid waterHall;
	protected final Grid fireHall;
	
	public Grid playModeGrid;
	protected Hero hero;
	protected static ArrayList<Monster> monsters;
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

	public boolean isNearHero(Tile otherTile, int n) {
		int otherTileX = playModeGrid.findXofTile(otherTile);
		int otherTileY = playModeGrid.findYofTile(otherTile);
		double euclideanDistance = 
		Math.sqrt((hero.getPosX() - otherTileX) * (hero.getPosX() - otherTileX) + 
				  (hero.getPosY() - otherTileY) * (hero.getPosY() - otherTileY));

		return (euclideanDistance <= n);
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
			if (isEmptyTileType(tile.getTileType())){
				emptyTiles.add(tile);
			}
		}
		
		return emptyTiles;
	}
	
	public boolean isEmptyTileType(char c) {
		if (c == 'E' || c == 'e') {
			return true;
		}
		return false;
	}
	
	public boolean isWalkableTileType(char c) {
		if (c == 'E' || c == 'e') {
			return true;
		}
		return false;
	}
	
	public boolean checkHeroDirection(Directions dir) {
		boolean canWalk = false;
		Tile heroTile = playModeGrid.findTileWithIndex(hero.getPosX(), hero.getPosY());

		switch (dir) {
			case Directions.NORTH -> {
				Tile aboveTile = playModeGrid.findNorthTile(heroTile);
				if (aboveTile != null) {
					if (isWalkableTileType(aboveTile.getTileType())) {
						canWalk = true;
						return true;
					}
				}
			}
			case Directions.EAST -> {
				Tile rightTile = playModeGrid.findEastTile(heroTile);
				if (rightTile != null) {
					if (isWalkableTileType(rightTile.getTileType())) {
						canWalk = true;
						return true;
					}
				}
			}
			case Directions.SOUTH -> {
				Tile belowTile = playModeGrid.findSouthTile(heroTile);
				if (belowTile != null) {
					if (isWalkableTileType(belowTile.getTileType())) {
						canWalk = true;
						return true;
					}
				}
			}
			case Directions.WEST -> {
				Tile leftTile = playModeGrid.findWestTile(heroTile);
				if (leftTile != null) {
					if (isWalkableTileType(leftTile.getTileType())) {
						canWalk = true;
						return true;
					}
				}
			}
			default -> throw new AssertionError();
		}

		return canWalk;
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

	public int getTopLeftXCoordinate() {
		return this.topLeftXCoordinate;
	}

	public int getTopLeftYCoordinate() {
		return this.topLeftYCoordinate;
	}
	
	public static void main(String[] args) {
		PlayModeController playGrid = new PlayModeController(null,null,null,null);
		Hero hero = playGrid.hero;
		System.out.println(playGrid.playModeGrid.toString());
		/*
		playGrid.updateGrid();
		System.out.println(playGrid.playModeGrid.toString());
		*/
		Tile tile = playGrid.getRandomEmptyTile();
		System.out.println(hero.getPosX());
		System.out.println(hero.getPosY());
		System.out.println(playGrid.playModeGrid.findXofTile(tile));
		System.out.println(playGrid.playModeGrid.findYofTile(tile));
		System.out.println(playGrid.isNearHero(tile, 4));



		long startTime = System.currentTimeMillis();
		playGrid.createMonster(0,0, MonsterType.FIGHTER);
		Monster monster = monsters.get(0);
		
		while(System.currentTimeMillis()-startTime < 5000){
			System.out.println(System.currentTimeMillis());
			monster.moveRandomly(playGrid.playModeGrid);
			System.err.println(playGrid.playModeGrid.toString());
			try {
                Thread.sleep(1000); 
            } catch (InterruptedException e) {
                System.err.println("Sleep interrupted!");
				
            }
		}
	}
}

