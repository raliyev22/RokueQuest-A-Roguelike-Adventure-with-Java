package main.model;

import main.utils.Grid;
import main.utils.Tile;
import main.controller.PlayModeController;
import java.util.Set;

public class RevealEnchantment extends Enchantment {
    public RevealEnchantment(int x, int y) {
        super(x, y, "Reveal");
    }

    @Override
    public void applyEffect(Hero hero, Grid grid, PlayModeController controller) {
        System.out.println("Reveal effect stored in hero's inventory.");
        deactivate();
    }

    public void useReveal(Tile tile, Grid grid, PlayModeController controller) {
        Set<Tile> highlightedTiles = grid.findNxNSquare(tile, 4);
        System.out.println("Revealing a 4x4 region around tile: " + tile);
        for (Tile t : highlightedTiles) {
            Rectangle highlight = new Rectangle(t.getLeftSide(), t.getTopSide(), 32, 32);
            highlight.setFill(Color.GREEN);
            controller.getView().addToPane(highlight);
        }

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(10), e -> controller.getView().clearHighlights()));
        timeline.setCycleCount(1);
        timeline.play();
    }
}