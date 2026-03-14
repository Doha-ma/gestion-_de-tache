package com.virtualcampushub;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.util.Optional;

public class EventsView {
    private final VBox content;

    public EventsView() {
        content = new VBox(18);
        content.setPadding(new Insets(20));
        content.getStyleClass().add("view");

        Label header = new Label("Événements");
        header.getStyleClass().add("view-title");

        Label subtitle = new Label("Glissez un événement sur une date pour le reprogrammer.");
        subtitle.getStyleClass().add("view-subtitle");

        HBox calendarAndList = new HBox(18);
        calendarAndList.setAlignment(Pos.TOP_LEFT);

        GridPane calendarGrid = createCalendarGrid();
        calendarGrid.getStyleClass().add("calendar-grid");

        ListView<EventItem> eventList = createEventList(calendarGrid);
        eventList.setPrefWidth(320);
        eventList.getStyleClass().add("event-list");

        calendarAndList.getChildren().addAll(calendarGrid, eventList);
        content.getChildren().addAll(header, subtitle, calendarAndList);
    }

    public VBox getContent() {
        return content;
    }

    private GridPane createCalendarGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(8);
        grid.setVgap(8);
        grid.setPadding(new Insets(10));

        for (int col = 0; col < 7; col++) {
            grid.getColumnConstraints().add(new javafx.scene.layout.ColumnConstraints(100));
        }

        for (int row = 0; row < 6; row++) {
            grid.getRowConstraints().add(new javafx.scene.layout.RowConstraints(80));
        }

        LocalDate start = LocalDate.now().withDayOfMonth(1);
        int length = start.lengthOfMonth();
        int dayOfWeek = start.getDayOfWeek().getValue();

        int dayIndex = 1;
        for (int r = 0; r < 6; r++) {
            for (int c = 0; c < 7; c++) {
                VBox cell = new VBox();
                cell.getStyleClass().add("calendar-cell");
                cell.setPadding(new Insets(6));

                if (r == 0 && c < dayOfWeek - 1) {
                    // empty
                } else if (dayIndex <= length) {
                    Label dayLabel = new Label(String.valueOf(dayIndex));
                    dayLabel.getStyleClass().add("calendar-day");
                    cell.getChildren().add(dayLabel);

                    final int currentDayIndex = dayIndex;
                    cell.setOnDragOver(this::onDragOver);
                    cell.setOnDragDropped(e -> onDropped(e, currentDayIndex));
                    dayIndex++;
                }

                grid.add(cell, c, r);
            }
        }

        return grid;
    }

    private ListView<EventItem> createEventList(GridPane calendarGrid) {
        ObservableList<EventItem> items = FXCollections.observableArrayList(
                new EventItem("Hackathon UX", LocalDate.now().plusDays(2)),
                new EventItem("Conférence IA", LocalDate.now().plusDays(5)),
                new EventItem("Session Networking", LocalDate.now().plusDays(9))
        );

        ListView<EventItem> listView = new ListView<>(items);

        listView.setCellFactory(lv -> {
            ListCell<EventItem> cell = new ListCell<>() {
                @Override
                protected void updateItem(EventItem item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        HBox box = new HBox(12);
                        Label title = new Label(item.getTitle());
                        title.getStyleClass().add("event-title");
                        Label when = new Label(item.getDate().toString());
                        when.getStyleClass().add("event-date");
                        box.getChildren().addAll(title, when);
                        setGraphic(box);
                    }
                }
            };

            cell.setOnDragDetected(e -> {
                if (cell.getItem() == null) {
                    return;
                }
                Dragboard dragboard = cell.startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();
                content.putString(cell.getItem().getTitle());
                dragboard.setContent(content);
                e.consume();
            });

            cell.setOnMouseClicked(e -> {
                if (!cell.isEmpty()) {
                    showSignupPopup(cell.getItem());
                }
            });

            return cell;
        });

        return listView;
    }

    private void onDragOver(DragEvent event) {
        if (event.getGestureSource() != event.getGestureTarget() && event.getDragboard().hasString()) {
            event.acceptTransferModes(TransferMode.MOVE);
        }
        event.consume();
    }

    private void onDropped(DragEvent event, int day) {
        Dragboard db = event.getDragboard();
        if (db.hasString()) {
            showReschedulePopup(db.getString(), day);
            event.setDropCompleted(true);
        } else {
            event.setDropCompleted(false);
        }
        event.consume();
    }

    private void showSignupPopup(EventItem item) {
        TextInputDialog dialog = new TextInputDialog("Oui");
        dialog.setTitle("Inscription à l'événement");
        dialog.setHeaderText(item.getTitle());
        dialog.setContentText("Souhaitez-vous vous inscrire ?");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(answer -> {
            // Simple acknowledgement
        });
    }

    private void showReschedulePopup(String title, int day) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Replanifier l'événement");
        dialog.setHeaderText(title);
        dialog.setContentText("Nouvelle date (jour du mois) : " + day);
        dialog.showAndWait();
    }

    private static class EventItem {
        private final String title;
        private final LocalDate date;

        public EventItem(String title, LocalDate date) {
            this.title = title;
            this.date = date;
        }

        public String getTitle() {
            return title;
        }

        public LocalDate getDate() {
            return date;
        }
    }
}
