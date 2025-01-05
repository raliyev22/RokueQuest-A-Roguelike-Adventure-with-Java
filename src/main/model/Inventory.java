package main.model;

import java.util.HashMap;
import java.util.Map;
import main.controller.PlayModeController;
import main.utils.Grid;
import main.utils.Tile;

public class Inventory {
    private final Map<Enchantment, Integer> enchantments;

    public Inventory() {
        this.enchantments = new HashMap<>();
    }

    public Map<Enchantment, Integer> getEnchantments() {
        return enchantments;
    }

    public void addEnchantment(Enchantment enchantment) {
        enchantments.put(enchantment, enchantments.getOrDefault(enchantment, 0) + 1);
        System.out.println(enchantment.getName() + " added to inventory.");
    }

    public void useEnchantment(Enchantment enchantment, Hero hero, Grid grid, PlayModeController controller) {
        if (enchantments.containsKey(enchantment) && enchantments.get(enchantment) > 0) {
            if (enchantment instanceof RevealEnchantment reveal) {
                Tile heroTile = grid.findTileWithIndex(hero.getPosX(), hero.getPosY());
                reveal.useReveal(heroTile, grid, controller);
            } else if (enchantment instanceof CloakOfProtectionEnchantment cloak) {
                cloak.useCloak(hero, controller);
            } else if (enchantment instanceof LuringGemEnchantment gem) {
                gem.useGem(hero, 'N', grid, controller); // Example direction
            } else {
                System.out.println("No specific logic for this enchantment yet.");
            }

            enchantments.put(enchantment, enchantments.get(enchantment) - 1);
            if (enchantments.get(enchantment) == 0) {
                enchantments.remove(enchantment);
            }
        } else {
            System.out.println("Enchantment not available in inventory.");
        }
    }
}