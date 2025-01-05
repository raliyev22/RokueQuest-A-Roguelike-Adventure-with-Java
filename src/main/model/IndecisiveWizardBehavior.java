package main.model;



import main.controller.PlayModeController;

public class IndecisiveWizardBehavior implements WizardBehavior {
    private long startTime;

    public IndecisiveWizardBehavior() {
        this.startTime = System.nanoTime();
    }

    @Override
    public void execute(WizardMonster wizard, PlayModeController controller) {
        long now = System.nanoTime();
        if (now - startTime >= 2_000_000_000L) { // Disappear after 2 seconds
            System.out.println("Wizard is indecisive and disappearing.");
            controller.removeMonster(wizard);
        }
    }

}
