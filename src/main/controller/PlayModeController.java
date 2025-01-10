package main.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import main.Main;
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
    
    public static double time;
    public static double totalTime;
    
    protected int hallTimeMultiplier = 500;
    private long lastUpdateTime = 0; // Tracks the last time the timer was updated
    private static final long ONE_SECOND_IN_NANOS = 1_000_000_000L; // One second in nanoseconds

    private long remainingMonsterSpawnTime;
    private long lastMonsterSpawnTime = 0;
    private static final long MONSTER_SPAWN_INTERVAL = 3_000_000_000L; // 8 seconds in nanoseconds

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
            view.refresh(playModeGrid, time);
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
            view = new PlayModeView(playModeGrid, time, primaryStage);
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
    }
    
    
    private void handleKeyPressed(KeyCode code) {
        if (isCountdownRunning) {
            return;
        }
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
                adjustedNow = now - totalPausedTime;

                if (lastFrameTime > 0 && adjustedNow - lastFrameTime < FRAME_DURATION_NANOS) {
                    return;
                }
                lastFrameTime = adjustedNow;               

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
                    view.showGameOverPopup(false);
                    soundPlayer.playSoundEffectInThread("gameLoser");
                    stopGameLoop();
                    return;
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
                    hero.increaseTakingDamageAnimationCounter();

                    if (hero.getTakingDamageAnimationCounter() == 0) {
                        blinkHeroSprite();
                    }
                    
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
                        
                        if (checkRune(clickedTile)) {
                            stopGameLoop();
                            hero.isMoving = false;
                            lastMonsterSpawnTime = 0;
                            lastUpdateTime = 0;
                            
                            if(hallType == HallType.FIRE){
                                soundPlayer.playSoundEffectInThread("gameWinner");
                                view.showGameOverPopup(true);
                            }
                            else{
                                soundPlayer.playSoundEffectInThread("door");
                                try {
                                    Thread.sleep(1500);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                startGameLoop();
                            }
                            initializePlayMode();
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
        long now = System.nanoTime();
        if (now - lastToggleTime < TOGGLE_DEBOUNCE_DELAY) {
            return;
        }
        lastToggleTime = now;
        soundPlayer.playSoundEffectInThread("escButton");
        if (isRunning){
            stopGameLoop();
            view.showPauseGame();
            soundPlayer.pauseSoundEffect("background");
            view.saveButton.setOnAction(e -> save());
            pauseStartTime = System.nanoTime();
        }
        else {
            totalPausedTime += now - pauseStartTime;
            startGameLoop();
            view.hidePauseGame();
            soundPlayer.resumeSoundEffect("background");
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
    
    // private double calculateDistance(int x1, int y1, int x2, int y2) {
    //     return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    // }
    
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
        soundPlayer.playSoundEffectInThread("blueButtons");
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
        StringBuilder str = new StringBuilder(saves.get(saves.size()-1));
        str.delete(0,4);
        str.delete(str.length()-4,str.length());
        int num = Integer.valueOf(str.toString()) + 1;
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
            
            view = new PlayModeView(playModeGrid, time, primaryStage);
            
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

}


