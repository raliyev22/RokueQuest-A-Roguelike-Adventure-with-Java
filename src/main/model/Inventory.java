package main.model;

import java.util.HashMap;

public class Inventory {
    private final HashMap<Enchantment.Type, Integer> enchantments;

    public Inventory() {
        enchantments = new HashMap<>();
    }

    // Add an enchantment to the inventory
    public void addEnchantment(Enchantment.Type type) {
        enchantments.put(type, enchantments.getOrDefault(type, 0) + 1);
    }

    // Use an enchantment (reduces count by 1, removes if count becomes 0)
    public boolean useEnchantment(Enchantment.Type type) {
        if (enchantments.containsKey(type) && enchantments.get(type) > 0) {
            enchantments.put(type, enchantments.get(type) - 1);
            if (enchantments.get(type) == 0) {
                enchantments.remove(type);
            }
            return true; // Successfully used the enchantment
        }
        return false; // Enchantment not available
    }


    // Get all enchantments in the inventory
    public HashMap<Enchantment.Type, Integer> getEnchantments() {
        return new HashMap<>(enchantments); // Return a copy to ensure immutability
    }

    // Clear the inventory (optional utility method)
    public void clear() {
        enchantments.clear();
    }
}
