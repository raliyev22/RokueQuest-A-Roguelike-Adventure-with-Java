package main.model;

public class ArcherMonster extends Monster {
    public static int ARCHER_RANGE = 2;

    public ArcherMonster(int x, int y) {
        this.posX = x;
        this.posY = y;
        type = MonsterType.ARCHER;
    }
}
