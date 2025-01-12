package oldFiles;

import java.io.File;
import java.util.List;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import main.model.*;



public class Draw extends Application{
    //hello
    //sfjkvsfjkvjf

    static final Image blockImage = new Image("/rokue-like_assets/Block_x2_32_40.png");
    static final Image sideBorder = new Image("/rokue-like_assets/BlockSideBorder_x2_10_36.png");
    static final Image bottomBorder = new Image("/rokue-like_assets/BlockBottomBorder_x2_10_46.png");
    static final Image upBorder = new Image("/rokue-like_assets/BlockUpBorder_x2_10_48.png");

    public void start(Stage primaryStage) {
        // Create a rectangle
        
        double blockCount = 10;
        double sideBorderCount = 8;


        // Pane to hold the hall
        Pane pane = new Pane();

        double blockWidth = 32;
        double blockHeight = 40;

        double sideBorderWidth = 10;
        double sideBorderHeight = 36;

        double bottomBorderWidth = 10;
        double bottomBorderHeight = 46;

        double upBorderWidth = 10;
        double upBorderHeight = 48;

        Rectangle upBorder1 = new Rectangle(0,0,upBorderWidth+1,upBorderHeight);
        upBorder1.setFill(new ImagePattern(upBorder));
        pane.getChildren().add(upBorder1);

        Rectangle downBorder1 = new Rectangle(0,upBorderHeight+sideBorderCount*sideBorderHeight,bottomBorderWidth+1,bottomBorderHeight);
        downBorder1.setFill(new ImagePattern(bottomBorder));
        pane.getChildren().add(downBorder1);

        Rectangle upBorder2 = new Rectangle(upBorderWidth+blockCount*blockWidth,0,upBorderWidth,upBorderHeight);
        upBorder2.setFill(new ImagePattern(upBorder));
        pane.getChildren().add(upBorder2);

        Rectangle downBorder2 = new Rectangle(bottomBorderWidth+blockCount*blockWidth,upBorderHeight+sideBorderCount*sideBorderHeight,bottomBorderWidth,bottomBorderHeight);
        downBorder2.setFill(new ImagePattern(bottomBorder));
        pane.getChildren().add(downBorder2);


        // Top wall
        for (int i = 0; i < blockCount; i++) {
            Rectangle topWall = new Rectangle(upBorderWidth+i*blockWidth, 0, blockWidth+1, blockHeight);
            //Rectangle topWall = new Rectangle(i*blockWidth, 0, blockWidth, blockHeight);
            topWall.setFill(new ImagePattern(blockImage));
            pane.getChildren().add(topWall);
        }

        // Bottom wall
        for (int i = 0; i < blockCount; i++) {
            Rectangle bottomWall = new Rectangle(bottomBorderWidth+i * blockWidth, 6+upBorderHeight+sideBorderCount*sideBorderHeight, blockWidth+1,blockHeight);
            bottomWall.setFill(new ImagePattern(blockImage));
            pane.getChildren().add(bottomWall);
        }

        // Left wall
        for (int i = 0; i < sideBorderCount; i++) {
            Rectangle leftWall = new Rectangle(0,upBorderHeight+ i * sideBorderHeight, sideBorderWidth+1, sideBorderHeight);
            leftWall.setFill(new ImagePattern(sideBorder));
            pane.getChildren().add(leftWall);
        }

        // Right wall
        for (int i = 0; i < sideBorderCount; i++) {
            Rectangle rightWall = new Rectangle(sideBorderWidth+blockWidth*blockCount,upBorderHeight+ i * sideBorderHeight, sideBorderWidth+1, sideBorderHeight);
            rightWall.setFill(new ImagePattern(sideBorder));
            pane.getChildren().add(rightWall);
        }

        // Create a scene
        Scene scene = new Scene(pane, upBorderWidth*2+blockCount*blockWidth, upBorderHeight+bottomBorderHeight+sideBorderCount*sideBorderHeight);
        primaryStage.setTitle("Tiled Hall Example");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
