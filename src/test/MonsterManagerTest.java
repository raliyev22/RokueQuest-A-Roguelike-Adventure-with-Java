package test;

import main.model.*;

import org.junit.Test;
import static org.junit.Assert.*;

import main.controller.*;
import main.utils.*;

public class MonsterManagerTest {

    @Test
    public void testRepOkWithValidState() {
        Grid grid = new Grid(10, 9, 64, 64, 100, 150);
        Hero hero = new Hero(0, 0);
        MonsterManager manager = new MonsterManager(grid, hero);
        
        manager.createMonster(1, 1, MonsterType.WIZARD, System.nanoTime());
        manager.createMonster(2, 2, MonsterType.ARCHER, System.nanoTime());
        
        assertTrue("repOk should return true for a valid state", manager.repOk());
    }

    @Test
    public void testRepOkWithInvalidState() {
        Grid grid = new Grid(10, 9, 64, 64, 100, 150);
        Hero hero = new Hero(0, 0);
        MonsterManager manager = new MonsterManager(grid, hero);
        
        // Create two monsters at the same position
        manager.createMonster(1, 1, MonsterType.WIZARD, System.nanoTime());
        manager.createMonster(1, 1, MonsterType.ARCHER, System.nanoTime());
        
        assertFalse("repOk should return false for an invalid state", manager.repOk());
    }
    
    @Test
    public void testRepOkWithEmptyMonsterList() {
        Grid grid = new Grid(10, 9, 64, 64, 100, 150);
        Hero hero = new Hero(0, 0);
        MonsterManager manager = new MonsterManager(grid, hero);
        
        assertTrue("repOk should return true for empty monster list", manager.repOk());
    }

    @Test
    public void testRepOkWithMonsterAtBoundary() {
        Grid grid = new Grid(10, 9, 64, 64, 100, 150);
        Hero hero = new Hero(0, 0);
        MonsterManager manager = new MonsterManager(grid, hero);

        // Create a monster exactly at the bottom-right boundary of the grid
        manager.createMonster(9, 8, MonsterType.ARCHER, System.nanoTime());

        assertTrue("repOk should return true for a monster placed at the boundary", manager.repOk());
    }

    @Test
    public void testRepOkWithSingleMonsterMovementIteration() {
        Grid grid = new Grid(10, 9, 64, 64, 100, 150);
        Hero hero = new Hero(0, 0);
        MonsterManager manager = new MonsterManager(grid, hero);

        // Create three monsters that are close to one another and at the border of the hall.
        manager.createMonster(9, 8, MonsterType.ARCHER, System.nanoTime());
        manager.createMonster(9, 7, MonsterType.FIGHTER, System.nanoTime());
        manager.createMonster(8, 8, MonsterType.WIZARD, System.nanoTime());

        manager.moveAllMonsters(System.nanoTime());

        assertTrue("repOk should return true after the monsters move", manager.repOk());
    }


    @Test
    public void testRepOkWithValidFighterMonsterAttack() {
        Grid grid = new Grid(10, 9, 64, 64, 100, 150);
        Hero hero = new Hero(4, 5);
        MonsterManager manager = new MonsterManager(grid, hero);

        int heroLife = hero.getLiveCount();

        PlayModeController controller = new PlayModeController();
        controller.playModeGrid = grid;

        // Create a fighter monster near the hero
        Monster monster =manager.createMonster(3, 5, MonsterType.FIGHTER, System.nanoTime());

        if(monster instanceof FighterMonster){
            manager.fighterTestAttack((FighterMonster)monster);
        }
        

        assertTrue("repOkHeroLife should return true after the attack", manager.repOkHeroLife(hero, heroLife));
    }


    @Test
    public void testRepOkWithInvalidFighterMonsterAttack() {
        Grid grid = new Grid(10, 9, 64, 64, 100, 150);
        Hero hero = new Hero(4, 5);
        MonsterManager manager = new MonsterManager(grid, hero);

        int heroLife = hero.getLiveCount();

        PlayModeController controller = new PlayModeController();
        controller.playModeGrid = grid;

        // Create a fighter monster near the hero
        Monster monster =manager.createMonster(0, 5, MonsterType.FIGHTER, System.nanoTime());

        if(monster instanceof FighterMonster){
            manager.fighterTestAttack((FighterMonster)monster);
        }
        

        assertTrue("repOkHeroLife should return false after the attack", !manager.repOkHeroLife(hero, heroLife));
    }



    @Test
    public void testRepOkWithValidArcherMonsterAttack() {
        Grid grid = new Grid(10, 9, 64, 64, 100, 150);
        Hero hero = new Hero(4, 5);
        MonsterManager manager = new MonsterManager(grid, hero);

        int heroLife = hero.getLiveCount();

        PlayModeController controller = new PlayModeController();
        controller.playModeGrid = grid;

        // Create a fighter monster near the hero
        Monster monster =manager.createMonster(5, 4, MonsterType.ARCHER, System.nanoTime());

        if(monster instanceof ArcherMonster){
            manager.archerTestAttack((ArcherMonster)monster);
        }
        

        assertTrue("repOkHeroLife should return true after the attack", manager.repOkHeroLife(hero, heroLife));
    }


    @Test
    public void testRepOkWithInvalidArcherMonsterAttack() {
        Grid grid = new Grid(10, 9, 64, 64, 100, 150);
        Hero hero = new Hero(4, 5);
        MonsterManager manager = new MonsterManager(grid, hero);

        int heroLife = hero.getLiveCount();

        PlayModeController controller = new PlayModeController();
        controller.playModeGrid = grid;

        // Create a fighter monster near the hero
        Monster monster =manager.createMonster(6, 3, MonsterType.ARCHER, System.nanoTime());

        if(monster instanceof ArcherMonster){
            manager.archerTestAttack((ArcherMonster)monster);
        }
        

        assertTrue("repOkHeroLife should return false after the attack", !manager.repOkHeroLife(hero, heroLife));
    }


    @Test
    public void testRepOkWithValidWizardTeleportation() {
        Grid grid = new Grid(10, 9, 64, 64, 0, 0);
        Hero hero = new Hero(4, 5);
        MonsterManager manager = new MonsterManager(grid, hero);


        PlayModeController controller = new PlayModeController();
        PlayModeController.totalTime = 50;
        PlayModeController.time = 40;
        controller.playModeGrid = grid;

        // Create a fighter monster near the hero
        Monster monster =manager.createMonster(6, 3, MonsterType.WIZARD, System.nanoTime());


        for(int a=0;a<3;a++){
            Tile tile=grid.getRandomEmptyTile();
            tile.changeTileType('P');
        }

        if(monster instanceof WizardMonster){
            ((WizardMonster)monster).act(controller);
        }
        

        assertTrue("repOkTeleportation should return true", manager.repOkTeleportation(controller.runeXCoordinate, controller.runeYCoordinate, controller));
    }






}
