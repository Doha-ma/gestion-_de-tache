package com.virtualcampushub;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.geometry.Rectangle2D;

public class Main extends Application {
    public static final String APP_TITLE = "Virtual Campus Hub";

    @Override
    public void start(Stage stage) {
        ViewManager viewManager = new ViewManager();

        // Dimensions professionnelles pour PFE
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        double width = Math.min(1600, screenBounds.getWidth() * 0.85);
        double height = Math.min(1000, screenBounds.getHeight() * 0.85);

        Scene scene = new Scene(viewManager.getRoot(), width, height);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        // Configuration professionnelle de la fenêtre
        stage.setTitle("Virtual Campus Hub - PFE 2026");
        stage.setScene(scene);
        stage.setMinWidth(1200);
        stage.setMinHeight(800);
        stage.setMaxWidth(screenBounds.getWidth());
        stage.setMaxHeight(screenBounds.getHeight());

        // Centrage intelligent
        stage.setX((screenBounds.getWidth() - width) / 2);
        stage.setY((screenBounds.getHeight() - height) / 2);

        // Icône de l'application (si disponible)
        try {
            // stage.getIcons().add(new Image(getClass().getResourceAsStream("/icon.png")));
        } catch (Exception e) {
            // Icône par défaut si non disponible
        }

        // Listener pour ajustements dynamiques professionnels
        ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) -> {
            adjustLayoutForProfessionalDisplay(scene, stage.getWidth(), stage.getHeight());
        };

        stage.widthProperty().addListener(stageSizeListener);
        stage.heightProperty().addListener(stageSizeListener);

        stage.show();

        // Démarrer par l'écran de connexion
        viewManager.showLogin();
    }

    /**
     * Ajuste la disposition pour un affichage professionnel selon la taille de la fenêtre
     */
    private void adjustLayoutForProfessionalDisplay(Scene scene, double width, double height) {
        // Ajustements pour écrans larges (professionnel)
        if (width >= 1600) {
            scene.getRoot().getStyleClass().add("wide-screen");
        } else {
            scene.getRoot().getStyleClass().remove("wide-screen");
        }

        // Ajustements pour écrans compacts
        if (width < 1300) {
            scene.getRoot().getStyleClass().add("compact-mode");
        } else {
            scene.getRoot().getStyleClass().remove("compact-mode");
        }

        // Ajustements pour très petits écrans
        if (height < 900) {
            scene.getRoot().getStyleClass().add("small-screen");
        } else {
            scene.getRoot().getStyleClass().remove("small-screen");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
