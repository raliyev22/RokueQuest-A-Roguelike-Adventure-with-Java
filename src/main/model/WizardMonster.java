package main.model;

public class WizardMonster extends Monster {
    public WizardMonster(int x, int y){
        this.x = x;
        this.y = y;
    }
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    public char getType(){
        return 'W';
    }
}
