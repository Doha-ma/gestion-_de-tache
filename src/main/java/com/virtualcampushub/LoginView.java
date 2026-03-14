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

import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginView {
    private final BorderPane content;
    private final TextField emailField;
    private final PasswordField passwordField;
    private final Button loginButton;
    private final Button registerButton;
    private final Label statusLabel;
    private final Label networkStatusLabel;
    private final ExecutorService executor;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private boolean isLoggedIn = false;
    private String currentUsername = "";
    private final ViewManager viewManager;

    public LoginView(ViewManager viewManager) {
        this.viewManager = viewManager;
        content = new BorderPane();
        content.getStyleClass().add("login-view");
        executor = Executors.newCachedThreadPool();

        // Header
        VBox headerBox = new VBox(10);
        headerBox.setAlignment(Pos.CENTER);
        headerBox.setPadding(new Insets(40, 20, 20, 20));

        Label titleLabel = new Label("Virtual Campus Hub");
        titleLabel.getStyleClass().add("login-title");
        titleLabel.setFont(Font.font("Inter", FontWeight.BOLD, 32));

        Label subtitleLabel = new Label("Connexion à votre espace étudiant");
        subtitleLabel.getStyleClass().add("login-subtitle");
        subtitleLabel.setFont(Font.font("Inter", FontWeight.NORMAL, 16));

        headerBox.getChildren().addAll(titleLabel, subtitleLabel);

        // Login Form
        VBox formBox = new VBox(20);
        formBox.setAlignment(Pos.CENTER);
        formBox.setPadding(new Insets(40, 60, 40, 60));
        formBox.setMaxWidth(400);
        formBox.getStyleClass().add("login-form");

        // Email field
        VBox emailBox = new VBox(8);
        emailBox.setAlignment(Pos.CENTER_LEFT);

        Label emailLabel = new Label("Adresse email");
        emailLabel.getStyleClass().add("login-label");
        emailLabel.setFont(Font.font("Inter", FontWeight.MEDIUM, 14));

        emailField = new TextField();
        emailField.setPromptText("votre.email@exemple.com");
        emailField.getStyleClass().add("login-input");
        emailField.setPrefWidth(300);

        emailBox.getChildren().addAll(emailLabel, emailField);

        // Password field
        VBox passwordBox = new VBox(8);
        passwordBox.setAlignment(Pos.CENTER_LEFT);

        Label passwordLabel = new Label("Mot de passe");
        passwordLabel.getStyleClass().add("login-label");
        passwordLabel.setFont(Font.font("Inter", FontWeight.MEDIUM, 14));

        passwordField = new PasswordField();
        passwordField.setPromptText("Entrez votre mot de passe");
        passwordField.getStyleClass().add("login-input");
        passwordField.setPrefWidth(300);

        passwordBox.getChildren().addAll(passwordLabel, passwordField);

        // Login button
        loginButton = new Button("Se connecter");
        loginButton.getStyleClass().add("login-button");
        loginButton.setPrefWidth(300);
        loginButton.setPrefHeight(45);

        // Register button
        registerButton = new Button("Créer un compte");
        registerButton.getStyleClass().add("register-link-button");
        registerButton.setPrefWidth(300);
        registerButton.setPrefHeight(35);

        // Status labels
        statusLabel = new Label("");
        statusLabel.getStyleClass().add("login-status");
        statusLabel.setFont(Font.font("Inter", FontWeight.NORMAL, 12));

        networkStatusLabel = new Label("Vérification du réseau...");
        networkStatusLabel.getStyleClass().add("network-status");
        networkStatusLabel.setFont(Font.font("Inter", FontWeight.NORMAL, 12));

        formBox.getChildren().addAll(emailBox, passwordBox, loginButton, registerButton, statusLabel, networkStatusLabel);

        // Center container
        VBox centerContainer = new VBox();
        centerContainer.setAlignment(Pos.CENTER);
        centerContainer.getChildren().add(formBox);

        content.setTop(headerBox);
        content.setCenter(centerContainer);

        // Event handlers
        setupEventHandlers();

        // Start network detection
        startNetworkDetection();
    }

    private void setupEventHandlers() {
        loginButton.setOnAction(e -> attemptLogin());
        registerButton.setOnAction(e -> viewManager.showRegister());

        // Enter key support
        passwordField.setOnAction(e -> attemptLogin());

        // Real-time validation
        emailField.textProperty().addListener((obs, oldText, newText) -> {
            updateLoginButtonState();
        });

        passwordField.textProperty().addListener((obs, oldText, newText) -> {
            updateLoginButtonState();
        });
    }

    private void updateLoginButtonState() {
        boolean hasEmail = !emailField.getText().trim().isEmpty() && isValidEmail(emailField.getText().trim());
        boolean hasPassword = !passwordField.getText().isEmpty();
        loginButton.setDisable(!(hasEmail && hasPassword));
    }

    private boolean isValidEmail(String email) {
        return email.contains("@") && email.contains(".") && email.length() >= 5;
    }

    private void attemptLogin() {
        String email = emailField.getText().trim();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showStatus("Veuillez remplir tous les champs", Color.ORANGE);
            return;
        }

        if (!isValidEmail(email)) {
            showStatus("Veuillez entrer une adresse email valide", Color.ORANGE);
            return;
        }

        // Désactiver le bouton pendant le traitement
        loginButton.setDisable(true);
        loginButton.setText("Connexion en cours...");
        showStatus("Connexion en cours...", Color.BLUE);

        // Traitement en arrière-plan
        executor.submit(() -> {
            try {
                // Vérifier la connexion à la base de données
                if (!DatabaseConnection.testConnection()) {
                    javafx.application.Platform.runLater(() -> {
                        showStatus("Erreur de connexion à la base de données", Color.RED);
                        loginButton.setDisable(false);
                        loginButton.setText("Se connecter");
                    });
                    return;
                }

                // Simuler un délai de traitement
                Thread.sleep(1000);

                // Tenter la connexion
                UserDAO.User user = UserDAO.loginUser(email, password);

                javafx.application.Platform.runLater(() -> {
                    if (user != null) {
                        currentUsername = user.getUsername();
                        showStatus("Connexion réussie ! Bienvenue " + currentUsername, Color.GREEN);
                        loginButton.setText("Connecté ✓");

                        // Animation de succès
                        animateSuccess();

                        // Rediriger vers la messagerie après connexion réussie
                        new Thread(() -> {
                            try {
                                Thread.sleep(1200);
                                javafx.application.Platform.runLater(() -> viewManager.showChat(currentUsername));
                            } catch (InterruptedException ex) {
                                // Ignorer
                            }
                        }).start();

                    } else {
                        showStatus("Email ou mot de passe incorrect", Color.RED);
                        loginButton.setDisable(false);
                        loginButton.setText("Se connecter");
                    }
                });

            } catch (InterruptedException e) {
                javafx.application.Platform.runLater(() -> {
                    showStatus("Opération interrompue", Color.ORANGE);
                    loginButton.setDisable(false);
                    loginButton.setText("Se connecter");
                });
            }
        });
    }

    private void showStatus(String message, Color color) {
        statusLabel.setText(message);
        statusLabel.setTextFill(color);

        // Animate status change
        FadeTransition ft = new FadeTransition(Duration.millis(300), statusLabel);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.play();
    }

    private void animateSuccess() {
        // Success animation
        FadeTransition ft = new FadeTransition(Duration.millis(500), content);
        ft.setFromValue(1.0);
        ft.setToValue(0.9);
        ft.setAutoReverse(true);
        ft.setCycleCount(2);
        ft.play();
    }

    private void startNetworkDetection() {
        executor.submit(() -> {
            try {
                // Get local IP address
                InetAddress localHost = InetAddress.getLocalHost();
                String localIP = localHost.getHostAddress();

                // Check if we're on a local network (192.168.x.x or 10.x.x.x)
                boolean isLocalNetwork = localIP.startsWith("192.168.") || localIP.startsWith("10.");

                javafx.application.Platform.runLater(() -> {
                    if (isLocalNetwork) {
                        networkStatusLabel.setText("✓ Connecté au réseau local (" + localIP + ")");
                        networkStatusLabel.setTextFill(Color.GREEN);
                    } else {
                        networkStatusLabel.setText("⚠ Réseau détecté: " + localIP);
                        networkStatusLabel.setTextFill(Color.ORANGE);
                    }
                });

            } catch (UnknownHostException e) {
                javafx.application.Platform.runLater(() -> {
                    networkStatusLabel.setText("✗ Impossible de détecter le réseau");
                    networkStatusLabel.setTextFill(Color.RED);
                });
            }
        });
    }

    private void startNetworkCommunication() {
        executor.submit(() -> {
            try {
                // Start server socket for receiving connections
                serverSocket = new ServerSocket(8888);
                System.out.println("Serveur de chat démarré sur le port 8888");

                while (isLoggedIn) {
                    try {
                        clientSocket = serverSocket.accept();
                        System.out.println("Nouvelle connexion acceptée: " + clientSocket.getInetAddress());

                        // Handle the connection in a separate thread
                        handleClientConnection(clientSocket);
                    } catch (IOException e) {
                        if (isLoggedIn) {
                            System.out.println("Erreur lors de l'acceptation d'une connexion: " + e.getMessage());
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println("Erreur lors du démarrage du serveur: " + e.getMessage());
            }
        });
    }

    private void handleClientConnection(Socket socket) {
        executor.submit(() -> {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

                // Send welcome message
                out.println("Bienvenue sur Virtual Campus Hub! Connecté en tant que: " + currentUsername);

                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println("Message reçu: " + message);

                    // Echo back the message (in real app, broadcast to all connected clients)
                    out.println("Echo: " + message);
                }

            } catch (IOException e) {
                System.out.println("Erreur de communication avec le client: " + e.getMessage());
            }
        });
    }

    public BorderPane getContent() {
        return content;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public String getCurrentUsername() {
        return currentUsername;
    }

    public void logout() {
        isLoggedIn = false;
        currentUsername = "";

        // Close sockets
        try {
            if (clientSocket != null && !clientSocket.isClosed()) {
                clientSocket.close();
            }
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            System.out.println("Erreur lors de la fermeture des sockets: " + e.getMessage());
        }

        // Reset UI
        emailField.clear();
        passwordField.clear();
        loginButton.setDisable(true);
        loginButton.setText("Se connecter");
        showStatus("", Color.BLACK);
    }

    public void shutdown() {
        logout();
        executor.shutdown();
    }
}
