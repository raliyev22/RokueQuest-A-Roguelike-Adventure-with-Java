package main.model;
import main.utils.Grid;


public abstract class Monster {
    int x;
    int y;
    MonsterType type;
    Grid grid;
    
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
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
    public void moveRandomly(Grid grid){
    }
}

