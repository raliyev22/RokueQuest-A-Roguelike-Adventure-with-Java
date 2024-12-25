package main.model;


import java.util.Random;
import main.utils.Grid;
import main.utils.Tile;

public class ArcherMonster extends Monster {
    public ArcherMonster(int x, int y) {
        this.x = x;
        this.y = y;
        type = MonsterType.ARCHER;
    }
    public void moveRandomly(Grid grid){
        Random rand = new Random();
        int randomMove = rand.nextInt(5);
        Boolean existMovement = false;
        if(grid.indexInRange(x+1,y)){
            if(grid.findTileWithIndex(x+1,y).getTileType() == 'E'){
                existMovement = true;
            }
        }
        if(grid.indexInRange(x-1,y)){
            if(grid.findTileWithIndex(x-1,y).getTileType() == 'E'){
                existMovement = true;
            }
        }
        if(grid.indexInRange(x,y+1)){
            if(grid.findTileWithIndex(x,y+1).getTileType() == 'E'){
                existMovement = true;
            }
        }
        if(grid.indexInRange(x,y-1)){
            if(grid.findTileWithIndex(x,y-1).getTileType() == 'E'){
                existMovement = true;
            }
        }
        
        if(existMovement){
            
        
            if(randomMove == 1){
                if(grid.indexInRange(x+1,y)){
                    Tile tile = grid.findTileWithIndex(x+1, y);
                    if(!(tile.getTileType() == 'E')){
                        moveRandomly(grid);
                    }
                    else{
                        grid.changeTileWithIndex(x, y,'E');
                        this.x = x+1;
                        tile.changeTileType('A');
                        
                    }
                }
                else{
                    moveRandomly(grid);
                }
            }
            if(randomMove == 2){
                if(grid.indexInRange(x-1,y)){
                    Tile tile = grid.findTileWithIndex(x-1, y);
                    if(!(tile.getTileType() == 'E')){
                        moveRandomly(grid);
                    }
                    else{
                        grid.changeTileWithIndex(x, y,'E');
                        this.x = x-1;
                        tile.changeTileType('A');

                    }
                }
                else{
                    moveRandomly(grid);
                }
            }
            if(randomMove == 3){
                if(grid.indexInRange(x,y+1)){
                    Tile tile = grid.findTileWithIndex(x, y+1);
                    if(!(tile.getTileType() == 'E')){
                        moveRandomly(grid);
                    }
                    else{
                        grid.changeTileWithIndex(x, y,'E');
                        this.y = y+1;
                        tile.changeTileType('A');
                    }
                }
                else{
                    moveRandomly(grid);
                }
            }
            if(randomMove == 4){
                if(grid.indexInRange(x,y-1)){
                    Tile tile = grid.findTileWithIndex(x, y-1);
                    if(!(tile.getTileType() == 'E')){
                        moveRandomly(grid);
                    }
                    else{
                        grid.changeTileWithIndex(x, y,'E');
                        this.y = y-1;
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
