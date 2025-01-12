package test;

import org.junit.Before;
import org.junit.Test;

import main.utils.Grid;
import main.utils.Tile;

public class GridTest {
    private Grid grid;

    @Before
    public void setup() {
        grid = new Grid(9, 10, 64, 64, 10, 20);
        grid.changeTileWithIndex(0, 0, 'L');
        grid.changeTileWithIndex(0, 2, 'A');
        grid.changeTileWithIndex(3, 5, 'F');
        grid.changeTileWithIndex(2, 2, 'W');
        grid.changeTileWithIndex(1, 8, 'B');
        grid.changeTileWithIndex(8, 2, 'A');
        grid.changeTileWithIndex(1, 2, 'C');
    }


    @Test
    public void testChangeTileWithIndex() {
        grid.changeTileWithIndex(0, -1, 'A');
        grid.changeTileWithIndex(-1, 2, 'A');
        grid.changeTileWithIndex(100, 8, 'B');
        grid.changeTileWithIndex(8, 42, 'A');
        grid.changeTileWithIndex(2024, 2025, 'A');
        grid.changeTileWithIndex(0, 0, '!');
        grid.changeTileWithIndex(0, 0, '?');
        grid.changeTileWithIndex(0, 0, '!');
    }

    @Test
    public void testCopyTileMap() throws Exception {
        Grid otherGrid1 = new Grid(9, 10, 32, 32, 100, 200);
        otherGrid1.changeTileWithIndex(0, 0, 'K');
        otherGrid1.changeTileWithIndex(0, 1, 'L');
        otherGrid1.changeTileWithIndex(1, 0, 'M');
        otherGrid1.changeTileWithIndex(1, 1, 'N');
        otherGrid1.changeTileWithIndex(2, 2, 'O');
        grid.copyTileMap(otherGrid1);
        
        Tile newTile11 = grid.findTileWithIndex(1, 1);
        Tile newTile12 = grid.findTileWithIndex(1, 2);
        if (newTile11.getTileType() != 'N') {
            throw new Exception("Grid was not copied properly");
        }
        if (newTile12.getTileType() != 'E') {
            throw new Exception("Grid was not copied properly");
        }

        Grid otherGrid2 = new Grid(2, 2, 64, 64, 10, 20);
        otherGrid2.changeTileWithIndex(0, 0, 'Z');
        otherGrid2.changeTileWithIndex(0, 1, 'Y');
        otherGrid2.changeTileWithIndex(1, 0, 'X');
        otherGrid2.changeTileWithIndex(1, 1, 'Q');
        otherGrid2.changeTileWithIndex(2, 2, 'P');
        grid.copyTileMap(otherGrid2);

        Tile otherTile55 = grid.findTileWithIndex(5, 5);
        Tile otherTile12 = grid.findTileWithIndex(1, 2);
        if (otherTile55.getTileType() != 'E') {
            throw new Exception("Grid was not copied properly");
        }
        if (otherTile12.getTileType() != 'E') {
            throw new Exception("Grid was not copied properly");
        }
    }

    @Test
    public void testTwoTilesAreNeighbours() throws Exception {
        Tile tile00 = grid.findTileWithIndex(0, 0);
        Tile tile10 = grid.findTileWithIndex(1, 0);
        Tile tile01 = grid.findTileWithIndex(0, 1);
        Tile tile11 = grid.findTileWithIndex(1, 1);
        Tile tile20 = grid.findTileWithIndex(2, 0);
        Tile tile02 = grid.findTileWithIndex(0, 2);
        Tile tile21 = grid.findTileWithIndex(2, 1);
        Tile tile12 = grid.findTileWithIndex(1, 2);
        Tile tile22 = grid.findTileWithIndex(2, 2);

        if (grid.twoTilesAreNeighbours(tile00, tile10) != true) {
            throw new Exception("twoTilesAreNeighbours() function does not work properly");
        }
        if (grid.twoTilesAreNeighbours(tile11, tile01) != true) {
            throw new Exception("twoTilesAreNeighbours() function does not work properly");
        }
        if (grid.twoTilesAreNeighbours(tile12, tile22) != true) {
            throw new Exception("twoTilesAreNeighbours() function does not work properly");
        }
        if (grid.twoTilesAreNeighbours(tile11, tile11) != true) {
            throw new Exception("twoTilesAreNeighbours() function does not work properly");
        }
        if (grid.twoTilesAreNeighbours(tile00, tile22) != false) {
            throw new Exception("twoTilesAreNeighbours() function does not work properly");
        }
        if (grid.twoTilesAreNeighbours(tile00, tile20) != false) {
            throw new Exception("twoTilesAreNeighbours() function does not work properly");
        }
        if (grid.twoTilesAreNeighbours(tile02, tile22) != false) {
            throw new Exception("twoTilesAreNeighbours() function does not work properly");
        }
        if (grid.twoTilesAreNeighbours(tile11, tile22) != true) {
            throw new Exception("twoTilesAreNeighbours() function does not work properly");
        }
        if (grid.twoTilesAreNeighbours(tile12, tile21) != true) {
            throw new Exception("twoTilesAreNeighbours() function does not work properly");
        }
    }
}
