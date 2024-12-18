package main.utils;

import javafx.util.Pair;

// Tile is used in tile-maps such as building area, playing area etc.
public class Tile {
	protected int leftSide;
	protected int rightSide;
	protected int topSide;
	protected int bottomSide;
	protected char tileType; // E = empty, M = monster, H = hero etc.
	

	// Normal constructor
	public Tile(int leftSide, int rightSide, int topSide, int bottomSide, char tileType) {
		super();
		this.leftSide = leftSide;
		this.rightSide = rightSide;
		this.topSide = topSide;
		this.bottomSide = bottomSide;
		this.tileType = tileType;
	}

	public int getLeftSide(){
		return this.leftSide;
	}

	public int getTopSide(){
		return this.topSide;
	}

	//Constructor without specifying tileType
	public Tile(int leftSide, int rightSide, int topSide, int bottomSide) {
		// Using this() function to call above constructor. If we change above, this changes too.
		this(leftSide, rightSide, topSide, bottomSide, 'E');
	}

	// toString Method
	@Override
	public String toString() {
		return "Tile [leftSide=" + leftSide + ", rightSide=" + rightSide + ", topSide=" + topSide + 
		", bottomSide=" + bottomSide + ", tileType=" + tileType + "]";
	}

	public boolean isInside(Pair<Float, Float> point) {
		float xCoordinate = point.getKey();
		float yCoordinate = point.getValue();
		
		return isInside(xCoordinate, yCoordinate);
	}

	public void changeTileType(char c) {
		this.tileType = c;
	}

	public boolean tileisEmpty() {
		return (this.tileType == 'E');
	}

	public boolean tileisWalkable() {
		return ((this.tileType == 'E') || (this.tileType == 'X'));
	}
	
	public boolean isInside(float xCoordinate, float yCoordinate) {
		// Check if x is in range
		if ((leftSide < xCoordinate) || (rightSide >= xCoordinate))
			return false;
		
		// Check if y is in range
		if ((bottomSide < yCoordinate) || (topSide >= yCoordinate))
			return false;
		
		return true;
	}
	public char getTileType(){
		return this.tileType;
	}
}
