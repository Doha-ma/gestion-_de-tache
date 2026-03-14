package com.virtualcampushub;

import javafx.animation.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.util.Duration;

import java.time.*;
import java.time.format.TextStyle;
import java.util.*;

public class EventsView {

    private final ViewManager viewManager;
    private ScrollPane root;

    private record Event(String title, String desc, String date, String time, String color) {}

    private final List<Event> events = new ArrayList<>(List.of(
            new Event("Examen de Physique", "Examen final du semestre", "2026-03-20", "09:00", "#FF6B6B"),
            new Event("Soutenance de Projet", "Présentation du projet JavaFX", "2026-03-25", "14:00", "#6C63FF"),
            new Event("Atelier IA", "Workshop Intelligence Artificielle", "2026-04-02", "10:30", "#43E97B"),
            new Event("Remise des diplômes", "Cérémonie de fin d'année", "2026-06-15", "16:00", "#f7971e")
    ));

    public EventsView(ViewManager viewManager) {
        this.viewManager = viewManager;
        buildUI();
    }

    private void buildUI() {
        HBox mainContent = new HBox(24);
        mainContent.setPadding(new Insets(32));
        mainContent.setStyle("-fx-background-color: #0f0e1a;");

        // Calendrier (gauche)
        VBox calendarSection = buildCalendarSection();
        HBox.setHgrow(calendarSection, Priority.ALWAYS);

        // Liste événements (droite)
        VBox eventsSection = buildEventsSection();
        eventsSection.setMinWidth(360);
        eventsSection.setMaxWidth(360);

        mainContent.getChildren().addAll(calendarSection, eventsSection);

        root = new ScrollPane(mainContent);
        root.setFitToWidth(true);
        root.setStyle("-fx-background-color: #0f0e1a; -fx-background: #0f0e1a;");
    }

    private VBox buildCalendarSection() {
        VBox section = new VBox(20);

        Label title = new Label("Calendrier");
        title.setStyle("-fx-font-size: 22; -fx-font-weight: bold; -fx-text-fill: white; -fx-font-family: 'Segoe UI';");

        // Navigation mois
        YearMonth current = YearMonth.now();
        HBox nav = new HBox(12);
        nav.setAlignment(Pos.CENTER_LEFT);

        Label monthLabel = new Label(
                current.getMonth().getDisplayName(TextStyle.FULL, Locale.FRENCH).substring(0, 1).toUpperCase()
                        + current.getMonth().getDisplayName(TextStyle.FULL, Locale.FRENCH).substring(1)
                        + " " + current.getYear()
        );
        monthLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-text-fill: white; -fx-font-family: 'Segoe UI';");

        Region spc = new Region();
        HBox.setHgrow(spc, Priority.ALWAYS);

        Button prevBtn = navBtn("‹");
        Button nextBtn = navBtn("›");

        nav.getChildren().addAll(monthLabel, spc, prevBtn, nextBtn);

        // Grille calendrier
        GridPane calGrid = buildCalendarGrid(current);

        VBox card = new VBox(16);
        card.setPadding(new Insets(24));
        card.setStyle("""
            -fx-background-color: #1e1d2e;
            -fx-background-radius: 20;
            -fx-border-color: rgba(255,255,255,0.07);
            -fx-border-radius: 20;
            -fx-border-width: 1;
            """);
        card.getChildren().addAll(nav, calGrid);
        section.getChildren().addAll(title, card);
        return section;
    }

    private GridPane buildCalendarGrid(YearMonth ym) {
        GridPane grid = new GridPane();
        grid.setHgap(4);
        grid.setVgap(4);

        String[] dayNames = {"Lun", "Mar", "Mer", "Jeu", "Ven", "Sam", "Dim"};
        for (int i = 0; i < 7; i++) {
            Label h = new Label(dayNames[i]);
            h.setMinWidth(42);
            h.setAlignment(Pos.CENTER);
            h.setStyle("-fx-font-size: 11; -fx-text-fill: rgba(255,255,255,0.4); -fx-font-family: 'Segoe UI'; -fx-font-weight: bold;");
            grid.add(h, i, 0);
        }

        LocalDate first = ym.atDay(1);
        int startCol = first.getDayOfWeek().getValue() - 1; // 0=Mon
        int daysInMonth = ym.lengthOfMonth();
        LocalDate today = LocalDate.now();

        // Jours avec événements
        Set<Integer> eventDays = new HashSet<>();
        for (Event e : events) {
            try {
                LocalDate d = LocalDate.parse(e.date());
                if (d.getYear() == ym.getYear() && d.getMonthValue() == ym.getMonthValue()) {
                    eventDays.add(d.getDayOfMonth());
                }
            } catch (Exception ignored) {}
        }

        int col = startCol, row = 1;
        for (int day = 1; day <= daysInMonth; day++) {
            StackPane cell = new StackPane();
            cell.setMinSize(42, 42);
            cell.setMaxSize(42, 42);

            boolean isToday = (day == today.getDayOfMonth() && ym.getYear() == today.getYear()
                    && ym.getMonthValue() == today.getMonthValue());
            boolean hasEvent = eventDays.contains(day);

            Circle bg = new Circle(18);
            if (isToday) {
                bg.setFill(Color.web("#6C63FF"));
            } else if (hasEvent) {
                bg.setFill(Color.web("#6C63FF", 0.15));
                bg.setStroke(Color.web("#6C63FF", 0.5));
                bg.setStrokeWidth(1.5);
            } else {
                bg.setFill(Color.TRANSPARENT);
            }

            Label dayLabel = new Label(String.valueOf(day));
            dayLabel.setStyle("-fx-font-size: 13; -fx-font-family: 'Segoe UI'; -fx-text-fill: "
                    + (isToday ? "white" : hasEvent ? "#a89cff" : "rgba(255,255,255,0.7)") + ";"
                    + (isToday || hasEvent ? " -fx-font-weight: bold;" : ""));

            // Point indicateur
            if (hasEvent && !isToday) {
                Circle dot = new Circle(3, Color.web("#6C63FF"));
                StackPane.setAlignment(dot, Pos.BOTTOM_CENTER);
                StackPane.setMargin(dot, new Insets(0, 0, 2, 0));
                cell.getChildren().addAll(bg, dayLabel, dot);
            } else {
                cell.getChildren().addAll(bg, dayLabel);
            }

            cell.setOnMouseEntered(e -> {
                if (!isToday) bg.setFill(Color.web("#6C63FF", 0.1));
                cell.setStyle("-fx-cursor: hand;");
            });
            cell.setOnMouseExited(e -> {
                if (!isToday && !hasEvent) bg.setFill(Color.TRANSPARENT);
                else if (!isToday && hasEvent) bg.setFill(Color.web("#6C63FF", 0.15));
            });

            grid.add(cell, col, row);
            col++;
            if (col == 7) { col = 0; row++; }
        }

        return grid;
    }

    private VBox buildEventsSection() {
        VBox section = new VBox(16);

        HBox header = new HBox(12);
        header.setAlignment(Pos.CENTER_LEFT);
        Label title = new Label("Événements");
        title.setStyle("-fx-font-size: 22; -fx-font-weight: bold; -fx-text-fill: white; -fx-font-family: 'Segoe UI';");

        Region spc = new Region();
        HBox.setHgrow(spc, Priority.ALWAYS);

        Button addBtn = new Button("+ Ajouter");
        addBtn.setStyle("""
            -fx-background-color: #6C63FF;
            -fx-text-fill: white;
            -fx-font-size: 12;
            -fx-font-weight: bold;
            -fx-background-radius: 10;
            -fx-cursor: hand;
            -fx-padding: 8 14 8 14;
            """);
        addBtn.setOnAction(e -> showAddEventDialog(section));

        header.getChildren().addAll(title, spc, addBtn);

        VBox eventsList = new VBox(12);
        for (int i = 0; i < events.size(); i++) {
            VBox card = buildEventCard(events.get(i), eventsList, i);
            card.setOpacity(0);
            card.setTranslateX(20);
            eventsList.getChildren().add(card);

            final int idx = i;
            PauseTransition delay = new PauseTransition(Duration.millis(idx * 80));
            delay.setOnFinished(ev -> {
                FadeTransition fade = new FadeTransition(Duration.millis(300), card);
                fade.setToValue(1.0);
                TranslateTransition translate = new TranslateTransition(Duration.millis(300), card);
                translate.setToX(0);
                translate.setInterpolator(Interpolator.EASE_OUT);
                ParallelTransition pt = new ParallelTransition(fade, translate);
                pt.play();
            });
            delay.play();
        }

        section.getChildren().addAll(header, eventsList);
        return section;
    }

    private VBox buildEventCard(Event event, VBox list, int idx) {
        VBox card = new VBox(8);
        card.setPadding(new Insets(16));
        card.setStyle("""
            -fx-background-color: #1e1d2e;
            -fx-background-radius: 14;
            -fx-border-color: rgba(255,255,255,0.07);
            -fx-border-radius: 14;
            -fx-border-width: 1;
            """);

        // Barre couleur gauche simulée
        HBox inner = new HBox(12);
        inner.setAlignment(Pos.TOP_LEFT);

        Rectangle colorBar = new Rectangle(4, 50);
        colorBar.setArcWidth(4);
        colorBar.setArcHeight(4);
        colorBar.setFill(Color.web(event.color()));

        VBox info = new VBox(5);
        HBox.setHgrow(info, Priority.ALWAYS);

        Label titleLabel = new Label(event.title());
        titleLabel.setStyle("-fx-font-size: 13; -fx-font-weight: bold; -fx-text-fill: white; -fx-font-family: 'Segoe UI';");

        Label descLabel = new Label(event.desc());
        descLabel.setStyle("-fx-font-size: 11; -fx-text-fill: rgba(255,255,255,0.5); -fx-font-family: 'Segoe UI';");

        HBox meta = new HBox(10);
        meta.setAlignment(Pos.CENTER_LEFT);
        Label dateLabel = new Label("📅 " + event.date());
        dateLabel.setStyle("-fx-font-size: 11; -fx-text-fill: " + event.color() + "; -fx-font-family: 'Segoe UI';");
        Label timeLabel = new Label("🕐 " + event.time());
        timeLabel.setStyle("-fx-font-size: 11; -fx-text-fill: rgba(255,255,255,0.4); -fx-font-family: 'Segoe UI';");
        meta.getChildren().addAll(dateLabel, timeLabel);

        info.getChildren().addAll(titleLabel, descLabel, meta);

        Button delBtn = new Button("✕");
        delBtn.setStyle("""
            -fx-background-color: rgba(255,107,107,0.15);
            -fx-text-fill: #FF6B6B;
            -fx-background-radius: 8;
            -fx-cursor: hand;
            -fx-font-size: 11;
            -fx-padding: 4 8 4 8;
            """);
        delBtn.setOnAction(e -> {
            FadeTransition ft = new FadeTransition(Duration.millis(200), card);
            ft.setToValue(0);
            ft.setOnFinished(ev -> list.getChildren().remove(card));
            ft.play();
            events.remove(idx);
        });

        inner.getChildren().addAll(colorBar, info, delBtn);
        card.getChildren().add(inner);
        return card;
    }

    private void showAddEventDialog(VBox section) {
        Dialog<String[]> dialog = new Dialog<>();
        dialog.setTitle("Nouvel événement");
        dialog.setHeaderText(null);
        dialog.getDialogPane().setStyle("-fx-background-color: #1e1d2e;");

        ButtonType addType = new ButtonType("Ajouter", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addType, ButtonType.CANCEL);

        VBox form = new VBox(12);
        form.setPadding(new Insets(20));

        TextField titleField = dialogField("Titre de l'événement");
        TextField descField = dialogField("Description");
        TextField dateField = dialogField("Date (AAAA-MM-JJ)");
        TextField timeField = dialogField("Heure (HH:MM)");

        form.getChildren().addAll(
                styledLabel("Titre"), titleField,
                styledLabel("Description"), descField,
                styledLabel("Date"), dateField,
                styledLabel("Heure"), timeField
        );

        dialog.getDialogPane().setContent(form);
        dialog.setResultConverter(btn -> {
            if (btn == addType) {
                return new String[]{titleField.getText(), descField.getText(),
                        dateField.getText(), timeField.getText()};
            }
            return null;
        });

        dialog.showAndWait().ifPresent(result -> {
            if (result[0] != null && !result[0].isEmpty()) {
                String[] colors = {"#4facfe", "#43e97b", "#6C63FF", "#f093fb", "#f7971e"};
                String color = colors[(int) (Math.random() * colors.length)];
                events.add(new Event(result[0], result[1], result[2], result[3], color));
                // Rebuild
                VBox evList = (VBox) ((VBox) section.getChildren().get(1));
                evList.getChildren().clear();
                for (int i = 0; i < events.size(); i++) {
                    evList.getChildren().add(buildEventCard(events.get(i), evList, i));
                }
            }
        });
    }

    private TextField dialogField(String prompt) {
        TextField f = new TextField();
        f.setPromptText(prompt);
        f.setStyle("""
            -fx-background-color: rgba(255,255,255,0.07);
            -fx-text-fill: white;
            -fx-prompt-text-fill: rgba(255,255,255,0.3);
            -fx-background-radius: 8;
            -fx-border-color: rgba(255,255,255,0.12);
            -fx-border-radius: 8;
            -fx-border-width: 1;
            -fx-padding: 8 12 8 12;
            """);
        return f;
    }

    private Label styledLabel(String text) {
        Label l = new Label(text);
        l.setStyle("-fx-text-fill: rgba(255,255,255,0.6); -fx-font-size: 12; -fx-font-family: 'Segoe UI';");
        return l;
    }

    private Button navBtn(String text) {
        Button btn = new Button(text);
        btn.setPrefSize(32, 32);
        btn.setStyle("""
            -fx-background-color: rgba(255,255,255,0.07);
            -fx-text-fill: white;
            -fx-background-radius: 8;
            -fx-font-size: 16;
            -fx-cursor: hand;
            """);
        return btn;
    }

    public ScrollPane getView() { return root; }
}
