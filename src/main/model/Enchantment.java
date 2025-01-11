// Enchantment.java
package main.model;

import javafx.util.Pair;
import main.controller.PlayModeController;
import main.utils.Grid;
import main.utils.Tile;
import main.view.PlayModeView;

import java.security.SecureRandom;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Set;

public class Enchantment {
    public enum Type {
        EXTRA_TIME, REVEAL, CLOAK_OF_PROTECTION, LURING_GEM, EXTRA_LIFE
    }

    private Type type;
    private int posX;
    private int posY;
    private long spawnTime;
    private boolean isActive;

    public Enchantment(Type type, int posX, int posY, long spawnTime) {
        this.type = type;
        this.posX = posX;
        this.posY = posY;
        this.spawnTime = spawnTime;
        this.isActive = true;
    }

    public Type getType() {
        return type;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public long getSpawnTime() {
        return spawnTime;
    }

    public boolean isActive() {
        return isActive;
    }

    public void deactivate() {
        this.isActive = false;
    }

    // Starts a timer to deactivate the enchantment after a specified duration
    public void startExpirationTimer(long durationInMillis, Runnable onExpire) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                deactivate();
                onExpire.run();
                timer.cancel();
            }
        }, durationInMillis);
    }

    public static Enchantment spawnRandomEnchantment(Grid grid, long spawnTime) {
        SecureRandom random = new SecureRandom();
        int posX, posY;
        Enchantment.Type type = Enchantment.Type.values()[random.nextInt(Enchantment.Type.values().length)];
        Tile randomTile = grid.getRandomEmptyTile();
        posX = grid.findXofTile(randomTile);
        posY = grid.findYofTile(randomTile);

        return new Enchantment(type, posX, posY, spawnTime);
    }

    public static void handleEnchantmentEffect(Enchantment enchantment, Hero hero, PlayModeController controller) {
        switch (enchantment.getType()) {
            case EXTRA_TIME -> controller.addTime(5);
            case EXTRA_LIFE -> hero.increaseLives(1);
            case REVEAL -> hero.addToBag(enchantment.getType());
            case CLOAK_OF_PROTECTION -> hero.addToBag(enchantment.getType());
            case LURING_GEM -> hero.addToBag(enchantment.getType());
        }
    }

    public static void highlightRevealArea(Grid grid, Tile runeTile, PlayModeView view) {
        Set<Tile> highlightTiles = grid.findNxNSquare(runeTile, 4);
        for (Tile tile : highlightTiles) {
            view.highlightTile(tile, true); // Custom method to visually highlight
        }
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                for (Tile tile : highlightTiles) {
                    view.highlightTile(tile, false); // Remove highlight after 10 seconds
                }
            }
        }, 10000); // Highlight lasts for 10 seconds
    }

    @Override
    public String toString() {
        return "Enchantment{" +
                "type=" + type +
                ", posX=" + posX +
                ", posY=" + posY +
                ", isActive=" + isActive +
                '}';
    }
}
