package com.virtualcampushub;

import javafx.animation.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.util.Duration;

public class ProfileView {

    private final ViewManager viewManager;
    private ScrollPane root;

    public ProfileView(ViewManager viewManager) {
        this.viewManager = viewManager;
        buildUI();
    }

    private void buildUI() {
        VBox content = new VBox(24);
        content.setPadding(new Insets(32));
        content.setStyle("-fx-background-color: #0f0e1a;");

        // Carte profil principale
        VBox profileCard = buildProfileCard();

        // Statistiques
        HBox statsRow = buildStatsRow();

        // Paramètres
        VBox settingsCard = buildSettingsCard();

        content.getChildren().addAll(profileCard, statsRow, settingsCard);

        root = new ScrollPane(content);
        root.setFitToWidth(true);
        root.setStyle("-fx-background-color: #0f0e1a; -fx-background: #0f0e1a;");
        root.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    }

    private VBox buildProfileCard() {
        VBox card = new VBox(20);
        card.setPadding(new Insets(32));
        card.setStyle("""
            -fx-background-color: linear-gradient(135deg, #1a1929, #252436);
            -fx-background-radius: 20;
            -fx-border-color: rgba(108,99,255,0.2);
            -fx-border-radius: 20;
            -fx-border-width: 1;
            """);

        HBox mainRow = new HBox(24);
        mainRow.setAlignment(Pos.CENTER_LEFT);

        // Avatar avec animation
        StackPane avatarContainer = new StackPane();
        Circle avatarBg = new Circle(52);
        avatarBg.setFill(new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#6C63FF")),
                new Stop(1, Color.web("#FF6584"))));

        Circle avatarRing = new Circle(58);
        avatarRing.setFill(Color.TRANSPARENT);
        avatarRing.setStroke(Color.web("#6C63FF", 0.4));
        avatarRing.setStrokeWidth(2);

        String username = viewManager.getCurrentUsername();
        Label avatarLetter = new Label(username.substring(0, 1).toUpperCase());
        avatarLetter.setStyle("-fx-font-size: 36; -fx-font-weight: bold; -fx-text-fill: white; -fx-font-family: 'Segoe UI';");

        avatarContainer.getChildren().addAll(avatarRing, avatarBg, avatarLetter);

        // Animation pulsation
        ScaleTransition pulse = new ScaleTransition(Duration.seconds(2), avatarRing);
        pulse.setFromX(1.0);
        pulse.setFromY(1.0);
        pulse.setToX(1.08);
        pulse.setToY(1.08);
        pulse.setAutoReverse(true);
        pulse.setCycleCount(Animation.INDEFINITE);
        pulse.play();

        // Infos
        VBox infoBox = new VBox(6);
        Label nameLabel = new Label(username);
        nameLabel.setStyle("-fx-font-size: 26; -fx-font-weight: bold; -fx-text-fill: white; -fx-font-family: 'Segoe UI';");

        Label emailLabel = new Label(username.toLowerCase() + "@campus.fr");
        emailLabel.setStyle("-fx-font-size: 14; -fx-text-fill: rgba(255,255,255,0.55); -fx-font-family: 'Segoe UI';");

        HBox badges = new HBox(8);
        badges.getChildren().addAll(
                buildBadge("Étudiant", "#6C63FF"),
                buildBadge("Actif", "#43E97B"),
                buildBadge("Niveau 3", "#f7971e")
        );

        infoBox.getChildren().addAll(nameLabel, emailLabel, badges);
        HBox.setHgrow(infoBox, Priority.ALWAYS);

        // Actions
        VBox actionsBox = new VBox(10);
        actionsBox.setAlignment(Pos.CENTER_RIGHT);

        Button editBtn = new Button("✏️  Modifier le profil");
        editBtn.setPrefHeight(40);
        editBtn.setPrefWidth(180);
        editBtn.setStyle("""
            -fx-background-color: #6C63FF;
            -fx-text-fill: white;
            -fx-font-size: 12;
            -fx-font-weight: bold;
            -fx-background-radius: 12;
            -fx-cursor: hand;
            """);
        editBtn.setOnAction(e -> showEditDialog());

        Button logoutBtn = new Button("🚪  Déconnexion");
        logoutBtn.setPrefHeight(40);
        logoutBtn.setPrefWidth(180);
        logoutBtn.setStyle("""
            -fx-background-color: rgba(255,107,107,0.15);
            -fx-text-fill: #FF6B6B;
            -fx-font-size: 12;
            -fx-font-weight: bold;
            -fx-background-radius: 12;
            -fx-border-color: rgba(255,107,107,0.3);
            -fx-border-radius: 12;
            -fx-border-width: 1;
            -fx-cursor: hand;
            """);
        logoutBtn.setOnAction(e -> viewManager.showLogin());

        actionsBox.getChildren().addAll(editBtn, logoutBtn);

        mainRow.getChildren().addAll(avatarContainer, infoBox, actionsBox);

        // Progression générale
        VBox progressSection = new VBox(10);

        HBox progressHeader = new HBox();
        Label progLabel = new Label("Progression générale du semestre");
        progLabel.setStyle("-fx-font-size: 13; -fx-text-fill: rgba(255,255,255,0.6); -fx-font-family: 'Segoe UI';");
        Region spc = new Region();
        HBox.setHgrow(spc, Priority.ALWAYS);
        Label progValue = new Label("63%");
        progValue.setStyle("-fx-font-size: 14; -fx-font-weight: bold; -fx-text-fill: #6C63FF; -fx-font-family: 'Segoe UI';");
        progressHeader.getChildren().addAll(progLabel, spc, progValue);

        StackPane progressBar = new StackPane();
        progressBar.setPrefHeight(8);

        Rectangle bgBar = new Rectangle(Double.MAX_VALUE, 8);
        bgBar.setFill(Color.web("#ffffff", 0.07));
        bgBar.setArcWidth(8);
        bgBar.setArcHeight(8);
        bgBar.widthProperty().bind(progressBar.widthProperty());

        Rectangle fgBar = new Rectangle(0, 8);
        fgBar.setFill(new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#6C63FF")),
                new Stop(1, Color.web("#FF6584"))));
        fgBar.setArcWidth(8);
        fgBar.setArcHeight(8);
        progressBar.getChildren().addAll(bgBar, fgBar);
        StackPane.setAlignment(fgBar, Pos.CENTER_LEFT);

        progressBar.widthProperty().addListener((obs, old, w) -> {
            if (w.doubleValue() > 0) {
                Timeline tl = new Timeline(
                        new KeyFrame(Duration.millis(1000),
                                new KeyValue(fgBar.widthProperty(), w.doubleValue() * 0.63, Interpolator.EASE_OUT))
                );
                tl.play();
            }
        });

        progressSection.getChildren().addAll(progressHeader, progressBar);

        card.getChildren().addAll(mainRow, new Separator(), progressSection);
        return card;
    }

    private HBox buildStatsRow() {
        HBox row = new HBox(16);

        String[][] stats = {
                {"📚", "6", "Cours suivis", "#4facfe"},
                {"✅", "18", "Tâches complètes", "#43e97b"},
                {"🏆", "15.4", "Note moyenne", "#f7971e"},
                {"⏱", "87h", "Heures étudiées", "#6C63FF"}
        };

        for (String[] s : stats) {
            VBox card = new VBox(8);
            card.setPadding(new Insets(20));
            card.setAlignment(Pos.CENTER);
            card.setStyle("""
                -fx-background-color: #1e1d2e;
                -fx-background-radius: 16;
                -fx-border-color: rgba(255,255,255,0.07);
                -fx-border-radius: 16;
                -fx-border-width: 1;
                """);
            HBox.setHgrow(card, Priority.ALWAYS);

            Label icon = new Label(s[0]);
            icon.setStyle("-fx-font-size: 22;");

            Label value = new Label(s[1]);
            value.setStyle("-fx-font-size: 22; -fx-font-weight: bold; -fx-text-fill: " + s[3] + "; -fx-font-family: 'Segoe UI';");

            Label label = new Label(s[2]);
            label.setStyle("-fx-font-size: 11; -fx-text-fill: rgba(255,255,255,0.45); -fx-font-family: 'Segoe UI';");

            card.getChildren().addAll(icon, value, label);
            row.getChildren().add(card);
        }

        return row;
    }

    private VBox buildSettingsCard() {
        VBox card = new VBox(0);
        card.setStyle("""
            -fx-background-color: #1e1d2e;
            -fx-background-radius: 16;
            -fx-border-color: rgba(255,255,255,0.07);
            -fx-border-radius: 16;
            -fx-border-width: 1;
            """);

        Label title = new Label("Préférences");
        title.setStyle("-fx-font-size: 14; -fx-font-weight: bold; -fx-text-fill: white; -fx-padding: 20; -fx-font-family: 'Segoe UI';");

        Separator sep = new Separator();
        sep.setStyle("-fx-background-color: rgba(255,255,255,0.06);");

        String[][] settings = {
                {"🔔", "Notifications", "Recevoir les alertes de cours"},
                {"🌙", "Mode sombre", "Interface sombre activée"},
                {"🌐", "Langue", "Français"},
                {"🔒", "Confidentialité", "Gérer les données personnelles"}
        };

        card.getChildren().addAll(title, sep);

        for (int i = 0; i < settings.length; i++) {
            String[] s = settings[i];
            HBox item = new HBox(14);
            item.setPadding(new Insets(14, 20, 14, 20));
            item.setAlignment(Pos.CENTER_LEFT);
            if (i < settings.length - 1) {
                item.setStyle("-fx-border-color: rgba(255,255,255,0.04); -fx-border-width: 0 0 1 0;");
            }

            Label icon = new Label(s[0]);
            icon.setStyle("-fx-font-size: 18;");

            VBox textBox = new VBox(2);
            Label name = new Label(s[1]);
            name.setStyle("-fx-font-size: 13; -fx-text-fill: rgba(255,255,255,0.85); -fx-font-family: 'Segoe UI';");
            Label desc = new Label(s[2]);
            desc.setStyle("-fx-font-size: 11; -fx-text-fill: rgba(255,255,255,0.4); -fx-font-family: 'Segoe UI';");
            textBox.getChildren().addAll(name, desc);
            HBox.setHgrow(textBox, Priority.ALWAYS);

            Label arrow = new Label("›");
            arrow.setStyle("-fx-font-size: 18; -fx-text-fill: rgba(255,255,255,0.3);");

            item.getChildren().addAll(icon, textBox, arrow);
            item.setOnMouseEntered(e -> item.setStyle(item.getStyle() + "-fx-background-color: rgba(255,255,255,0.03);"));
            item.setOnMouseExited(e -> item.setStyle(item.getStyle().replace("-fx-background-color: rgba(255,255,255,0.03);", "")));

            card.getChildren().add(item);
        }

        return card;
    }

    private Label buildBadge(String text, String color) {
        Label badge = new Label(text);
        badge.setStyle(String.format("""
            -fx-background-color: %s22;
            -fx-text-fill: %s;
            -fx-background-radius: 20;
            -fx-border-color: %s44;
            -fx-border-radius: 20;
            -fx-border-width: 1;
            -fx-padding: 3 10 3 10;
            -fx-font-size: 11;
            -fx-font-weight: bold;
            """, color, color, color));
        return badge;
    }

    private void showEditDialog() {
        Alert dialog = new Alert(Alert.AlertType.INFORMATION);
        dialog.setTitle("Modifier le profil");
        dialog.setHeaderText("Modification du profil");
        dialog.setContentText("Fonctionnalité disponible dans la prochaine version.");
        dialog.showAndWait();
    }

    public ScrollPane getView() { return root; }
}
