package test;

import main.controller.BuildModeController;
import main.utils.*;
import main.model.*;
import org.junit.BeforeClass;
import org.junit.Test;

import javafx.application.Platform;

import static org.junit.Assert.*;

public class BuildModeControllerTest {

    @BeforeClass
    public static void initJavaFX() {
        // Initialize JavaFX runtime
        Platform.startup(() -> {});
    }

    @Test
    public void testCannotPlaceTwoObjectsInSameTile() {
        BuildModeController buildModeController = new BuildModeController();
        TiledHall hall = new TiledHall(10, 7, new Grid(10, 9, 32, 32, 10, 40), 1);
        buildModeController.addTiledHall(hall, HallType.EARTH);

        Grid grid = hall.getGrid();

        boolean firstPlacement = buildModeController.placeObject(hall, 2 * 32 + 10, 2 * 32 + 40, 'P', false); // 'P' for Pillar
        boolean secondPlacement = buildModeController.placeObject(hall, 2 * 32 + 10, 2 * 32 + 40, 'C', false); // 'C' for Chest

        assertTrue("First object should be placed successfully", firstPlacement);
        assertFalse("Second object cannot be placed in the same tile", secondPlacement);

        assertTrue("The grid should satisfy repOk after valid placement", buildModeController.repOk());
    }

    @Test
    public void testCannotPlaceObjectOutsideHall() {
        BuildModeController buildModeController = new BuildModeController();
        TiledHall hall = new TiledHall(10, 7, new Grid(10, 9, 32, 32, 10, 40), 1);
        buildModeController.addTiledHall(hall, HallType.EARTH);

        double outOfBoundsX = 15 * 32 + 10; // X outside the grid
        double outOfBoundsY = 15 * 32 + 40; // Y outside the grid

        boolean placement = buildModeController.placeObject(hall, outOfBoundsX, outOfBoundsY, 'C', false); // 'C' for Chest

        assertFalse("Objects cannot be placed outside a hall", placement);

        assertTrue("The grid should still satisfy repOk after invalid placement attempt", buildModeController.repOk());
    }

    @Test
    public void testCannotPlaceObjectBehindLargeObject() {
        BuildModeController buildModeController = new BuildModeController();
        TiledHall hall = new TiledHall(10, 7, new Grid(10, 9, 32, 32, 10, 40), 1);
        buildModeController.addTiledHall(hall, HallType.EARTH);

        boolean tallObjectPlacement = buildModeController.placeObject(hall, 2 * 32 + 10, 2 * 32 + 40, 'P', true); // 'P' for Pillar

        boolean placementBehindTallObject = buildModeController.placeObject(hall, 2 * 32 + 10, 1 * 32 + 40, 'C', false); // 'C' for Chest

        assertTrue("Tall object should be placed successfully", tallObjectPlacement);

        assertFalse("Objects cannot be placed behind a tall object", placementBehindTallObject);

        assertTrue("The grid should still satisfy repOk after placement attempts", buildModeController.repOk());
    }
}
