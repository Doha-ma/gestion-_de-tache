package com.virtualcampushub;

import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class TopBar {
    private final HBox content;

    public TopBar(ViewManager viewManager) {
        content = new HBox(12);
        content.getStyleClass().add("topbar");
        content.setAlignment(Pos.CENTER_LEFT);
        content.setPadding(new Insets(12, 18, 12, 18));

        Label title = new Label("Virtual Campus Hub");
        title.getStyleClass().add("topbar-title");

        TextField search = new TextField();
        search.setPromptText("Rechercher...");
        search.getStyleClass().add("topbar-search");
        search.setPrefWidth(250);

        Button notify = new Button("🔔");
        notify.getStyleClass().add("topbar-button");
        notify.setOnAction(e -> {
            viewManager.showToast("Vous n'avez pas de nouvelles notifications.");
            animateNotify(notify);
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        content.getChildren().addAll(title, spacer, search, notify);
    }

    public HBox getContent() {
        return content;
    }

    private void animateNotify(Button button) {
        TranslateTransition t = new TranslateTransition(Duration.millis(80), button);
        t.setByY(-6);
        t.setAutoReverse(true);
        t.setCycleCount(2);
        t.play();
    }
}
