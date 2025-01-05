package main.view;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import main.model.Enchantment;
import main.model.Inventory;

import java.util.Map;

public class InventoryView extends Pane {
    private final Inventory inventory;
    private final ImageView inventoryBackground;

    public InventoryView(Inventory inventory) {
        this.inventory = inventory;

        // Set the inventory background image
        inventoryBackground = new ImageView(new Image("/rokue-like_assets/Inventory.png"));
        inventoryBackground.setFitWidth(200); // Adjust based on your image dimensions
        inventoryBackground.setFitHeight(100);
        this.getChildren().add(inventoryBackground);

        updateInventoryDisplay();
    }

    public void updateInventoryDisplay() {
        // Clear previous enchantment images
        this.getChildren().clear();
        this.getChildren().add(inventoryBackground);

        // Dynamically place enchantment images on the inventory background
        int slotX = 20; // Starting x position for enchantments
        int slotY = 20; // Starting y position for enchantments
        int slotSpacing = 50; // Spacing between enchantments

        for (Map.Entry<Enchantment, Integer> entry : inventory.getEnchantments().entrySet()) {
            Enchantment enchantment = entry.getKey();
            int count = entry.getValue();

            // Enchantment icon
            ImageView enchantmentIcon = new ImageView(new Image("/rokue-like_assets/" + enchantment.getName() + ".png"));
            enchantmentIcon.setFitWidth(32);
            enchantmentIcon.setFitHeight(32);
            enchantmentIcon.setLayoutX(slotX);
            enchantmentIcon.setLayoutY(slotY);

            // Enchantment count label
            Label countLabel = new Label("x" + count);
            countLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
            countLabel.setTextFill(javafx.scene.paint.Color.WHITE);
            countLabel.setLayoutX(slotX + 25); // Adjust position to overlay or place near the icon
            countLabel.setLayoutY(slotY);

            this.getChildren().addAll(enchantmentIcon, countLabel);
            slotX += slotSpacing;
        }
    }
}
