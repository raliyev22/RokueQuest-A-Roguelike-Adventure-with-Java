package main.model;

import javafx.scene.image.Image;

public class Hero {
    public final int maxLives = 3;
    int posX, posY, remainingLives;
    Image img;

    public Hero(int posX, int posY, Image img) {
        this.posX = posX;
        this.posY = posY;
        this.img = img;
        remainingLives = 3;
    }

    public void move(Directions direction) {
        switch (direction) {
            case Directions.NORTH -> {
                posY++;
            }
            case Directions.SOUTH -> {
                posY--;
            }
            case Directions.EAST -> {
                posX++;
            }
            case Directions.WEST -> {
                posX--;
            }
            default -> throw new IllegalArgumentException("Invalid direction");
        }
    }

    public void increaseLives(int num){
        remainingLives += num;
        if(remainingLives > maxLives){
            remainingLives = maxLives;
        }
    }

    public void decreaseLives() {
        remainingLives--;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public char getCharType() {
        return 'H';
    }

}
