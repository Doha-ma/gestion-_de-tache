package com.virtualcampushub;

import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

public class ChatView {
    private final BorderPane content;

    public ChatView() {
        content = new BorderPane();
        content.getStyleClass().add("view");
        content.setPadding(new Insets(20));

        VBox headerBox = new VBox(4);
        headerBox.getStyleClass().add("view-header");
        headerBox.setPadding(new Insets(0, 0, 10, 0));

        javafx.scene.control.Label header = new javafx.scene.control.Label("Messagerie interne");
        header.getStyleClass().add("view-title");
        headerBox.getChildren().add(header);

        ObservableList<ChatMessage> messages = FXCollections.observableArrayList(
                new ChatMessage("Bonjour ! Prêt pour la séance de projet ?", false),
                new ChatMessage("Oui, j'ai préparé quelques idées.", true)
        );

        ListView<ChatMessage> listView = new ListView<>(messages);
        listView.getStyleClass().add("chat-list");
        listView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(ChatMessage item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    HBox row = new HBox();
                    row.setPadding(new Insets(6));
                    row.setMaxWidth(Double.MAX_VALUE);

                    javafx.scene.control.Label label = new javafx.scene.control.Label(item.getText());
                    label.setWrapText(true);
                    label.setTextAlignment(TextAlignment.LEFT);
                    label.getStyleClass().add(item.isMine() ? "chat-bubble-me" : "chat-bubble-them");
                    label.setMaxWidth(380);

                    if (item.isMine()) {
                        row.setAlignment(Pos.CENTER_RIGHT);
                    } else {
                        row.setAlignment(Pos.CENTER_LEFT);
                    }

                    row.getChildren().add(label);
                    setGraphic(row);
                }
            }
        });

        TextField input = new TextField();
        input.setPromptText("Écrire un message...");
        input.getStyleClass().add("chat-input");
        input.setPrefWidth(500);

        Button send = new Button("Envoyer");
        send.getStyleClass().add("chat-send");

        send.setOnAction(e -> {
            if (!input.getText().isBlank()) {
                ChatMessage message = new ChatMessage(input.getText(), true);
                messages.add(message);
                input.clear();
                listView.scrollTo(messages.size() - 1);
                animateBubble();
            }
        });

        HBox inputBar = new HBox(10, input, send);
        inputBar.setAlignment(Pos.CENTER_LEFT);
        inputBar.setPadding(new Insets(12, 0, 0, 0));

        content.setTop(headerBox);
        content.setCenter(listView);
        content.setBottom(inputBar);
    }

    public BorderPane getContent() {
        return content;
    }

    private void animateBubble() {
        TranslateTransition tt = new TranslateTransition(Duration.millis(140));
        tt.setByY(-4);
        tt.setAutoReverse(true);
        tt.setCycleCount(2);
        tt.play();
    }

    private static class ChatMessage {
        private final String text;
        private final boolean mine;

        public ChatMessage(String text, boolean mine) {
            this.text = text;
            this.mine = mine;
        }

        public String getText() {
            return text;
        }

        public boolean isMine() {
            return mine;
        }
    }
}
