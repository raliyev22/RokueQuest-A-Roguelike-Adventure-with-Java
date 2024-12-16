package main.model;

import main.utils.Grid;
import main.utils.Tile;
import java.util.ArrayList;
import java.util.List;

public class Hall {
    private final String hallName;   // Name of the hall (e.g., "Hall of Earth", "Hall of Water")
    private final Grid grid;         // Grid structure for the hall
    private final int minObjectCount; // Minimum objects required in the hall
    private final List<Tile> placedObjects; // Tracks placed objects

    public Hall(String hallName, int rows, int columns, int tileWidth, int tileHeight, int minObjectCount) {
        this.hallName = hallName;
        this.grid = new Grid(rows, columns, tileWidth, tileHeight, 0, 0); // Creates a grid for the hall
        this.minObjectCount = minObjectCount;
        this.placedObjects = new ArrayList<>();
    }

    /**
     * Place an object at a given position if the tile is not already occupied.
     */
    public boolean placeObject(int x, int y, char objectType) {
        Tile tile = grid.findTileWithIndex(x, y);
        if (tile.getTileType() == 'E') { // Assuming 'E' represents an empty tile
            tile.changeTileType(objectType); // Change the tile type
            placedObjects.add(tile);
            return true; // Successfully placed
        }
        System.err.println("Tile at (" + x + ", " + y + ") is already occupied.");
        return false; // Failed to place
    }

    /**
     * Check if the minimum object placement condition is met.
     */
    public boolean isPlacementSatisfied() {
        return placedObjects.size() >= minObjectCount;
    }

    /**
     * Reset the hall by clearing all placed objects.
     */
    public void resetHall() {
        for (Tile tile : placedObjects) {
            tile.changeTileType('E'); // Reset the tile to empty
        }
        placedObjects.clear();
    }

    /**
     * Display the hall grid in the console (for debugging).
     */
    public void displayHall() {
        System.out.println("Hall: " + hallName);
        System.out.println(grid);
    }

    // Getters
    public String getHallName() {
        return hallName;
    }

    public int getMinObjectCount() {
        return minObjectCount;
    }

    public List<Tile> getPlacedObjects() {
        return placedObjects;
    }
}
