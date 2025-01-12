package main.controller;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.Image;
import javax.swing.text.View;
import main.model.*;
import main.utils.*;
import main.view.PlayModeView;

/**
 * Overview: Manages all monsters in the game, including their creation, positions, movements, 
 * and interactions with the hero. Provides methods to create monsters, move them across the grid, 
 * and handle their specific behaviors (e.g., attacking the hero or teleporting runes).
 */
/**
 * Abstract Function: 
 * AF(c) = A mapping between each monster `m` in `c.monsterList` and a position `(x, y)` on the grid `c.playModeGrid`.
 * - Each monster is represented on the grid at its current position.
 * - The list `c.monsterList` maintains the active monsters in the game.
 * - The grid `c.playModeGrid` represents the current state of the game world, including monsters and obstacles.
 */
/**
 * Representation Invariant: 
 * RI(c) = 
 * - No two monsters can occupy the same position at the same time on `c.playModeGrid`.
 * - All monsters in `c.monsterList` must have valid positions within the bounds of `c.playModeGrid`.
 * - `c.monsterList` should not contain null references.
 */

public class MonsterManager {
    // How much of the time monster stays in place, i.e. if it is 0.1,
    // monster does not move 90 percent of the time. Note that a monster 
    // cannot shoot while moving, so we want for them to stay still for some time.
    protected final double MONSTER_STAY_RATE = 0.8;
    protected final double MONSTER_MOVE_INTERVAL = 1_000_000_000L;
    protected final double MONSTER_WAIT_PERIOD = 1_000_000_000L;
    private static final long RUNE_TELEPORT_INTERVAL = 3_000_000_000L; // 3 seconds in nanoseconds
    
    protected Grid playModeGrid;
    protected Hero hero;
    protected List<Monster> monsterList;
    protected PlayModeView playModeView;
    private SoundEffects soundPlayer = SoundEffects.getInstance();
    
    public MonsterManager(Grid grid, Hero hero) {
        this.playModeGrid = grid;
        this.hero = hero;
        this.monsterList = new ArrayList<>();
    }
    
    public Monster createMonster(int xPosition, int yPosition, long now) {
        SecureRandom rng = new SecureRandom();
        
        int luckyType = rng.nextInt(3);
        
        MonsterType type = MonsterType.WIZARD;
        
        // luckyType = 0;
        if (luckyType == 0) {
            type = MonsterType.WIZARD;
        } else if (luckyType == 1) {
            type = MonsterType.ARCHER;
        } else if (luckyType == 2) {
            type = MonsterType.FIGHTER;
        }
        
        return createMonster(xPosition, yPosition, type, now);
    }
    
    
    /** 
     * Create a monster, add it to the grid and the list, and render it on the screen.
     * @param xPosition x index that the monster is created on. Must be between 0 and playModeGrid.rowLength - 1 (inclusive)
     * @param yPosition y index that the monster is created on. Must be between 0 and playModeGrid.columnLength - 1 (inclusive)
     * @param type type of the monster.
     * @param now the time that the monster is created at.
     * @return Monster the monster created.
     */
    public Monster createMonster(int xPosition, int yPosition, MonsterType type, long now) {
        Monster monster = null;
        
        switch (type) {
            case MonsterType.FIGHTER -> {
                monster = new FighterMonster(xPosition, yPosition);
            }
            case MonsterType.ARCHER -> {
                monster = new ArcherMonster(xPosition, yPosition);
            }
            case MonsterType.WIZARD -> {
                monster = new WizardMonster(xPosition, yPosition);
            }
            default -> throw new IllegalArgumentException("Invalid monster type");
        }

        monster.spawnTime = now;
        
        Tile monsterTile = playModeGrid.findTileWithIndex(xPosition, yPosition);
        monster.currentX = monsterTile.getLeftSide();
        monster.currentY = monsterTile.getTopSide();
        monster.targetX = monsterTile.getLeftSide();
        monster.targetY = monsterTile.getTopSide();
        
        playModeGrid.changeTileWithIndex(xPosition, yPosition, monster.getCharType());
        this.monsterList.add(monster);
        if (playModeView != null) {
            playModeView.createMonsterView(monster);
            playModeView.redrawTallItems();
        }        
        // playModeView.updateMonsterPosition(monsterList.size() - 1, xPosition, yPosition);

        return monster;
    }

    public void actAllMonsters(long now, PlayModeController controller) {
        for (int i = 0; i < monsterList.size(); i++) {
            Monster monster = monsterList.get(i);

            if (now - monster.spawnTime >= MONSTER_WAIT_PERIOD) {
                if (monster instanceof WizardMonster wizardMonster) {
                    long timeSinceLastAct = now - wizardMonster.getLastActTime();
                    if (timeSinceLastAct >= RUNE_TELEPORT_INTERVAL) {
                        wizardMonster.act(controller);
                        wizardMonster.setLastActTime(now);
                    }
                } else if (monster instanceof ArcherMonster archerMonster) {
                    archerAttack(now, archerMonster);
                } else if (monster instanceof FighterMonster fighterMonster) {
                    fighterAttack(now, fighterMonster);
                }
            }
        }
    }

    public void archerAttack(long now, ArcherMonster archerMonster) {
        if (!archerMonster.isMoving && !hero.isMoving && !hero.isTakingDamage) {
            if (isArcherInRange(archerMonster)) {
                hero.isTakingDamage = true;
                hero.decreaseLives();
                hero.lastDamagedFrame = now;
                playModeView.updateHeroLife(hero.getLiveCount());
                soundPlayer.playSoundEffectInThread("archer");
            }
        }
    }

    public boolean isArcherInRange(ArcherMonster archerMonster) {
        int manhattanDistance = Math.abs(hero.getPosX() - archerMonster.posX) + Math.abs(hero.getPosY() - archerMonster.posY);
        if (manhattanDistance <= ArcherMonster.ARCHER_RANGE) {
            return true;
        }
        return false;
    }

    public void fighterAttack(long now, FighterMonster fighterMonster) {
        if (!fighterMonster.isMoving && !hero.isMoving && !hero.isTakingDamage) {
            if (isFighterInRange(fighterMonster)) {
                hero.isTakingDamage = true;
                hero.decreaseLives();
                hero.lastDamagedFrame = now;
                playModeView.updateHeroLife(hero.getLiveCount());
                soundPlayer.playSoundEffectInThread("fighter");
            }
        }
    }

    public boolean isFighterInRange(FighterMonster fighterMonster) {
        int manhattanDistance = Math.abs(hero.getPosX() - fighterMonster.posX) + Math.abs(hero.getPosY() - fighterMonster.posY);
        if (manhattanDistance <= FighterMonster.FIGHTER_RANGE) {
            return true;
        }
        return false;
    }
    
    public void moveAllMonsters(long now) { 
        for (int i = 0; i < monsterList.size(); i++) {
            Monster monster = monsterList.get(i);

            if (monster.type == MonsterType.WIZARD) {
                continue;
            }

            if (monster.isMoving) {
                moveMonster(i);
            }
            if (now - monster.lastMovedTime >= MONSTER_MOVE_INTERVAL) {
                if(monster.lastMovedTime != 0){
                    moveMonster(i);
                }
                monster.lastMovedTime = now;
            }
        }
    }
    
    public void moveMonster(int monsterID) {
        Monster monster = monsterList.get(monsterID);
        if (monster == null) {
            return;
        }

        Tile monsterTile = playModeGrid.findTileWithIndex(monster.posX, monster.posY);
        int monsterViewLeftSide = monsterTile.getLeftSide();
        int monsterViewTopSide = monsterTile.getTopSide();
        
        if (!monster.isMoving) {
            monster.currentX = monsterViewLeftSide;
            monster.currentY = monsterViewTopSide;
        }
        
        if (!monster.isMoving) {
            Directions movementDirection = pickMovementDirection(monster);
            if (movementDirection != null) {
                monsterTile.changeTileType('?');
                
                if (movementDirection == Directions.NORTH) {
                    Tile northTile = playModeGrid.findNorthTile(monsterTile);
                    if (northTile != null) {
                        northTile.changeTileType('?');
                    }
                    monster.targetY = monster.currentY - playModeGrid.getTileHeight();
                } else if (movementDirection == Directions.SOUTH) {
                    Tile southTile = playModeGrid.findSouthTile(monsterTile);
                    if (southTile != null) {
                        southTile.changeTileType('?');
                    }
                    monster.targetY = monster.currentY + playModeGrid.getTileHeight();
                } else if (movementDirection == Directions.WEST) {
                    Tile westTile = playModeGrid.findWestTile(monsterTile);
                    if (westTile != null) {
                        westTile.changeTileType('?');
                    }
                    monster.targetX = monster.currentX - playModeGrid.getTileWidth();
                } else if (movementDirection == Directions.EAST) {
                    Tile eastTile = playModeGrid.findEastTile(monsterTile);
                    if (eastTile != null) {
                        eastTile.changeTileType('?');
                    }
                    monster.targetX = monster.currentX + playModeGrid.getTileWidth();
                } else {
                    System.err.println("moveMonster error");
                }

                monster.isMoving = true;
                monster.movingDirection = movementDirection;
            }
        }
        
        if (monster.currentX < monster.targetX) {
            monster.currentX = Math.min(monster.currentX + monster.speed, monster.targetX);
            playModeView.updateMonsterPosition(monsterID, monster.currentX, monster.currentY);
        } else if (monster.currentX > monster.targetX) {
            monster.currentX = Math.max(monster.currentX - monster.speed, monster.targetX);
            playModeView.updateMonsterPosition(monsterID, monster.currentX, monster.currentY);
        }
        
        if (monster.currentY < monster.targetY) {
            monster.currentY = Math.min(monster.currentY + monster.speed, monster.targetY);
            playModeView.updateMonsterPosition(monsterID, monster.currentX, monster.currentY);
        } else if (monster.currentY > monster.targetY) {
            monster.currentY = Math.max(monster.currentY - monster.speed, monster.targetY);
            playModeView.updateMonsterPosition(monsterID, monster.currentX, monster.currentY);
        }
        
        if (monster.currentX == monster.targetX && monster.currentY == monster.targetY) {
            monster.isMoving = false;
            if (monster.movingDirection != null) {
                // System.out.printf("Before moving x: %d y: %d%n", monster.posX, monster.posY);
                // System.out.println(playModeGrid);
                
                moveMonsterOnGrid(monster.movingDirection, monster);
                
                // System.out.printf("After moving x: %d y: %d%n", monster.posX, monster.posY);
                // System.out.println(playModeGrid);
                monster.movingDirection = null;
            }
        }
    }
    
    
    public Directions pickMovementDirection(Monster monster) {
        SecureRandom rng = new SecureRandom();
        
        if (rng.nextDouble() > MONSTER_STAY_RATE) {
            return null; // null means no direction
        }
        
        Tile monsterTile = playModeGrid.findTileWithIndex(monster.posX, monster.posY);
        if (monsterTile == null) {
            System.err.println("pickMovementDirection null pointer");
        }
        
        List<Directions> moveableDirections = playModeGrid.findWalkableDirections(monsterTile);
        int moveableDirectionCount = moveableDirections.size();
        
        if (moveableDirectionCount > 0) {
            int luckyDirectionIndex = rng.nextInt(moveableDirectionCount);
            return moveableDirections.get(luckyDirectionIndex);
        }

        return null;
    }
    
    public void moveMonsterOnGrid(Directions dir, Monster monster) {
        int xIndexOld = monster.posX;
        int yIndexOld = monster.posY;
        playModeGrid.changeTileWithIndex(xIndexOld, yIndexOld, 'E');
        
        monster.move(dir);
        
        int xIndexNew = monster.posX;
        int yIndexNew = monster.posY;
        playModeGrid.changeTileWithIndex(xIndexNew, yIndexNew, monster.getCharType());
    }
    
    public static Image getMonsterImage(Monster monster) {
        if (monster.getCharType() == 'A') {
            return Images.IMAGE_ARCHER_x4;
        } else if (monster.getCharType() == 'F') {
            return Images.IMAGE_FIGHTER_x4;
        } else if (monster.getCharType() == 'W') {
            return Images.IMAGE_WIZARD_x4;
        } else {
            return null;
        }
    }
    
    public void setPlayModeView(PlayModeView view) {
        this.playModeView = view;
    }

    /**
     * Checks the representation invariant of the MonsterManager class.
     * @return true if the representation invariant holds, false otherwise.
     */
    public boolean repOk() {
        // Ensure no two monsters occupy the same position
        for (int i = 0; i < monsterList.size(); i++) {
            Monster monster1 = monsterList.get(i);
            for (int j = i + 1; j < monsterList.size(); j++) {
                Monster monster2 = monsterList.get(j);
                if (monster1.posX == monster2.posX && monster1.posY == monster2.posY) {
                    return false; // Two monsters occupy the same position
                }
            }
        }

        // Ensure all monsters are within grid bounds
        for (Monster monster : monsterList) {
            if (monster.posX < 0 || monster.posX >= playModeGrid.getRowLength() ||
                monster.posY < 0 || monster.posY >= playModeGrid.getColumnLength()) {
                return false; // Monster is out of grid bounds
            }
        }

        return true; // Representation invariant holds
    }

}
