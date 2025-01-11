package main.model;

import java.util.HashMap;

public class Hero {
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
	private boolean isProtected;

	private HashMap<Enchantment.Type, Integer> enchantments;


	private boolean isTeleported=false;
	
	
	public Hero(int posX, int posY) {
		this.posX = posX;
		this.posY = posY;
		this.remainingLives = 4;
		this.isMoving = false;
		this.isTakingDamage = false;
		this.isProtected = false;
		this.facingDirection = Directions.EAST;
		this.enchantments = new HashMap<>();
	}

	public void move(Directions direction) {
		switch (direction) {
			case NORTH -> {
				posY--;
			}
			case SOUTH -> {
				posY++;
			}
			case EAST -> {
				posX++;
			}
			case WEST -> {
				posX--;
			}
			default -> throw new IllegalArgumentException("Invalid direction");
		}
	}
	public boolean isProtected() {
		return isProtected;
	}

	public void setProtected(boolean isProtected) {
		this.isProtected = isProtected;
	}

	public void addEnchantment(Enchantment.Type type) {
		enchantments.put(type, enchantments.getOrDefault(type, 0) + 1);
	}

	public void addToBag(Enchantment.Type type) {
		enchantments.put(type, enchantments.getOrDefault(type, 0) + 1);
	}

	public Enchantment consumeEnchantment(Enchantment.Type type) {
		if (enchantments.containsKey(type) && enchantments.get(type) > 0) {
			enchantments.put(type, enchantments.get(type) - 1);
			if (enchantments.get(type) == 0) {
				enchantments.remove(type);
			}
			return new Enchantment(type, posX, posY, System.currentTimeMillis());
		}
		return null;
	}

	public HashMap<Enchantment.Type, Integer> getEnchantments() {
		return enchantments;
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
	public void setPosY(int pos){
		this.posY= pos;
	}
	public void setPosX(int pos){
		this.posX = pos;
	}
	
	public char getCharType() {
		if (this.facingDirection == Directions.WEST) {
			return 'L';
		}
		else {
			return 'R';
		}
	}

	public Boolean getIsTeleported(){
		return this.isTeleported;
	}

	public void setIsTeleported(Boolean bool){
		this.isTeleported = bool;
	}
}
