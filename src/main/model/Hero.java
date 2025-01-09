package main.model;

import javafx.scene.image.Image;

public class Hero {
    public static final long INVINCIBILITY_FRAMES = 1_000_000_000L;
	public final int maxLives = 4;
    public final int speed = 4;

	private int posX, posY;
    private int remainingLives;

    public int targetX, targetY;
    public int currentX, currentY;

	public boolean isMoving;
	public boolean isTakingDamage;
    public Directions movingDirection;
	public Directions facingDirection;
    
    public long lastDamagedFrame = 0;
	private boolean isTeleported=false;
	private Image sprite;
	
	public Hero(int posX, int posY) {
		this.posX = posX;
		this.posY = posY;
		this.remainingLives = 4;
		this.isMoving = false;
		this.isTakingDamage = false;
		this.facingDirection = Directions.EAST;

        sprite = Images.IMAGE_PLAYERRIGHT_x4;
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

	public int getRemainingLives(){
		return remainingLives;
	}
	public void setPosY(int pos){
		this.posY= pos;
	}
	public void setPosX(int pos){
		this.posX = pos;
	}
	public void setRemaningLives(int lives){
		this.remainingLives = lives;
	}
	
	public char getCharType() {
		if (this.facingDirection == Directions.WEST) {
			return 'L';
		}
		else {
			return 'R';
		}
	}

    public Image getSprite() {
        return this.sprite;
    }

    public void setSprite(Image newSprite) {
        this.sprite = newSprite;
    }

	public Boolean getIsTeleported(){
		return this.isTeleported;
	}

	public void setIsTeleported(Boolean bool){
		this.isTeleported = bool;
	}
}
