package main.controller;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import main.model.*;
import main.utils.*;
import main.view.GameOverView;
import main.view.PlayModeView;

public class PlayModeController extends Application {
	protected final int ROW = 10;
	protected final int COLUMN = 9;
	protected final int tileWidth = 64;
	protected final int tileHeight = 64;
	protected final int topLeftXCoordinate = 100;
	protected final int topLeftYCoordinate = 150;
	private final double speed = 3;
	
	public static Grid earthHall;
	public static Grid airHall;
	public static Grid waterHall;
	public static Grid fireHall;

	public static int earthHallObjectCount;
	public static int airHallObjectCount;
	public static int waterHallObjectCount;
	public static int fireHallObjectCount;
	
	public Grid playModeGrid;
	protected Hero hero;
	protected ArrayList<Monster> monsters;
	protected HallType hallType;
	
	private double targetX,targetY;
	private double currentX,currentY;
	
	private int runeXCoordinate;
	private int runeYCoordinate;
	private boolean mouseClicked = false;
	private double mouseX;
	private double mouseY;
	
	protected int time;
	private long lastUpdateTime = 0; // Tracks the last time the timer was updated
	private static final long ONE_SECOND_IN_NANOS = 1_000_000_000L; // One second in nanoseconds

	private long lastMonsterUpdateTime = 0; // Tracks the last monster update time
    private static final long MONSTER_UPDATE_INTERVAL = 300_000_000L; // Monster movement update interval (500ms)


	private PlayModeView view;
	private boolean upPressed, downPressed, leftPressed, rightPressed;
	private boolean initialized = false;
	public boolean isPaused = false;

	private Random random=new Random();
	
	/*
	public PlayModeController() {
		initializePlayMode();
	}
	*/

	private void initializePlayMode() {
		playModeGrid = new Grid(ROW, COLUMN, tileWidth, tileHeight, topLeftXCoordinate, topLeftYCoordinate);
		
		if (null == this.hallType) {
			this.hallType = HallType.EARTH;
			playModeGrid.copyTileMap(earthHall);
			this.time = PlayModeController.earthHallObjectCount*5;
		} else switch (this.hallType) {
			case EARTH -> {
				this.hallType = HallType.AIR;
				playModeGrid.copyTileMap(airHall);
				this.time = PlayModeController.airHallObjectCount*5;
			}
			case AIR -> {
				this.hallType = HallType.WATER;
				playModeGrid.copyTileMap(waterHall);
				this.time = PlayModeController.waterHallObjectCount*5;
			}
			case WATER -> {
				this.hallType = HallType.FIRE;
				playModeGrid.copyTileMap(fireHall);
				this.time = PlayModeController.fireHallObjectCount*5;
			}
			default -> {
				this.hallType = HallType.EARTH;
				playModeGrid.copyTileMap(earthHall);
				this.time = PlayModeController.earthHallObjectCount*5;
			}
		}
		
		//playModeGrid.copyTileMap(earthHall);
		
		Tile initialHeroTile = getRandomEmptyTile();
		int randomXCoordinate = playModeGrid.findXofTile(initialHeroTile);
		int randomYCoordinate = playModeGrid.findYofTile(initialHeroTile);
		Tile heroTile = playModeGrid.findTileWithIndex(randomXCoordinate, randomYCoordinate);
		hero = initializeHero(randomXCoordinate, randomYCoordinate);
		
		monsters = new ArrayList<>();
		
		Tile runeTile = getRandomHallObjectTile();
		runeXCoordinate = playModeGrid.findXofTile(runeTile);
		runeYCoordinate = playModeGrid.findYofTile(runeTile);
		
		if (view == null){
			view = new PlayModeView(playModeGrid, time);
			view.updateHeroPosition(heroTile.getLeftSide(), heroTile.getTopSide());
		} else {
			view.refresh(playModeGrid, time);
			view.updateHeroPosition(heroTile.getLeftSide(), heroTile.getTopSide());
		}
	}
	
	public void start(Stage primaryStage) {
		initializePlayMode();
		
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
			default -> {
				//System.out.println("Unhandled Key Released: " + code);
			}
		}
	}
	
	
	private void startGameLoop() {
		AnimationTimer gameLoop = new AnimationTimer() {
			private boolean isMoving = false;
			Directions movingDirection = null;

			
			private long lastMonsterSpawnTime = 0;
			private static final long MONSTER_SPAWN_INTERVAL = 8_000_000_000L; // 8 seconds in nanoseconds
			private boolean monsterInitialized = false;

			private int counter = -1;
			

			
			@Override
			public void handle(long now) {
				if (time < 0) {
                    view.showGameOver();
                    this.stop();
                    return;
                }

				if (hero.getLiveCount() == 0) {
                    view.showGameOver();
                    this.stop();
                    return;
                }


				if (now - lastUpdateTime >= ONE_SECOND_IN_NANOS) {
        			view.updateTime(time); // Update the view
					time--;
        			lastUpdateTime = now;
					counter++;
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
						moveHeroDirection(movingDirection);
						movingDirection = null;
					}
				}

				

				//monster spawn logic
				if (now - lastMonsterSpawnTime >= MONSTER_SPAWN_INTERVAL && counter>=8) {

					int randomInt = random.nextInt(3);

					Monster monster = null;
					Tile initialMonsterTile = getRandomEmptyTile();

					int randomXCoordinate = playModeGrid.findXofTile(initialMonsterTile);
					int randomYCoordinate = playModeGrid.findYofTile(initialMonsterTile);
					Tile monsterTile = playModeGrid.findTileWithIndex(randomXCoordinate, randomYCoordinate);

					Rectangle monsterView = new Rectangle(64,64);

					switch (randomInt) {
						case 0:
							monster = createMonster(randomXCoordinate, randomYCoordinate, MonsterType.FIGHTER,monsterTile);
							monsterView.setFill(new ImagePattern(Images.IMAGE_FIGHTER_x4));
							monster.setMonsterView(monsterView);
							break;
						case 1:
							monster = createMonster(randomXCoordinate, randomYCoordinate, MonsterType.ARCHER,monsterTile);
							monsterView.setFill(new ImagePattern(Images.IMAGE_ARCHER_x4));
							monster.setMonsterView(monsterView);
							break;
						case 2:
							monster = createMonster(randomXCoordinate, randomYCoordinate, MonsterType.WIZARD,monsterTile);
							monsterView.setFill(new ImagePattern(Images.IMAGE_WIZARD_x4));
							monster.setMonsterView(monsterView);
							break;
						default:
							monster = createMonster(randomXCoordinate, randomYCoordinate, MonsterType.FIGHTER,monsterTile);
							monsterView.setFill(new ImagePattern(Images.IMAGE_FIGHTER_x4));
							monster.setMonsterView(monsterView);
					}

					
					view.updateMonsterPosition(monsterView,monsterTile.getLeftSide(), monsterTile.getTopSide());
					view.addToPane(monsterView);

					lastMonsterSpawnTime = now; 
				}

				if (now - lastMonsterUpdateTime >= MONSTER_UPDATE_INTERVAL) {
					for(Monster monster : monsters){
						switch(monster.getType()){
							case MonsterType.FIGHTER:
								moveCharacter(monster);
								break;
							case MonsterType.ARCHER:
								moveCharacter(monster);
								break;
							case MonsterType.WIZARD:
								break;
	
						}
					}
					lastMonsterUpdateTime = now;
				}


				
				

				//monster movement
				


				
				if (mouseClicked) {
					if (playModeGrid.coordinatesAreInGrid(mouseX, mouseY)) {
						Tile clickedTile = playModeGrid.findTileUsingCoordinates(mouseX, mouseY);
						
						if (checkRune(clickedTile)) {
							initialized = false;
							isMoving = false;
							counter=-1;
							initializePlayMode();
							
						}
					}
					
					mouseClicked = false;
				}
				
			}
		};
		gameLoop.start();
	}

	private double calculateDistance(int x1, int y1, int x2, int y2) {
		return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
	}

	private void attackHeroIfInRange(Monster monster) {
		double distance = calculateDistance(hero.getPosX(), hero.getPosY(), monster.getX(), monster.getY());
	
		// FIGHTER attacks if adjacent
		if (monster.getType() == MonsterType.FIGHTER && distance <= 1) {
			hero.decreaseLives();
			view.updateHeroLife(hero.getLiveCount());
		}
	
		// ARCHER attacks if within 3 tiles
		if (monster.getType() == MonsterType.ARCHER && distance <= 3) {
			hero.decreaseLives();
			view.updateHeroLife(hero.getLiveCount());
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
	
	public void moveHeroDirection(Directions dir) {
		int xIndexOld = hero.getPosX();
		int yIndexOld = hero.getPosY();
		playModeGrid.changeTileWithIndex(xIndexOld, yIndexOld, 'E');
		
		hero.move(dir);
		
		int xIndexNew = hero.getPosX();
		int yIndexNew = hero.getPosY();
		playModeGrid.changeTileWithIndex(xIndexNew, yIndexNew, getHeroCharType());
		//System.out.println(playModeGrid);
	}

	public char getHeroCharType() {
		return this.hero.getCharType();
	}

	public Image getHeroImage() {
		if (getHeroCharType() == 'R') {
			return Images.IMAGE_PLAYERRIGHT_x4;
		} else if (getHeroCharType() == 'L') {
			return Images.IMAGE_PLAYERLEFT_x4;
		}else {
			return null;
		}
	}

	public void moveMonsterDirection(Directions dir,Monster monster) {
		int xIndexOld = monster.getX();
		int yIndexOld = monster.getY();
		playModeGrid.changeTileWithIndex(xIndexOld, yIndexOld, 'E');
		
		monster.move(dir);
		
		int xIndexNew = monster.getX();
		int yIndexNew = monster.getY();
		playModeGrid.changeTileWithIndex(xIndexNew, yIndexNew, monster.getCharType());
		System.out.println(playModeGrid);
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
	
	public static boolean isHallObjectTile(Tile tile) {
		return isHallObjectTileType(tile.getTileType());
	}
	
	public static boolean isHallObjectTileType(char c) {
		if (c == 'B' || c == 'C' || c == 'D' || c == 'G' || c == 'H'
		|| c == 'J' || c == 'K' || c == 'M' || c == 'P' || c == 'S' || c == 'T'){
			return true;
		}
		return false;
	}
	
	public ArrayList<Tile> getHallObjectTiles() {
		ArrayList<Tile> hallObjectTiles = new ArrayList<>();
		
		for (Tile tile: playModeGrid.getTileMap()) {
			if (isHallObjectTile(tile)){
				hallObjectTiles.add(tile);
			}
		}
		
		return hallObjectTiles;
	}
	
	public Tile getRandomHallObjectTile() {
		SecureRandom rng = new SecureRandom();
		ArrayList<Tile> hallObjectTiles = getHallObjectTiles();
		
		int luckyTileInd = rng.nextInt(hallObjectTiles.size());
		
		return hallObjectTiles.get(luckyTileInd);
	}
	
	public static boolean isEmptyTileType(char c) {
		if (c == 'E' || c == 'e') {
			return true;
		}
		return false;
	}
	
	public boolean isWalkableTile(Tile tile){
		if(tile == null){
			return false;
		}
		return isWalkableTileType(tile.getTileType());
	}
	
	public static boolean isWalkableTileType(char c) {
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

		// Grid earthHall1 = new Grid(10, 9, 64, 64, 0, 0);
		// earthHall1.changeTileWithIndex(5, 5, 'P');
		// earthHall1.changeTileWithIndex(0, 5, 'B');
		// earthHall1.changeTileWithIndex(0, 0, 'B');
		// earthHall1.changeTileWithIndex(0, 8, 'B');
		// earthHall1.changeTileWithIndex(7, 7, 'B');
		// PlayModeController.earthHall = earthHall1;
		
		// Grid airHall1 = new Grid(10, 9, 64, 64, 0, 0);
		// airHall1.changeTileWithIndex(3, 5, 'H');
		// airHall1.changeTileWithIndex(2, 5, 'B');
		// airHall1.changeTileWithIndex(5, 6, 'S');
		// airHall1.changeTileWithIndex(0, 8, 'T');
		// airHall1.changeTileWithIndex(7, 2, 'D');
		// PlayModeController.airHall = airHall1;
		
		// Grid waterHall1 = new Grid(10, 9, 64, 64, 0, 0);
		// waterHall1.changeTileWithIndex(2, 5, 'T');
		// waterHall1.changeTileWithIndex(0, 5, 'B');
		// waterHall1.changeTileWithIndex(2, 3, 'J');
		// waterHall1.changeTileWithIndex(0, 8, 'S');
		// waterHall1.changeTileWithIndex(7, 7, 'B');
		// PlayModeController.waterHall = waterHall1;
		
		
		// Grid fireHall1 = new Grid(10, 9, 64, 64, 0, 0);
		// fireHall1.changeTileWithIndex(2, 5, 'H');
		// fireHall1.changeTileWithIndex(0, 5, 'B');
		// fireHall1.changeTileWithIndex(9, 0, 'S');
		// fireHall1.changeTileWithIndex(7, 8, 'T');
		// fireHall1.changeTileWithIndex(7, 7, 'D');
		// PlayModeController.fireHall = fireHall1;

		launch(args);
	}
	
	
}


