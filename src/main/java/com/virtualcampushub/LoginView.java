package com.virtualcampushub;

import javafx.animation.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.text.*;
import javafx.util.Duration;

public class LoginView {

    private final ViewManager viewManager;
    private StackPane root;
    private TextField emailField;
    private PasswordField passwordField;
    private Label messageLabel;

    public LoginView(ViewManager viewManager) {
        this.viewManager = viewManager;
        buildUI();
    }

    private void buildUI() {
        root = new StackPane();
        root.getStyleClass().add("auth-root");

        // Fond avec gradient
        root.setStyle("-fx-background-color: linear-gradient(135deg, #0f0c29 0%, #302b63 50%, #24243e 100%);");

        // Cercles décoratifs en fond
        Circle c1 = new Circle(250);
        c1.setFill(Color.web("#6C63FF", 0.08));
        c1.setTranslateX(-400);
        c1.setTranslateY(-200);

        Circle c2 = new Circle(180);
        c2.setFill(Color.web("#FF6584", 0.06));
        c2.setTranslateX(400);
        c2.setTranslateY(250);

        Circle c3 = new Circle(120);
        c3.setFill(Color.web("#43E97B", 0.05));
        c3.setTranslateX(200);
        c3.setTranslateY(-300);

        // Carte centrale
        VBox card = new VBox(20);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(50, 60, 50, 60));
        card.setMaxWidth(460);
        card.setStyle("""
            -fx-background-color: rgba(255,255,255,0.05);
            -fx-background-radius: 24;
            -fx-border-color: rgba(255,255,255,0.12);
            -fx-border-radius: 24;
            -fx-border-width: 1;
            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 40, 0, 0, 20);
            """);

        // Logo / Icône
        Label logo = new Label("🎓");
        logo.setStyle("-fx-font-size: 48;");

        // Titre
        Label title = new Label("Virtual Campus Hub");
        title.setStyle("""
            -fx-font-size: 26;
            -fx-font-weight: bold;
            -fx-text-fill: white;
            -fx-font-family: 'Segoe UI';
            """);

        Label subtitle = new Label("Connectez-vous à votre espace académique");
        subtitle.setStyle("-fx-font-size: 13; -fx-text-fill: rgba(255,255,255,0.6); -fx-font-family: 'Segoe UI';");
        subtitle.setWrapText(true);
        subtitle.setTextAlignment(TextAlignment.CENTER);

        // Séparateur
        Separator sep = new Separator();
        sep.setStyle("-fx-background-color: rgba(255,255,255,0.1);");

        // Champs
        emailField = createTextField("✉  Adresse email", false);
        passwordField = (PasswordField) createTextField("🔒  Mot de passe", true);

        // Message erreur/succès
        messageLabel = new Label();
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(340);
        messageLabel.setAlignment(Pos.CENTER);
        messageLabel.setStyle("-fx-font-size: 12; -fx-text-fill: #FF6B6B; -fx-font-family: 'Segoe UI';");

        // Bouton connexion
        Button loginBtn = new Button("Se connecter");
        loginBtn.setPrefWidth(340);
        loginBtn.setPrefHeight(46);
        loginBtn.getStyleClass().add("btn-primary");
        loginBtn.setStyle("""
            -fx-background-color: linear-gradient(to right, #6C63FF, #3b5bdb);
            -fx-text-fill: white;
            -fx-font-size: 14;
            -fx-font-weight: bold;
            -fx-background-radius: 12;
            -fx-cursor: hand;
            -fx-effect: dropshadow(gaussian, rgba(108,99,255,0.5), 15, 0, 0, 4);
            """);
        loginBtn.setOnMouseEntered(e -> loginBtn.setStyle("""
            -fx-background-color: linear-gradient(to right, #7c74ff, #4c6aed);
            -fx-text-fill: white;
            -fx-font-size: 14;
            -fx-font-weight: bold;
            -fx-background-radius: 12;
            -fx-cursor: hand;
            -fx-effect: dropshadow(gaussian, rgba(108,99,255,0.7), 20, 0, 0, 6);
            """));
        loginBtn.setOnMouseExited(e -> loginBtn.setStyle("""
            -fx-background-color: linear-gradient(to right, #6C63FF, #3b5bdb);
            -fx-text-fill: white;
            -fx-font-size: 14;
            -fx-font-weight: bold;
            -fx-background-radius: 12;
            -fx-cursor: hand;
            -fx-effect: dropshadow(gaussian, rgba(108,99,255,0.5), 15, 0, 0, 4);
            """));
        loginBtn.setOnAction(e -> handleLogin());

        // Lien inscription
        HBox registerBox = new HBox(6);
        registerBox.setAlignment(Pos.CENTER);
        Label noAccount = new Label("Pas encore de compte ?");
        noAccount.setStyle("-fx-text-fill: rgba(255,255,255,0.6); -fx-font-size: 12;");
        Hyperlink registerLink = new Hyperlink("S'inscrire");
        registerLink.setStyle("-fx-text-fill: #6C63FF; -fx-font-size: 12; -fx-border-color: transparent;");
        registerLink.setOnAction(e -> viewManager.showRegister());
        registerBox.getChildren().addAll(noAccount, registerLink);

        // Entrée avec Enter
        passwordField.setOnAction(e -> handleLogin());

        card.getChildren().addAll(logo, title, subtitle, sep, emailField, passwordField,
                messageLabel, loginBtn, registerBox);

        root.getChildren().addAll(c1, c2, c3, card);

        // Animation d'entrée
        card.setTranslateY(30);
        card.setOpacity(0);
        TranslateTransition tt = new TranslateTransition(Duration.millis(500), card);
        tt.setToY(0);
        tt.setInterpolator(Interpolator.EASE_OUT);
        FadeTransition ft = new FadeTransition(Duration.millis(500), card);
        ft.setToValue(1);
        ParallelTransition pt = new ParallelTransition(tt, ft);
        pt.setDelay(Duration.millis(100));
        pt.play();
    }

    private TextField createTextField(String prompt, boolean isPassword) {
        TextField field = isPassword ? new PasswordField() : new TextField();
        field.setPromptText(prompt);
        field.setPrefHeight(46);
        field.setPrefWidth(340);
        field.setStyle("""
            -fx-background-color: rgba(255,255,255,0.08);
            -fx-text-fill: white;
            -fx-prompt-text-fill: rgba(255,255,255,0.4);
            -fx-background-radius: 12;
            -fx-border-color: rgba(255,255,255,0.15);
            -fx-border-radius: 12;
            -fx-border-width: 1;
            -fx-padding: 0 16 0 16;
            -fx-font-size: 13;
            -fx-font-family: 'Segoe UI';
            """);
        field.focusedProperty().addListener((obs, old, focused) -> {
            if (focused) {
                field.setStyle(field.getStyle().replace("rgba(255,255,255,0.15)", "#6C63FF"));
            } else {
                field.setStyle(field.getStyle().replace("#6C63FF", "rgba(255,255,255,0.15)"));
            }
        });
        return field;
    }

    private void handleLogin() {
        String email = emailField.getText().trim();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showMessage("Veuillez remplir tous les champs.", false);
            return;
        }

        // Tentative connexion DB
        boolean loggedIn = false;
        boolean dbAvailable = DatabaseConnection.isConnected();

        if (dbAvailable) {
            loggedIn = UserDAO.loginUser(email, password);
        } else {
            // Mode démo : accepter admin@campus.fr / admin123
            loggedIn = email.equals("admin@campus.fr") && password.equals("admin123");
        }

        if (loggedIn) {
            String username = dbAvailable ? UserDAO.getUsernameByEmail(email) : "Admin";
            int userId = dbAvailable ? UserDAO.getUserIdByEmail(email) : 1;
            viewManager.setCurrentUsername(username);
            viewManager.setCurrentUserId(userId);
            showMessage("✅ Connexion réussie ! Bienvenue " + username, true);

            // Transition après 800ms
            PauseTransition pause = new PauseTransition(Duration.millis(800));
            pause.setOnFinished(e -> viewManager.showDashboard());
            pause.play();
        } else {
            showMessage("❌ Email ou mot de passe incorrect.", false);
            shakeCard();
        }
    }

    private void showMessage(String msg, boolean success) {
        messageLabel.setText(msg);
        messageLabel.setStyle("-fx-font-size: 12; -fx-font-family: 'Segoe UI'; -fx-text-fill: "
                + (success ? "#43E97B" : "#FF6B6B") + ";");
    }

    private void shakeCard() {
        TranslateTransition shake = new TranslateTransition(Duration.millis(60), root);
        shake.setCycleCount(6);
        shake.setAutoReverse(true);
        shake.setByX(10);
        shake.play();
    }

    public StackPane getView() { return root; }
}
