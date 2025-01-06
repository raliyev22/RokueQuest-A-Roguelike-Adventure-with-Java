package main.controller;

import java.io.File;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Random;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import main.model.*;
import main.utils.*;
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
    
    private int runeXCoordinate;
    private int runeYCoordinate;
    private boolean mouseClicked = false;
    private double mouseX;
    private double mouseY;
    
    public int time;
    
    protected int hallTimeMultiplier = 500;
    private long lastUpdateTime = 0; // Tracks the last time the timer was updated
    private static final long ONE_SECOND_IN_NANOS = 1_000_000_000L; // One second in nanoseconds
    
    private long lastMonsterUpdateTime = 0; // Tracks the last monster update time
    private long lastMonsterSpawnTime = 0;
    private static final long MONSTER_UPDATE_INTERVAL = 300_000_000L; // Monster movement update interval (500ms)

	  private static final int TARGET_FPS = 120;
	  private static final long FRAME_DURATION_NANOS = 1_000_000_000 / TARGET_FPS;
  	private long lastFrameTime = 0;

    private PlayModeView view;
    private boolean upPressed, downPressed, leftPressed, rightPressed;
    
    private Random random = new Random();
    
    private AnimationTimer gameLoop;
    private boolean isRunning = false;
    private SoundEffects soundPlayer = SoundEffects.getInstance(); // Singleton instance

    private boolean escPressedFlag = false; 

    // public PlayModeController() {
    //     initializePlayMode();
    // }
    
    public void initializePlayMode() {
        // Create a new empty grid
        playModeGrid = new Grid(ROW, COLUMN, tileWidth, tileHeight, topLeftXCoordinate, topLeftYCoordinate);
        
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
            case FIRE -> { // Add game over screen here
                stopGameLoop();
                view.showGameOverPopup(true);
                return;
            }
        }
        
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
        
        monsterManager = new MonsterManager(playModeGrid);
        
        if(view  != null){ // Else we have already come from another grid, which means we only need to refresh the view
            view.refresh(playModeGrid, time);
            view.updateHeroPosition(heroTile.getLeftSide(), heroTile.getTopSide());
            view.pauseButton.setOnAction(e -> togglePause());
            monsterManager.setPlayModeView(view);
        }
    }
    
    public void start(Stage primaryStage) {
        initializePlayMode();
        initializeSoundEffects();
        Tile heroTile = playModeGrid.findTileWithIndex(hero.getPosX(), hero.getPosY());
        // If view is null (which means we are in the first hall), create a new one
        if (view == null){
            view = new PlayModeView(playModeGrid, time, primaryStage);
            view.updateHeroPosition(heroTile.getLeftSide(), heroTile.getTopSide());
            view.pauseButton.setOnAction(e -> togglePause());
            monsterManager.setPlayModeView(view);
        }

        Scene scene = view.getScene();
        initialize(scene);
        
        primaryStage.setTitle("Play Mode");
        primaryStage.setScene(scene);
        // primaryStage.setFullScreen(true);
        // primaryStage.setFullScreenExitHint("");
        primaryStage.show();
        
        startGameLoop();
    }
    
    private void initializeSoundEffects() {
        soundPlayer.addSoundEffect("step", "src/main/sounds/step.wav");
        soundPlayer.setVolume("step", -10);
        soundPlayer.addSoundEffect("door", "src/main/sounds/door.wav");
        soundPlayer.setVolume("door", -10);
        soundPlayer.addSoundEffect("gameWinner", "src/main/sounds/gameWinner.wav");
        soundPlayer.setVolume("gameWinner", -15);
        soundPlayer.addSoundEffect("gameLoser", "src/main/sounds/gameLoser.wav");
        soundPlayer.setVolume("gameLoser", -15);
        soundPlayer.addSoundEffect("archer", "src/main/sounds/archer.wav");
        soundPlayer.addSoundEffect("fighter", "src/main/sounds/fighter.wav");
        soundPlayer.addSoundEffect("wizard", "src/main/sounds/wizard.wav");
        soundPlayer.setVolume("wizard", -10);
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
    
    
    private void handleKeyPressed(KeyCode code) {
        //System.out.println("Key Pressed: " + code); // Debugging statement
        switch (code) {
            case UP, W -> upPressed = true;
            case DOWN, S -> downPressed = true;
            case LEFT, A -> leftPressed = true;
            case RIGHT, D -> rightPressed = true;
            case ESCAPE -> {
                if (!escPressedFlag) {
                    togglePause();
                    escPressedFlag = true; 
                }   
            }         
                default -> {
                //System.out.println("Unhandled Key Pressed: " + code);
            }
        }
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
                if (lastFrameTime > 0 && now - lastFrameTime < FRAME_DURATION_NANOS) {
                    return;
                }
                lastFrameTime = now;

                if (lastUpdateTime == 0) {
                    lastUpdateTime = now;
                } 
                if (lastMonsterSpawnTime == 0) {
                    lastMonsterSpawnTime = now;
                }
                
                if (time < 0) {
                    view.showGameOverPopup(false);
                    playSoundEffectInThread("gameLoser");
                    stopGameLoop();
                    return;
                }
                
                if (hero.getLiveCount() <= 0) {
                    view.showGameOverPopup(false);
                    playSoundEffectInThread("gameLoser");
                    stopGameLoop();
                    return;
                }
                
                if (now - lastUpdateTime >= ONE_SECOND_IN_NANOS) {
                    view.updateTime(time); // Update the view
                    time--;
                    lastUpdateTime = now;
                }
                
                moveHero();
                
                view.changeHeroSprite(getHeroImage());
                
                //monster spawn logic
                if (now - lastMonsterSpawnTime >= MONSTER_UPDATE_INTERVAL) {
                    Tile initialMonsterTile = getRandomEmptyTile();
                    
                    int randomXCoordinate = playModeGrid.findXofTile(initialMonsterTile);
                    int randomYCoordinate = playModeGrid.findYofTile(initialMonsterTile);
                    
                    monsterManager.createMonster(randomXCoordinate, randomYCoordinate);     
                    
                    lastMonsterSpawnTime = now; 
                }
                
                monsterManager.moveAllMonsters(now);
                
                if (mouseClicked) {
                    if (playModeGrid.coordinatesAreInGrid(mouseX, mouseY)) {
                        Tile clickedTile = playModeGrid.findTileUsingCoordinates(mouseX, mouseY);
                        
                        if (checkRune(clickedTile)) {
                            stopGameLoop();
                            hero.isMoving = false;
                            lastMonsterSpawnTime = 0;
                            lastUpdateTime = 0;
                            
                            if(hallType == HallType.FIRE){
                                playSoundEffectInThread("gameWinner");
                            }
                            else{
                                playSoundEffectInThread("door");
                                try {
                                    Thread.sleep(1500);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            initializePlayMode();
                            startGameLoop();
                        }
                    }
                    
                    mouseClicked = false;
                }
                
            }
        };
        gameLoop.start();
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
        if (isRunning){
            stopGameLoop();
            view.showPauseGame();
        }
        else {
            startGameLoop();
            view.hidePauseGame();
        }
    }

    public Hero initializeHero(int xCoordinate, int yCoordinate) {
        Hero hero = new Hero(xCoordinate, yCoordinate);
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
                
                playSoundEffectInThread("step");        
            } else if (downPressed && isWalkableTile(playModeGrid.findSouthTile(heroTile))) {
                heroTile.changeTileType('?');
                Tile southTile = playModeGrid.findSouthTile(heroTile);
                if (southTile != null) {
                    southTile.changeTileType('?');
                }
                hero.targetY = hero.currentY + tileHeight;
                hero.isMoving = true;
                hero.movingDirection = Directions.SOUTH;
                
                playSoundEffectInThread("step");        
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
                
                playSoundEffectInThread("step");        
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
                
                playSoundEffectInThread("step");        
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
    
    public Image getHeroImage() {
        if (hero.getCharType() == 'R') {
            return Images.IMAGE_PLAYERRIGHT_x4;
        } else if (hero.getCharType() == 'L') {
            return Images.IMAGE_PLAYERLEFT_x4;
        }else {
            return null;
        }
    }
    
    private double calculateDistance(int x1, int y1, int x2, int y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }
    
    private void  teleportRune() {
        SecureRandom rng = new SecureRandom();
        ArrayList<Tile> hallObjects = getHallObjectTiles();
        
        int luckyHallObjectIndex = rng.nextInt(hallObjects.size());
        Tile luckyHallObjectTile = hallObjects.get(luckyHallObjectIndex);
        
        int newRuneX = playModeGrid.findXofTile(luckyHallObjectTile);
        int newRuneY = playModeGrid.findYofTile(luckyHallObjectTile);
        
        runeXCoordinate = newRuneX;
        runeYCoordinate = newRuneY;
    }
    
    private void attackHeroIfInRange(Monster monster) {
        double distance = calculateDistance(hero.getPosX(), hero.getPosY(), monster.getX(), monster.getY());
        
        // FIGHTER attacks if adjacent
        if (monster.getType() == MonsterType.FIGHTER && distance <= 1) {
            hero.decreaseLives();
            view.updateHeroLife(hero.getLiveCount());
            playSoundEffectInThread("fighter");        
        }
        
        // ARCHER attacks if within 3 tiles
        if (monster.getType() == MonsterType.ARCHER && distance <= 3) {
            hero.decreaseLives();
            view.updateHeroLife(hero.getLiveCount());
            playSoundEffectInThread("archer");        
        }
        
        // Game over check
        // if (hero.getLiveCount() <= 0) {
        // 	view.showGameOver();
        // 	System.out.println("Game Over!");
        // }
    }
    
    
    public void moveCharacter(Monster monster) {
        if (!monster.getIsMoving()) {
            // Determine a new direction
            Directions newDirection = getRandomDirection(monster);
            monster.setMovingDirection(newDirection);
            monster.setIsMoving(true);
        }
        
        // Move the monster based on its current direction
        Directions direction = monster.getMovingDirection();
        if (direction != null) {
            Tile currentTile = playModeGrid.findTileWithIndex(monster.getX(), monster.getY());
            Tile targetTile = getTargetTile(currentTile, direction);
            
            if (isWalkableTile(targetTile)) {
                updateMonsterPosition(monster, targetTile);
                attackHeroIfInRange(monster);
            } else {
                monster.setIsMoving(false); // Reset movement if the target is not walkable
            }
        }
    }
    
    public void playSoundEffectInThread(String label) {
        new Thread(() -> {
            soundPlayer.playSoundEffect(label);
        }).start();
    }    
    
    private Directions getRandomDirection(Monster monster) {
        Random random = new Random();
        Directions[] directions = Directions.values();
        return directions[random.nextInt(directions.length)];
    }
    
    private Tile getTargetTile(Tile currentTile, Directions direction) {
        return switch (direction) {
            case NORTH -> playModeGrid.findNorthTile(currentTile);
            case SOUTH -> playModeGrid.findSouthTile(currentTile);
            case EAST -> playModeGrid.findEastTile(currentTile);
            case WEST -> playModeGrid.findWestTile(currentTile);
        };
    }
    
    private void updateMonsterPosition(Monster monster, Tile targetTile) {
        int targetX = playModeGrid.findXofTile(targetTile);
        int targetY = playModeGrid.findYofTile(targetTile);
        
        // Update the grid and monster position
        playModeGrid.changeTileWithIndex(monster.getX(), monster.getY(), 'E'); // Clear old position
        playModeGrid.changeTileWithIndex(targetX, targetY, monster.getCharType()); // Set new position
        monster.setX(targetX);
        monster.setY(targetY);
        
        // Update the view
        Rectangle monsterView = monster.getMonsterView();
        view.updateMonsterPosition(monsterView, targetTile.getLeftSide(), targetTile.getTopSide());
        
        // Mark movement as completed
        monster.setIsMoving(false);
    }
    
    
    public boolean checkRune(Tile tile) {
        if ((playModeGrid.findXofTile(tile) == runeXCoordinate) 
        && (playModeGrid.findYofTile(tile) == runeYCoordinate)) {
            if ((Math.abs(runeXCoordinate - hero.getPosX()) <= 1) 
            && (Math.abs(runeYCoordinate - hero.getPosY()) <= 1)) {
                return true;
            }
        }
        return false;
    }
    
    public void moveMonsterDirection(Directions dir,Monster monster) {
        int xIndexOld = monster.getX();
        int yIndexOld = monster.getY();
        playModeGrid.changeTileWithIndex(xIndexOld, yIndexOld, 'E');
        
        monster.move(dir);
        
        int xIndexNew = monster.getX();
        int yIndexNew = monster.getY();
        playModeGrid.changeTileWithIndex(xIndexNew, yIndexNew, monster.getCharType());
    }
    
    public Monster createMonster(int xCoordinate, int yCoordinate, MonsterType type,Tile monsterTile) {
        Monster monster = null;
        switch (type) {
            case MonsterType.FIGHTER -> {
                monster = new FighterMonster(xCoordinate,yCoordinate,monsterTile);
            }
            case MonsterType.ARCHER -> {
                monster = new ArcherMonster(xCoordinate,yCoordinate,monsterTile);
            }
            case MonsterType.WIZARD -> {
                monster = new WizardMonster(xCoordinate,yCoordinate,monsterTile);
            }
        }
        monsters.add(monster);
        playModeGrid.changeTileWithIndex(monster.getX(), monster.getY(), monster.getCharType());
        return monster;
    }
    
    public boolean isNearHero(Tile otherTile, int n) {
        int otherTileX = playModeGrid.findXofTile(otherTile);
        int otherTileY = playModeGrid.findYofTile(otherTile);
        double euclideanDistance = 
        Math.sqrt((hero.getPosX() - otherTileX) * (hero.getPosX() - otherTileX) + 
        (hero.getPosY() - otherTileY) * (hero.getPosY() - otherTileY));
        
        return (euclideanDistance <= n);
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
        Grid.isEmptyTileType(c);
    }
    
    public boolean isWalkableTile(Tile tile){
        return Grid.isWalkableTile(tile);
    }
    
    public static boolean isWalkableTileType(char c) {
        return Grid.isWalkableTileType(c);
    }
    
    //
    
    public Grid getPlayModeGrid() {
        return this.playModeGrid;
    }
    
    public int getTopLeftXCoordinate() {
        return this.topLeftXCoordinate;
    }
    
    public int getTopLeftYCoordinate() {
        return this.topLeftYCoordinate;
    }
}


