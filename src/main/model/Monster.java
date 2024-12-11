package main.model;

public abstract class Monster {
    int x;
    int y;
    MonsterType type;
    
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
}
