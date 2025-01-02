package main.model;

import javafx.scene.shape.Rectangle;
import main.utils.Grid;
import main.utils.Tile;



public abstract class Monster {
    int x;
    int y;
    MonsterType type;
    Grid grid;
    Tile tile;
    Rectangle monsterView;
    Directions movingDirection=null;

    Boolean isMoving=false;

    public void setMovingDirection(Directions dir){this.movingDirection = dir;}

    public Directions getMovingDirection(){return this.movingDirection;}

    public Boolean getIsMoving(){
        return this.isMoving;
    }
    public void setIsMoving(Boolean bool){
        this.isMoving = bool;
    }
    
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    public void setY(int y){
        this.y = y;
    }
    public void setX(int x){
        this.x = x;
    }
    public MonsterType getType(){
        return type;
    }
    public Tile getTile(){
        return tile;
    }
    public Rectangle getMonsterView(){
        return this.monsterView;
    }
    public void setMonsterView(Rectangle monsterView){
        this.monsterView = monsterView;
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
				y--;
			}
			case Directions.SOUTH -> {
				y++;
			}
			case Directions.EAST -> {
				x++;
			}
			case Directions.WEST -> {
				x--;
			}
			default -> throw new IllegalArgumentException("Invalid direction");
		}
	}
}

