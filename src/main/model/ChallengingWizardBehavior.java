package main.model;

import main.controller.PlayModeController;

public class ChallengingWizardBehavior implements WizardBehavior {
    private long lastRuneTeleportationTime = 0;
    private static final long RUNE_TELEPORT_INTERVAL = 3_000_000_000L; // 3 seconds in nanoseconds

    @Override
    public void execute(WizardMonster wizard, PlayModeController controller) {
        long now = System.nanoTime();
        if (now - lastRuneTeleportationTime >= RUNE_TELEPORT_INTERVAL) {
            // System.out.println("Wizard is challenging! Teleporting the rune.");
            controller.teleportRune();
            // controller.playSoundEffectInThread("wizard");
            lastRuneTeleportationTime = now;
        }
    }

}
