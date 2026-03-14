package com.virtualcampushub;

import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

public class RegisterView {
    private final BorderPane content;
    private final TextField usernameField;
    private final TextField emailField;
    private final PasswordField passwordField;
    private final PasswordField confirmPasswordField;
    private final Button registerButton;
    private final Button backToLoginButton;
    private final Label statusLabel;
    private final ViewManager viewManager;

    public RegisterView(ViewManager viewManager) {
        this.viewManager = viewManager;
        content = new BorderPane();
        content.getStyleClass().add("register-view");

        // Header
        VBox headerBox = new VBox(10);
        headerBox.setAlignment(Pos.CENTER);
        headerBox.setPadding(new Insets(40, 20, 20, 20));

        Label titleLabel = new Label("Virtual Campus Hub");
        titleLabel.getStyleClass().add("register-title");
        titleLabel.setFont(Font.font("Inter", FontWeight.BOLD, 32));

        Label subtitleLabel = new Label("Créer un nouveau compte");
        subtitleLabel.getStyleClass().add("register-subtitle");
        subtitleLabel.setFont(Font.font("Inter", FontWeight.NORMAL, 16));

        headerBox.getChildren().addAll(titleLabel, subtitleLabel);

        // Register Form
        VBox formBox = new VBox(20);
        formBox.setAlignment(Pos.CENTER);
        formBox.setPadding(new Insets(40, 60, 40, 60));
        formBox.setMaxWidth(400);
        formBox.getStyleClass().add("register-form");

        // Username field
        VBox usernameBox = new VBox(8);
        usernameBox.setAlignment(Pos.CENTER_LEFT);

        Label usernameLabel = new Label("Nom d'utilisateur");
        usernameLabel.getStyleClass().add("register-label");
        usernameLabel.setFont(Font.font("Inter", FontWeight.MEDIUM, 14));

        usernameField = new TextField();
        usernameField.setPromptText("Choisissez un nom d'utilisateur");
        usernameField.getStyleClass().add("register-input");
        usernameField.setPrefWidth(300);

        usernameBox.getChildren().addAll(usernameLabel, usernameField);

        // Email field
        VBox emailBox = new VBox(8);
        emailBox.setAlignment(Pos.CENTER_LEFT);

        Label emailLabel = new Label("Adresse email");
        emailLabel.getStyleClass().add("register-label");
        emailLabel.setFont(Font.font("Inter", FontWeight.MEDIUM, 14));

        emailField = new TextField();
        emailField.setPromptText("votre.email@exemple.com");
        emailField.getStyleClass().add("register-input");
        emailField.setPrefWidth(300);

        emailBox.getChildren().addAll(emailLabel, emailField);

        // Password field
        VBox passwordBox = new VBox(8);
        passwordBox.setAlignment(Pos.CENTER_LEFT);

        Label passwordLabel = new Label("Mot de passe");
        passwordLabel.getStyleClass().add("register-label");
        passwordLabel.setFont(Font.font("Inter", FontWeight.MEDIUM, 14));

        passwordField = new PasswordField();
        passwordField.setPromptText("Créez un mot de passe sécurisé");
        passwordField.getStyleClass().add("register-input");
        passwordField.setPrefWidth(300);

        passwordBox.getChildren().addAll(passwordLabel, passwordField);

        // Confirm Password field
        VBox confirmPasswordBox = new VBox(8);
        confirmPasswordBox.setAlignment(Pos.CENTER_LEFT);

        Label confirmPasswordLabel = new Label("Confirmer le mot de passe");
        confirmPasswordLabel.getStyleClass().add("register-label");
        confirmPasswordLabel.setFont(Font.font("Inter", FontWeight.MEDIUM, 14));

        confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirmez votre mot de passe");
        confirmPasswordField.getStyleClass().add("register-input");
        confirmPasswordField.setPrefWidth(300);

        confirmPasswordBox.getChildren().addAll(confirmPasswordLabel, confirmPasswordField);

        // Buttons
        registerButton = new Button("Créer un compte");
        registerButton.getStyleClass().add("register-button");
        registerButton.setPrefWidth(300);
        registerButton.setPrefHeight(45);

        backToLoginButton = new Button("← Retour à la connexion");
        backToLoginButton.getStyleClass().add("back-button");
        backToLoginButton.setPrefWidth(300);
        backToLoginButton.setPrefHeight(35);

        // Status label
        statusLabel = new Label("");
        statusLabel.getStyleClass().add("register-status");
        statusLabel.setFont(Font.font("Inter", FontWeight.NORMAL, 12));

        formBox.getChildren().addAll(usernameBox, emailBox, passwordBox, confirmPasswordBox,
                                   registerButton, backToLoginButton, statusLabel);

        // Center container
        VBox centerContainer = new VBox();
        centerContainer.setAlignment(Pos.CENTER);
        centerContainer.getChildren().add(formBox);

        content.setTop(headerBox);
        content.setCenter(centerContainer);

        // Event handlers
        setupEventHandlers();
    }

    private void setupEventHandlers() {
        registerButton.setOnAction(e -> attemptRegistration());

        backToLoginButton.setOnAction(e -> viewManager.showLogin());

        // Real-time validation
        usernameField.textProperty().addListener((obs, oldText, newText) -> {
            updateRegisterButtonState();
        });

        emailField.textProperty().addListener((obs, oldText, newText) -> {
            updateRegisterButtonState();
        });

        passwordField.textProperty().addListener((obs, oldText, newText) -> {
            updateRegisterButtonState();
        });

        confirmPasswordField.textProperty().addListener((obs, oldText, newText) -> {
            updateRegisterButtonState();
        });
    }

    private void updateRegisterButtonState() {
        boolean hasUsername = !usernameField.getText().trim().isEmpty();
        boolean hasEmail = !emailField.getText().trim().isEmpty() && isValidEmail(emailField.getText().trim());
        boolean hasPassword = passwordField.getText().length() >= 6;
        boolean passwordsMatch = passwordField.getText().equals(confirmPasswordField.getText());

        registerButton.setDisable(!(hasUsername && hasEmail && hasPassword && passwordsMatch));
    }

    private boolean isValidEmail(String email) {
        return email.contains("@") && email.contains(".") && email.length() >= 5;
    }

    private void attemptRegistration() {
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();

        // Validation côté client
        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showStatus("Veuillez remplir tous les champs", Color.ORANGE);
            return;
        }

        if (username.length() < 3) {
            showStatus("Le nom d'utilisateur doit contenir au moins 3 caractères", Color.ORANGE);
            return;
        }

        if (!isValidEmail(email)) {
            showStatus("Veuillez entrer une adresse email valide", Color.ORANGE);
            return;
        }

        if (password.length() < 6) {
            showStatus("Le mot de passe doit contenir au moins 6 caractères", Color.ORANGE);
            return;
        }

        if (!password.equals(confirmPasswordField.getText())) {
            showStatus("Les mots de passe ne correspondent pas", Color.ORANGE);
            return;
        }

        // Désactiver le bouton pendant le traitement
        registerButton.setDisable(true);
        registerButton.setText("Création du compte...");
        showStatus("Création du compte en cours...", Color.BLUE);

        // Traitement en arrière-plan
        new Thread(() -> {
            try {
                // Vérifier la connexion à la base de données
                if (!DatabaseConnection.testConnection()) {
                    javafx.application.Platform.runLater(() -> {
                        showStatus("Erreur de connexion à la base de données", Color.RED);
                        registerButton.setDisable(false);
                        registerButton.setText("Créer un compte");
                    });
                    return;
                }

                // Vérifier si l'email existe déjà
                if (UserDAO.emailExists(email)) {
                    javafx.application.Platform.runLater(() -> {
                        showStatus("Cette adresse email est déjà utilisée", Color.ORANGE);
                        registerButton.setDisable(false);
                        registerButton.setText("Créer un compte");
                    });
                    return;
                }

                // Vérifier si le nom d'utilisateur existe déjà
                if (UserDAO.usernameExists(username)) {
                    javafx.application.Platform.runLater(() -> {
                        showStatus("Ce nom d'utilisateur est déjà pris", Color.ORANGE);
                        registerButton.setDisable(false);
                        registerButton.setText("Créer un compte");
                    });
                    return;
                }

                // Simuler un délai de traitement
                Thread.sleep(1500);

                // Enregistrer l'utilisateur
                boolean success = UserDAO.registerUser(username, email, password);

                javafx.application.Platform.runLater(() -> {
                    if (success) {
                        showStatus("Compte créé avec succès ! Vous pouvez maintenant vous connecter.", Color.GREEN);
                        registerButton.setText("✓ Compte créé");

                        // Animation de succès
                        animateSuccess();

                        // Rediriger vers la connexion après 2 secondes
                        new Thread(() -> {
                            try {
                                Thread.sleep(2000);
                                javafx.application.Platform.runLater(() -> {
                                    viewManager.showLogin();
                                });
                            } catch (InterruptedException ex) {
                                // Ignorer
                            }
                        }).start();

                    } else {
                        showStatus("Erreur lors de la création du compte", Color.RED);
                        registerButton.setDisable(false);
                        registerButton.setText("Créer un compte");
                    }
                });

            } catch (InterruptedException e) {
                javafx.application.Platform.runLater(() -> {
                    showStatus("Opération interrompue", Color.ORANGE);
                    registerButton.setDisable(false);
                    registerButton.setText("Créer un compte");
                });
            }
        }).start();
    }

    private void showStatus(String message, Color color) {
        statusLabel.setText(message);
        statusLabel.setTextFill(color);

        // Animation du message de statut
        FadeTransition ft = new FadeTransition(Duration.millis(300), statusLabel);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.play();
    }

    private void animateSuccess() {
        // Animation de succès
        FadeTransition ft = new FadeTransition(Duration.millis(500), content);
        ft.setFromValue(1.0);
        ft.setToValue(0.95);
        ft.setAutoReverse(true);
        ft.setCycleCount(2);
        ft.play();
    }

    public BorderPane getContent() {
        return content;
    }
}