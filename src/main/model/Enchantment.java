package main.model;

public abstract class Enchantment {
    protected int x; // Enchantment'ın x pozisyonu
    protected int y; // Enchantment'ın y pozisyonu
    protected boolean isActive; // Enchantment'ın aktif olup olmadığı
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
        this.isActive = false; // Enchantment toplandığında devre dışı kalır
    }

    public abstract void applyEffect(Hero hero); // Alt sınıflar bu metodu uygulayacak
}
