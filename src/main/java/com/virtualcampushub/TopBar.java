package com.virtualcampushub;

import javafx.animation.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Duration;

public class TopBar {

    private final ViewManager viewManager;
    private HBox root;
    private Label titleLabel;
    private Label toastLabel;

    public TopBar(ViewManager viewManager) {
        this.viewManager = viewManager;
        buildUI();
    }

    private void buildUI() {
        root = new HBox();
        root.setPrefHeight(64);
        root.setPadding(new Insets(0, 24, 0, 24));
        root.setAlignment(Pos.CENTER_LEFT);
        root.setStyle("""
            -fx-background-color: #1a1929;
            -fx-border-color: rgba(255,255,255,0.07);
            -fx-border-width: 0 0 1 0;
            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 2);
            """);

        // Titre de la vue courante
        titleLabel = new Label("Tableau de bord");
        titleLabel.setStyle("""
            -fx-font-size: 18;
            -fx-font-weight: bold;
            -fx-text-fill: white;
            -fx-font-family: 'Segoe UI';
            """);

        // Spacer
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Recherche
        TextField searchField = new TextField();
        searchField.setPromptText("🔍  Rechercher...");
        searchField.setPrefWidth(220);
        searchField.setPrefHeight(36);
        searchField.setStyle("""
            -fx-background-color: rgba(255,255,255,0.07);
            -fx-text-fill: white;
            -fx-prompt-text-fill: rgba(255,255,255,0.35);
            -fx-background-radius: 20;
            -fx-border-color: rgba(255,255,255,0.12);
            -fx-border-radius: 20;
            -fx-border-width: 1;
            -fx-padding: 0 16 0 16;
            -fx-font-size: 12;
            -fx-font-family: 'Segoe UI';
            """);

        // Toast notification
        toastLabel = new Label();
        toastLabel.setVisible(false);
        toastLabel.setStyle("""
            -fx-background-color: #43E97B;
            -fx-text-fill: #0a0a0a;
            -fx-background-radius: 20;
            -fx-padding: 6 16 6 16;
            -fx-font-size: 12;
            -fx-font-weight: bold;
            -fx-font-family: 'Segoe UI';
            """);

        // Bouton notifications
        Button notifBtn = new Button("🔔");
        notifBtn.setPrefSize(40, 40);
        notifBtn.setStyle("""
            -fx-background-color: rgba(255,255,255,0.07);
            -fx-background-radius: 12;
            -fx-font-size: 16;
            -fx-cursor: hand;
            -fx-border-color: rgba(255,255,255,0.1);
            -fx-border-radius: 12;
            -fx-border-width: 1;
            """);
        notifBtn.setOnAction(e -> showToast("Aucune nouvelle notification"));
        notifBtn.setOnMouseEntered(e -> notifBtn.setStyle("""
            -fx-background-color: rgba(108,99,255,0.2);
            -fx-background-radius: 12;
            -fx-font-size: 16;
            -fx-cursor: hand;
            -fx-border-color: rgba(108,99,255,0.4);
            -fx-border-radius: 12;
            -fx-border-width: 1;
            """));
        notifBtn.setOnMouseExited(e -> notifBtn.setStyle("""
            -fx-background-color: rgba(255,255,255,0.07);
            -fx-background-radius: 12;
            -fx-font-size: 16;
            -fx-cursor: hand;
            -fx-border-color: rgba(255,255,255,0.1);
            -fx-border-radius: 12;
            -fx-border-width: 1;
            """));

        HBox rightSection = new HBox(12);
        rightSection.setAlignment(Pos.CENTER_RIGHT);
        rightSection.getChildren().addAll(toastLabel, searchField, notifBtn);

        root.getChildren().addAll(titleLabel, spacer, rightSection);
    }

    public void setTitle(String title) {
        titleLabel.setText(title);
        FadeTransition ft = new FadeTransition(Duration.millis(200), titleLabel);
        ft.setFromValue(0.3);
        ft.setToValue(1.0);
        ft.play();
    }

    public void showToast(String message) {
        toastLabel.setText(message);
        toastLabel.setVisible(true);
        toastLabel.setOpacity(1.0);

        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(e -> {
            FadeTransition fade = new FadeTransition(Duration.millis(400), toastLabel);
            fade.setToValue(0);
            fade.setOnFinished(ev -> toastLabel.setVisible(false));
            fade.play();
        });
        pause.play();
    }

    public HBox getView() { return root; }
}
