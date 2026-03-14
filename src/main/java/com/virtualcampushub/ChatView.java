package com.virtualcampushub;

import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
    private final BorderPane content;
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

    public ChatView(String username) {
        this.currentUsername = username;
        content = new BorderPane();
        content.getStyleClass().add("chat-view");
        content.setPadding(new Insets(20));

        executor = Executors.newCachedThreadPool();
        messages = FXCollections.observableArrayList();

        setupUI();
        startChatServer();
    }

    private void setupUI() {
        // Header
        VBox headerBox = new VBox(4);
        headerBox.getStyleClass().add("view-header");
        headerBox.setPadding(new Insets(0, 0, 10, 0));

        Label header = new Label("Messagerie Instantanée - " + currentUsername);
        header.getStyleClass().add("view-title");
        header.setFont(Font.font("Inter", FontWeight.BOLD, 20));

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

        content.setTop(headerBox);
        content.setCenter(messageListView);
        content.setBottom(inputBar);

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
        TranslateTransition tt = new TranslateTransition(Duration.millis(200));
        tt.setByY(-5);
        tt.setAutoReverse(true);
        tt.setCycleCount(2);
        tt.play();
    }

    private void startChatServer() {
        executor.submit(() -> {
            try {
                serverSocket = new ServerSocket(8889); // Different port from login
                System.out.println("Serveur de chat démarré sur le port 8889");

                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        clientSocket = serverSocket.accept();
                        System.out.println("Nouvelle connexion chat: " + clientSocket.getInetAddress());
                        handleChatConnection(clientSocket);
                    } catch (IOException e) {
                        if (!Thread.currentThread().isInterrupted()) {
                            System.out.println("Erreur d'acceptation: " + e.getMessage());
                        }
                    }
                }
            } catch (IOException e) {
                System.err.println("Erreur démarrage serveur chat: " + e.getMessage());
            }
        });
    }

    private void handleChatConnection(Socket socket) {
        executor.submit(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {

                String message;
                while ((message = reader.readLine()) != null) {
                    final String receivedMessage = message;
                    javafx.application.Platform.runLater(() -> {
                        addMessage("Utilisateur distant", receivedMessage, false);
                    });
                }

            } catch (IOException e) {
                System.err.println("Erreur connexion chat: " + e.getMessage());
            }
        });
    }

    private void broadcastMessage(String message) {
        // In a real implementation, this would send to all connected clients
        System.out.println("Broadcasting: " + message);
    }

    public BorderPane getContent() {
        return content;
    }

    public void shutdown() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            if (clientSocket != null && !clientSocket.isClosed()) {
                clientSocket.close();
            }
        } catch (IOException e) {
            System.err.println("Erreur fermeture chat: " + e.getMessage());
        }
        executor.shutdown();
    }

    private static class ChatMessage {
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

        public String getSender() {
            return sender;
        }

        public String getContent() {
            return content;
        }

        public boolean isOwnMessage() {
            return isOwnMessage;
        }

        public String getTimestamp() {
            return timestamp;
        }
    }
}
