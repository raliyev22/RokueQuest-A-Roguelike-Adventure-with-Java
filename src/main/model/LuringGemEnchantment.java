package main.model;

import main.utils.Grid;
import main.controller.PlayModeController;
public class LuringGemEnchantment extends Enchantment {
    public LuringGemEnchantment(int x, int y) {
        super(x, y, "Luring Gem");
    }

    @Override
    public void applyEffect(Hero hero, Grid grid, PlayModeController controller) {
        System.out.println("Luring Gem stored in hero's inventory.");
        deactivate();
    }

    public void useGem(Hero hero, char direction, Grid grid, PlayModeController controller) {
        System.out.println("Throwing lure in direction: " + direction);
        controller.lureMonsters(hero, direction, grid); // Logic to lure monsters
    }
}

// Extra life e