package main.model;
import main.utils.Tile;

public class WizardMonster extends Monster {
    public WizardMonster(int x, int y,Tile monsterTile){
        this.x = x;
        this.y = y;
        this.tile = monsterTile;
        this.type = MonsterType.WIZARD;
    }
}
