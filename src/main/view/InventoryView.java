package main.view;

import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import main.model.Enchantment;
import main.model.Images;

import java.util.Set;

public class InventoryView extends StackPane {
    private final int ICON_SIZE = 40; // Icon size
    private final int CELL_PADDING = 2; // Padding between cells
    private final double INVENTORY_WIDTH = 200; // Inventory width
    private final double INVENTORY_HEIGHT = 300; // Inventory height

    private final Image inventoryBackground = Images.IMAGE_INVENTORY; // Background image
    private Rectangle background;
    private HBox enchantmentRow; // HBox to arrange enchantments horizontally

    public InventoryView() {
        // Set up the background
        background = new Rectangle(INVENTORY_WIDTH, INVENTORY_HEIGHT);
        background.setFill(new ImagePattern(inventoryBackground));

        // Create the enchantment row
        enchantmentRow = new HBox(CELL_PADDING);
        enchantmentRow.setPadding(new javafx.geometry.Insets(CELL_PADDING));

        // Adjust position to move up and right
        enchantmentRow.setTranslateX(33); // Slightly to the right
        enchantmentRow.setTranslateY(INVENTORY_HEIGHT / 2 - ICON_SIZE / 2 - 15);

        // Add background and enchantment row to the StackPane
        getChildren().addAll(background, enchantmentRow);
    }

    public void updateInventory(Set<Enchantment.Type> enchantments) {
        enchantmentRow.getChildren().clear(); // Clear previous enchantments

        for (Enchantment.Type type : enchantments) {
            // Create an icon for the enchantment
            Image enchantmentImage = Images.convertEnchantmentToImage(type);
            if (enchantmentImage != null) {
                ImageView enchantmentImageView = new ImageView(enchantmentImage);
                enchantmentImageView.setFitWidth(ICON_SIZE);
                enchantmentImageView.setFitHeight(ICON_SIZE);
                enchantmentRow.getChildren().add(enchantmentImageView); // Add the icon to the row
            }
        }
    }
}
