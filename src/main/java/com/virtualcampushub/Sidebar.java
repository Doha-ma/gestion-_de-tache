package com.virtualcampushub;

import javafx.animation.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Map;

public class Sidebar {

    private final ViewManager viewManager;
    private VBox root;
    private final Map<String, Button> navButtons = new HashMap<>();
    private String activeKey = "dashboard";

    public Sidebar(ViewManager viewManager) {
        this.viewManager = viewManager;
        buildUI();
    }

    private void buildUI() {
        root = new VBox();
        root.setPrefWidth(220);
        root.setStyle("""
            -fx-background-color: #12111A;
            -fx-border-color: rgba(255,255,255,0.07);
            -fx-border-width: 0 1 0 0;
            """);

        // Logo section
        VBox logoBox = new VBox(6);
        logoBox.setPadding(new Insets(28, 20, 24, 20));
        logoBox.setAlignment(Pos.CENTER_LEFT);

        HBox logoRow = new HBox(12);
        logoRow.setAlignment(Pos.CENTER_LEFT);

        Label logoIcon = new Label("🎓");
        logoIcon.setStyle("-fx-font-size: 28;");

        VBox logoText = new VBox(2);
        Label appName = new Label("Virtual Campus");
        appName.setStyle("-fx-font-size: 14; -fx-font-weight: bold; -fx-text-fill: white; -fx-font-family: 'Segoe UI';");
        Label appSub = new Label("Hub");
        appSub.setStyle("-fx-font-size: 12; -fx-text-fill: #6C63FF; -fx-font-family: 'Segoe UI';");
        logoText.getChildren().addAll(appName, appSub);
        logoRow.getChildren().addAll(logoIcon, logoText);
        logoBox.getChildren().add(logoRow);

        // Séparateur
        Separator sep1 = new Separator();
        sep1.setStyle("-fx-background-color: rgba(255,255,255,0.08);");

        // Menu section
        VBox menuSection = new VBox(4);
        menuSection.setPadding(new Insets(16, 12, 0, 12));

        Label menuLabel = new Label("MENU");
        menuLabel.setStyle("-fx-font-size: 10; -fx-text-fill: rgba(255,255,255,0.3); -fx-padding: 0 0 8 8; -fx-font-family: 'Segoe UI';");

        menuSection.getChildren().addAll(menuLabel,
                createNavBtn("dashboard", "🏠", "Tableau de bord", () -> viewManager.showDashboard()),
                createNavBtn("chat", "💬", "Messagerie", () -> viewManager.showChat()),
                createNavBtn("courses", "📚", "Mes Cours", () -> viewManager.showCourses()),
                createNavBtn("events", "📅", "Événements", () -> viewManager.showEvents())
        );

        // Séparateur
        Separator sep2 = new Separator();
        sep2.setStyle("-fx-background-color: rgba(255,255,255,0.08);");
        VBox.setMargin(sep2, new Insets(8, 12, 8, 12));

        // Section Compte
        VBox accountSection = new VBox(4);
        accountSection.setPadding(new Insets(0, 12, 0, 12));

        Label accountLabel = new Label("COMPTE");
        accountLabel.setStyle("-fx-font-size: 10; -fx-text-fill: rgba(255,255,255,0.3); -fx-padding: 0 0 8 8; -fx-font-family: 'Segoe UI';");

        accountSection.getChildren().addAll(accountLabel,
                createNavBtn("profile", "👤", "Mon Profil", () -> viewManager.showProfile())
        );

        // Spacer
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        // Info utilisateur en bas
        VBox userBox = new VBox(6);
        userBox.setPadding(new Insets(16, 16, 20, 16));
        userBox.setStyle("""
            -fx-background-color: rgba(108,99,255,0.1);
            -fx-background-radius: 12;
            """);
        VBox.setMargin(userBox, new Insets(0, 12, 16, 12));

        HBox userRow = new HBox(10);
        userRow.setAlignment(Pos.CENTER_LEFT);

        Circle avatar = new Circle(18);
        avatar.setFill(Color.web("#6C63FF"));

        Label avatarLetter = new Label(viewManager.getCurrentUsername().substring(0, 1).toUpperCase());
        avatarLetter.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13;");
        StackPane avatarStack = new StackPane(avatar, avatarLetter);

        VBox userInfo = new VBox(2);
        Label userName = new Label(viewManager.getCurrentUsername());
        userName.setStyle("-fx-text-fill: white; -fx-font-size: 12; -fx-font-weight: bold; -fx-font-family: 'Segoe UI';");
        Label userStatus = new Label("● En ligne");
        userStatus.setStyle("-fx-text-fill: #43E97B; -fx-font-size: 10; -fx-font-family: 'Segoe UI';");
        userInfo.getChildren().addAll(userName, userStatus);

        userRow.getChildren().addAll(avatarStack, userInfo);
        userBox.getChildren().add(userRow);

        root.getChildren().addAll(logoBox, sep1, menuSection, sep2, accountSection, spacer, userBox);
    }

    private Button createNavBtn(String key, String icon, String label, Runnable action) {
        Button btn = new Button();
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setPrefHeight(44);

        HBox content = new HBox(12);
        content.setAlignment(Pos.CENTER_LEFT);
        content.setPadding(new Insets(0, 8, 0, 8));

        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-font-size: 16;");

        Label textLabel = new Label(label);
        textLabel.setStyle("-fx-font-size: 13; -fx-font-family: 'Segoe UI';");

        content.getChildren().addAll(iconLabel, textLabel);
        btn.setGraphic(content);

        applyInactiveStyle(btn, textLabel);

        btn.setOnMouseEntered(e -> {
            if (!key.equals(activeKey)) {
                btn.setStyle("""
                    -fx-background-color: rgba(255,255,255,0.07);
                    -fx-background-radius: 10;
                    -fx-border-color: transparent;
                    -fx-cursor: hand;
                    """);
                textLabel.setStyle("-fx-font-size: 13; -fx-font-family: 'Segoe UI'; -fx-text-fill: white;");
            }
        });
        btn.setOnMouseExited(e -> {
            if (!key.equals(activeKey)) applyInactiveStyle(btn, textLabel);
        });

        btn.setOnAction(e -> {
            action.run();
        });

        navButtons.put(key, btn);
        return btn;
    }

    private void applyInactiveStyle(Button btn, Label text) {
        btn.setStyle("""
            -fx-background-color: transparent;
            -fx-background-radius: 10;
            -fx-border-color: transparent;
            -fx-cursor: hand;
            """);
        text.setStyle("-fx-font-size: 13; -fx-font-family: 'Segoe UI'; -fx-text-fill: rgba(255,255,255,0.55);");
    }

    private void applyActiveStyle(Button btn, Label text) {
        btn.setStyle("""
            -fx-background-color: rgba(108,99,255,0.2);
            -fx-background-radius: 10;
            -fx-border-color: rgba(108,99,255,0.4);
            -fx-border-radius: 10;
            -fx-border-width: 1;
            -fx-cursor: hand;
            """);
        text.setStyle("-fx-font-size: 13; -fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-text-fill: #a89cff;");
    }

    public void setActive(String key) {
        // Reset ancien
        navButtons.forEach((k, btn) -> {
            if (btn.getGraphic() instanceof HBox hbox) {
                Label txt = (Label) hbox.getChildren().get(1);
                applyInactiveStyle(btn, txt);
            }
        });

        // Activer nouveau
        activeKey = key;
        Button activeBtn = navButtons.get(key);
        if (activeBtn != null && activeBtn.getGraphic() instanceof HBox hbox) {
            Label txt = (Label) hbox.getChildren().get(1);
            applyActiveStyle(activeBtn, txt);

            ScaleTransition st = new ScaleTransition(Duration.millis(150), activeBtn);
            st.setFromX(0.96);
            st.setFromY(0.96);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();
        }
    }

    public VBox getView() { return root; }
}
