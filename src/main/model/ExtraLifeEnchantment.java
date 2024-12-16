package main.model;

public class ExtraLifeEnchantment extends Enchantment {

    public ExtraLifeEnchantment(int x, int y) {
        super(x, y, "Extra Life Enchantment");
    }

    @Override
    public void applyEffect(Hero hero) {
        if (isActive) {
            hero.increaseLives(1); // Hero'nun canını 1 artırır
            System.out.println("Extra Life collected! Hero now has " + hero.getLiveCount() + " lives.");
            deactivate();
        }
    }
}
