package main.view;

import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import main.model.Enchantment;
import main.model.Images;

import java.util.List;
import java.util.Set;

public class InventoryView extends StackPane {
    private final int ICON_SIZE = 30; // Icon size
    private final int CELL_PADDING = 8; // Padding between cells
    private final double INVENTORY_WIDTH = 200; // Inventory width
    private final double INVENTORY_HEIGHT = 300; // Inventory height
    private final int MAX_COLUMNS = 3; // Maximum columns in a row

    private final Image inventoryBackground = Images.IMAGE_INVENTORY; // Background image
    private Rectangle background;
    private GridPane enchantmentGrid; // GridPane to arrange enchantments

    public InventoryView() {
        // Set up the background
        background = new Rectangle(INVENTORY_WIDTH, INVENTORY_HEIGHT);
        background.setFill(new ImagePattern(inventoryBackground));

        // Create the enchantment grid
        enchantmentGrid = new GridPane();
        enchantmentGrid.setHgap(CELL_PADDING); // Horizontal gap
        enchantmentGrid.setVgap(CELL_PADDING); // Vertical gap
        enchantmentGrid.setTranslateX(45); // Slightly to the right
        enchantmentGrid.setTranslateY(120); // Move down for visibility

        // Add background and enchantment grid to the StackPane
        getChildren().addAll(background, enchantmentGrid);
    }

    public void updateInventory(List<Enchantment.Type> enchantments) {
        enchantmentGrid.getChildren().clear(); // Clear previous enchantments
        int row = 0;
        int col = 0;

        for (Enchantment.Type type : enchantments) {
            // Create an icon for the enchantment
            Image enchantmentImage = Images.convertEnchantmentToImage(type);
            if (enchantmentImage != null) {
                ImageView enchantmentImageView = new ImageView(enchantmentImage);
                enchantmentImageView.setFitWidth(ICON_SIZE);
                enchantmentImageView.setFitHeight(ICON_SIZE);

                // Add the icon to the grid
                enchantmentGrid.add(enchantmentImageView, col, row);
                col++;
                if (col >= MAX_COLUMNS) {
                    col = 0;
                    row++;
                }
            }
        }
    }
}
