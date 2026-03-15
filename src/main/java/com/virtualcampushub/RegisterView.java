package com.virtualcampushub;

import javafx.animation.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.util.Duration;

public class RegisterView {

    private final ViewManager viewManager;
    private StackPane root;
    private TextField usernameField, emailField, fullNameField;
    private PasswordField passwordField, confirmField;
    private Label messageLabel;

    public RegisterView(ViewManager viewManager) {
        this.viewManager = viewManager;
        buildUI();
    }

    private void buildUI() {
        root = new StackPane();
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #0f0c29 0%, #302b63 50%, #24243e 100%);");

        Circle c1 = new Circle(200);
        c1.setFill(Color.web("#FF6584", 0.07));
        c1.setTranslateX(350);
        c1.setTranslateY(-200);

        Circle c2 = new Circle(160);
        c2.setFill(Color.web("#6C63FF", 0.07));
        c2.setTranslateX(-350);
        c2.setTranslateY(200);

        VBox card = new VBox(16);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(45, 60, 45, 60));
        card.setMaxWidth(460);
        card.setStyle("""
            -fx-background-color: rgba(255,255,255,0.05);
            -fx-background-radius: 24;
            -fx-border-color: rgba(255,255,255,0.12);
            -fx-border-radius: 24;
            -fx-border-width: 1;
            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 40, 0, 0, 20);
            """);

        Label logo = new Label("🎓");
        logo.setStyle("-fx-font-size: 40;");

        Label title = new Label("Créer un compte");
        title.setStyle("-fx-font-size: 24; -fx-font-weight: bold; -fx-text-fill: white; -fx-font-family: 'Segoe UI';");

        Label subtitle = new Label("Rejoignez Virtual Campus Hub");
        subtitle.setStyle("-fx-font-size: 13; -fx-text-fill: rgba(255,255,255,0.6); -fx-font-family: 'Segoe UI';");

        usernameField = createStyledField("👤  Nom d'utilisateur", false);
        emailField = createStyledField("✉  Adresse email", false);
        fullNameField = createStyledField("👤  Nom complet (optionnel)", false);
        passwordField = (PasswordField) createStyledField("🔒  Mot de passe (min. 6 car.)", true);
        confirmField = (PasswordField) createStyledField("🔒  Confirmer le mot de passe", true);

        messageLabel = new Label();
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(340);
        messageLabel.setAlignment(Pos.CENTER);
        messageLabel.setStyle("-fx-font-size: 12; -fx-text-fill: #FF6B6B; -fx-font-family: 'Segoe UI';");

        Button registerBtn = new Button("Créer mon compte");
        registerBtn.setPrefWidth(340);
        registerBtn.setPrefHeight(46);
        registerBtn.setStyle("""
            -fx-background-color: linear-gradient(to right, #FF6584, #e05574);
            -fx-text-fill: white;
            -fx-font-size: 14;
            -fx-font-weight: bold;
            -fx-background-radius: 12;
            -fx-cursor: hand;
            -fx-effect: dropshadow(gaussian, rgba(255,101,132,0.5), 15, 0, 0, 4);
            """);
        registerBtn.setOnAction(e -> handleRegister());

        HBox loginBox = new HBox(6);
        loginBox.setAlignment(Pos.CENTER);
        Label hasAccount = new Label("Déjà un compte ?");
        hasAccount.setStyle("-fx-text-fill: rgba(255,255,255,0.6); -fx-font-size: 12;");
        Hyperlink loginLink = new Hyperlink("Se connecter");
        loginLink.setStyle("-fx-text-fill: #6C63FF; -fx-font-size: 12; -fx-border-color: transparent;");
        loginLink.setOnAction(e -> viewManager.showLogin());
        loginBox.getChildren().addAll(hasAccount, loginLink);

        card.getChildren().addAll(logo, title, subtitle, usernameField, emailField, fullNameField,
                passwordField, confirmField, messageLabel, registerBtn, loginBox);

        root.getChildren().addAll(c1, c2, card);

        // Animation entrée
        card.setTranslateY(30);
        card.setOpacity(0);
        ParallelTransition pt = new ParallelTransition(
                slideIn(card), fadeInNode(card)
        );
        pt.setDelay(Duration.millis(100));
        pt.play();
    }

    private TextField createStyledField(String prompt, boolean isPassword) {
        TextField field = isPassword ? new PasswordField() : new TextField();
        field.setPromptText(prompt);
        field.setPrefHeight(44);
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
        return field;
    }

    private TranslateTransition slideIn(javafx.scene.Node node) {
        TranslateTransition tt = new TranslateTransition(Duration.millis(500), node);
        tt.setToY(0);
        tt.setInterpolator(Interpolator.EASE_OUT);
        return tt;
    }

    private FadeTransition fadeInNode(javafx.scene.Node node) {
        FadeTransition ft = new FadeTransition(Duration.millis(500), node);
        ft.setToValue(1);
        return ft;
    }

    private void handleRegister() {
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String fullName = fullNameField.getText().trim();
        String password = passwordField.getText();
        String confirm = confirmField.getText();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
            showMessage("❌ Veuillez remplir tous les champs.", false);
            return;
        }
        if (password.length() < 6) {
            showMessage("❌ Le mot de passe doit comporter au moins 6 caractères.", false);
            return;
        }
        if (!password.equals(confirm)) {
            showMessage("❌ Les mots de passe ne correspondent pas.", false);
            return;
        }
        if (!email.contains("@")) {
            showMessage("❌ Format d'email invalide.", false);
            return;
        }

        boolean success;
        if (DatabaseConnection.isConnected()) {
            if (UserDAO.emailExists(email)) {
                showMessage("❌ Cet email est déjà utilisé.", false);
                return;
            }
            if (UserDAO.usernameExists(username)) {
                showMessage("❌ Ce nom d'utilisateur est déjà pris.", false);
                return;
            }
            success = UserDAO.registerUser(username, email, password, fullName);
        } else {
            // Mode démo : simuler succès
            success = true;
            showMessage("⚠️  Mode démo : compte simulé (DB hors ligne).", true);
        }

        if (success) {
            showMessage("✅ Compte créé avec succès ! Redirection...", true);
            PauseTransition pause = new PauseTransition(Duration.millis(1200));
            pause.setOnFinished(e -> viewManager.showLogin());
            pause.play();
        } else {
            showMessage("❌ Erreur lors de la création du compte.", false);
        }
    }

    private void showMessage(String msg, boolean success) {
        messageLabel.setText(msg);
        messageLabel.setStyle("-fx-font-size: 12; -fx-font-family: 'Segoe UI'; -fx-text-fill: "
                + (success ? "#43E97B" : "#FF6B6B") + ";");
    }

    public StackPane getView() { return root; }
}
