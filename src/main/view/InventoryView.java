package main.view;

import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import main.model.Enchantment;
import main.model.Images;

import java.util.HashMap;

public class InventoryView extends StackPane {
    private final int ICON_SIZE = 40; // Icon size
    private final int CELL_PADDING = 2; // Further reduced padding between cells
    private final double INVENTORY_WIDTH = 200; // Inventory width
    private final double INVENTORY_HEIGHT = 300; // Inventory height
    private HashMap<Enchantment.Type, Integer> enchantments; // Enchantment counts

    private final Image inventoryBackground = Images.IMAGE_INVENTORY; // Background image
    private Rectangle background;
    private HBox enchantmentRow; // HBox to arrange enchantments horizontally

    public InventoryView() {
        this.enchantments = new HashMap<>();

        // Set up the background
        background = new Rectangle(INVENTORY_WIDTH, INVENTORY_HEIGHT);
        background.setFill(new ImagePattern(inventoryBackground));

        // Create the enchantment row
        enchantmentRow = new HBox(CELL_PADDING); // Horizontal layout with reduced spacing
        enchantmentRow.setPadding(new javafx.geometry.Insets(CELL_PADDING)); // Padding around enchantments

        // Adjust position to move up and right
        double rowHeight = ICON_SIZE + CELL_PADDING;
        enchantmentRow.setTranslateX(33); // Slightly to the right
        enchantmentRow.setTranslateY(INVENTORY_HEIGHT / 2 - rowHeight / 2 - 15); // Further up

        // Add background and enchantment row to the StackPane
        getChildren().addAll(background, enchantmentRow);
    }

    public void updateInventory(HashMap<Enchantment.Type, Integer> enchantments) {
        this.enchantments = enchantments;
        enchantmentRow.getChildren().clear(); // Clear previous enchantments

        for (var entry : enchantments.entrySet()) {
            Enchantment.Type type = entry.getKey();
            int count = entry.getValue();

            if (count > 0) { // Only display enchantments with count > 0
                // Create a VBox to hold the icon and count
                VBox enchantmentBox = new VBox(2); // Reduced spacing between icon and count
                enchantmentBox.setPadding(new javafx.geometry.Insets(1)); // Padding inside the box

                // Create image for the enchantment
                Image enchantmentImage = Images.convertEnchantmentToImage(type);
                if (enchantmentImage != null) {
                    ImageView enchantmentImageView = new ImageView(enchantmentImage);
                    enchantmentImageView.setFitWidth(ICON_SIZE);
                    enchantmentImageView.setFitHeight(ICON_SIZE);

                    enchantmentBox.getChildren().add(enchantmentImageView); // Add the icon to the box
                }

                // Create a count label for the enchantment
                Label countLabel = new Label("x" + count); // Display count as "xN"
                countLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: white; -fx-font-weight: bold;");
                VBox.setMargin(countLabel, new javafx.geometry.Insets(0, 0, 0, 10)); // Top, Right, Bottom, Left margin


                enchantmentBox.getChildren().add(countLabel); // Add the count label to the box

                enchantmentRow.getChildren().add(enchantmentBox); // Add the enchantment box to the HBox
            }
        }
    }
}
