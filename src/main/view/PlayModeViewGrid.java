package main.view;

import main.utils.Grid;

public class PlayModeViewGrid {
    public static void main(String[] args) {
        Grid myGrid = new Grid(3,3,5,5,10,10);
        myGrid.changeTileWithIndex(2, 2, 'P');
        System.out.println(myGrid.toString());
    }
}
