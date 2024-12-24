package main.model;

import javafx.scene.image.Image;

public class Hero {
	public final int maxLives = 4;
	private int posX, posY, remainingLives;
	private Image img;
	public boolean isMoving;
	public boolean isTakingDamage;
	public Directions facingDirection;
	
	
	public Hero(int posX, int posY, Image img) {
		this.posX = posX;
		this.posY = posY;
		this.img = img;
		this.remainingLives = 4;
		this.isMoving = false;
		this.isTakingDamage = false;
		this.facingDirection = Directions.EAST;
	}

	public Hero(int posX, int posY) {
		this.posX = posX;
		this.posY = posY;
		this.img = null;
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
		remainingLives--;
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
	
}
