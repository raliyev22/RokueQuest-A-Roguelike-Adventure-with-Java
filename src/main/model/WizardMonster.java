package main.model;

public class WizardMonster extends Monster {
    public WizardMonster(int x, int y){
        this.posX = x;
        this.posY = y;
        this.type = MonsterType.WIZARD;
    }
}
