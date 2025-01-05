package main.model;
import main.controller.PlayModeController;
import main.utils.Grid;

public abstract class Enchantment {
    protected int x; // Enchantment's x position
    protected int y; // Enchantment's y position
    protected boolean isActive; // Whether the enchantment is active
    protected String name;

    public Enchantment(int x, int y, String name) {
        this.x = x;
        this.y = y;
        this.name = name;
        this.isActive = true;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isActive() {
        return isActive;
    }

    public String getName() {
        return name;
    }

    public void deactivate() {
        this.isActive = false; // Enchantment becomes inactive when collected
    }

    public abstract void applyEffect(Hero hero, Grid grid, PlayModeController controller); // Subclasses implement this method

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Enchantment that = (Enchantment) obj;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
