package main.model;

import main.controller.PlayModeController;
import main.utils.Grid;

public class ExtraLifeEnchantment extends Enchantment {
    public ExtraLifeEnchantment(int x, int y) {
        super(x, y, "ExtraLife");
    }

    @Override
    public void applyEffect(Hero hero, Grid grid, PlayModeController controller) {
        if (hero.getLiveCount() < hero.maxLives) {
            hero.increaseLives(1);
            System.out.println("Extra Life collected! Hero's current lives: " + hero.getLiveCount());
        } else {
            System.out.println("Hero already has maximum lives.");
        }
        this.deactivate();
    }
}
