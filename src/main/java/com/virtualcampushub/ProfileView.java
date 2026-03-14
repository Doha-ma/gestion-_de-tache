package com.virtualcampushub;

import javafx.animation.FillTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Separator;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class ProfileView {
    private final VBox content;

    public ProfileView() {
        content = new VBox(20);
        content.setPadding(new Insets(20));
        content.getStyleClass().add("view");

        Label header = new Label("Profil");
        header.getStyleClass().add("view-title");

        VBox profileCard = new VBox(16);
        profileCard.getStyleClass().add("profile-card");
        profileCard.setPadding(new Insets(18));

        Circle avatar = new Circle(42, Color.web("#6A8AE4"));
        avatar.setEffect(new DropShadow(15, Color.rgb(0, 0, 0, 0.25)));

        FillTransition pulse = new FillTransition(Duration.seconds(2), avatar, Color.web("#6A8AE4"), Color.web("#7BB9FF"));
        pulse.setAutoReverse(true);
        pulse.setCycleCount(FillTransition.INDEFINITE);
        pulse.play();

        Label name = new Label("Alex Dupont");
        name.getStyleClass().add("profile-name");

        Label email = new Label("alex.dupont@campus.fr");
        email.getStyleClass().add("profile-email");

        Separator sep = new Separator();

        VBox progressBox = new VBox(12);
        Label progressTitle = new Label("Progression des tâches");
        progressTitle.getStyleClass().add("section-title");

        ProgressBar progress = new ProgressBar(0.63);
        progress.getStyleClass().add("profile-progress");

        Label progressLabel = new Label("63% complété");
        progressLabel.getStyleClass().add("profile-progress-label");

        progressBox.getChildren().addAll(progressTitle, progress, progressLabel);

        Button edit = new Button("Modifier le profil");
        edit.getStyleClass().add("profile-button");

        HBox headerBox = new HBox(14, avatar, new VBox(4, name, email));
        headerBox.setAlignment(Pos.CENTER_LEFT);

        profileCard.getChildren().addAll(headerBox, sep, progressBox, edit);

        content.getChildren().addAll(header, profileCard);
    }

    public VBox getContent() {
        return content;
    }
}
