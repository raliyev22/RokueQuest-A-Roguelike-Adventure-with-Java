package main.controller;

import main.model.HallType;
import main.model.Images;
import main.utils.Grid;
import main.utils.Tile;
import main.utils.TiledHall;
import main.view.BuildModeView;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.util.Pair;

import java.util.*;

public class BuildModeController {

    private HashMap<TiledHall, List<Tile>> tileMap;
    private HashMap<TiledHall, HallType> hallTypeMap;
    private List<Pair<Integer, Integer>> runeLocationList;
    static final Image tileImage = new Image("/rokue-like_assets/Tile_x2_32_32.png");

    static final Image CHEST_IMAGE = new Image("/rokue-like_assets/Build_Mode_Chest_Full_View.png");
    static final Image Pillar_IMAGE = new Image("/rokue-like_assets/Pillar_x2_32_64.png");
    static final Image Ladder_IMAGE = new Image("/rokue-like_assets/TileWithLadder_x2_32_32.png");
    static final Image BoxOnBox_IMAGE = new Image("/rokue-like_assets/BoxOnTopOfBox_x2_32_64.png");
    static final Image Cube_IMAGE = new Image("/rokue-like_assets/Cube_x2_32_32.png");
    static final Image Skull_IMAGE = new Image("/rokue-like_assets/Skull_x2_32_32.png");

    static final Image BOX_IMAGE = new Image("/rokue-like_assets/Box_x2_32_42.png");
    
    static final Image CHEST = new Image("/rokue-like_assets/ChestHeart_x2_32_28.png");
    static final Image Exit_Button = new Image("/rokue-like_assets/ExitButton_x2_32_32.png");

    public BuildModeController() {
        tileMap = new HashMap<>();
        hallTypeMap = new HashMap<>();
        runeLocationList = new ArrayList<>();
    }

    public void addTiledHall(TiledHall hall, HallType type) {
        tileMap.put(hall, new ArrayList<>());
        hallTypeMap.put(hall, type);
    }

    /**
     * Places an object in the specified hall at the given coordinates.
     * Handles blocking tiles behind larger objects (like Pillars).
     * 
     * @param hall      The hall where the object is being placed.
     * @param x         The X-coordinate of the placement.
     * @param y         The Y-coordinate of the placement.
     * @param tileType  The type of object being placed.
     * @param isTallObject Indicates if the object is tall and requires blocking a tile behind it.
     * @return True if the object was successfully placed, false otherwise.
     */
    public boolean placeObject(TiledHall hall, double x, double y, char tileType, boolean isTallObject) {
        if (!tileMap.containsKey(hall)) return false;

        Grid grid = hall.getGrid();
        Tile targetTile = grid.findTileUsingCoordinates(x, y);

        // Check if the tile is within grid boundaries and unoccupied
        if (targetTile == null || targetTile.getTileType() != 'E') {
            return false;
        }

        // Mark the tile with the new object type
        targetTile.changeTileType(tileType);
        tileMap.get(hall).add(targetTile);

        // Handle blocking the tile behind for tall objects
        if (isTallObject) {
            Tile behindTile = grid.findTileUsingCoordinates(x, y - grid.getTileHeight());
            if (behindTile != null && behindTile.getTileType() == 'E') {
                behindTile.changeTileType('!'); // Block the tile behind with a '!'
            }
        }

        return true;
    }

    public Pair<Integer, Integer> getRuneLocation(TiledHall hall) {
        if (!tileMap.containsKey(hall) || tileMap.get(hall).isEmpty()) return null;

        List<Tile> placedTiles = tileMap.get(hall);
        Random random = new Random();
        Tile randomTile = placedTiles.get(random.nextInt(placedTiles.size()));

        return new Pair<>(randomTile.getLeftSide(), randomTile.getTopSide());
    }

    public void highlightRuneLocation(TiledHall hall, int x, int y, int tileSize, Runnable onFinish) {
        // Here you can trigger highlighting logic via the View.
        // This is placeholder logic for grid-based highlighting.
        if (!tileMap.containsKey(hall)) return;
        Tile centerTile = hall.getGrid().findTileUsingCoordinates(x, y);
        if (centerTile == null) return;

        // Example callback to handle the highlight.
        onFinish.run();
    }

    public boolean repOk() {
        // Check for null references in tileMap and hallTypeMap
        if (tileMap == null || hallTypeMap == null) {
            return false;
        }

        // Check that each TiledHall in tileMap exists in hallTypeMap
        for (TiledHall hall : tileMap.keySet()) {
            if (!hallTypeMap.containsKey(hall) || hall.getGrid() == null) {
                return false;
            }
        }

        // Check for unique object placement within each TiledHall
        for (TiledHall hall : tileMap.keySet()) {
            List<Tile> placedTiles = tileMap.get(hall);
            HashSet<Tile> uniqueTiles = new HashSet<>(placedTiles);
            if (uniqueTiles.size() != placedTiles.size()) {
                return false; // Duplicate objects in the same tile
            }

            // Ensure all objects are within the hall's grid boundaries
            for (Tile tile : placedTiles) {
                if (!hall.getGrid().coordinatesAreInGrid(tile.getLeftSide(), tile.getTopSide())) {
                    return false; // Object placed outside the hall
                }
            }
        }

        // All checks passed
        return true;
    }
    

    public HashMap<TiledHall, List<Tile>> getTileMap() {
        return tileMap;
    }

    public HashMap<TiledHall, HallType> getHallTypeMap() {
        return hallTypeMap;
    }

    public List<Pair<Integer, Integer>> getRuneLocationList() {
        return runeLocationList;
    }

}
