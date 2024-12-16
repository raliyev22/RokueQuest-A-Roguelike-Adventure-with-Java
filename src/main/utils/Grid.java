package main.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javafx.util.Pair;
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

	public boolean indexInRange(int index) {
        return ((index >= 0) && (index <= this.rowLength * this.columnLength - 1));
	}

	public boolean indexInRange(int x, int y) {
        return ((x >= 0) && (x <= this.rowLength - 1) && (y >= 0) && (y <= this.columnLength - 1));
	}
	
	public Tile findTileWithIndex(int x, int y) {
		if (indexInRange(x, y)) {
			return tileMap.get(tileIndex(x, y));
		} else {
			return null;
		}
	}
	
	public void changeTileWithIndex(int x, int y, char c) {
		if (indexInRange(tileIndex(x, y))) {
			Tile currentTile = findTileWithIndex(x, y);
			currentTile.changeTileType(c);
		}
	}

	public Pair<Integer, Integer> findCoordinatesofTile(int index) {
		int x = index % this.rowLength;
		int y = index / this.rowLength;

		Pair<Integer, Integer> coordinates = new Pair<Integer,Integer>(x, y);
		return coordinates;
	}

	public int findXofTile(Tile tile) {
		int leftSide = tile.leftSide;

		int x = (leftSide - this.bottomLeftXCoordinate) / this.tileWidth;
		return x;
	}

	public int findYofTile(Tile tile) {
		int bottomSide = tile.bottomSide;

		int y = (bottomSide - this.bottomLeftYCoordinate) / this.tileHeight;
		return y;
	}

	public Pair<Integer, Integer> findCoordinatesofTile(Tile tile) {
		int x = findXofTile(tile);
		int y = findYofTile(tile);

		Pair<Integer, Integer> coordinates = new Pair<Integer,Integer>(x, y);
		return coordinates;
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

	public boolean isTopTile(Tile tile) {
		return ((tile.location.equals(TileLocation.TOP_LEFT)) 
		|| (tile.location.equals(TileLocation.TOP)) 
		|| (tile.location.equals(TileLocation.TOP_RIGHT)));
	}

	public boolean isLeftTile(Tile tile) {
		return ((tile.location.equals(TileLocation.TOP_LEFT)) 
		|| (tile.location.equals(TileLocation.LEFT)) 
		|| (tile.location.equals(TileLocation.BOTTOM_LEFT)));
	}

	public boolean isBottomTile(Tile tile) {
		return ((tile.location.equals(TileLocation.BOTTOM_LEFT)) 
		|| (tile.location.equals(TileLocation.BOTTOM)) 
		|| (tile.location.equals(TileLocation.BOTTOM_RIGHT)));
	}

	public boolean isRightTile(Tile tile) {
		return ((tile.location.equals(TileLocation.TOP_RIGHT)) 
		|| (tile.location.equals(TileLocation.RIGHT)) 
		|| (tile.location.equals(TileLocation.BOTTOM_RIGHT)));
	}

	// Finds the above tile
	public Tile findNorthTile(Tile currentTile) {
		if (isTopTile(currentTile)) {
			return null;
		} else {
			int x = findXofTile(currentTile);
			int y = findYofTile(currentTile);
			int newY = y + 1;

			Tile aboveTile = findTileWithIndex(x, newY);
			return aboveTile;
		}
	}

	// Finds the below tile
	public Tile findSouthTile(Tile currentTile) {
		if (isBottomTile(currentTile)) {
			return null;
		} else {
			int x = findXofTile(currentTile);
			int y = findYofTile(currentTile);
			int newY = y - 1;

			Tile belowTile = findTileWithIndex(x, newY);
			return belowTile;
		}
	}

	// Finds the right tile
	public Tile findEastTile(Tile currentTile) {
		if (isRightTile(currentTile)) {
			return null;
		} else {
			int x = findXofTile(currentTile);
			int y = findYofTile(currentTile);
			int newX = x + 1;

			Tile rightTile = findTileWithIndex(newX, y);
			return rightTile;
		}
	}

	// Finds the left tile
	public Tile findWestTile(Tile currentTile) {
		if (isLeftTile(currentTile)) {
			return null;
		} else {
			int x = findXofTile(currentTile);
			int y = findYofTile(currentTile);
			int newX = x - 1;

			Tile leftTile = findTileWithIndex(newX, y);
			return leftTile;
		}
	}

	// Finds the north, south, east, west tiles.
	public Set<Directions> findAdjacentTiles(Tile tile) {
		HashSet<Directions> adjacentTiles = new HashSet<>();
		
		Tile aboveTile = findNorthTile(tile);
		if (aboveTile != null) {
			adjacentTiles.add(Directions.NORTH);
		}

		Tile belowTile = findSouthTile(tile);
		if (belowTile != null) {
			adjacentTiles.add(Directions.SOUTH);
		}

		Tile rightTile = findEastTile(tile);
		if (rightTile != null) {
			adjacentTiles.add(Directions.EAST);
		}
		
		Tile leftTile = findWestTile(tile);
		if (leftTile != null) {
			adjacentTiles.add(Directions.WEST);
		}

		return adjacentTiles;
	}

	public Set<Directions> findAdjacentTilesWithIndex(int x, int y) {
		Tile currentTile = findTileWithIndex(x, y);
		
		return findAdjacentTiles(currentTile);
	}

	public List<Tile> getTileMap() {
		return this.tileMap;
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
		System.out.println(myGrid.findAdjacentTilesWithIndex(2,0));
		System.out.println(mytile1);
		System.out.println(myGrid.findNorthTile(mytile1));
	}
}
