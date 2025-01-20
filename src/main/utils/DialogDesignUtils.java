package main.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;

public class DialogDesignUtils {

    /**
     * Applies custom styling to the dialog pane and its elements.
     *
     * @param alert The alert dialog to be styled.
     */
    public static void styleDialog(Alert alert) {
        DialogPane dialogPane = alert.getDialogPane();

        // Style the entire dialog
        dialogPane.setStyle(
            "-fx-background-color: #352645; " + // Purple background
            "-fx-border-color: #FFFFFF; " +     // White border
            "-fx-border-width: 2px;"
        );

        // Style the header panel
        dialogPane.lookup(".header-panel").setStyle(
            "-fx-background-color: #5A3E77; " + // Dark purple
            "-fx-font-size: 18px; " +
            "-fx-font-weight: bold; " +
            "-fx-text-fill: white; " +
            "-fx-padding: 10px;"
        );

        // Style the content area
        dialogPane.lookup(".content").setStyle(
            "-fx-font-size: 14px; " +
            "-fx-text-fill: #FFD700; " + // Gold text
            "-fx-padding: 10px;"
        );

        // Style the OK button
        dialogPane.lookupButton(ButtonType.OK).setStyle(
            "-fx-background-color: #5A3E77; " + // Dark purple button
            "-fx-text-fill: white; " +
            "-fx-font-size: 14px; " +
            "-fx-padding: 5px 10px; " +
            "-fx-background-radius: 5px;"
        );
    }

    /**
     * Returns a styled button.
     *
     * @param text The button text.
     * @return A styled button.
     */
    public static javafx.scene.control.Button createStyledButton(String text) {
        javafx.scene.control.Button button = new javafx.scene.control.Button(text);
        button.setStyle(
            "-fx-background-color: #303843; " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 18px; " +
            "-fx-padding: 10px 27px; " +
            "-fx-background-radius: 10; " +
            "-fx-border-color: #FFFFFF; " +
            "-fx-border-width: 1px; " +
            "-fx-border-radius: 10; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 5, 0, 0, 1);"
        );

        // Hover effect
        button.setOnMouseEntered(e -> button.setStyle(
            "-fx-background-color: rgb(78, 90, 107); " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 18px; " +
            "-fx-padding: 10px 27px; " +
            "-fx-background-radius: 10; " +
            "-fx-border-color: #FFFFFF; " +
            "-fx-border-width: 1px; " +
            "-fx-border-radius: 10; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 5, 0, 0, 1);"
        ));

        button.setOnMouseExited(e -> button.setStyle(
            "-fx-background-color: #303843; " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 18px; " +
            "-fx-padding: 10px 27px; " +
            "-fx-background-radius: 10; " +
            "-fx-border-color: #FFFFFF; " +
            "-fx-border-width: 1px; " +
            "-fx-border-radius: 10; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 5, 0, 0, 1);"
        ));

        return button;
    }
}
