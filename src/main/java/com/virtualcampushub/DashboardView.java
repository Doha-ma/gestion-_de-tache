package com.virtualcampushub;

import javafx.animation.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class DashboardView {
    private final VBox content;

    public DashboardView() {
        content = new VBox(20);
        content.setPadding(new Insets(20));
        content.getStyleClass().add("view");

        Label header = new Label("Tableau de bord");
        header.getStyleClass().add("view-title");

        FlowPane cardPane = new FlowPane();
        cardPane.setHgap(18);
        cardPane.setVgap(18);
        cardPane.getStyleClass().add("card-pane");

        cardPane.getChildren().addAll(
                createCard("Cours en cours", "12", "Suivez votre progression", "card-blue"),
                createCard("Prochains événements", "5", "Restez à jour", "card-purple"),
                createCard("Tâches restantes", "3", "Ne ratez rien", "card-green"));

        content.getChildren().addAll(header, cardPane, createProgressChart());
        animateCards(cardPane);
    }

    public Region getContent() {
        return content;
    }

    private VBox createCard(String title, String value, String subtitle, String styleClass) {
        VBox card = new VBox(10);
        card.getStyleClass().addAll("dashboard-card", styleClass);
        card.setPadding(new Insets(18));

        Label t = new Label(title);
        t.getStyleClass().add("card-title");

        Label v = new Label(value);
        v.getStyleClass().add("card-value");

        Label s = new Label(subtitle);
        s.getStyleClass().add("card-subtitle");

        card.getChildren().addAll(t, v, s);

        // Améliorer les effets hover avec des animations fluides
        card.setOnMouseEntered(e -> {
            ScaleTransition scaleUp = new ScaleTransition(Duration.millis(200), card);
            scaleUp.setToX(1.03);
            scaleUp.setToY(1.03);

            TranslateTransition liftUp = new TranslateTransition(Duration.millis(200), card);
            liftUp.setToY(-6);

            ParallelTransition hoverIn = new ParallelTransition(scaleUp, liftUp);
            hoverIn.play();
        });

        card.setOnMouseExited(e -> {
            ScaleTransition scaleDown = new ScaleTransition(Duration.millis(200), card);
            scaleDown.setToX(1.0);
            scaleDown.setToY(1.0);

            TranslateTransition liftDown = new TranslateTransition(Duration.millis(200), card);
            liftDown.setToY(0);

            ParallelTransition hoverOut = new ParallelTransition(scaleDown, liftDown);
            hoverOut.play();
        });

        // Animation de clic
        card.setOnMousePressed(e -> {
            ScaleTransition press = new ScaleTransition(Duration.millis(100), card);
            press.setToX(0.97);
            press.setToY(0.97);
            press.play();
        });

        card.setOnMouseReleased(e -> {
            ScaleTransition release = new ScaleTransition(Duration.millis(100), card);
            release.setToX(1.03);
            release.setToY(1.03);
            release.play();
        });

        return card;
    }

    private LineChart<String, Number> createProgressChart() {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Semaines");
        yAxis.setLabel("Progression (%)");

        LineChart<String, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.getStyleClass().add("dashboard-chart");
        chart.setLegendVisible(false);
        chart.setAnimated(false);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.getData().add(new XYChart.Data<>("S1", 20));
        series.getData().add(new XYChart.Data<>("S2", 40));
        series.getData().add(new XYChart.Data<>("S3", 55));
        series.getData().add(new XYChart.Data<>("S4", 75));
        series.getData().add(new XYChart.Data<>("S5", 86));
        chart.getData().add(series);

        animateChart(series);

        return chart;
    }

    private void animateCards(FlowPane cardPane) {
        for (int i = 0; i < cardPane.getChildren().size(); i++) {
            Node card = cardPane.getChildren().get(i);

            // Position initiale
            card.setOpacity(0);
            card.setScaleX(0.8);
            card.setScaleY(0.8);
            card.setTranslateY(20);

            // Animation d'entrée avec délai échelonné
            Timeline cardAnimation = new Timeline();
            KeyFrame kf1 = new KeyFrame(Duration.millis(200 + i * 100),
                new KeyValue(card.opacityProperty(), 0.0),
                new KeyValue(card.scaleXProperty(), 0.8),
                new KeyValue(card.scaleYProperty(), 0.8),
                new KeyValue(card.translateYProperty(), 20)
            );
            KeyFrame kf2 = new KeyFrame(Duration.millis(500 + i * 100),
                new KeyValue(card.opacityProperty(), 1.0, Interpolator.EASE_OUT),
                new KeyValue(card.scaleXProperty(), 1.0, Interpolator.EASE_OUT),
                new KeyValue(card.scaleYProperty(), 1.0, Interpolator.EASE_OUT),
                new KeyValue(card.translateYProperty(), 0, Interpolator.EASE_OUT)
            );

            cardAnimation.getKeyFrames().addAll(kf1, kf2);
            cardAnimation.play();
        }
    }

    private void animateChart(XYChart.Series<String, Number> series) {
        Timeline timeline = new Timeline();
        for (int i = 0; i < series.getData().size(); i++) {
            XYChart.Data<String, Number> point = series.getData().get(i);
            Number target = point.getYValue();
            point.setYValue(0);
            timeline.getKeyFrames().add(new KeyFrame(Duration.millis(400 + i * 120), new KeyValue(point.YValueProperty(), target)));
        }
        timeline.play();
    }
}

