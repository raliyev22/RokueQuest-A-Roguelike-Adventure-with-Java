package main.model;

import javafx.scene.image.Image;

import java.util.HashMap;

public class Hero {
	public static final long INVINCIBILITY_FRAMES = 1_000_000_000L;
	public final int maxLives = 4;
	public final int speed = 4;

	private int posX, posY;
	private int remainingLives;

	public int targetX, targetY;
	public int currentX, currentY;
	private boolean isProtected;
	private HashMap<Enchantment.Type, Integer> enchantments;
	public boolean isMoving;
	public boolean isTakingDamage;
	public Directions movingDirection;
	public Directions facingDirection;

	public long lastDamagedFrame = 0;
	private boolean isTeleported=false;
	protected Image sprite;
	private int takingDamageAnimationCounter = 0;
	private final int TAKING_DAMAGE_ANIMATION_LOOP = 3;

	public Hero(int posX, int posY) {
		this.posX = posX;
		this.posY = posY;
		this.remainingLives = 4;
		this.isMoving = false;
		this.isTakingDamage = false;
		this.enchantments = new HashMap<>();
		this.facingDirection = Directions.EAST;

		 sprite = Images.IMAGE_PLAYERRIGHT_x4;
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
	public void increaseLives(int num) {
		remainingLives += num;
		if (remainingLives > maxLives) {
			remainingLives = maxLives;
		}
	}

	public void decreaseLives() {
		if (!isProtected && remainingLives > 0) { // Only decrease if not protected
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

	public int getTakingDamageAnimationCounter() {
		return this.takingDamageAnimationCounter;
	}

	public void increaseTakingDamageAnimationCounter() {
		if (this.takingDamageAnimationCounter <= this.TAKING_DAMAGE_ANIMATION_LOOP) {
			this.takingDamageAnimationCounter += 1;
		} else {
			this.takingDamageAnimationCounter = 0;
		}
	}

	public Boolean getIsTeleported(){
		return this.isTeleported;
	}

	public void setIsTeleported(Boolean bool){
		this.isTeleported = bool;
	}
}