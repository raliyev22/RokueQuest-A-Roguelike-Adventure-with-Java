package main.model;

import main.controller.PlayModeController;
import main.utils.SoundEffects;

public class ChallengingWizardBehavior implements WizardBehavior {
    private SoundEffects soundPlayer = SoundEffects.getInstance(); // Singleton instance

    @Override
    public void execute(WizardMonster wizard, PlayModeController controller) {
        controller.teleportRune();
        soundPlayer.playSoundEffect("wizard");
    }

}
