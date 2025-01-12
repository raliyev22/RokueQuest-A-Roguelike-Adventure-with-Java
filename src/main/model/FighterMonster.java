package main.model;

import main.utils.Grid;
import main.utils.Tile;

import java.util.Random;

public class FighterMonster extends Monster{

    public static int FIGHTER_RANGE = 1;
    public FighterMonster(int x, int y) {
        this.posX = x;
        this.posY = y;
        type = MonsterType.FIGHTER;
    }
    public void chaseLure(int targetX, int targetY, Grid grid) {
        int dx = Integer.compare(targetX, posX); // Determine x-direction
        int dy = Integer.compare(targetY, posY); // Determine y-direction

        // Determine the next tile to move to
        int nextX = posX + dx;
        int nextY = posY + dy;

        if (grid.indexInRange(nextX, nextY)) {
            Tile nextTile = grid.findTileWithIndex(nextX, nextY);
            if (nextTile.getTileType() == 'E') { // Ensure the tile is walkable
                grid.changeTileWithIndex(posX, posY, 'E'); // Clear current position
                posX = nextX;
                posY = nextY;
                grid.changeTileWithIndex(posX, posY, 'F'); // Update to new position
            }
        }
    }

    public void moveRandomly(Grid grid){
        Random rand = new Random();
        int randomMove = rand.nextInt(4);
        Boolean existMovement = false;
        if(grid.indexInRange(posX+1,posY)){
            if(grid.findTileWithIndex(posX+1,posY).getTileType() == 'E'){
                existMovement = true;
            }
        }
        if(grid.indexInRange(posX-1,posY)){
            if(grid.findTileWithIndex(posX-1,posY).getTileType() == 'E'){
                existMovement = true;
            }
        }
        if(grid.indexInRange(posX,posY+1)){
            if(grid.findTileWithIndex(posX,posY+1).getTileType() == 'E'){
                existMovement = true;
            }
        }
        if(grid.indexInRange(posX,posY-1)){
            if(grid.findTileWithIndex(posX,posY-1).getTileType() == 'E'){
                existMovement = true;
            }
        }
        
        if(existMovement){
            
        
            if(randomMove == 0){
                if(grid.indexInRange(posX+1,posY)){
                    Tile tile = grid.findTileWithIndex(posX+1, posY);
                    if(!(tile.getTileType() == 'E')){
                        moveRandomly(grid);
                    }
                    else{
                        grid.changeTileWithIndex(posX, posY,'E');
                        this.posX = posX+1;
                        tile.changeTileType('F');
                        
                    }
                }
                else{
                    moveRandomly(grid);
                }
            }
            if(randomMove == 1){
                if(grid.indexInRange(posX-1,posY)){
                    Tile tile = grid.findTileWithIndex(posX-1, posY);
                    if(!(tile.getTileType() == 'E')){
                        moveRandomly(grid);
                    }
                    else{
                        grid.changeTileWithIndex(posX, posY,'E');
                        this.posX = posX-1;
                        tile.changeTileType('F');

                    }
                }
                else{
                    moveRandomly(grid);
                }
            }
            if(randomMove == 2){
                if(grid.indexInRange(posX,posY+1)){
                    Tile tile = grid.findTileWithIndex(posX, posY+1);
                    if(!(tile.getTileType() == 'E')){
                        moveRandomly(grid);
                    }
                    else{
                        grid.changeTileWithIndex(posX, posY,'E');
                        this.posY = posY+1;
                        tile.changeTileType('F');
                    }
                }
                else{
                    moveRandomly(grid);
                }
            }
            if(randomMove == 3){
                if(grid.indexInRange(posX,posY-1)){
                    Tile tile = grid.findTileWithIndex(posX, posY-1);
                    if(!(tile.getTileType() == 'E')){
                        moveRandomly(grid);
                    }
                    else{
                        grid.changeTileWithIndex(posX, posY,'E');
                        this.posY = posY-1;
                        tile.changeTileType('F');
                    }
                }
                else{
                    moveRandomly(grid);
                }

            }
        }
    }


}
