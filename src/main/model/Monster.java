package main.model;

import javafx.scene.shape.Rectangle;

public abstract class Monster {
    public final int speed = 5;
    public MonsterType type;

    public int posX, posY; // position on grid

    public int targetX, targetY; // location of where the monsterView will go to
    public int currentX, currentY; // location of where the monsterView is currently drawn

    public Directions movingDirection = null;
    public Boolean isMoving = false;

    private long lastRuneTeleportation = 0; // For wizard monster
    public long lastMovedTime = 0;

    public Rectangle monsterView;
    
    public MonsterType getType(){
        return type;
    }

    public char getCharType(){
        switch (type){
            case MonsterType.FIGHTER -> {
                return 'F';
            }
            case MonsterType.ARCHER -> {
                return 'A';
            }
            case MonsterType.WIZARD -> {
                return 'W';
            }
            default -> throw new IllegalArgumentException("Invalid monster type");
        }
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

    public long getLastRuneTeleportation() {
        return lastRuneTeleportation;
    }

    public void setLastRuneTeleportation(long time) {
        this.lastRuneTeleportation = time;
    }
}

