package main.model;

import java.util.HashSet;
import java.util.Set;

public class Inventory {
    private final Set<Enchantment.Type> enchantments; // Store collected enchantments as a set

    public Inventory() {
        enchantments = new HashSet<>();
    }

    // Add an enchantment to the inventory
    public void addEnchantment(Enchantment.Type type) {
        enchantments.add(type); // Add the enchantment type
    }

    // Use an enchantment (remove it from the set if it exists)
    public boolean useEnchantment(Enchantment.Type type) {
        if (enchantments.contains(type)) {
            enchantments.remove(type); // Remove the enchantment after use
            return true; // Successfully used the enchantment
        }
        return false; // Enchantment not available
    }

    // Check if the inventory contains a specific enchantment
    public boolean hasEnchantment(Enchantment.Type type) {
        return enchantments.contains(type);
    }

    // Get all enchantments in the inventory (for UI updates)
    public Set<Enchantment.Type> getEnchantments() {
        return new HashSet<>(enchantments); // Return a copy to ensure immutability
    }

    // Clear the inventory (optional utility method)
    public void clear() {
        enchantments.clear();
    }
}
