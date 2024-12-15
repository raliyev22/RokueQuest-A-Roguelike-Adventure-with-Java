package main.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import main.model.Directions;

// This class creates tile-maps. Maybe it will have utility functions too
public class Grid {
	protected final int rowLength; // How many tiles are there within a single row
	protected final int columnLength; // How many tiles are there within a single column
	protected final int tileWidth;
	protected final int tileHeight;
	// We will create the tile map starting from bottom left
	protected final int bottomLeftXCoordinate;
	protected final int bottomLeftYCoordinate;
	protected List<Tile> tileMap;
	
	public Grid(int rowLength, int columnLength, int tileWidth, int tileHeight, 
	int bottomLeftXCoordinate, int bottomLeftYCoordinate) {
		super();
		if ((rowLength <= 0) || (columnLength <= 0) || (tileWidth <= 0) || (tileHeight <= 0))
			System.err.println("Non-positive number while creating tile map.");
		
		this.rowLength = rowLength;
		this.columnLength = columnLength;
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
		this.bottomLeftXCoordinate = bottomLeftXCoordinate;
		this.bottomLeftYCoordinate = bottomLeftYCoordinate;
		this.tileMap = createTileMap(rowLength, columnLength, 
		tileWidth, tileHeight, bottomLeftXCoordinate, bottomLeftYCoordinate);
	}

	public boolean indexInRange(int rowLength, int columnLength, int index) {
        return ((index >= 0) && (index <= rowLength * columnLength - 1));
	}

	public TileLocation findTileLocation(int rowLength, int columnLength, int index) {
		if (!indexInRange(rowLength, columnLength, index)) {
			System.err.println("Index not in range for findTileLocation");
			return null;
		}
			
		if (index == 0)
			return TileLocation.BOTTOM_LEFT;
		
		if (index == rowLength - 1)
			return TileLocation.BOTTOM_RIGHT;
		
		if (index == rowLength * (columnLength - 1))
			return TileLocation.TOP_LEFT;
		
		if (index == rowLength * columnLength - 1)
			return TileLocation.TOP_RIGHT;
		
		if ((0 < index) && (index < rowLength - 1))
			return TileLocation.BOTTOM;
			
		if ((rowLength * (columnLength - 1) < index) && (index < rowLength * columnLength - 1))
			return TileLocation.TOP;
		
		if (index % rowLength == 0)
			return TileLocation.LEFT;
		
		if ((index + 1) % rowLength == 0)
			return TileLocation.RIGHT;

		return TileLocation.INSIDE;
	}
	
	// Creating a tile map using other variables
	private List<Tile> createTileMap(int rowLength, int columnLength, 
	int tileWidth, int tileHeight, int bottomLeftXCoordinate, int bottomLeftYCoordinate) {
		
		int capacity = rowLength * columnLength;
		ArrayList<Tile> tileM = new ArrayList<>(capacity);
		
		int leftSide = bottomLeftXCoordinate;
		int bottomSide = bottomLeftYCoordinate;
		int rightSide = leftSide + tileWidth;
		int topSide = bottomSide + tileHeight; 
		
		for (int index = 0; index < capacity; index++) {
			TileLocation currTileLocation = findTileLocation(rowLength, columnLength, index);
			Tile currentTile = new Tile(leftSide, rightSide, topSide, bottomSide, currTileLocation);
			tileM.add(index, currentTile);
			// System.out.println(String.format("%d: %s\n", index, currentTile));
			
			// Find the next positions of every side
			// If we are at the end of the row, go up 1 and start from left
			if ((index + 1) % rowLength == 0) {
				leftSide = bottomLeftXCoordinate;
				bottomSide = bottomSide + tileHeight;
			}
			// Otherwise just go 1 tile to the right
			else {
				leftSide = leftSide + tileWidth;
			}
			rightSide = leftSide + tileWidth;
			topSide = bottomSide + tileHeight;
		}
		
		return tileM;
	}
	
	// For a grid like below, tileIndex(1, 2) returns 7:
	// 09 10 11
	// 06 07 08
	// 03 04 05
	// 00 01 02
	public int tileIndex(int x, int y) {
		return y * rowLength + x; // Uses 0 indexing
	}
	
	public Tile findTileWithIndex(int x, int y) {
		return tileMap.get(tileIndex(x, y));
	}
	
	public void changeTileWithIndex(int x, int y, char c) {
		Tile currentTile = findTileWithIndex(x, y);
		currentTile.changeTileType(c);
	}
	
	// Return available directions of a tile, for example we cannot go left or down
	// from the bottom left tile, so this function would return {NORTH, EAST}
	public Set<Directions> availableDirections(int x, int y) {
		HashSet<Directions> dirs = new HashSet<>();
		Tile currentTile = findTileWithIndex(x, y);
		
		switch (currentTile.location) { // Have you seen a code that is this elegantly written?
			case TileLocation.BOTTOM_LEFT -> {
				dirs.add(Directions.NORTH);
				dirs.add(Directions.EAST);
			}
			case TileLocation.BOTTOM -> {
				dirs.add(Directions.NORTH);
				dirs.add(Directions.WEST);
				dirs.add(Directions.EAST);
			}
			case TileLocation.BOTTOM_RIGHT -> {
				dirs.add(Directions.NORTH);
				dirs.add(Directions.WEST);
			}
			case TileLocation.LEFT -> {
				dirs.add(Directions.NORTH);
				dirs.add(Directions.EAST);
				dirs.add(Directions.SOUTH);
			}
			case TileLocation.INSIDE -> {
				dirs.add(Directions.NORTH);
				dirs.add(Directions.WEST);
				dirs.add(Directions.SOUTH);
				dirs.add(Directions.EAST);
			}
			case TileLocation.RIGHT -> {
				dirs.add(Directions.NORTH);
				dirs.add(Directions.WEST);
				dirs.add(Directions.SOUTH);
			}
			case TileLocation.TOP_LEFT -> {
				dirs.add(Directions.SOUTH);
				dirs.add(Directions.EAST);
			}
			case TileLocation.TOP -> {
				dirs.add(Directions.EAST);
				dirs.add(Directions.SOUTH);
				dirs.add(Directions.WEST);
			}
			case TileLocation.TOP_RIGHT -> {
				dirs.add(Directions.SOUTH);
				dirs.add(Directions.WEST);
			}
			default -> throw new AssertionError();
		}
		return dirs;
	}

	public Tile findNorthTile(Tile currentTile) { //TODO
		if ((currentTile.location.equals(TileLocation.TOP_LEFT)) 
		|| (currentTile.location.equals(TileLocation.TOP)) 
		|| (currentTile.location.equals(TileLocation.TOP_RIGHT))) {
			return null;
		}

		
	}
	// Finds the north, south, east, west tiles.
	public Set<Integer> findAdjacentTilesWithIndex(int row, int column) { //TODO
		HashSet<Integer> adjacentTiles = new HashSet<>();
		
		return adjacentTiles;
	}
	
	@Override
	public String toString() {
		String str = "TileMap:\n";
		for (int i = this.columnLength - 1; i >= 0; i--) {
			for (int j = 0; j < this.rowLength; j++) {
				int index = i * this.rowLength + j;
				Tile printTile = tileMap.get(index);
				char onTile = printTile.tileType;
				String add = "";
				if (index < 10)
					add += "0";
				add += String.valueOf(index) + " " + onTile;
				str += add;
				if (j < this.rowLength - 1)
					str+= ", ";
			}
			str += "\n";
		}
		return str;
	}
	
	public static void main(String[] args) {
		Grid myGrid = new Grid(3, 5, 20, 10, 5, 15);
		System.out.println(myGrid);

		Tile mytile0 = myGrid.findTileWithIndex(1,1);
		System.out.println(mytile0);
		Tile mytile1 = myGrid.findTileWithIndex(1,2);
		System.out.println(mytile1);
		Tile mytile2 = myGrid.findTileWithIndex(2,1);
		System.out.println(mytile2);
		
		mytile1.changeTileType('A');
		System.out.println(mytile1);
		System.out.println(myGrid);
	}
}
