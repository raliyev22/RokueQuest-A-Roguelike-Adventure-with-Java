package main.model;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import main.controller.PlayModeController;
import main.utils.Grid;
import main.utils.Tile;
import main.view.PlayModeView;

import java.security.SecureRandom;
import java.util.HashSet;
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
    private Set<Tile> extraTimeRunes = new HashSet<>();
    private static final int MAX_EXTRA_TIME_SECONDS = 5;
    private int totalExtraTimeGained = 0;
    private Timeline expirationTimeline;

    public Enchantment(Type type, int posX, int posY, long spawnTime) {
        this.type = type;
        this.posX = posX;
        this.posY = posY;
        this.spawnTime = spawnTime;
        this.isActive = true;
    }
    public void setExpirationTimeline(Timeline timeline) {
        this.expirationTimeline = timeline;
    }

    public Timeline getExpirationTimeline() {
        return this.expirationTimeline;
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
            case EXTRA_TIME ->  Images.IMAGE_CLOCK_x2;
            case EXTRA_LIFE -> Images.IMAGE_HEART_ENCH_X4;
            case CLOAK_OF_PROTECTION -> Images.IMAGE_CLOAK_x4;
            case LURING_GEM -> Images.IMAGE_ALLURE_x4;
            case REVEAL -> Images.IMAGE_REVEAL_x4;
        };
    }

    public static char getChar (Type type) {
        return switch (type) {
            case EXTRA_TIME ->  'X';
            case EXTRA_LIFE -> 'M';
            case CLOAK_OF_PROTECTION -> 'O';
            case LURING_GEM -> 'N';
            case REVEAL -> 'Q';
        };
    }

    public char getChar () {
        return getChar(this.type);
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
        char c = Enchantment.getChar(type);
        grid.changeTileWithIndex(posX, posY, c);

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
    public static void highlightArea(int xPoint, int yPoint, int width, int height, PlayModeView view) {
        Rectangle highlightRectangle = new Rectangle(xPoint, yPoint, width, height);
        highlightRectangle.setStroke(Color.GOLD); // Border color
        highlightRectangle.setFill(Color.TRANSPARENT); // Transparent fill

        // Add the rectangle to the view
        Platform.runLater(() -> view.getPane().getChildren().add(highlightRectangle));

        // Remove the rectangle after a set time
        PauseTransition pause = new PauseTransition(Duration.seconds(10));
        pause.setOnFinished(event -> Platform.runLater(() -> view.getPane().getChildren().remove(highlightRectangle)));
        pause.play();
    }

    public static Enchantment getEnchantmentFromType(Type type) {
        // Use default or placeholder values
        int defaultPosX = 0;
        int defaultPosY = 0;
        long defaultSpawnTime = System.currentTimeMillis();

        return new Enchantment(type, defaultPosX, defaultPosY, defaultSpawnTime);
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