package com.virtualcampushub;

import javafx.animation.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatView {
    private final ViewManager viewManager;
    private ScrollPane root;
    private ListView<ChatMessage> messageListView;
    private TextField messageInput;
    private Button sendButton;
    private final ObservableList<ChatMessage> messages;
    private final ExecutorService executor;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private boolean isConnected = false;
    private String currentUsername;

    public ChatView(ViewManager viewManager) {
        this.viewManager = viewManager;
        this.currentUsername = viewManager.getCurrentUsername();
        root = new ScrollPane();
        root.setFitToWidth(true);
        root.setStyle("-fx-background-color: #0f0e1a; -fx-background: #0f0e1a;");
        root.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        executor = Executors.newCachedThreadPool();
        messages = FXCollections.observableArrayList();

        setupUI();
        startChatServer();
    }

    private void setupUI() {
        VBox content = new VBox(16);
        content.setPadding(new Insets(20));
        content.getStyleClass().add("view");
        content.setStyle("-fx-background-color: #0f0e1a;");

        // Header
        VBox headerBox = new VBox(4);
        headerBox.getStyleClass().add("view-header");
        headerBox.setPadding(new Insets(0, 0, 10, 0));

        Label header = new Label("Messagerie Instantanée - " + currentUsername);
        header.getStyleClass().add("view-title");
        header.setFont(Font.font("Inter", FontWeight.BOLD, 20));
        header.setTextFill(Color.WHITE);

        Label statusLabel = new Label("Connecté au réseau local");
        statusLabel.getStyleClass().add("chat-status");
        statusLabel.setFont(Font.font("Inter", FontWeight.NORMAL, 12));
        statusLabel.setTextFill(Color.GREEN);

        headerBox.getChildren().addAll(header, statusLabel);

        // Messages area
        messageListView = new ListView<>(messages);
        messageListView.getStyleClass().add("chat-messages");
        messageListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(ChatMessage item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    HBox row = new HBox();
                    row.setPadding(new Insets(8));
                    row.setMaxWidth(Double.MAX_VALUE);

                    Label messageLabel = new Label(item.getContent());
                    messageLabel.setWrapText(true);
                    messageLabel.setTextAlignment(TextAlignment.LEFT);
                    messageLabel.getStyleClass().add(item.isOwnMessage() ? "chat-bubble-own" : "chat-bubble-other");
                    messageLabel.setMaxWidth(350);

                    Label timestampLabel = new Label(item.getTimestamp());
                    timestampLabel.getStyleClass().add("chat-timestamp");
                    timestampLabel.setFont(Font.font("Inter", FontWeight.NORMAL, 10));

                    VBox messageBox = new VBox(2);
                    messageBox.getChildren().addAll(messageLabel, timestampLabel);

                    if (item.isOwnMessage()) {
                        row.setAlignment(Pos.CENTER_RIGHT);
                        messageBox.setAlignment(Pos.CENTER_RIGHT);
                    } else {
                        row.setAlignment(Pos.CENTER_LEFT);
                        messageBox.setAlignment(Pos.CENTER_LEFT);
                    }

                    row.getChildren().add(messageBox);
                    setGraphic(row);
                }
            }
        });

        // Input area
        messageInput = new TextField();
        messageInput.setPromptText("Tapez votre message...");
        messageInput.getStyleClass().add("chat-input");
        messageInput.setPrefWidth(400);

        sendButton = new Button("Envoyer");
        sendButton.getStyleClass().add("chat-send");
        sendButton.setDisable(true);

        // Enable send button only when there's text
        messageInput.textProperty().addListener((obs, oldText, newText) -> {
            sendButton.setDisable(newText.trim().isEmpty());
        });

        sendButton.setOnAction(e -> sendMessage());
        messageInput.setOnAction(e -> sendMessage());

        HBox inputBar = new HBox(10, messageInput, sendButton);
        inputBar.setAlignment(Pos.CENTER_LEFT);
        inputBar.setPadding(new Insets(12, 0, 0, 0));

        content.getChildren().addAll(header, messageListView, inputBar);

        root.setContent(content);

        // Add welcome message
        addMessage("Système", "Bienvenue dans le chat, " + currentUsername + "!", false);
        addMessage("Système", "Vous êtes connecté au réseau local. Vous pouvez communiquer avec d'autres utilisateurs.", false);
    }

    private void sendMessage() {
        String message = messageInput.getText().trim();
        if (!message.isEmpty()) {
            addMessage(currentUsername, message, true);
            messageInput.clear();

            // Broadcast to connected clients (in a real implementation)
            broadcastMessage(currentUsername + ": " + message);
        }
    }

    private void addMessage(String sender, String content, boolean isOwn) {
        ChatMessage message = new ChatMessage(sender, content, isOwn);
        messages.add(message);
        messageListView.scrollTo(messages.size() - 1);

        // Animate new message
        animateMessage();
    }

    private void animateMessage() {
        // Get the last added message cell
        int lastIndex = messages.size() - 1;
        messageListView.scrollTo(lastIndex);

        // Find the cell and animate it
        messageListView.layout();
        messageListView.getChildrenUnmodifiable().forEach(node -> {
            if (node instanceof javafx.scene.control.ListCell) {
                javafx.scene.control.ListCell<?> cell = (javafx.scene.control.ListCell<?>) node;
                if (cell.getIndex() == lastIndex) {
                    // Animation d'apparition sophistiquée
                    cell.setOpacity(0);
                    cell.setScaleX(0.8);
                    cell.setScaleY(0.8);
                    cell.setTranslateY(20);

                    FadeTransition fadeIn = new FadeTransition(Duration.millis(400), cell);
                    fadeIn.setFromValue(0.0);
                    fadeIn.setToValue(1.0);

                    ScaleTransition scaleIn = new ScaleTransition(Duration.millis(400), cell);
                    scaleIn.setFromX(0.8);
                    scaleIn.setFromY(0.8);
                    scaleIn.setToX(1.0);
                    scaleIn.setToY(1.0);

                    TranslateTransition slideIn = new TranslateTransition(Duration.millis(400), cell);
                    slideIn.setFromY(20);
                    slideIn.setToY(0);

                    ParallelTransition messageAnimation = new ParallelTransition(fadeIn, scaleIn, slideIn);
                    messageAnimation.setInterpolator(Interpolator.EASE_OUT);
                    messageAnimation.play();
                }
            }
        });
    }

    private void startChatServer() {
        executor.submit(() -> {
            try {
                // Start server socket for receiving connections
                serverSocket = new ServerSocket(8888);
                System.out.println("Serveur de chat démarré sur le port 8888");

                while (isConnected) {
                    try {
                        clientSocket = serverSocket.accept();
                        System.out.println("Nouvelle connexion acceptée: " + clientSocket.getInetAddress());

                        // Handle the connection in a separate thread
                        handleClientConnection(clientSocket);
                    } catch (IOException e) {
                        if (isConnected) {
                            System.out.println("Erreur lors de l'acceptation d'une connexion: " + e.getMessage());
                        }
                    }
                }
            } catch (IOException e) {
                System.err.println("Erreur démarrage serveur chat: " + e.getMessage());
            }
        });
    }

    private void handleClientConnection(Socket socket) {
        executor.submit(() -> {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                String receivedMessage;
                while ((receivedMessage = in.readLine()) != null) {
                    final String msg = receivedMessage;
                    javafx.application.Platform.runLater(() -> {
                        addMessage("Utilisateur distant", msg, false);
                    });
                }
            } catch (IOException e) {
                System.err.println("Erreur connexion client: " + e.getMessage());
            }
        });
    }

    private void broadcastMessage(String message) {
        if (out != null) {
            out.println(message);
        }
    }

    public ScrollPane getView() {
        return root;
    }

    public void shutdown() {
        isConnected = false;
        try {
            if (serverSocket != null) serverSocket.close();
            if (clientSocket != null) clientSocket.close();
            if (out != null) out.close();
            if (in != null) in.close();
        } catch (IOException e) {
            System.err.println("Erreur fermeture chat: " + e.getMessage());
        }
        executor.shutdown();
    }

    public static class ChatMessage {
        private final String sender;
        private final String content;
        private final boolean isOwnMessage;
        private final String timestamp;

        public ChatMessage(String sender, String content, boolean isOwnMessage) {
            this.sender = sender;
            this.content = content;
            this.isOwnMessage = isOwnMessage;
            this.timestamp = java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm"));
        }

        public String getSender() { return sender; }
        public String getContent() { return content; }
        public boolean isOwnMessage() { return isOwnMessage; }
        public String getTimestamp() { return timestamp; }
    }
}
