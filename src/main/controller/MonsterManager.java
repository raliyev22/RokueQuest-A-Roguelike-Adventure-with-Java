package main.controller;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.Image;
import javax.swing.text.View;
import main.model.*;
import main.utils.*;
import main.view.PlayModeView;

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
    
    protected Monster createMonster(int xPosition, int yPosition, MonsterType type, long now) {
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
        playModeView.createMonsterView(monster);
        // playModeView.updateMonsterPosition(monsterList.size() - 1, xPosition, yPosition);
        playModeView.redrawTallItems();

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
}
