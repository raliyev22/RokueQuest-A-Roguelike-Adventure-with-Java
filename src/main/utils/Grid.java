package main.utils;
		
import java.util.ArrayList;

// This class creates tile-maps. Maybe it will have utility functions too
public class Grid {
	protected final int rowNumber; // Number of rows
	protected final int columnNumber; // Number of columns
	protected final int tileWidth;
	protected final int tileHeight;
	// We will create the tile map starting from bottom left
	protected final int bottomLeftXCoordinate;
	protected final int bottomLeftYCoordinate;
	protected ArrayList<Tile> tileMap;
	
	public Grid(int rowNumber, int columnNumber, int tileWidth, int tileHeight, 
	int bottomLeftXCoordinate, int bottomLeftYCoordinate) {
		super();
		if ((rowNumber <= 0) || (columnNumber <= 0) || (tileWidth <= 0) || (tileHeight <= 0))
			System.err.println("Non-positive number while creating tile map.");
		this.rowNumber = rowNumber;
		this.columnNumber = columnNumber;
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
		this.bottomLeftXCoordinate = bottomLeftXCoordinate;
		this.bottomLeftYCoordinate = bottomLeftYCoordinate;
		this.tileMap = createTileMap(rowNumber, columnNumber, 
		tileWidth, tileHeight, bottomLeftXCoordinate, bottomLeftYCoordinate);
	}
	
	// Creating a tile map using other variables
	public static ArrayList<Tile> createTileMap(int rowNumber, int columnNumber, 
	int tileWidth, int tileHeight, 
	int bottomLeftXCoordinate, int bottomLeftYCoordinate) {
		
		int capacity = rowNumber * columnNumber;
		ArrayList<Tile> tileMap = new ArrayList<>(capacity);
		
		int leftSide = bottomLeftXCoordinate;
		int bottomSide = bottomLeftYCoordinate;
		int rightSide = leftSide + tileWidth;
		int topSide = bottomSide + tileHeight; 
		
		for (int index = 0; index < capacity; index++) {
			Tile currentTile = new Tile(leftSide, rightSide, topSide, bottomSide);
			tileMap.add(index, currentTile);
			// System.out.println(String.format("%d: %s\n", index, currentTile));
			
			// Find the next positions of every side
			// If we are at the end of the row, go up 1 and start from left
			if ((index + 1) % columnNumber == 0) {
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
		
		return tileMap;
	}
	
	public Tile findTileWithIndex(int row, int column) {
		return tileMap.get((row - 1) * columnNumber + column - 1);
	}

	public void changeTileWithIndex(int xCoordinate, int yCoordinate, char c) {
		Tile currentTile = findTileWithIndex(yCoordinate, xCoordinate);
        currentTile.tileType = c;
	}
	
	
	@Override
	public String toString() {
		String str = "TileMap:\n";
		for (int i = this.rowNumber - 1; i >= 0; i--) {
			for (int j = 0; j < this.columnNumber; j++) {
				int index = i * this.columnNumber + j;
				Tile printTile = tileMap.get(index);
				char onTile = printTile.tileType;
				String add = "";
				if (index < 10)
				    add += "0";
				add += String.valueOf(index) + " " + onTile;
				str += add;
				if (j < this.columnNumber - 1)
					str+= ", ";
			}
			str += "\n";
		}
		return str;
	}
	
	public static void main(String[] args) {
		Grid myGrid = new Grid(3, 5, 20, 10, 5, 15);
		System.out.println(myGrid);
		Tile mytile = myGrid.findTileWithIndex(1,1);
		System.out.println(mytile);
	}
}
