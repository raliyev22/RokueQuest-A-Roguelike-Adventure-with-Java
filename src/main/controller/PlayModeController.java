package main.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TimerTask;
import java.util.Timer;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.Main;
import main.model.*;
import main.utils.*;
import main.view.InventoryView;
import main.view.PlayModeView;

public class PlayModeController extends Application {
    protected final int ROW = 10;
    protected final int COLUMN = 9;
    protected final int tileWidth = 64;
    protected final int tileHeight = 64;
    protected final int topLeftXCoordinate = 100;
    protected final int topLeftYCoordinate = 150;

    public static Grid earthHall;
    public static Grid airHall;
    public static Grid waterHall;
    public static Grid fireHall;

    public Grid playModeGrid;
    protected Hero hero;
    protected MonsterManager monsterManager;
    protected HallType hallType;
    private List<Enchantment> activeEnchantments;
    private Map<Tile, Integer> runeExtraTimeMap; // Tracks extra time added per rune
    private boolean luringGemActivated = false;

    private static final long ENCHANTMENT_SPAWN_INTERVAL = 12_000_000_000L; // 12 seconds in nanoseconds

    private Inventory inventory;
    private InventoryView inventoryView;
    private long lastEnchantmentSpawnTime = 0;
    public int runeXCoordinate;
    public int runeYCoordinate;
    private boolean mouseClicked = false;
    private double mouseX;
    private double mouseY;

    public static double time;
    public static double totalTime;

    protected int hallTimeMultiplier = 500;
    private long lastUpdateTime = 0; // Tracks the last time the timer was updated
    private static final long ONE_SECOND_IN_NANOS = 1_000_000_000L; // One second in nanoseconds

    private long remainingMonsterSpawnTime;
    private long lastMonsterSpawnTime = 0;
    private static final long MONSTER_SPAWN_INTERVAL = 5_000_000_000L; // 8 seconds in nanoseconds

	private static final int TARGET_FPS = 120;
	private static final long FRAME_DURATION_NANOS = 1_000_000_000 / TARGET_FPS;
  	private long lastFrameTime = 0;

    private PlayModeView view;
    private boolean upPressed, downPressed, leftPressed, rightPressed;

    private long adjustedNow;
    private AnimationTimer gameLoop;
    private boolean isRunning = false;
    private SoundEffects soundPlayer = SoundEffects.getInstance(); // Singleton instance

    private boolean escPressedFlag = false; 
    private long pauseStartTime = 0;
    private long totalPausedTime = 0;
    private long lastToggleTime = 0;
    private static final long TOGGLE_DEBOUNCE_DELAY = 300_000_000L;
    private boolean isCountdownRunning = false;

    private Stage primaryStage;

    // public PlayModeController() {
    //     initializePlayMode();
    // }

    public void initializePlayMode() {
        // Create a new empty grid
        playModeGrid = new Grid(ROW, COLUMN, tileWidth, tileHeight, topLeftXCoordinate, topLeftYCoordinate);
        activeEnchantments = new ArrayList<>(); // Initialize enchantment list
        lastEnchantmentSpawnTime = System.nanoTime();
        inventory = new Inventory();
        inventoryView = new InventoryView();
        // Populate the grid with the objects stored in the static variables
        if (null == this.hallType) {
            this.hallType = HallType.EARTH;
            playModeGrid.copyTileMap(earthHall);
            this.time = (getHallObjectTiles().size()) * hallTimeMultiplier;
        } else switch (this.hallType) {
            case EARTH -> {
                this.hallType = HallType.AIR;
                playModeGrid.copyTileMap(airHall);
                this.time = (getHallObjectTiles().size()) * hallTimeMultiplier;
            }
            case AIR -> {
                this.hallType = HallType.WATER;
                playModeGrid.copyTileMap(waterHall);
                this.time = (getHallObjectTiles().size()) * hallTimeMultiplier;
            }
            case WATER -> {
                this.hallType = HallType.FIRE;
                playModeGrid.copyTileMap(fireHall);
                this.time = (getHallObjectTiles().size()) * hallTimeMultiplier;
            }
            case FIRE -> {
                stopGameLoop();
                view.showGameOverPopup(true);
                System.err.println("playmodecontroller line 103 fire hall bug");
                return;
            }
        }
        this.totalTime = time;

        //playModeGrid.copyTileMap(earthHall);
        // Find the first random tile that the hero will spawn on
        Tile initialHeroTile = getRandomEmptyTile();
        int randomXCoordinate = playModeGrid.findXofTile(initialHeroTile);
        int randomYCoordinate = playModeGrid.findYofTile(initialHeroTile);
        Tile heroTile = playModeGrid.findTileWithIndex(randomXCoordinate, randomYCoordinate);
        hero = initializeHero(randomXCoordinate, randomYCoordinate);

        hero.targetX = heroTile.getLeftSide();
        hero.targetY = heroTile.getTopSide();

        // Create monster manager

        // Find the random object that the rune will spawn in
        Tile runeTile = getRandomHallObjectTile();
        runeXCoordinate = playModeGrid.findXofTile(runeTile);
        runeYCoordinate = playModeGrid.findYofTile(runeTile);
      
        monsterManager = new MonsterManager(playModeGrid, hero);

        if(view  != null){ // Else we have already come from another grid, which means we only need to refresh the view
            view.refresh(playModeGrid, time, hallType);
            view.updateHeroPosition(heroTile.getLeftSide(), heroTile.getTopSide());
            initializeSetOnActions();
            monsterManager.setPlayModeView(view);
        }
    }

    public void start(Stage primaryStage) {
        initializePlayMode();
        Tile heroTile = playModeGrid.findTileWithIndex(hero.getPosX(), hero.getPosY());
        // If view is null (which means we are in the first hall), create a new one
        if (view == null){
            view = new PlayModeView(playModeGrid, time, primaryStage,hallType);
            view.updateHeroPosition(heroTile.getLeftSide(), heroTile.getTopSide());
            initializeSetOnActions();
            monsterManager.setPlayModeView(view);
        }

        Scene scene = view.getScene();
        initialize(scene);

        primaryStage.setTitle("Play Mode");
        primaryStage.setScene(scene);
        // primaryStage.setFullScreen(true);
        // primaryStage.setFullScreenExitHint("");
        primaryStage.show();
        this.primaryStage = primaryStage;

        startGameLoop();
    }
   private Monster findClosestFighterMonster(int heroX, int heroY) {
        Monster closestFighter = null;
        double minDistance = Double.MAX_VALUE;

        for (Monster monster : monsterManager.monsterList) {
            if (monster.getType() == MonsterType.FIGHTER) { // Only consider Fighter Monsters
                double distance = calculateManhattanDistance(heroX, heroY, monster.getX(), monster.getY());
                if (distance < minDistance) {
                    minDistance = distance;
                    closestFighter = monster;
                }
            }
        }

        return closestFighter;
    }

    private double calculateManhattanDistance(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }
    public void useLuringGem(String direction) {
        if (luringGemActivated) {
            int heroX = hero.getPosX();
            int heroY = hero.getPosY();

            // Find the closest Fighter Monster
            Monster closestFighter = findClosestFighterMonster(heroX, heroY);

            if (closestFighter != null) {
                // Determine the target position based on direction
                int targetX = closestFighter.getX();
                int targetY = closestFighter.getY();

                switch (direction.toUpperCase()) {
                    case "W" -> targetY--; // Up
                    case "A" -> targetX--; // Left
                    case "S" -> targetY++; // Down
                    case "D" -> targetX++; // Right
                }

                if (playModeGrid.indexInRange(targetX, targetY)) {
                    lureSpecificMonster(closestFighter, targetX, targetY);
                }
            }

            luringGemActivated = false;
        }
    }


    public void lureSpecificMonster(Monster monster, int targetX, int targetY) {
        if (monster == null || playModeGrid == null || view == null) {
            System.err.println("Invalid parameters for luring monster");
            return;
        }

        Tile currentTile = playModeGrid.findTileWithIndex(monster.posX, monster.posY);
        Tile targetTile = playModeGrid.findTileWithIndex(targetX, targetY);

        if (currentTile != null && targetTile != null && Grid.isWalkableTile(targetTile)) {
            // Update the monster's attributes for movement
            monster.targetX = targetTile.getLeftSide();
            monster.targetY = targetTile.getTopSide();
            monster.isMoving = true;
            monster.setLured(true); // Indicate that the monster was lured

            // Highlight the target tile
            int highlightWidth = playModeGrid.getTileWidth();
            int highlightHeight = playModeGrid.getTileHeight();

            view.highlightArea(
                    targetTile.getLeftSide(),
                    targetTile.getTopSide(),
                    highlightWidth,
                    highlightHeight,
                    true // Enable highlighting
            );

            // Schedule removal of the highlight after 2 seconds
            Platform.runLater(() -> {
                view.highlightArea(
                        targetTile.getLeftSide(),
                        targetTile.getTopSide(),
                        highlightWidth,
                        highlightHeight,
                        false // Disable highlighting
                );
            });

            // Remove the rectangle after a set time
            // Schedule removal of the highlight after 2 seconds
            PauseTransition pause = new PauseTransition(Duration.seconds(2));
            pause.setOnFinished(event -> Platform.runLater(() -> {
                view.highlightArea(
                        targetTile.getLeftSide(),
                        targetTile.getTopSide(),
                        highlightWidth,
                        highlightHeight,
                        false // Disable highlighting
                );
            }));
            pause.play();
            playModeGrid.changeTileWithIndex(monster.posX, monster.posY, 'E'); // Clear old position
            monster.posX = targetX; // Update position
            monster.posY = targetY;
            playModeGrid.changeTileWithIndex(targetX, targetY, monster.getCharType()); // Update grid

        }
    }

    // public void handleMouseClick(double mouseX, double mouseY) {
    //     for (Map.Entry<Enchantment, Rectangle> entry : view.getEnchantmentViews().entrySet()) {
    //         Enchantment enchantment = entry.getKey();
    //         Rectangle enchantmentView = entry.getValue();

    //         if (enchantmentView != null && enchantmentView.contains(mouseX, mouseY)) {
    //             if (!activeEnchantments.contains(enchantment)) {
    //                 System.out.println("Enchantment not active: " + enchantment.getType());
    //                 return; // Avoid double collection
    //             }

    //             if (enchantment.getType() == Enchantment.Type.EXTRA_LIFE) {
    //                 hero.increaseLives(1);
    //                 view.updateHeroLife(hero.getLiveCount());
    //                 // Remove enchantment after use
    //                 activeEnchantments.remove(enchantment);
    //                 view.removeEnchantmentView(enchantment);
    //             } else {
    //                 // Handle other enchantments
    //                 view.collectEnchantment(enchantment, inventory);
    //             }
    //             break; // Exit loop after handling
    //         }
    //     }
    // }
    public void addTime(int seconds) {
        time += seconds; // Update the time
        view.updateTime(time); // Reflect the change in the view
        System.out.println("Time updated: " + time); // Debug log
    }

    public void useCloakOfProtection() {
        Enchantment.Type type = Enchantment.Type.CLOAK_OF_PROTECTION;
        if (hero.getEnchantments().containsKey(type)) {
            hero.consumeEnchantment(type);
            hero.setProtected(true);
            Timer protectionTimer = new Timer();
            protectionTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    hero.setProtected(false);
                }
            }, 20000); // Cloak lasts for 20 seconds
        }
    }



    private void handleKeyPressed(KeyCode code) {
        if (isCountdownRunning) {
            return;
        }

        switch (code) {
            case W:
                if (luringGemActivated) {
                    useLuringGem("W");
                    luringGemActivated = false; // Reset the activation
                } else {
                    upPressed = true;
                }
                break;
            case S:
                if (luringGemActivated) {
                    useLuringGem("S");
                    luringGemActivated = false;
                } else {
                    downPressed = true;
                }
                break;
            case A:
                if (luringGemActivated) {
                    useLuringGem("A");
                    luringGemActivated = false;
                } else {
                    leftPressed = true;
                }
                break;
            case D:
                if (luringGemActivated) {
                    useLuringGem("D");
                    luringGemActivated = false;
                } else {
                    rightPressed = true;
                }
                break;
            case ESCAPE:
                if (!escPressedFlag) {
                    togglePause();
                    escPressedFlag = true;
                }
                break;
            case R:
                if (inventory.useEnchantment(Enchantment.Type.REVEAL)) {
                    Tile runeTile = playModeGrid.findTileWithIndex(runeXCoordinate, runeYCoordinate);

                    if (runeTile != null) {
                        int tileWidth = playModeGrid.getTileWidth();
                        int tileHeight = playModeGrid.getTileHeight();

                        int xPoint = Math.max(playModeGrid.topLeftXCoordinate, runeTile.getLeftSide() - tileWidth);
                        int yPoint = Math.max(playModeGrid.topLeftYCoordinate, runeTile.getTopSide() - tileHeight);

                        Enchantment.highlightArea(xPoint, yPoint, 4 * tileWidth, 4 * tileHeight, view);
                    }

                    view.updateInventoryUI(inventory.getEnchantments());
                }
                break;
            case P:
                if (inventory.useEnchantment(Enchantment.Type.CLOAK_OF_PROTECTION)) {
                    hero.setProtectedFrom(MonsterType.ARCHER); // Protect only from archers
                    view.updateInventoryUI(inventory.getEnchantments());
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            hero.removeProtectionFrom(MonsterType.ARCHER);
                        }
                    }, 20000); // Cloak lasts for 20 seconds
                }
                break;
            case B:
                if (inventory.useEnchantment(Enchantment.Type.LURING_GEM)) {
                    luringGemActivated = true; // Enable direction selection
                    handleKeyPressed(code);
                    view.updateInventoryUI(inventory.getEnchantments());
                }
                break;
            default:
                // Handle unrecognized keys if necessary
                break;
        }
    }
    public void initialize(Scene scene) {
        scene.setOnKeyPressed(event -> handleKeyPressed(event.getCode()));
        scene.getRoot().requestFocus();
        scene.setOnKeyReleased(event -> handleKeyReleased(event.getCode()));
        scene.getRoot().requestFocus();
        scene.setOnMouseClicked(event -> {
            mouseClicked = true;
            mouseX = event.getX();
            mouseY = event.getY();
        });
        scene.getRoot().requestFocus();
    }

    public void initializeSetOnActions(){
        view.pauseButton.setOnAction(e -> {
            soundPlayer.playSoundEffect("blueButtons");
            togglePause();
        });

        view.exitButton.setOnAction(e -> {
            stopGameLoop();
            Main mainPage = new Main();
            javafx.geometry.Rectangle2D screenBounds1 = javafx.stage.Screen.getPrimary().getVisualBounds();

            // Set up the main stage in the center of the screen
            primaryStage.setX((screenBounds1.getWidth() - 600) / 2);
            primaryStage.setY((screenBounds1.getHeight() - 400) / 2);

            mainPage.start(primaryStage);
            soundPlayer.playSoundEffectInThread("blueButtons");
        });

        view.saveButton.setOnAction(e -> {
            soundPlayer.playSoundEffectInThread("blueButtons");
            soundPlayer.playSoundEffectInThread("save");
            save();
        });    
    }


    

    private void handleKeyReleased(KeyCode code) {
        switch (code) {
            case UP, W -> upPressed = false;
            case DOWN, S -> downPressed = false;
            case LEFT, A -> leftPressed = false;
            case RIGHT, D -> rightPressed = false;
            case ESCAPE -> escPressedFlag = false;
            default -> {
                //System.out.println("Unhandled Key Released: " + code);
            }
        }
    }


    public void startGameLoop() {
        if (isRunning) return;
        isRunning = true;

        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                adjustedNow = now - totalPausedTime;

                if (lastFrameTime > 0 && adjustedNow - lastFrameTime < FRAME_DURATION_NANOS) {
                    return;
                }
                lastFrameTime = adjustedNow;               
                spawnEnchantment();
                
                // if (mouseClicked) {
                //     handleMouseClick(mouseX, mouseY);
                //     mouseClicked = false;
                // }
                if (lastMonsterSpawnTime == 0) {
                    lastMonsterSpawnTime = adjustedNow-remainingMonsterSpawnTime;
                }

                if (time < 0) {
                    view.showGameOverPopup(false);
                    soundPlayer.playSoundEffectInThread("gameLoser");
                    stopGameLoop();
                    return;
                }

                if (hero.getLiveCount() <= 0) {
                    changeHeroSpriteToDamaged();
                    view.changeHeroSprite(hero.getSprite());
                    view.showGameOverPopup(false);
                    soundPlayer.playSoundEffectInThread("gameLoser");
                    stopGameLoop();
                    return;
                }
                if (now - lastEnchantmentSpawnTime >= ENCHANTMENT_SPAWN_INTERVAL) {
                    spawnEnchantment();
                    lastEnchantmentSpawnTime = now;
                }
                if (adjustedNow - lastUpdateTime >= ONE_SECOND_IN_NANOS) {
                    view.updateTime(time); // Update the view
                    time--;
                    lastUpdateTime = adjustedNow;
                }

                if (!hero.getIsTeleported()){
                    moveHero();
                }

                // If hero is taking damage, change blip animations
                if (hero.isTakingDamage) {
                    if (hero.getTakingDamageAnimationCounter() == 0) {
                        blinkHeroSprite();
                    }

                    hero.increaseTakingDamageAnimationCounter();

                    if (adjustedNow - hero.lastDamagedFrame >= Hero.INVINCIBILITY_FRAMES) {
                        hero.isTakingDamage = false;
                        changeHeroSpriteToNormal();
                    }
                }

                view.changeHeroSprite(hero.getSprite());

                //monster spawn logic
                if (adjustedNow - lastMonsterSpawnTime >= MONSTER_SPAWN_INTERVAL) {
                    Tile initialMonsterTile = getRandomEmptyTile();

                    int randomXCoordinate = playModeGrid.findXofTile(initialMonsterTile);
                    int randomYCoordinate = playModeGrid.findYofTile(initialMonsterTile);

                    monsterManager.createMonster(randomXCoordinate, randomYCoordinate, adjustedNow);     

                    lastMonsterSpawnTime = adjustedNow; 
                }

                monsterManager.actAllMonsters(adjustedNow, PlayModeController.this);

                monsterManager.moveAllMonsters(adjustedNow);

                if (mouseClicked) {
                    if (playModeGrid.coordinatesAreInGrid(mouseX, mouseY)) {
                        Tile clickedTile = playModeGrid.findTileUsingCoordinates(mouseX, mouseY);
                        for (Map.Entry<Enchantment, Rectangle> entry : view.getEnchantmentViews().entrySet()) {
                            Enchantment enchantment = entry.getKey();
                            Rectangle enchantmentView = entry.getValue();
                
                            if (enchantmentView != null && enchantmentView.contains(mouseX, mouseY)) {
                                if (!activeEnchantments.contains(enchantment)) {
                                    System.out.println("Enchantment not active: " + enchantment.getType());
                                    return; // Avoid double collection
                                }
                
                                if (enchantment.getType() == Enchantment.Type.EXTRA_LIFE) {
                                    hero.increaseLives(1);
                                    view.updateHeroLife(hero.getLiveCount());
                                    // Remove enchantment after use
                                    activeEnchantments.remove(enchantment);
                                    view.removeEnchantmentView(enchantment);
                                } else {
                                    // Handle other enchantments
                                    view.collectEnchantment(enchantment, inventory);
                                }
                                break; // Exit loop after handling
                            }
                        }
                        if (checkRune(clickedTile)) {
                            stopGameLoop();
                            hero.isMoving = false;
                            lastMonsterSpawnTime = 0;
                            lastUpdateTime = 0;

                            view.showWalls(playModeGrid, false);
                            view.walls.toFront();
                            soundPlayer.playSoundEffectInThread("door");

                            if(hallType == HallType.FIRE){
                                soundPlayer.playSoundEffectInThread("gameWinner");
                                view.showGameOverPopup(true);
                            }
                            else{
                                soundPlayer.playSoundEffectInThread("door");
                                new Timeline(new KeyFrame(Duration.seconds(1.75), e -> {
                                    startGameLoop();
                                    initializePlayMode();
                                })).play();
                            }
                        }
                    }

                    mouseClicked = false;
                }
                view.walls.toFront();
            }
        };
        gameLoop.start();
    }
    private void spawnEnchantment() {
        long currentTime = System.nanoTime();

        // Check if enough time has passed since the last enchantment spawn
        if (currentTime - lastEnchantmentSpawnTime >= ENCHANTMENT_SPAWN_INTERVAL) {
            Enchantment enchantmentToSpawn = Enchantment.spawnRandomEnchantment(playModeGrid, currentTime);

            // Try spawning Extra Time on the rune if available
            if (enchantmentToSpawn.getType() == Enchantment.Type.EXTRA_TIME) {
                Tile runeTile = playModeGrid.findTileWithIndex(runeXCoordinate, runeYCoordinate);

                if (runeTile != null && checkRune(runeTile)) { // Ensure the rune is still available and valid
                    activeEnchantments.add(enchantmentToSpawn);
                    view.addEnchantmentView(enchantmentToSpawn, runeTile.getLeftSide(), runeTile.getTopSide());
                    final Enchantment finalEnchantment = enchantmentToSpawn; // Effectively final for lambda
                    enchantmentToSpawn.startExpirationTimer(6000, () -> {
                        activeEnchantments.remove(finalEnchantment);
                        view.removeEnchantmentView(finalEnchantment);
                    });

                    lastEnchantmentSpawnTime = currentTime;
                    return; // Exit early since the Extra Time enchantment was successfully spawned
                }
            }
            Tile randomTile = getRandomEmptyTile();
            if (randomTile != null) {
                Enchantment newEnchantment = enchantmentToSpawn;

                if (enchantmentToSpawn.getType() == Enchantment.Type.EXTRA_TIME) {
                    newEnchantment = Enchantment.spawnRandomEnchantment(playModeGrid, currentTime);
                }

                final Enchantment finalFallbackEnchantment = newEnchantment; // Effectively final for lambda
                activeEnchantments.add(finalFallbackEnchantment);
                view.addEnchantmentView(finalFallbackEnchantment, randomTile.getLeftSide(), randomTile.getTopSide());

                // Schedule expiration
                finalFallbackEnchantment.startExpirationTimer(6000, () -> {
                    activeEnchantments.remove(finalFallbackEnchantment);
                    view.removeEnchantmentView(finalFallbackEnchantment);
                });
                lastEnchantmentSpawnTime = currentTime;
            }
        }
    }

    

    private void stopGameLoop() {
        if (!isRunning) return;
        isRunning = false;
        if (gameLoop != null) {
            gameLoop.stop();
        }
        System.out.println("Game loop stopped.");
    }

    private void togglePause() {
        long now = System.nanoTime();
        if (now - lastToggleTime < TOGGLE_DEBOUNCE_DELAY) {
            return;
        }
        lastToggleTime = now;

        if (isRunning){
            stopGameLoop();
            view.showPauseGame();
            soundPlayer.pauseSoundEffect("background");
            pauseStartTime = System.nanoTime();
        }
        else {
            totalPausedTime += now - pauseStartTime;
            startGameLoop();
            view.hidePauseGame();
            soundPlayer.resumeSoundEffect("background");
            initialize(view.getScene());
        }
    }

    public Hero initializeHero(int xCoordinate, int yCoordinate) {
        Hero hero = new Hero(xCoordinate, yCoordinate);
        hero.setSprite(Images.IMAGE_PLAYERRIGHT_x4);
        playModeGrid.changeTileWithIndex(hero.getPosX(), hero.getPosY(), hero.getCharType());
        return hero;
    }

    public void moveHero() {
        Tile heroTile = playModeGrid.findTileWithIndex(hero.getPosX(), hero.getPosY());
        int heroViewLeftSide = heroTile.getLeftSide();
        int heroViewTopSide = heroTile.getTopSide();

        if(!hero.isMoving){
            hero.currentX = heroViewLeftSide;
            hero.currentY = heroViewTopSide;
        }

        if (!hero.isMoving) {
            if (upPressed && isWalkableTile(playModeGrid.findNorthTile(heroTile))) {
                heroTile.changeTileType('?');
                Tile northTile = playModeGrid.findNorthTile(heroTile);
                if (northTile != null) {
                    northTile.changeTileType('?');
                }
                hero.targetY = hero.currentY - tileHeight;
                hero.isMoving = true;
                hero.movingDirection = Directions.NORTH;

                soundPlayer.playSoundEffectInThread("step");        
            } else if (downPressed && isWalkableTile(playModeGrid.findSouthTile(heroTile))) {
                heroTile.changeTileType('?');
                Tile southTile = playModeGrid.findSouthTile(heroTile);
                if (southTile != null) {
                    southTile.changeTileType('?');
                }
                hero.targetY = hero.currentY + tileHeight;
                hero.isMoving = true;
                hero.movingDirection = Directions.SOUTH;

                soundPlayer.playSoundEffectInThread("step");        
            } else if (leftPressed && isWalkableTile(playModeGrid.findWestTile(heroTile))) {
                heroTile.changeTileType('?');
                Tile westTile = playModeGrid.findWestTile(heroTile);
                if (westTile != null) {
                    westTile.changeTileType('?');
                }
                hero.targetX = hero.currentX - tileWidth;
                hero.isMoving = true;
                hero.facingDirection = Directions.WEST;
                hero.movingDirection = Directions.WEST;
                hero.setSprite(Images.IMAGE_PLAYERLEFT_x4);

                soundPlayer.playSoundEffectInThread("step");        
            } else if (rightPressed && isWalkableTile(playModeGrid.findEastTile(heroTile))) {
                heroTile.changeTileType('?');
                Tile eastTile = playModeGrid.findEastTile(heroTile);
                if (eastTile != null) {
                    eastTile.changeTileType('?');
                }
                hero.targetX = hero.currentX + tileWidth;
                hero.isMoving = true;
                hero.facingDirection = Directions.EAST;
                hero.movingDirection = Directions.EAST;
                hero.setSprite(Images.IMAGE_PLAYERRIGHT_x4);

                soundPlayer.playSoundEffectInThread("step");        
            }
        }

        if (hero.currentX < hero.targetX) {
            hero.currentX = Math.min(hero.currentX + hero.speed, hero.targetX);
            view.updateHeroPosition(hero.currentX, hero.currentY);
        } else if (hero.currentX > hero.targetX) {
            hero.currentX = Math.max(hero.currentX - hero.speed, hero.targetX);
            view.updateHeroPosition(hero.currentX, hero.currentY);
        }

        if (hero.currentY < hero.targetY) {
            hero.currentY = Math.min(hero.currentY + hero.speed, hero.targetY);
            view.updateHeroPosition(hero.currentX, hero.currentY);
        } else if (hero.currentY > hero.targetY) {
            hero.currentY = Math.max(hero.currentY - hero.speed, hero.targetY);
            view.updateHeroPosition(hero.currentX, hero.currentY);
        }

        if (hero.currentX == hero.targetX && hero.currentY == hero.targetY) {
            hero.isMoving = false;
            if (hero.movingDirection != null) {
                moveHeroOnGrid(hero.movingDirection);
                hero.movingDirection = null;
            }
        }
    }

    public void moveHeroOnGrid(Directions dir) {
        int xIndexOld = hero.getPosX();
        int yIndexOld = hero.getPosY();
        playModeGrid.changeTileWithIndex(xIndexOld, yIndexOld, 'E');

        hero.move(dir);

        int xIndexNew = hero.getPosX();
        int yIndexNew = hero.getPosY();
        playModeGrid.changeTileWithIndex(xIndexNew, yIndexNew, hero.getCharType());
        //System.out.println(playModeGrid);
    }

    public void blinkHeroSprite() {
        // Change hero sprite to red if normal, normal if red.
        if (hero.getSprite().equals(Images.IMAGE_PLAYERLEFT_x4)) {
            hero.setSprite(Images.IMAGE_PLAYERLEFTTAKINGDAMAGE_x4);
        } else if (hero.getSprite().equals(Images.IMAGE_PLAYERRIGHT_x4)) {
            hero.setSprite(Images.IMAGE_PLAYERRIGHTTAKINGDAMAGE_x4);
        } else if (hero.getSprite().equals(Images.IMAGE_PLAYERLEFTTAKINGDAMAGE_x4)) {
            hero.setSprite(Images.IMAGE_PLAYERLEFT_x4);
        } else if (hero.getSprite().equals(Images.IMAGE_PLAYERRIGHTTAKINGDAMAGE_x4)) {
            hero.setSprite(Images.IMAGE_PLAYERRIGHT_x4);
        }
    }

    public void changeHeroSpriteToNormal() {
        if (hero.getSprite().equals(Images.IMAGE_PLAYERLEFTTAKINGDAMAGE_x4)) {
            hero.setSprite(Images.IMAGE_PLAYERLEFT_x4);
        } else if (hero.getSprite().equals(Images.IMAGE_PLAYERRIGHTTAKINGDAMAGE_x4)) {
            hero.setSprite(Images.IMAGE_PLAYERRIGHT_x4);
        }
    }

    public void changeHeroSpriteToDamaged() {
        if (hero.getSprite().equals(Images.IMAGE_PLAYERLEFT_x4)) {
            hero.setSprite(Images.IMAGE_PLAYERLEFTTAKINGDAMAGE_x4);
        } else if (hero.getSprite().equals(Images.IMAGE_PLAYERRIGHT_x4)) {
            hero.setSprite(Images.IMAGE_PLAYERRIGHTTAKINGDAMAGE_x4);
        }
    }

    // private double calculateDistance(int x1, int y1, int x2, int y2) {
    //     return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    // }

    /**
     * Requires:
     * - `playModeGrid` must be properly initialized.
     * - The list returned by `getHallObjectTiles()` must not be empty (hallObjects.size() > 0).
     * 
     * Modifies:
     * - `runeXCoordinate`
     * - `runeYCoordinate`
     * 
     * Effects:
     * - Randomly selects a hall object from the available hall objects and updates the rune's coordinates.
     * - If only one hall object exists, the rune may remain in the same location.
     * - Updates `runeXCoordinate` and `runeYCoordinate` to the position of the selected hall object.
     */
    public void teleportRune() {
        SecureRandom rng = new SecureRandom();
        ArrayList<Tile> hallObjects = getHallObjectTiles();

        int luckyHallObjectIndex = rng.nextInt(hallObjects.size());
        Tile luckyHallObjectTile = hallObjects.get(luckyHallObjectIndex);

        int newRuneX = playModeGrid.findXofTile(luckyHallObjectTile);
        int newRuneY = playModeGrid.findYofTile(luckyHallObjectTile);

        runeXCoordinate = newRuneX;
        runeYCoordinate = newRuneY;
    }

    // private void attackHeroIfInRange(Monster monster) {
    //     double distance = calculateDistance(hero.getPosX(), hero.getPosY(), monster.getX(), monster.getY());

    //     // FIGHTER attacks if adjacent
    //     if (monster.getType() == MonsterType.FIGHTER && distance <= 1) {
    //         hero.decreaseLives();
    //         view.updateHeroLife(hero.getLiveCount());
    //         playSoundEffectInThread("fighter");        
    //     }

    //     // ARCHER attacks if within 3 tiles
    //     if (monster.getType() == MonsterType.ARCHER && distance <= 3) {
    //         hero.decreaseLives();
    //         view.updateHeroLife(hero.getLiveCount());
    //         playSoundEffectInThread("archer");        
    //     }

    //     // Game over check
    //     // if (hero.getLiveCount() <= 0) {
    //     // 	view.showGameOver();
    //     // 	System.out.println("Game Over!");
    //     // }
    // }

    public boolean checkRune(Tile tile) {
        if ((playModeGrid.findXofTile(tile) == runeXCoordinate) 
        && (playModeGrid.findYofTile(tile) == runeYCoordinate)) {
            if ((Math.abs(runeXCoordinate - hero.getPosX()) <= 1) 
            && (Math.abs(runeYCoordinate - hero.getPosY()) <= 1)) {
                showRuneParticleEffect(tile);
                return true;
            }
        }
        return false;
    }

    public Tile getRandomEmptyTile() {
        return playModeGrid.getRandomEmptyTile();
    }

    public ArrayList<Tile> getEmptyTiles() {
        return playModeGrid.getEmptyTiles();
    }

    public static boolean isHallObjectTile(Tile tile) {
        return Grid.isHallObjectTile(tile);
    }

    public static boolean isHallObjectTileType(char c) {
        return Grid.isHallObjectTileType(c);
    }

    public ArrayList<Tile> getHallObjectTiles() {
        return playModeGrid.getHallObjectTiles();
    }

    public Tile getRandomHallObjectTile() {
        SecureRandom rng = new SecureRandom();
        ArrayList<Tile> hallObjectTiles = getHallObjectTiles();

        int luckyTileInd = rng.nextInt(hallObjectTiles.size());

        return hallObjectTiles.get(luckyTileInd);
    }

    public static boolean isEmptyTileType(char c) {
        return Grid.isEmptyTileType(c);
    }

    public boolean isWalkableTile(Tile tile){
        return Grid.isWalkableTile(tile);
    }

    public static boolean isWalkableTileType(char c) {
        return Grid.isWalkableTileType(c);
    }

    public Grid getPlayModeGrid() {
        return this.playModeGrid;
    }

    public int getTopLeftXCoordinate() {
        return this.topLeftXCoordinate;
    }

    public int getTopLeftYCoordinate() {
        return this.topLeftYCoordinate;
    }

    public Hero getHero(){
        return this.hero;
    }
    public PlayModeView getView(){
        return this.view;
    }

    public void removeMonster(Monster monster) {
        monsterManager.monsterList.remove(monster);
        // monsterIterator.remove();
        playModeGrid.changeTileWithIndex(monster.posX, monster.posY, 'E');
        view.removeFromPane(monster.monsterView);
    }

    public Grid resolveAllMovingCharacters() {
        Grid resolvedGrid = new Grid(ROW, COLUMN, tileWidth, tileHeight, topLeftXCoordinate, topLeftYCoordinate);
        resolvedGrid.copyTileMap(playModeGrid);

        if (hero.isMoving) {
            Tile heroTile = resolvedGrid.findTileWithIndex(hero.getPosX(), hero.getPosY());
            Tile destinationTile = resolvedGrid.findTileUsingDirection(heroTile, hero.movingDirection);

            heroTile.changeTileType(hero.getCharType());
            destinationTile.changeTileType('E');
        }

        for (Monster monster: monsterManager.monsterList) {
            if (monster.isMoving) {
                Tile monsterTile = resolvedGrid.findTileWithIndex(monster.posX, monster.posY);
                Tile destinationTile = resolvedGrid.findTileUsingDirection(monsterTile, monster.movingDirection);

                monsterTile.changeTileType('E');
                destinationTile.changeTileType(monster.getCharType());
            }
        }

        return resolvedGrid;
    }

    public void save(){
        System.out.println("Game Saved!");
		String filePath = "src/saveFiles/allSaveFiles.txt";
        File file = new File(filePath);
        ArrayList<String> saves = new ArrayList<String>();
        try (Scanner scanner = new Scanner(file)){
            while (scanner.hasNextLine()){
                String line = scanner.nextLine();
                if(!line.isEmpty()){
                    saves.add(line);
                }
            }
        }catch (IOException e){
            System.out.println("An error occurred: " + e.getMessage());
        }
        int num = 1;
        if (!saves.isEmpty()){
            StringBuilder str = new StringBuilder(saves.get(saves.size()-1));
            str.delete(0,4);
            str.delete(str.length()-4,str.length());
            num = Integer.valueOf(str.toString()) + 1;

        }

        try (FileWriter writer = new FileWriter(file, true)){
            writer.write("\nsave" + String.valueOf(num) + ".txt");
        }catch(IOException e){
            System.out.println("An error occurred: " + e.getMessage());
        }
        createSaveFile("save" + String.valueOf(num));
    }


    public void createSaveFile(String name){
        String filePath = "src/saveFiles/" + name + ".txt";
        File file = new File(filePath);
        String earthallString = earthHall.toString();
		String airhallString = airHall.toString();
		String waterhalString = waterHall.toString();
		String firehallString = fireHall.toString();
		String playModeGridString = resolveAllMovingCharacters().toString(); // Change '?' before saving
		String currentHall = hallType.toString();

        try (FileWriter writer = new FileWriter(file)) {
			writer.write("EarthHall:");
            writer.write(earthallString);
			writer.write("\n");
			writer.write("AirHall:");
            writer.write(airhallString);
			writer.write("\n");
			writer.write("WaterHall:");
            writer.write(waterhalString);
			writer.write("\n");
			writer.write("FireHall:");
            writer.write(firehallString);
			writer.write("\n");			
            writer.write("PlayModeGrid:");
            writer.write(playModeGridString);
			writer.write("\n");
			writer.write("CurrentHall:\n");
			writer.write(currentHall);
			writer.write("\n");
			writer.write("TimeLeft:\n");
			writer.write(Double.toString(time));
			writer.write("\nHeroPosx:\n");
			writer.write(Integer.toString(hero.getPosX()));
			writer.write("\nHeroPosy:\n");
			writer.write(Integer.toString(hero.getPosY()));
			writer.write("\nHeroRemainingLives:\n");
			writer.write(Integer.toString(hero.getLiveCount()));
			writer.write("\nRuneXCoordinate:\n");
			writer.write(Integer.toString(runeXCoordinate));
			writer.write("\nRuneYCoordinate:\n");
			writer.write(Integer.toString(runeYCoordinate));
			writer.write("\nRemainingMonsterSpawnTime:\n");
            remainingMonsterSpawnTime = adjustedNow - lastMonsterSpawnTime;
            writer.write(Long.toString(remainingMonsterSpawnTime));

            System.out.println("File written successfully!");
        } catch (IOException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    public void load(Stage primaryStage, String fileName) {

        String filePath = "src/saveFiles/" + fileName;
        System.out.println("Loading game...");
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                System.out.println("No saved game file found.");
                return;
            }

            ArrayList<String> lines = new ArrayList<>();
            try (Scanner scanner = new Scanner(file)) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine().trim();
                    if (!line.isEmpty()) { // Skip empty lines
                        lines.add(line);
                    }
                }
            }

            int index = 0;

            // Load Earth Hall grid
            if (index < lines.size() && lines.get(index).startsWith("EarthHall:TileMap:")) {
                index++;
                earthHall = new Grid(10, 9, 64, 64, 100, 150);
                index = parseTileMap(earthHall, lines, index);
            } else {
                throw new RuntimeException("Earth Hall data missing or corrupted.");
            }

            // Load Air Hall grid
            if (index < lines.size() && lines.get(index).startsWith("AirHall:TileMap:")) {
                index++;
                airHall = new Grid(10, 9, 64, 64, 100, 150);
                index = parseTileMap(airHall, lines, index);
            } else {
                throw new RuntimeException("Air Hall data missing or corrupted.");
            }

            // Load Water Hall grid
            if (index < lines.size() && lines.get(index).startsWith("WaterHall:TileMap:")) {
                index++;
                waterHall = new Grid(10, 9, 64, 64, 100, 150);
                index = parseTileMap(waterHall, lines, index);
            } else {
                throw new RuntimeException("Water Hall data missing or corrupted.");
            }

            // Load Fire Hall grid
            if (index < lines.size() && lines.get(index).startsWith("FireHall:TileMap:")) {
                index++;
                fireHall = new Grid(10, 9, 64, 64, 100, 150);
                index = parseTileMap(fireHall, lines, index);
            } else {
                throw new RuntimeException("Fire Hall data missing or corrupted.");
            }

            // Load Current Play Mode grid
            if (index < lines.size() && lines.get(index).startsWith("PlayModeGrid:TileMap:")) {
                index++;
                playModeGrid = new Grid(10, 9, 64, 64, 100, 150);
                index = parseTileMap(playModeGrid, lines, index);
            } else {
                throw new RuntimeException("Play Mode Grid data missing or corrupted.");
            }

            // Load current hall type
            if (index < lines.size() && lines.get(index).equals("CurrentHall:")) {
                index++;
                hallType = HallType.valueOf(lines.get(index++));
            } else {
                throw new RuntimeException("Current Hall data missing or corrupted.");
            }

            // Load remaining time
            if (index < lines.size() && lines.get(index).equals("TimeLeft:")) {
                index++;
                time = Double.parseDouble(lines.get(index++));
            } else {
                throw new RuntimeException("TimeLeft data missing or corrupted.");
            }

            // Load hero's position and lives
            if (index < lines.size() && lines.get(index).equals("HeroPosx:")) {
                index++;
                int heroPosX = Integer.parseInt(lines.get(index++));

                if (index < lines.size() && lines.get(index).equals("HeroPosy:")) {
                    index++;
                    int heroPosY = Integer.parseInt(lines.get(index++));

                    if (index < lines.size() && lines.get(index).equals("HeroRemainingLives:")) {
                        index++;
                        int heroLives = Integer.parseInt(lines.get(index++));

                        hero = initializeHero(heroPosX, heroPosY);
                        hero.setRemaningLives(heroLives);
                    }
                }
            } else {
                throw new RuntimeException("Hero data missing or corrupted.");
            }

            // Load rune's position
            if (index < lines.size() && lines.get(index).equals("RuneXCoordinate:")) {
                index++;
                runeXCoordinate = Integer.parseInt(lines.get(index++));

                if (index < lines.size() && lines.get(index).equals("RuneYCoordinate:")) {
                    index++;
                    runeYCoordinate = Integer.parseInt(lines.get(index++));
                }
            } else {
                throw new RuntimeException("Rune data missing or corrupted.");
            }

            // Load last monster spawn time
            if (index < lines.size() && lines.get(index).equals("RemainingMonsterSpawnTime:")) {
                index++;
                remainingMonsterSpawnTime = Long.parseLong(lines.get(index++));
            } else {
                throw new RuntimeException("RemainingMonsterSpawnTime data missing or corrupted.");
}

            view = new PlayModeView(playModeGrid, time, primaryStage,hallType);

            monsterManager = new MonsterManager(playModeGrid, hero);

            // Refresh the view
            Tile heroTile = playModeGrid.findTileWithIndex(hero.getPosX(), hero.getPosY());
            hero.targetX = heroTile.getLeftSide();
            hero.targetY = heroTile.getTopSide();
            hero.isMoving = false;
            view.updateHeroPosition(heroTile.getLeftSide(), heroTile.getTopSide());
            view.updateHeroLife(hero.getRemainingLives());
            initializeSetOnActions();
            monsterManager.setPlayModeView(view);

            for (int y = 0; y < playModeGrid.getColumnLength(); y++) {
                for (int x = 0; x < playModeGrid.getRowLength(); x++) {
                    Tile tile = playModeGrid.findTileWithIndex(x, y);
                    char tileType = tile.getTileType();

                    MonsterType monsterType = null;

                    switch (tileType) {
                        case 'F' -> monsterType = MonsterType.FIGHTER;
                        case 'A' -> monsterType = MonsterType.ARCHER;
                        case 'W' -> monsterType = MonsterType.WIZARD;
                    }

                    if (monsterType != null) {
                        // Canavarı oluştur ve ekrana çiz
                        monsterManager.createMonster(x, y, monsterType, System.nanoTime());
                    }
                }
            }

            Scene scene = view.getScene();
            initialize(scene);

            primaryStage.setTitle("Play Mode");
            primaryStage.setScene(scene);
            primaryStage.setY(0);
            // primaryStage.setFullScreen(true);
            // primaryStage.setFullScreenExitHint("");
            primaryStage.show();
            this.primaryStage = primaryStage;
            view.showCountdownAndStart(() -> {
                isCountdownRunning= false;
                startGameLoop();
            });
            isCountdownRunning = true; 

            System.out.println("Game loaded successfully!");
        } catch (IOException e) {
            System.out.println("An error occurred while loading the game: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private int parseTileMap(Grid grid, ArrayList<String> lines, int index) {
        int tileCount = 0; // Keep track of processed tiles
        int totalTiles = grid.getRowLength()* grid.getColumnLength(); // Total tiles expected in the grid

        for (int i = 0; i < grid.getColumnLength(); i++) {
            String[] tiles = lines.get(index++).split(", ");
            for (int j = 0; j < tiles.length; j++) {
                String[] tileData = tiles[j].split(" ");
                char tileType = tileData[1].charAt(0); // Extract tile type
                grid.changeTileWithIndex(j, i, tileType);
                tileCount++;
            }
        }

        if (tileCount != totalTiles) {
            System.err.println("Warning: Parsed tile count does not match expected grid size.");
        }

        return index;
    }

    private void showRuneParticleEffect(Tile runeTile) {
        soundPlayer.playSoundEffectInThread("sparkle");
        double centerX = runeTile.getLeftSide() + tileWidth / 2.0;
        double centerY = runeTile.getTopSide() + tileHeight / 2.0;
    
        Group particleGroup = new Group();
        view.getPane().getChildren().add(particleGroup);
    
        Timeline timeline = new Timeline();
    
        for (int i = 0; i < 100; i++) {
            Circle particle = new Circle(3, Color.GOLD);
            particle.setCenterX(centerX);
            particle.setCenterY(centerY);
    
            double angle = Math.random() * 360;
            double distance = Math.random() * 100;
            double targetX = centerX + Math.cos(Math.toRadians(angle)) * distance;
            double targetY = centerY + Math.sin(Math.toRadians(angle)) * distance;
    
            particleGroup.getChildren().add(particle);
    
            timeline.getKeyFrames().addAll(
                new KeyFrame(Duration.millis(0),
                    new KeyValue(particle.opacityProperty(), 1),
                    new KeyValue(particle.centerXProperty(), centerX),
                    new KeyValue(particle.centerYProperty(), centerY)
                ),
                new KeyFrame(Duration.millis(800),
                    new KeyValue(particle.opacityProperty(), 0),
                    new KeyValue(particle.centerXProperty(), targetX),
                    new KeyValue(particle.centerYProperty(), targetY)
                )
            );
        }
    
        timeline.setOnFinished(e -> view.getPane().getChildren().remove(particleGroup));
        timeline.play();
    }
    
}
