package com.virtualcampushub;

import javafx.animation.*;
import javafx.geometry.*;
import javafx.scene.canvas.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.text.*;
import javafx.util.Duration;

public class DashboardView {

    private final ViewManager viewManager;
    private ScrollPane root;

    private static final String[][] METRICS = {
            {"📖", "Cours actifs", "6", "#4facfe", "#00f2fe"},
            {"🗓", "Événements", "3", "#43e97b", "#38f9d7"},
            {"✅", "Tâches", "12", "#f093fb", "#f5576c"},
            {"💬", "Messages", "7", "#4481eb", "#04befe"},
            {"🏆", "Note moy.", "15.4/20", "#f7971e", "#ffd200"},
            {"⏱", "Heures/sem.", "24h", "#667eea", "#764ba2"}
    };

    public DashboardView(ViewManager viewManager) {
        this.viewManager = viewManager;
        buildUI();
    }

    private void buildUI() {
        VBox content = new VBox(28);
        content.setPadding(new Insets(32, 32, 32, 32));
        content.setStyle("-fx-background-color: #0f0e1a;");

        // En-tête
        VBox header = new VBox(6);
        Label greeting = new Label("Bonjour, " + viewManager.getCurrentUsername() + " 👋");
        greeting.setStyle("-fx-font-size: 26; -fx-font-weight: bold; -fx-text-fill: white; -fx-font-family: 'Segoe UI';");
        Label subGreeting = new Label("Voici un résumé de votre activité académique");
        subGreeting.setStyle("-fx-font-size: 14; -fx-text-fill: rgba(255,255,255,0.5); -fx-font-family: 'Segoe UI';");
        header.getChildren().addAll(greeting, subGreeting);

        // Grille de métriques
        Label metricsTitle = new Label("Vue d'ensemble");
        metricsTitle.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-text-fill: rgba(255,255,255,0.8); -fx-font-family: 'Segoe UI';");

        GridPane grid = new GridPane();
        grid.setHgap(16);
        grid.setVgap(16);

        for (int i = 0; i < METRICS.length; i++) {
            VBox card = buildMetricCard(METRICS[i]);
            int col = i % 3;
            int row = i / 3;
            grid.add(card, col, row);
            GridPane.setHgrow(card, Priority.ALWAYS);

            // Animation d'entrée échelonnée
            card.setOpacity(0);
            card.setTranslateY(20);
            PauseTransition delay = new PauseTransition(Duration.millis(i * 80));
            delay.setOnFinished(e -> {
                ParallelTransition pt = new ParallelTransition(
                        fadeIn(card), slideUp(card)
                );
                pt.play();
            });
            delay.play();
        }

        // Colonnes de grille équilibrées
        for (int i = 0; i < 3; i++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setPercentWidth(33.33);
            grid.getColumnConstraints().add(cc);
        }

        // Graphique
        Label chartTitle = new Label("Progression hebdomadaire");
        chartTitle.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-text-fill: rgba(255,255,255,0.8); -fx-font-family: 'Segoe UI';");

        VBox chartCard = buildProgressChart();

        // Activités récentes
        Label activityTitle = new Label("Activités récentes");
        activityTitle.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-text-fill: rgba(255,255,255,0.8); -fx-font-family: 'Segoe UI';");

        VBox activityList = buildActivityList();

        content.getChildren().addAll(header, metricsTitle, grid, chartTitle, chartCard, activityTitle, activityList);

        root = new ScrollPane(content);
        root.setFitToWidth(true);
        root.setStyle("-fx-background-color: #0f0e1a; -fx-background: #0f0e1a;");
        root.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    }

    private VBox buildMetricCard(String[] data) {
        // data: [icon, label, value, colorStart, colorEnd]
        VBox card = new VBox(10);
        card.setPadding(new Insets(20, 20, 20, 20));
        card.setStyle(String.format("""
            -fx-background-color: #1e1d2e;
            -fx-background-radius: 16;
            -fx-border-color: rgba(255,255,255,0.07);
            -fx-border-radius: 16;
            -fx-border-width: 1;
            -fx-cursor: hand;
            """));

        HBox topRow = new HBox();
        topRow.setAlignment(Pos.CENTER_LEFT);

        // Icône avec fond coloré
        StackPane iconBox = new StackPane();
        Rectangle iconBg = new Rectangle(44, 44);
        iconBg.setArcWidth(12);
        iconBg.setArcHeight(12);
        iconBg.setFill(Color.web(data[3], 0.2));

        Label icon = new Label(data[0]);
        icon.setStyle("-fx-font-size: 20;");
        iconBox.getChildren().addAll(iconBg, icon);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label trendLabel = new Label("▲ +5%");
        trendLabel.setStyle("-fx-font-size: 11; -fx-text-fill: #43E97B; -fx-font-family: 'Segoe UI';");

        topRow.getChildren().addAll(iconBox, spacer, trendLabel);

        Label value = new Label(data[2]);
        value.setStyle(String.format("""
            -fx-font-size: 28;
            -fx-font-weight: bold;
            -fx-text-fill: %s;
            -fx-font-family: 'Segoe UI';
            """, data[3]));

        Label label = new Label(data[1]);
        label.setStyle("-fx-font-size: 12; -fx-text-fill: rgba(255,255,255,0.5); -fx-font-family: 'Segoe UI';");

        // Mini barre de progression
        StackPane progressBar = new StackPane();
        progressBar.setMaxHeight(3);
        progressBar.setPrefHeight(3);

        Rectangle bgBar = new Rectangle(Double.MAX_VALUE, 3);
        bgBar.setFill(Color.web("#ffffff", 0.08));
        bgBar.setArcWidth(3);
        bgBar.setArcHeight(3);

        Rectangle fgBar = new Rectangle(0, 3);
        fgBar.setFill(Color.web(data[3]));
        fgBar.setArcWidth(3);
        fgBar.setArcHeight(3);
        progressBar.getChildren().addAll(bgBar, fgBar);
        StackPane.setAlignment(fgBar, Pos.CENTER_LEFT);

        card.getChildren().addAll(topRow, value, label, progressBar);

        // Hover
        card.setOnMouseEntered(e -> {
            card.setStyle(String.format("""
                -fx-background-color: #252436;
                -fx-background-radius: 16;
                -fx-border-color: %s;
                -fx-border-radius: 16;
                -fx-border-width: 1;
                -fx-cursor: hand;
                -fx-effect: dropshadow(gaussian, %s, 20, 0, 0, 4);
                """, data[3] + "55", data[3] + "33"));
            ScaleTransition st = new ScaleTransition(Duration.millis(150), card);
            st.setToX(1.02);
            st.setToY(1.02);
            st.play();
        });
        card.setOnMouseExited(e -> {
            card.setStyle("""
                -fx-background-color: #1e1d2e;
                -fx-background-radius: 16;
                -fx-border-color: rgba(255,255,255,0.07);
                -fx-border-radius: 16;
                -fx-border-width: 1;
                -fx-cursor: hand;
                """);
            ScaleTransition st = new ScaleTransition(Duration.millis(150), card);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();
        });

        // Animer la barre de progression
        PauseTransition delay = new PauseTransition(Duration.millis(400));
        delay.setOnFinished(ev -> {
            double targetWidth = 200 * (0.4 + Math.random() * 0.5);
            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.millis(800),
                            new KeyValue(fgBar.widthProperty(), targetWidth, Interpolator.EASE_OUT))
            );
            timeline.play();
        });
        delay.play();

        return card;
    }

    private VBox buildProgressChart() {
        VBox card = new VBox(16);
        card.setPadding(new Insets(24));
        card.setStyle("""
            -fx-background-color: #1e1d2e;
            -fx-background-radius: 16;
            -fx-border-color: rgba(255,255,255,0.07);
            -fx-border-radius: 16;
            -fx-border-width: 1;
            """);

        Canvas canvas = new Canvas(0, 180);

        String[] weeks = {"Sem 1", "Sem 2", "Sem 3", "Sem 4", "Sem 5"};
        double[] values = {45, 58, 52, 73, 68};

        // Canvas responsive via listener
        card.widthProperty().addListener((obs, old, width) -> {
            canvas.setWidth(width.doubleValue() - 48);
            drawChart(canvas, weeks, values);
        });

        // Légende
        HBox legend = new HBox(20);
        legend.setAlignment(Pos.CENTER_RIGHT);
        Label l1 = new Label("● Progression (%)");
        l1.setStyle("-fx-text-fill: #6C63FF; -fx-font-size: 12; -fx-font-family: 'Segoe UI';");
        legend.getChildren().add(l1);

        card.getChildren().addAll(canvas, legend);
        return card;
    }

    private void drawChart(Canvas canvas, String[] labels, double[] values) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        double w = canvas.getWidth();
        double h = canvas.getHeight();
        double padLeft = 40, padRight = 20, padTop = 20, padBottom = 30;
        double chartW = w - padLeft - padRight;
        double chartH = h - padTop - padBottom;

        // Grille horizontale
        gc.setStroke(Color.web("#ffffff", 0.05));
        gc.setLineWidth(1);
        for (int i = 0; i <= 4; i++) {
            double y = padTop + (chartH / 4) * i;
            gc.strokeLine(padLeft, y, w - padRight, y);
            gc.setFill(Color.web("#ffffff", 0.3));
            gc.setFont(javafx.scene.text.Font.font("Segoe UI", 10));
            gc.fillText(String.valueOf(100 - i * 25), 2, y + 4);
        }

        // Points
        double[] px = new double[values.length];
        double[] py = new double[values.length];
        for (int i = 0; i < values.length; i++) {
            px[i] = padLeft + (chartW / (values.length - 1)) * i;
            py[i] = padTop + chartH - (values[i] / 100.0 * chartH);
        }

        // Zone remplie sous la courbe
        gc.beginPath();
        gc.moveTo(px[0], py[0]);
        for (int i = 1; i < values.length; i++) {
            double cpx = (px[i - 1] + px[i]) / 2;
            gc.bezierCurveTo(cpx, py[i - 1], cpx, py[i], px[i], py[i]);
        }
        gc.lineTo(px[values.length - 1], padTop + chartH);
        gc.lineTo(px[0], padTop + chartH);
        gc.closePath();

        LinearGradient fillGrad = new LinearGradient(0, padTop, 0, padTop + chartH, false,
                CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#6C63FF", 0.3)),
                new Stop(1, Color.web("#6C63FF", 0.0)));
        gc.setFill(fillGrad);
        gc.fill();

        // Ligne
        gc.setStroke(Color.web("#6C63FF"));
        gc.setLineWidth(2.5);
        gc.beginPath();
        gc.moveTo(px[0], py[0]);
        for (int i = 1; i < values.length; i++) {
            double cpx = (px[i - 1] + px[i]) / 2;
            gc.bezierCurveTo(cpx, py[i - 1], cpx, py[i], px[i], py[i]);
        }
        gc.stroke();

        // Points et labels
        for (int i = 0; i < values.length; i++) {
            gc.setFill(Color.web("#6C63FF"));
            gc.fillOval(px[i] - 5, py[i] - 5, 10, 10);
            gc.setFill(Color.WHITE);
            gc.fillOval(px[i] - 3, py[i] - 3, 6, 6);

            gc.setFill(Color.web("#ffffff", 0.7));
            gc.setFont(javafx.scene.text.Font.font("Segoe UI", 11));
            gc.fillText(labels[i], px[i] - 16, padTop + chartH + 18);

            // Valeur au-dessus du point
            gc.setFill(Color.web("#ffffff", 0.9));
            gc.fillText((int) values[i] + "%", px[i] - 10, py[i] - 10);
        }
    }

    private VBox buildActivityList() {
        VBox card = new VBox(0);
        card.setStyle("""
            -fx-background-color: #1e1d2e;
            -fx-background-radius: 16;
            -fx-border-color: rgba(255,255,255,0.07);
            -fx-border-radius: 16;
            -fx-border-width: 1;
            """);

        String[][] activities = {
                {"📚", "Cours 'Algorithmes' terminé", "Il y a 2h", "#43E97B"},
                {"✅", "Devoir de Math soumis", "Il y a 5h", "#4facfe"},
                {"💬", "Nouveau message de Prof. Martin", "Hier 18:30", "#f093fb"},
                {"🗓", "Rappel : Examen de Physique demain", "Hier 10:00", "#f7971e"},
        };

        for (int i = 0; i < activities.length; i++) {
            String[] act = activities[i];
            HBox item = new HBox(14);
            item.setPadding(new Insets(14, 20, 14, 20));
            item.setAlignment(Pos.CENTER_LEFT);
            if (i < activities.length - 1) {
                item.setStyle("-fx-border-color: rgba(255,255,255,0.05); -fx-border-width: 0 0 1 0;");
            }

            Circle dot = new Circle(8);
            dot.setFill(Color.web(act[3], 0.25));
            dot.setStroke(Color.web(act[3]));
            dot.setStrokeWidth(2);

            Label iconL = new Label(act[0]);
            iconL.setStyle("-fx-font-size: 16;");

            VBox textBox = new VBox(2);
            Label actLabel = new Label(act[1]);
            actLabel.setStyle("-fx-font-size: 13; -fx-text-fill: rgba(255,255,255,0.85); -fx-font-family: 'Segoe UI';");
            Label timeLabel = new Label(act[2]);
            timeLabel.setStyle("-fx-font-size: 11; -fx-text-fill: rgba(255,255,255,0.4); -fx-font-family: 'Segoe UI';");
            textBox.getChildren().addAll(actLabel, timeLabel);
            HBox.setHgrow(textBox, Priority.ALWAYS);

            item.getChildren().addAll(dot, iconL, textBox);
            card.getChildren().add(item);
        }

        return card;
    }

    private FadeTransition fadeIn(javafx.scene.Node node) {
        FadeTransition ft = new FadeTransition(Duration.millis(400), node);
        ft.setToValue(1.0);
        return ft;
    }

    private TranslateTransition slideUp(javafx.scene.Node node) {
        TranslateTransition tt = new TranslateTransition(Duration.millis(400), node);
        tt.setToY(0);
        tt.setInterpolator(Interpolator.EASE_OUT);
        return tt;
    }

    public ScrollPane getView() { return root; }
}
