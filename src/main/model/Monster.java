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
    Boolean initialized=false;

    int targetX;
    int targetY;

    Boolean isMoving=false;

    public Boolean getIsMoving(){return this.isMoving;}
    public void setIsMoving(Boolean bool){this.isMoving = bool;}

    public int getTargetX(){
        return this.targetX;
    }

    public int getTargetY(){
        return this.targetY;
    }

    public boolean getInitialized(){
        return this.initialized;
    }
    
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
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
    public void setTargetX(int x){
        this.targetX = x;
    }
    public void setTargetY(int y){
        this.targetY = y;
    }
    public void setInitialized(Boolean bool){
        this.initialized = bool;
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
    public void moveRandomly(Grid grid){
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

