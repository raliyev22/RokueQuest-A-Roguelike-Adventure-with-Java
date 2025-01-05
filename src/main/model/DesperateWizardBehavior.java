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

        grid.changeTileWithIndex(hero.getPosX(), hero.getPosY(), 'E');
        hero.setPosX(controller.playModeGrid.findXofTile(randomTile));
        hero.setPosY(controller.playModeGrid.findYofTile(randomTile));


        grid.changeTileWithIndex(hero.getPosX(), hero.getPosY(), hero.getCharType());
        controller.getView().updateHeroPosition(randomTile.getLeftSide(), randomTile.getTopSide());

        hero.setIsTeleported(false);
        controller.removeMonster(wizard); // Wizard disappears after this action
        System.out.println(grid);
    }

}
