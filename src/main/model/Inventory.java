package main.model;

import java.util.ArrayList;
import java.util.List;

public class Inventory {
    private final List<Enchantment.Type> enchantments; // Store collected enchantments as a list

    public Inventory() {
        enchantments = new ArrayList<>();
    }

    // Add an enchantment to the inventory
    public boolean addEnchantment(Enchantment.Type type) {
        if (enchantments.size() < 6) { // Limit inventory to 6 slots
            enchantments.add(type); // Add the enchantment type
            return true; // Successfully added the enchantment
        } else {
            System.out.println("Inventory is full! Cannot add more enchantments.");
            return false; // Inventory full
        }
    }

    // Use an enchantment (remove one instance from the list if it exists)
    public boolean useEnchantment(Enchantment.Type type) {
        if (enchantments.contains(type)) {
            enchantments.remove(type); // Remove one instance of the enchantment
            return true; // Successfully used the enchantment
        }
        return false; // Enchantment not available
    }

    // Check if the inventory contains a specific enchantment
    public boolean hasEnchantment(Enchantment.Type type) {
        return enchantments.contains(type);
    }

    // Get all enchantments in the inventory (for UI updates)
    public List<Enchantment.Type> getEnchantments() {
        return new ArrayList<>(enchantments); // Return a copy to ensure immutability
    }

    // Clear the inventory (optional utility method)
    public void clear() {
        enchantments.clear();
    }
}
