package main.utils;

import java.util.ArrayList;

// This class creates tile-maps. Maybe it will have utility functions too
public class Grid {
	protected int rowNumber; // Number of rows
	protected int columnNumber; // Number of columns
	protected int tileWidth;
	protected int tileHeight;
	// We will create the tile map starting from bottom left
	protected int bottomLeftXCoordinate;
	protected int bottomLeftYCoordinate;
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
	public ArrayList<Tile> createTileMap(int rowNumber, int columnNumber, 
			int tileWidth, int tileHeight, 
			int bottomLeftXCoordinate, int bottomLeftYCoordinate) {

		int capacity = rowNumber * columnNumber;
		ArrayList<Tile> tileMap = new ArrayList<Tile>(capacity);

		int leftSide = bottomLeftXCoordinate;
		int bottomSide = bottomLeftYCoordinate;
		int rightSide = leftSide + tileWidth;
		int topSide = bottomSide + tileHeight; 

		for (int index = 0; index < capacity; index++) {
			Tile currentTile = new Tile(leftSide, rightSide, topSide, bottomSide);
			tileMap.add(index, currentTile);
			System.out.println(String.format("%d: %s\n", index, currentTile));
			
			// Find the next positions of every side
			// If we are at the end of the row, go down 1 and start from left
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
	
	@Override
	public String toString() {
		String str = "TileMap:\n[";
		for (int i = 0; i < this.tileMap.size(); i++) {
			char onTile = tileMap.get(i).tileType;
			String add;
			if ((i + 1) % columnNumber == 0) {
				if ((i + 1) == this.tileMap.size()) {
					add = onTile + "]";
				} else {
					add = onTile + "\n";					
				}
			} else {
				add = onTile + ", ";
			}
			str += add;
		}
		return str;
	}

	public static void main(String[] args) {
		Grid myTileMap = new Grid(3, 5, 20, 10, 5, 15);
		System.out.println(myTileMap);
    }
}
