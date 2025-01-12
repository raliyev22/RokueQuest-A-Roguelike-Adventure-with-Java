package main.model;

import javafx.scene.image.Image;

public abstract class Monster {
    public final int speed = 5;
    public MonsterType type;
    public long spawnTime = 0;
    public long lastActTime = 0;
    public int posX, posY; // position on grid

    public int targetX, targetY; // location of where the monsterView will go to
    public int currentX, currentY; // location of where the monsterView is currently drawn

    public Directions movingDirection = null;
    public Boolean isMoving = false;

    private boolean lured = false;

    private long lastRuneTeleportation = 0; // For wizard monster
    public long lastMovedTime = 0;
    
    public MonsterType getType(){
        return type;
    }

    public char getCharType(){
        switch (type){
            case FIGHTER -> {
                return 'F';
            }
            case ARCHER -> {
                return 'A';
            }
            case WIZARD -> {
                return 'W';
            }
            default -> throw new IllegalArgumentException("Invalid monster type");
        }
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
    public static Image getMonsterImage(Monster monster) {
        switch (monster.getType()) {
            case ARCHER:
                return Images.IMAGE_ARCHER_X2 ;
            case FIGHTER:
                return Images.IMAGE_FIGHTER_x2;
            case WIZARD:
                return Images.IMAGE_WIZARD_x2;
            default:
                throw new IllegalArgumentException("Unknown monster type: " + monster.getType());
        }
    }
    public boolean isLured() {
        return lured;
    }

    public void setLured(boolean lured) {
        this.lured = lured;
    }
    public int getX() {return posX;}
    public int getY() {return posY;}

    public long getLastActTime() {
        return lastActTime;
    }

    public void setLastActTime(long lastActTime) {
        this.lastActTime = lastActTime;
    }

    public long getLastRuneTeleportation() {
        return lastRuneTeleportation;
    }

    public void setLastRuneTeleportation(long time) {
        this.lastRuneTeleportation = time;
    }
}

