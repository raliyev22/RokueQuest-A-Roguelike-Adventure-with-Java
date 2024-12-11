package main.model;

public class WizardMonster extends Monster {
    public WizardMonster(int x, int y){
        this.x = x;
        this.y = y;
        this.type = MonsterType.WIZARD;
    }
}
