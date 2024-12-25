package main.model;

import main.utils.Grid;

public class MonsterFactory {

    // Method to create a Monster based on the type
    public Monster createMonster(MonsterType type, int x, int y) {
        switch (type) {
            case FIGHTER -> {
                return new FighterMonster(x, y );
            }
            case ARCHER -> {
                return new ArcherMonster(x, y );
            }
            case WIZARD -> {
                return new WizardMonster(x, y);
            }
            default -> throw new IllegalArgumentException("Invalid MonsterType: " + type);
        }
    }
}
