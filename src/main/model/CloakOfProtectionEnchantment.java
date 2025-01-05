package main.model;

import main.utils.Grid;
import main.controller.PlayModeController;

public class CloakOfProtectionEnchantment extends Enchantment {
    public CloakOfProtectionEnchantment(int x, int y) {
        super(x, y, "Cloak of Protection");
    }

    @Override
    public void applyEffect(Hero hero, Grid grid, PlayModeController controller) {
        System.out.println("Cloak of Protection stored in hero's inventory.");
        deactivate();
    }

    public void useCloak(Hero hero, PlayModeController controller) {
        System.out.println("Hero is now hidden from archer monsters for 20 seconds.");
        hero.setInvisible(true);
        controller.startCloakTimer(hero, 20); // Start cloak timer
    }
}