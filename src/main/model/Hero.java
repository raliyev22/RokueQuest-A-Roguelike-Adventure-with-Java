package main.model;

import javafx.scene.image.Image;

public class Hero {
    int posX, posY, size, remainingLives;
    char type;
    Image img;

    public Hero(int posX, int posY, int size, Image img) {
        this.posX = posX;
        this.posY = posY;
        this.size = size;
        this.img = img;
        remainingLives = 3;
    }

    public void move(int dx, int dy) {
        posX += dx;
        posY += dy;
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

    public char getType() {
        return 'H';
    }

}
