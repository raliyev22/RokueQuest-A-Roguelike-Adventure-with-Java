package main.model;

public class FighterMonster extends Monster{
    public static int FIGHTER_RANGE = 1;

    public FighterMonster(int x, int y) {
        this.posX = x;
        this.posY = y;
        type = MonsterType.FIGHTER;
    }

}
