package main.utils;

import java.awt.font.NumericShaper;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javafx.util.Pair;
import main.controller.PlayModeController;
import main.model.Directions;

// This class creates tile-maps. Maybe it will have utility functions too
public class Grid {
	protected final int rowLength; // How many tiles are there within a single row
	protected final int columnLength; // How many tiles are there within a single column
	protected final int tileWidth;
	protected final int tileHeight;
	// We will create the tile map starting from top left
	public int topLeftXCoordinate;
	public int topLeftYCoordinate;
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

	public void copyTileMap(Grid otherGrid) {
		if (otherGrid == null) {
			return ;
		}
		if (this.rowLength != otherGrid.rowLength) {
			return ;
		}
		if (this.columnLength != otherGrid.columnLength) {
			return ;
		}

		for (int i = 0; i < this.tileMap.size(); i++) {
			char otherChar = otherGrid.tileMap.get(i).tileType;
			if (otherChar == '!') {
				otherChar = 'E';
			}
			this.tileMap.get(i).changeTileType(otherChar);
		}
	}
	
	public void setTopLeftXCordinate(int topLeftXCoordinate){
		this.topLeftXCoordinate = topLeftXCoordinate;
	}

	public void setTopLeftYCordinate(int topLeftYCoordinate){
		this.topLeftYCoordinate = topLeftYCoordinate;
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

	public boolean twoTilesAreNeighbours(Tile firstTile, Tile secondTile) {
		int firstTileX = findXofTile(firstTile);
		int firstTileY = findYofTile(firstTile);
		int secondTileX = findXofTile(secondTile);
		int secondTileY = findYofTile(secondTile);
		if (((firstTileX - secondTileX) <= 1) && ((firstTileY - secondTileY) <= 1)) {
			return true;
		}
		return false;
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
			int newY = y - 1;

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
			int newY = y + 1;

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

    // Finds walkable directions
    public List<Directions> findWalkableDirections(Tile tile) {
        ArrayList<Directions> walkableDirections = new ArrayList<>();
		
		Tile aboveTile = findNorthTile(tile);
		if (aboveTile != null) {
            if (isWalkableTile(aboveTile)) {
                walkableDirections.add(Directions.NORTH);
            }
		}

		Tile belowTile = findSouthTile(tile);
		if (belowTile != null) {
            if (isWalkableTile(belowTile)) {
			    walkableDirections.add(Directions.SOUTH);
            }
		}

		Tile rightTile = findEastTile(tile);
		if (rightTile != null) {
            if (isWalkableTile(rightTile)) {
			    walkableDirections.add(Directions.EAST);
            }
		}
		
		Tile leftTile = findWestTile(tile);
		if (leftTile != null) {
            if (isWalkableTile(leftTile)) {
			    walkableDirections.add(Directions.WEST);
            }
		}

		return walkableDirections;
    }

	// Finds which directions we can go to, only considering out of bounds.
	public Set<Directions> findAvailableDirections(Tile tile) {
		HashSet<Directions> availableDirections = new HashSet<>();
		
		Tile aboveTile = findNorthTile(tile);
		if (aboveTile != null) {
			availableDirections.add(Directions.NORTH);
		}

		Tile belowTile = findSouthTile(tile);
		if (belowTile != null) {
			availableDirections.add(Directions.SOUTH);
		}

		Tile rightTile = findEastTile(tile);
		if (rightTile != null) {
			availableDirections.add(Directions.EAST);
		}
		
		Tile leftTile = findWestTile(tile);
		if (leftTile != null) {
			availableDirections.add(Directions.WEST);
		}

		return availableDirections;
	}

	public Set<Directions> findAvailableDirectionsWithIndex(int x, int y) {
		Tile currentTile = findTileWithIndex(x, y);
		
		return findAvailableDirections(currentTile);
	}

	// Find adjacent tiles, i.e. tiles with connected edge. So this returns 4 tiles, plus shaped.
	public Set<Tile> findAdjacentTiles(Tile tile) {
		int x = findXofTile(tile);
		int y = findYofTile(tile);

		return findAdjacentTilesWithIndex(x, y);
	}

	// Find adjacent tiles, i.e. tiles with connected edge. So this returns 4 tiles, plus shaped.
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
			Tile leftTile = findTileWithIndex(x - 1, y);
			adjacentTiles.add(leftTile);
		}
		if (x + 1 < this.rowLength) {
			Tile rightTile = findTileWithIndex(x + 1, y);
			adjacentTiles.add(rightTile);
		}

		return adjacentTiles;
	}

	//Find neighbouring tiles, i.e. tiles with common point. Returns 8 tiles for a tile in the middle.
	public Set<Tile> findNeighbouringTiles(Tile tile) {
		int x = findXofTile(tile);
		int y = findYofTile(tile);

		return findNeighbouringTilesWithIndex(x, y);
	}

	//Find neighbouring tiles, i.e. tiles with common point. Returns 8 tiles for a tile in the middle.
	public Set<Tile> findNeighbouringTilesWithIndex(int x, int y) {
		HashSet<Tile> neighbouringTiles = new HashSet<>();

		if (y > 0) {
			Tile aboveTile = findTileWithIndex(x, y - 1);
			neighbouringTiles.add(aboveTile);
			if (x > 0) {
				Tile aboveLeftTile = findTileWithIndex(x - 1, y - 1);
				neighbouringTiles.add(aboveLeftTile);
			}
			if (x + 1 < this.rowLength) {
				Tile aboveRightTile = findTileWithIndex(x + 1, y - 1);
				neighbouringTiles.add(aboveRightTile);
			}
		}
		if (y + 1 < this.columnLength) {
			Tile belowTile = findTileWithIndex(x, y + 1);
			neighbouringTiles.add(belowTile);

			if (x > 0) {
				Tile belowLeftTile = findTileWithIndex(x - 1, y + 1);
				neighbouringTiles.add(belowLeftTile);
			}
			if (x + 1 < this.rowLength) {
				Tile belowRightTile = findTileWithIndex(x + 1, y + 1);
				neighbouringTiles.add(belowRightTile);
			}
		}
		if (x > 0) {
			Tile leftTile = findTileWithIndex(x - 1, y);
			neighbouringTiles.add(leftTile);
		}
		if (x + 1 < this.rowLength) {
			Tile rightTile = findTileWithIndex(x + 1, y);
			neighbouringTiles.add(rightTile);
		}

		return neighbouringTiles;
	}

	// Find a random NxN square that contains the tile. Useful for Reveal Enchantment.
	public Set<Tile> findNxNSquare(Tile tile, int N) {
		int x = findXofTile(tile);
		int y = findYofTile(tile);

		return findNxNSquareWithIndex(x, y, N);
	}

	// Find a random NxN square that contains the tile. Useful for Reveal Enchantment.
	public Set<Tile> findNxNSquareWithIndex(int x, int y, int N) {
		if ((N > this.rowLength) || (N > this.columnLength)) {
			Set<Tile> foo = new HashSet<>(this.tileMap);
			return foo;
		}

		SecureRandom rng = new SecureRandom();

		// The square needs to be within these bounds
		int leftStart = Math.max(x - N + 1, 0);
		int leftEnd = Math.min(x, this.rowLength - N);

		int topStart = Math.max(y - N + 1, 0);
		int topEnd = Math.min(y, this.columnLength - N);

		// int possibleSquaresCount = (leftEnd - leftStart) * (topEnd - topStart);

		int luckySquareX = rng.nextInt(leftStart, leftEnd + 1);
		int luckySquareY = rng.nextInt(topStart, topEnd + 1);

		HashSet<Tile> square = (HashSet<Tile>) constructRectangleOfTiles
		(luckySquareX, luckySquareX + N - 1, luckySquareY, luckySquareY + N - 1);

		return square;
	}

	public Set<Tile> constructRectangleOfTiles(int left, int right, int top, int bottom)  {
		HashSet<Tile> rectangle = new HashSet<>();
		for (int x = left; x <= right; x++) {
			for (int y = top; y <= bottom; y++) {
				Tile currTile = findTileWithIndex(x, y);

				rectangle.add(currTile);
			}
		}

		return rectangle;
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

    public Tile getRandomEmptyTile() {
        SecureRandom rng = new SecureRandom();
        ArrayList<Tile> emptyTiles = getEmptyTiles();
        
        int luckyTileInd = rng.nextInt(emptyTiles.size());
        
        return emptyTiles.get(luckyTileInd);
    }

    public ArrayList<Tile> getEmptyTiles() {
        ArrayList<Tile> emptyTiles = new ArrayList<>();
        
        for (Tile tile: this.getTileMap()) {
            if (isEmptyTileType(tile.getTileType())){
                emptyTiles.add(tile);
            }
        }
        
        return emptyTiles;
    }

    public static boolean isWalkableTile(Tile tile){
        if(tile == null){
            return false;
        }
        return isWalkableTileType(tile.getTileType());
    }
    
    public static boolean isWalkableTileType(char c) {
        if (c == 'E' || c == 'e') {
            return true;
        }
        return false;
    }

    public static boolean isEmptyTileType(char c) {
        if (c == 'E' || c == 'e') {
            return true;
        }
        return false;
    }

	public List<Tile> getTileMap() {
		return this.tileMap;
	}

    public static boolean isHallObjectTile(Tile tile) {
        return isHallObjectTileType(tile.getTileType());
    }
    
    public static boolean isHallObjectTileType(char c) {
        if (c == 'B' || c == 'C' || c == 'D' || c == 'G' || c == 'H'
        || c == 'J' || c == 'K' || c == 'M' || c == 'P' || c == 'S' || c == 'T'){
            return true;
        }
        return false;
    }
    
    public ArrayList<Tile> getHallObjectTiles() {
        ArrayList<Tile> hallObjectTiles = new ArrayList<>();
        
        for (Tile tile: this.getTileMap()) {
            if (isHallObjectTile(tile)){
                hallObjectTiles.add(tile);
            }
        }
        
        return hallObjectTiles;
    }

    public int getTileHeight() {
        return tileHeight;
    }

    public int getTileWidth() {
        return tileWidth;
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
		Grid myGrid = new Grid(8, 9, 20, 10, 3, 11);
		System.out.println(myGrid);

		Tile mytile0 = myGrid.findTileWithIndex(2,4);

		System.out.println("---------------------");

		for (Tile elem : myGrid.findNxNSquare(mytile0, 2)) {
			System.out.println(myGrid.findCoordinatesofTile(elem));
		}
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
