package main.model;

public class ArcherMonster extends Monster {
    public ArcherMonster(int x, int y) {
        this.x = x;
        this.y = y;
        type = MonsterType.ARCHER;
    }
}
