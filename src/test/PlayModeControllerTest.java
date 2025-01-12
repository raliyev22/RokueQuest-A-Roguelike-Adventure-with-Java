package test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;

import main.controller.PlayModeController;
import main.model.*;
import main.utils.Grid;
import main.utils.Tile;

public class PlayModeControllerTest {
    private PlayModeController controller;
    private Grid testGrid;

    @Before
    public void setUp() {
        controller = new PlayModeController();
        testGrid = new Grid(10, 9, 64, 64, 100, 150);
        controller.playModeGrid = testGrid;

        // Hall object türünden birkaç rastgele kare ekleyelim
        testGrid.changeTileWithIndex(1, 1, 'H');
        testGrid.changeTileWithIndex(2, 2, 'H');
        testGrid.changeTileWithIndex(3, 3, 'H');
    }

    @Test
    public void testTeleportRuneEventuallyDifferentLocations() {
        int maxAttempts = 10;
        boolean locationChanged = false;
        
        int initialX = controller.runeXCoordinate;
        int initialY = controller.runeYCoordinate;
    
        for (int i = 0; i < maxAttempts; i++) {
            controller.teleportRune();
            int newX = controller.runeXCoordinate;
            int newY = controller.runeYCoordinate;
            
            if (initialX != newX || initialY != newY) {
                locationChanged = true;
                break;
            }
        }
    
        assertTrue("Rune should eventually teleport to a different location after several attempts", locationChanged);
    }
    

    @Test
    public void testTeleportRuneValidLocation() {
        controller.teleportRune();
        Tile runeTile = controller.playModeGrid.findTileWithIndex(controller.runeXCoordinate, controller.runeYCoordinate);
        assertTrue("Rune should teleport to a hall object tile", PlayModeController.isHallObjectTile(runeTile));
    }

    @Test(expected = RuntimeException.class)
    public void testTeleportRuneWithNoHallObjects() {
        controller.playModeGrid = new Grid(10, 9, 64, 64, 100, 150); // Boş grid
        controller.teleportRune();
    }
}
