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
	// We will create the tile map starting from top left
	protected final int topLeftXCoordinate;
	protected final int topLeftYCoordinate;
	protected List<Tile> tileMap;
	
	public Grid(int rowLength, int columnLength, int tileWidth, int tileHeight, 
	int topLeftXCoordinate, int topLeftYCoordinate) {
		super();
		if ((rowLength <= 0) || (columnLength <= 0) || (tileWidth <= 0) || (tileHeight <= 0))
			System.err.println("Non-positive number while creating tile map.");
		
		this.rowLength = rowLength;
		this.columnLength = columnLength;
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
		this.topLeftXCoordinate = topLeftXCoordinate;
		this.topLeftYCoordinate = topLeftYCoordinate;
		this.tileMap = createTileMap(rowLength, columnLength, 
		tileWidth, tileHeight, topLeftXCoordinate, topLeftYCoordinate);
	}
	
	// Creating a tile map using other variables
	private List<Tile> createTileMap(int rowLength, int columnLength, 
	int tileWidth, int tileHeight, int topLeftXCoordinate, int topLeftYCoordinate) {
		
		int capacity = rowLength * columnLength;
		ArrayList<Tile> tileM = new ArrayList<>(capacity);
		
		int leftSide = topLeftXCoordinate;
		int topSide = topLeftYCoordinate; 
		int rightSide = leftSide + tileWidth;
		int bottomSide = topLeftYCoordinate + tileHeight;
		
		for (int index = 0; index < capacity; index++) {
			Tile currentTile = new Tile(leftSide, rightSide, topSide, bottomSide);
			tileM.add(index, currentTile);
			// System.out.println(String.format("%d: %s\n", index, currentTile));
			
			// Find the next positions of every side
			// If we are at the end of the row, go up 1 and start from left
			if ((index + 1) % rowLength == 0) {
				leftSide = topLeftXCoordinate;
				topSide = topSide + tileHeight;
			}
			// Otherwise just go 1 tile to the right
			else {
				leftSide = leftSide + tileWidth;
			}
			rightSide = leftSide + tileWidth;
			bottomSide = topSide + tileHeight;
		}
		
		return tileM;
	}
	
	// For a grid like below, tileIndex(1, 2) returns 7:
	// 00 01 02
	// 03 04 05
	// 06 07 08
	// 09 10 11
	public int tileIndex(int x, int y) {
		return y * this.rowLength + x; // Uses 0 indexing
	}

	public boolean indexInRange(int index) {
        return ((index >= 0) && (index < this.rowLength * this.columnLength));
	}

	public boolean indexInRange(int x, int y) {
        return ((x >= 0) && (x < this.rowLength) && (y >= 0) && (y < this.columnLength));
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

		int x = (leftSide - this.topLeftXCoordinate) / this.tileWidth;
		return x;
	}

	public int findYofTile(Tile tile) {
		int topSide = tile.topSide;

		int y = (topSide - this.topLeftYCoordinate) / this.tileHeight;
		return y;
	}

	public Pair<Integer, Integer> findCoordinatesofTile(Tile tile) {
		int x = findXofTile(tile);
		int y = findYofTile(tile);

		Pair<Integer, Integer> coordinates = new Pair<Integer,Integer>(x, y);
		return coordinates;
	}

	public boolean isTopTile(Tile tile) {
		int y = findYofTile(tile);

		return (y == 0);
	}

	public boolean isLeftTile(Tile tile) {
		int x = findXofTile(tile);

		return (x == 0);
	}

	public boolean isBottomTile(Tile tile) {
		int y = findYofTile(tile);

		return (y == this.columnLength - 1);
	}

	public boolean isRightTile(Tile tile) {
		int x = findXofTile(tile);

		return (x == this.rowLength - 1);
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

	// Finds which directions we can go to, only considering out of bounds.
	public Set<Directions> findAvailableDirections(Tile tile) {
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

	public Set<Directions> findAvailableDirectionsWithIndex(int x, int y) {
		Tile currentTile = findTileWithIndex(x, y);
		
		return findAvailableDirections(currentTile);
	}

	public Set<Tile> findAdjacentTiles(Tile tile) {
		int x = findXofTile(tile);
		int y = findYofTile(tile);

		return findAdjacentTilesWithIndex(x, y);
	}

	public Set<Tile> findAdjacentTilesWithIndex(int x, int y) {
		HashSet<Tile> adjacentTiles = new HashSet<>();

		if (y > 0) {
			Tile aboveTile = findTileWithIndex(x, y - 1);
			adjacentTiles.add(aboveTile);
		}
		if (y + 1 < this.columnLength) {
			Tile belowTile = findTileWithIndex(x, y + 1);
			adjacentTiles.add(belowTile);
		}
		if (x > 0) {
			Tile rightTile = findTileWithIndex(x - 1, y);
			adjacentTiles.add(rightTile);
		}
		if (x + 1 < this.rowLength) {
			Tile leftTile = findTileWithIndex(x + 1, y);
			adjacentTiles.add(leftTile);
		}

		return adjacentTiles;
	}

	// For coordinates x,y check if they are inside the grid or not
	public boolean coordinatesAreInGrid(double x, double y) {
		return ((x >= this.topLeftXCoordinate) 
		&& (y >= this.topLeftYCoordinate)
		&& (x < this.topLeftXCoordinate + this.rowLength * this.tileWidth)
		&& (y < this.topLeftYCoordinate + this.columnLength * this.tileHeight));
	}

	// For coordinates x,y check which tile it is in
	public Tile findTileUsingCoordinates(double x, double y) {
		if (!coordinatesAreInGrid(x, y)) {
			return null;
		}

		double tileX = Math.floor((x - this.topLeftXCoordinate) / this.tileWidth);
		double tileY = Math.floor((y - this.topLeftYCoordinate) / this.tileHeight);

		return findTileWithIndex((int) tileX, (int) tileY);
	}


	public List<Tile> getTileMap() {
		return this.tileMap;
	}
	
	@Override
	public String toString() {
		String str = "TileMap:\n";
		for (int i = 0; i < this.columnLength; i++) {
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
		Grid myGrid = new Grid(3, 5, 20, 10, 3, 11);
		System.out.println(myGrid);

		Tile mytile0 = myGrid.findTileWithIndex(2,4);
		System.out.println(mytile0);

		System.out.println(myGrid.coordinatesAreInGrid(62.99999999999999, 60.99999999));
		System.out.println(myGrid.findTileUsingCoordinates(62.99999999999999, 60.99999999));
		/*
		Tile mytile1 = myGrid.findTileWithIndex(1,2);
		System.out.println(mytile1);
		Tile mytile2 = myGrid.findTileWithIndex(2,1);
		System.out.println(mytile2);
		
		mytile1.changeTileType('A');
		System.out.println(mytile1);
		System.out.println(myGrid);
		System.out.println(myGrid.findAdjacentTilesWithIndex(1,0));
		System.out.println(mytile1);
		System.out.println(myGrid.findNorthTile(mytile1));
		System.out.println(myGrid.findSouthTile(mytile1));
		*/
	}
}
