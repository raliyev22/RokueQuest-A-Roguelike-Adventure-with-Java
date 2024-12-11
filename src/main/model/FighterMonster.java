package main.model;

public class FighterMonster extends Monster{
    public FighterMonster(int x, int y){
        this.x = x;
        this.y = y;
        type = MonsterType.FIGHTER;
    }
}
