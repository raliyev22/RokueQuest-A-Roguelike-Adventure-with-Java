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
}
