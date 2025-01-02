package main.model;

import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class Hero {
	public final int maxLives = 4;
    public int speed = 5;

	private int posX, posY;
    private int remainingLives;

    public int targetX, targetY;
    public int currentX, currentY;
    public Rectangle heroView;

	public boolean isMoving;
	public boolean isTakingDamage;
    public Directions movingDirection;
	public Directions facingDirection;
	
	
	public Hero(int posX, int posY) {
		this.posX = posX;
		this.posY = posY;
		this.remainingLives = 4;
		this.isMoving = false;
		this.isTakingDamage = false;
		this.facingDirection = Directions.EAST;
	}

	public void move(Directions direction) {
		switch (direction) {
			case Directions.NORTH -> {
				posY--;
			}
			case Directions.SOUTH -> {
				posY++;
			}
			case Directions.EAST -> {
				posX++;
			}
			case Directions.WEST -> {
				posX--;
			}
			default -> throw new IllegalArgumentException("Invalid direction");
		}
	}
	
	public void increaseLives(int num){
		remainingLives += num;
		if(remainingLives > maxLives){
			remainingLives = maxLives;
		}
	}
	
	public void decreaseLives() {
		if(remainingLives>0){
			remainingLives--;
		}
		
	}
	
	public int getLiveCount(){
		return remainingLives;
	}
	
	public int getPosX() {
		return posX;
	}
	
	public int getPosY() {
		return posY;
	}
	
	public char getCharType() {
		if (this.facingDirection == Directions.WEST) {
			return 'L';
		}
		else {
			return 'R';
		}
	}

	public void setFill(ImagePattern imagePattern) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'setFill'");
	}
	
}
