package main.model;
import java.util.Iterator;

import main.controller.PlayModeController;
import main.utils.Tile;

public class WizardMonster extends Monster {

    private WizardBehavior behavior;

    public WizardMonster(int x, int y,Tile monsterTile){
        this.x = x;
        this.y = y;
        this.tile = monsterTile;
        this.type = MonsterType.WIZARD;
        updateBehavior();
    }


    public void updateBehavior() {
        double timePercentage = (double) PlayModeController.time / PlayModeController.totalTime;
        WizardBehavior newBehavior;

        // if (timePercentage < 0.3) {
        //     newBehavior = new DesperateWizardBehavior();
        // } else if (timePercentage > 0.7) {
        //     newBehavior = new ChallengingWizardBehavior();
        // } else {
        //     newBehavior = new IndecisiveWizardBehavior();
        // }
        newBehavior = new DesperateWizardBehavior();

        if (behavior == null || !newBehavior.getClass().equals(behavior.getClass())) {
            behavior = newBehavior;
        }
    }

    //@Override
    public void act(PlayModeController controller) {
        updateBehavior(); // Dynamically adjust behavior based on time left
        if (behavior != null) {
            behavior.execute(this, controller);
        }
    }
}
