package main.model;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.util.Duration;
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

    // Getters
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

    // Deactivate the enchantment
    public void deactivate() {
        this.isActive = false;
    }

    // Get the image representation for this enchantment
    public Image getImage() {
        return switch (type) {
            case EXTRA_TIME -> Images.IMAGE_CUBE_x2;
            case EXTRA_LIFE -> Images.IMAGE_CHESTHEARTOPEN_x2;
            case CLOAK_OF_PROTECTION -> Images.IMAGE_CLOAK_x2;
            case LURING_GEM -> Images.IMAGE_ALLURE_x4;
            case REVEAL -> Images.IMAGE_REVEAL_x2;
        };
    }

    // Start expiration timer and trigger the provided callback upon expiration
    // Enchantment.java
    public void startExpirationTimer(long durationInMillis, Runnable onExpire) {
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.millis(durationInMillis),
                event -> {
                    deactivate();
                    onExpire.run(); // UI updates directly on the JavaFX Application Thread
                }
        ));
        timeline.setCycleCount(1); // Run only once
        timeline.play();
    }

    // Spawn a random enchantment at a random empty tile on the grid
    public static Enchantment spawnRandomEnchantment(Grid grid, long spawnTime) {
        SecureRandom random = new SecureRandom();
        Tile randomTile = grid.getRandomEmptyTile();
        if (randomTile == null) return null; // Ensure no null tile

        int posX = grid.findXofTile(randomTile);
        int posY = grid.findYofTile(randomTile);
        Type type = Type.values()[random.nextInt(Type.values().length)];

        return new Enchantment(type, posX, posY, spawnTime);
    }

    // Handle the effects of the enchantment
//    public static void handleEnchantmentEffect(Enchantment enchantment, Hero hero, PlayModeController controller) {
//        switch (enchantment.getType()) {
//            case EXTRA_TIME -> controller.addTime(5);
//            case EXTRA_LIFE -> hero.increaseLives(1);
//            case REVEAL, CLOAK_OF_PROTECTION, LURING_GEM -> hero.addToBag(enchantment.getType());
//        }
//    }

    // Highlight a 4x4 area around the rune tile
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
