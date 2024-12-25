package main.controller;

import java.awt.Panel;
import java.security.SecureRandom;
import java.util.ArrayList;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.paint.ImagePattern;
import javafx.stage.Stage;
import main.model.*;
import main.utils.*;
import main.view.PlayModeView;

public class PlayModeController extends Application{
    protected final int ROW = 10;
    protected final int COLUMN = 9;
    protected final int tileWidth = 64;
    protected final int tileHeight = 64;
    protected final int topLeftXCoordinate = 80;
    protected final int topLeftYCoordinate = 80;
    private final double speed = 5;
    
    public static Grid earthHall;
    public static Grid airHall;
    public static Grid waterHall;
    public static  Grid fireHall;
    
    public Grid playModeGrid;
    protected Hero hero;
    protected ArrayList<Monster> monsters;
    protected HallType hallType;

    private double targetX,targetY;
    private double currentX,currentY;

    protected int time;
    private PlayModeView view;
    private boolean upPressed, downPressed, leftPressed, rightPressed;

    public PlayModeController() {
        playModeGrid = new Grid(ROW, COLUMN, tileWidth, tileHeight, topLeftXCoordinate, topLeftYCoordinate);

        hallType = HallType.EARTH;
        playModeGrid.copyTileMap(earthHall);
        
        Tile initialHeroTile = getRandomEmptyTile();
        int randomXCoordinate = playModeGrid.findXofTile(initialHeroTile);
        int randomYCoordinate = playModeGrid.findYofTile(initialHeroTile);
        Tile heroTile = playModeGrid.findTileWithIndex(randomXCoordinate, randomYCoordinate);
        hero = initializeHero(randomXCoordinate, randomYCoordinate);
        
        monsters = new ArrayList<>();
        view = new PlayModeView(playModeGrid);
        view.updateHeroPosition(heroTile.getLeftSide(), heroTile.getTopSide());
    }

    public void start(Stage primaryStage) {
        Scene scene = view.getScene();
        initialize(scene);

        primaryStage.setTitle("Play Example");
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.setFullScreenExitHint("");
        primaryStage.show();
        startGameLoop();
    }
    
    private void initialize(Scene scene) {
        scene.setOnKeyPressed(event -> handleKeyPressed(event.getCode()));
        scene.setOnKeyReleased(event -> handleKeyReleased(event.getCode()));
    }

    private void handleKeyPressed(KeyCode code) {
        switch (code) {
            case UP, W -> upPressed = true;
            case DOWN, S -> downPressed = true;
            case LEFT, A -> leftPressed = true;
            case RIGHT, D -> rightPressed = true;
            default -> {
                System.out.println("Unhandled Key Pressed: " + code);
            }
        }
    }
    
    private void handleKeyReleased(KeyCode code) {
        switch (code) {
            case UP, W -> upPressed = false;
            case DOWN, S -> downPressed = false;
            case LEFT, A -> leftPressed = false;
            case RIGHT, D -> rightPressed = false;
            default -> {
                System.out.println("Unhandled Key Released: " + code);
            }
        }
    }
    

    private void startGameLoop() {
        AnimationTimer gameLoop = new AnimationTimer() {
            private boolean isMoving = false;
            boolean initialized = false;
            Directions movingDirection = null;

            @Override
            public void handle(long now) {
                if (hero == null) {
                    return;
                }

                Tile heroTile = playModeGrid.findTileWithIndex(hero.getPosX(), hero.getPosY());
                double heroViewLeftSide = heroTile.getLeftSide();
                double heroViewTopSide = heroTile.getTopSide();

                if(!isMoving){
                    currentX = heroViewLeftSide;
                    currentY = heroViewTopSide;
                }

                if (!initialized) {
                    targetX = heroViewLeftSide;
                    targetY = heroViewTopSide;
                    initialized = true;
                }

                if (!isMoving) {
                    if (upPressed && isWalkableTile(playModeGrid.findNorthTile(heroTile))) {
                        targetY = currentY - tileHeight;
                        isMoving = true;
                        movingDirection = Directions.NORTH;
                    } else if (downPressed && isWalkableTile(playModeGrid.findSouthTile(heroTile))) {
                        targetY = currentY + tileHeight;
                        isMoving = true;
                        movingDirection = Directions.SOUTH;
                    } else if (leftPressed && isWalkableTile(playModeGrid.findWestTile(heroTile))) {
                        targetX = currentX - tileHeight;
                        isMoving = true;
                        hero.setFacingDirection(Directions.WEST);
                        movingDirection = Directions.WEST;
                    } else if (rightPressed && isWalkableTile(playModeGrid.findEastTile(heroTile))) {
                        targetX = currentX + tileHeight;
                        isMoving = true;
                        hero.setFacingDirection(Directions.EAST);
                        movingDirection = Directions.EAST;
                    }
                }
    
                if (currentX < targetX) {
                    currentX = Math.min(currentX + speed, targetX);
                    view.updateHeroPosition(currentX, currentY);
                } else if (currentX > targetX) {
                    currentX = Math.max(currentX - speed, targetX);
                    view.updateHeroPosition(currentX, currentY);
                }
    
                if (currentY < targetY) {
                    currentY = Math.min(currentY + speed, targetY);
                    view.updateHeroPosition(currentX, currentY);
                } else if (currentY > targetY) {
                    currentY = Math.max(currentY - speed, targetY);
                    view.updateHeroPosition(currentX, currentY);
                }
    
                if (currentX == targetX && currentY == targetY) {
                    isMoving = false;
                    if (movingDirection != null) {
                        hero.move(movingDirection);
                        movingDirection = null;
                    }
                }
            }
        };
        gameLoop.start();
    }
    

    public Hero initializeHero(int xCoordinate, int yCoordinate) {
        Hero hero = new Hero(xCoordinate, yCoordinate);
        playModeGrid.changeTileWithIndex(hero.getPosX(), hero.getPosY(), hero.getCharType());
        return hero;
    }

    public void setHeroDirection(Directions direction) {
        if (direction.equals(Directions.WEST)) {
            hero.setFill(new ImagePattern(Images.IMAGE_PLAYERLEFT_x4));
        } else if (direction.equals(Directions.EAST)) {
            hero.setFill(new ImagePattern(Images.IMAGE_PLAYERRIGHT_x4));
        }   
    }
    
    public Monster createMonster(int xCoordinate, int yCoordinate, MonsterType type) {
        Monster monster = null;
        switch (type) {
            case MonsterType.FIGHTER -> {
                monster = new FighterMonster(xCoordinate,yCoordinate);
            }
            case MonsterType.ARCHER -> {
                monster = new ArcherMonster(xCoordinate,yCoordinate);
            }
            case MonsterType.WIZARD -> {
                monster = new WizardMonster(xCoordinate,yCoordinate);
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
        SecureRandom rng = new SecureRandom();
        ArrayList<Tile> emptyTiles = getEmptyTiles();
        
        int luckyTileInd = rng.nextInt(emptyTiles.size());
        
        return emptyTiles.get(luckyTileInd);
    }
    
    public ArrayList<Tile> getEmptyTiles() {
        ArrayList<Tile> emptyTiles = new ArrayList<>();
        
        for (Tile tile: playModeGrid.getTileMap()) {
            if (isEmptyTileType(tile.getTileType())){
                emptyTiles.add(tile);
            }
        }
        
        return emptyTiles;
    }
    
    public boolean isEmptyTileType(char c) {
        if (c == 'E' || c == 'e') {
            return true;
        }
        return false;
    }

    public boolean isWalkableTile(Tile tile){
        if(tile==null){
            return false;
        }
        return isWalkableTileType(tile.getTileType());
    }
    
    public boolean isWalkableTileType(char c) {
        if (c == 'E' || c == 'e') {
            return true;
        }
        return false;
    }
    
    public boolean checkHeroDirection(Directions dir) {
        boolean canWalk = false;
        Tile heroTile = playModeGrid.findTileWithIndex(hero.getPosX(), hero.getPosY());

        switch (dir) {
            case Directions.NORTH -> {
                Tile aboveTile = playModeGrid.findNorthTile(heroTile);
                if (aboveTile != null) {
                    if (isWalkableTileType(aboveTile.getTileType())) {
                        canWalk = true;
                        return true;
                    }
                }
            }
            case Directions.EAST -> {
                Tile rightTile = playModeGrid.findEastTile(heroTile);
                if (rightTile != null) {
                    if (isWalkableTileType(rightTile.getTileType())) {
                        canWalk = true;
                        return true;
                    }
                }
            }
            case Directions.SOUTH -> {
                Tile belowTile = playModeGrid.findSouthTile(heroTile);
                if (belowTile != null) {
                    if (isWalkableTileType(belowTile.getTileType())) {
                        canWalk = true;
                        return true;
                    }
                }
            }
            case Directions.WEST -> {
                Tile leftTile = playModeGrid.findWestTile(heroTile);
                if (leftTile != null) {
                    if (isWalkableTileType(leftTile.getTileType())) {
                        canWalk = true;
                        return true;
                    }
                }
            }
            default -> throw new AssertionError();
        }

        return canWalk;
    }
    
    public Grid updateGrid() {
        this.playModeGrid = new Grid(ROW, COLUMN, tileWidth, tileHeight, topLeftXCoordinate, topLeftYCoordinate);
        for (Monster monster : monsters) {
            this.playModeGrid.changeTileWithIndex(monster.getX(), monster.getY(), monster.getCharType());
        }
        this.playModeGrid.changeTileWithIndex(hero.getPosX(), hero.getPosY(), hero.getCharType());
        return this.playModeGrid;
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

    public static void main(String[] args) {
        Grid earthHall1 = new Grid(10, 9, 64, 64, 0, 0);
        earthHall1.changeTileWithIndex(5, 5, 'P');        
        earthHall1.changeTileWithIndex(0, 5, 'b');        
        PlayModeController.earthHall = earthHall1;

        launch(args);
    }
    
    
}


