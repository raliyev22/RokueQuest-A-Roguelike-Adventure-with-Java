package main.model;

import main.controller.PlayModeController;
import main.utils.Grid;
import main.utils.Tile;

public class DesperateWizardBehavior implements WizardBehavior {
    @Override
    public void execute(WizardMonster wizard, PlayModeController controller) {

        Hero hero = controller.getHero();
        hero.setIsTeleported(true);

        Tile randomTile = controller.getRandomEmptyTile();

        Grid grid = controller.getPlayModeGrid();

        Tile oldHeroTile = grid.findTileWithIndex(hero.getPosX(), hero.getPosY());
        grid.changeTileWithIndex(hero.getPosX(), hero.getPosY(), 'E');
        if (hero.isMoving) {
            Tile nextTile = grid.findTileUsingDirection(oldHeroTile, hero.movingDirection);
            nextTile.changeTileType('E');
            hero.isMoving = false;
            hero.movingDirection = null;
        }

        hero.setPosX(controller.playModeGrid.findXofTile(randomTile));
        hero.setPosY(controller.playModeGrid.findYofTile(randomTile));
        hero.currentX = randomTile.getLeftSide();
        hero.currentY = randomTile.getTopSide();
        hero.targetX = randomTile.getLeftSide();
        hero.targetY = randomTile.getTopSide();

        grid.changeTileWithIndex(hero.getPosX(), hero.getPosY(), hero.getCharType());
        controller.getView().updateHeroPosition(randomTile.getLeftSide(), randomTile.getTopSide());

        hero.setIsTeleported(false);
        controller.removeMonster(wizard); // Wizard disappears after this action
        System.out.println(grid);
    }

}
