package main.model;

import main.controller.PlayModeController;

public class WizardMonster extends Monster {

    private WizardBehavior behavior;

    public WizardMonster(int x, int y){
        this.posX = x;
        this.posY = y;
        this.type = MonsterType.WIZARD;
        updateBehavior();
    }


    public void updateBehavior() {
        double timePercentage = (double) PlayModeController.time / PlayModeController.totalTime;
        WizardBehavior newBehavior;

        if (timePercentage < 0.3) {
            newBehavior = new DesperateWizardBehavior();
        } else if (timePercentage > 0.7) {
            newBehavior = new ChallengingWizardBehavior();
        } else {
            newBehavior = new IndecisiveWizardBehavior();
        }

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