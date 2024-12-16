package main.model;

public class ExtraTimeEnchantment extends Enchantment {

    private final int extraTime; // Eklenecek süre miktarı (saniye)

    public ExtraTimeEnchantment(int x, int y) {
        super(x, y, "Extra Time Enchantment");
        this.extraTime = 5; // Her seferinde 5 saniye ekler
    }

    @Override
    public void applyEffect(Hero hero) {
        if (isActive) {
            // hero.addTime(extraTime); // Hero'nun zamanını artırır
            System.out.println("Extra Time collected! +" + extraTime + " seconds added.");
            deactivate();
        }
    }
}
