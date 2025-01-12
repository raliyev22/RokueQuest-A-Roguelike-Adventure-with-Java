package main.model;

import main.utils.Grid;
import main.utils.Tile;

import java.util.Random;

public class ArcherMonster extends Monster {
    public static int ARCHER_RANGE = 2;
    public ArcherMonster(int x, int y) {
        this.posX = x;
        this.posY = y;
        type = MonsterType.ARCHER;
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
                        tile.changeTileType('A');
                        
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
                        tile.changeTileType('A');

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
                        tile.changeTileType('A');
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
                        tile.changeTileType('A');
                    }
                }
                else{
                    moveRandomly(grid);
                }

            }
        }



    }


}
