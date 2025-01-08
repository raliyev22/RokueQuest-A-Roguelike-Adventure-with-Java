package main.model;

import main.controller.PlayModeController;
import main.utils.SoundEffects;

public class ChallengingWizardBehavior implements WizardBehavior {
    private SoundEffects soundPlayer = SoundEffects.getInstance(); // Singleton instance

    public ChallengingWizardBehavior() {
        soundPlayer.addSoundEffect("wizard", "src/main/sounds/wizard.wav");
        soundPlayer.setVolume("wizard", -10);
    }

    @Override
    public void execute(WizardMonster wizard, PlayModeController controller) {
        controller.teleportRune();
        soundPlayer.playSoundEffect("wizard");
    }

}
