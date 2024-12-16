package main.model;

import java.util.Random;
import main.utils.Tile;

public class FighterMonster extends Monster{
    public FighterMonster(int x, int y){
        this.x = x;
        this.y = y;
        type = MonsterType.FIGHTER;
    }
    public void moveRandomly(){
        Random rand = new Random();
        int randomMove = rand.nextInt(5);
        Boolean existMovement = false;
        if(this.grid.indexInRange(x+1,y)){
            if(this.grid.findTileWithIndex(x+1,y).getTileType() == 'E'){
                existMovement = true;
            }
        }else if(this.grid.indexInRange(x-1,y)){
            if(this.grid.findTileWithIndex(x-1,y).getTileType() == 'E'){
                existMovement = true;
            }
        }else if(this.grid.indexInRange(x,y+1)){
            if(this.grid.findTileWithIndex(x,y+1).getTileType() == 'E'){
                existMovement = true;
            }
        }else if(this.grid.indexInRange(x,y-1)){
            if(this.grid.findTileWithIndex(x,y-1).getTileType() == 'E'){
                existMovement = true;
            }
        }
        
        if(existMovement){
            
        
            if(randomMove == 1){
                if(this.grid.indexInRange(x+1,y)){
                    Tile tile = this.grid.findTileWithIndex(x+1, y);
                    if(!(tile.getTileType() == 'E')){
                        moveRandomly();
                    }
                    else{
                        this.grid.changeTileWithIndex(x, y,'E');
                        this.x = x+1;
                        tile.changeTileType('M');
                        
                    }
                }
                else{
                    moveRandomly();
                }
            }
            if(randomMove == 2){
                if(this.grid.indexInRange(x-1,y)){
                    Tile tile = this.grid.findTileWithIndex(x-1, y);
                    if(!(tile.getTileType() == 'E')){
                        moveRandomly();
                    }
                    else{
                        this.grid.changeTileWithIndex(x, y,'E');
                        this.x = x-1;
                        tile.changeTileType('M');

                    }
                }
                else{
                    moveRandomly();
                }
            }
            if(randomMove == 3){
                if(this.grid.indexInRange(x,y+1)){
                    Tile tile = this.grid.findTileWithIndex(x, y+1);
                    if(!(tile.getTileType() == 'E')){
                        moveRandomly();
                    }
                    else{
                        this.grid.changeTileWithIndex(x, y,'E');
                        this.y = y+1;
                        tile.changeTileType('M');
                    }
                }
                else{
                    moveRandomly();
                }
            }
            if(randomMove == 4){
                if(this.grid.indexInRange(x,y-1)){
                    Tile tile = this.grid.findTileWithIndex(x, y-1);
                    if(!(tile.getTileType() == 'E')){
                        moveRandomly();
                    }
                    else{
                        this.grid.changeTileWithIndex(x, y,'E');
                        this.y = y-1;
                        tile.changeTileType('M');
                    }
                }
                else{
                    moveRandomly();
                }

            }
        }



    }

}
