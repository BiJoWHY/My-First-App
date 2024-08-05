package Cube_snake;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;

import java.io.InputStream;

public class Cube_Snake extends Application {

    private static final double DISABLE_X_COORDINATE = 675; // Координата X в плюс
    private static final double DISABLE_Y_COORDINATE = 621; // Координаты Y в плюс
    private static final double DISABLE_X_COORDINATE2 = 26; // Координаты X в минус
    private static final double DISABLE_Y_COORDINATE2 = 27; // Координаты Y в минус

    private double previousX;
    private double previousY;
    private boolean hasLost = false; // To track if lose has already occurred

    @Override
    public void start(Stage primaryStage) {
        Pane root = new Pane();

        // the background
        String imagePath = "/Cube_snake/Background.jpg";  // Путь к ресурсу
        InputStream resourceStream = getClass().getResourceAsStream(imagePath);
        ImageView backgroundView = new ImageView();

        if (resourceStream == null) {
            System.err.println("Error: Resource not found: " + imagePath);
        } else {
            Image backgroundImage = new Image(resourceStream);
            backgroundView.setImage(backgroundImage);

            backgroundView.setFitWidth(800);
            backgroundView.setFitHeight(648);
            backgroundView.setPreserveRatio(true);

            root.getChildren().add(backgroundView);
        }

        PhongMaterial materialMain = new PhongMaterial();
        materialMain.setDiffuseColor(Color.RED);

        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.BLUE);
        dropShadow.setRadius(15);
        dropShadow.setSpread(0.5);

        // Куб основной
        Box main = new Box(54, 54, 50);
        main.setMaterial(materialMain);
        main.setLayoutX(458);
        main.setLayoutY(513);

        previousX = main.getLayoutX();
        previousY = main.getLayoutY();

        root.getChildren().add(main);
        main.setEffect(dropShadow);

        Scene scene = new Scene(root, 755, 645); // width - ширина height - высота
        scene.setOnKeyPressed(event -> handleKeyPress(event, main));

        // title
        primaryStage.setTitle("Snake");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Listener for resizing
        scene.widthProperty().addListener((observable, oldValue, newValue) -> {
            backgroundView.setFitWidth(newValue.doubleValue());
        });

        scene.heightProperty().addListener((observable, oldValue, newValue) -> {
            backgroundView.setFitHeight(newValue.doubleValue());
        });

        // In order for keyboard events to be processed
        root.requestFocus();
    }

    private void handleKeyPress(KeyEvent event, Box main) {
        if (hasLost) {
            return; // If lose has already occurred, do not handle keypresses
        }

        // Saving current coordinates
        double currentX = main.getLayoutX();
        double currentY = main.getLayoutY();

        // Saving previous coordinates
        previousX = currentX;
        previousY = currentY;

        // Handling keystrokes
        switch (event.getCode()) {
            case W:
                main.setLayoutY(currentY - 54); // вверх
                break;
            case S:
                main.setLayoutY(currentY + 54); // вниз
                break;
            case A:
                main.setLayoutX(currentX - 54); // влево
                break;
            case D:
                main.setLayoutX(currentX + 54); // вправо
                break;
            default:
                break;
        }

        // Boundary check
        boolean blockedX = main.getLayoutX() >= DISABLE_X_COORDINATE || main.getLayoutX() <= DISABLE_X_COORDINATE2;
        boolean blockedY = main.getLayoutY() >= DISABLE_Y_COORDINATE || main.getLayoutY() <= DISABLE_Y_COORDINATE2;

        if (blockedX || blockedY) {
            // Return to previous position
            main.setLayoutX(previousX);
            main.setLayoutY(previousY);

            Label lose = new Label("You lose!");
            lose.setTextFill(Color.GHOSTWHITE);
            lose.setLayoutX(300);
            lose.setLayoutY(305);
            lose.setFont(Font.font("Arial", 30));

            // text smooth falling effect
            FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), lose);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.setCycleCount(1);

            Pane root = (Pane) main.getParent();
            root.getChildren().add(lose);
            fadeIn.play();

            // loss flag
            hasLost = true;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}