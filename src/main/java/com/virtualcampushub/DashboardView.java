package com.virtualcampushub;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
                createCard("Cours en cours", "12", "Suivez votre progression"),
                createCard("Prochains événements", "5", "Restez à jour"),
                createCard("Tâches restantes", "3", "Ne ratez rien"));

        content.getChildren().addAll(header, cardPane, createProgressChart());
        animateCards(cardPane);
    }

    public Region getContent() {
        return content;
    }

    private VBox createCard(String title, String value, String subtitle) {
        VBox card = new VBox(8);
        card.getStyleClass().add("dashboard-card");
        card.setPadding(new Insets(14));

        Label t = new Label(title);
        t.getStyleClass().add("card-title");

        Label v = new Label(value);
        v.getStyleClass().add("card-value");

        Label s = new Label(subtitle);
        s.getStyleClass().add("card-subtitle");

        card.getChildren().addAll(t, v, s);
        card.setEffect(new DropShadow(14, Color.rgb(0, 0, 0, 0.12)));

        card.setOnMouseEntered(e -> card.setTranslateY(-4));
        card.setOnMouseExited(e -> card.setTranslateY(0));

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
            Region card = (Region) cardPane.getChildren().get(i);
            card.setOpacity(0);
            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(card.opacityProperty(), 0)),
                    new KeyFrame(Duration.millis(400 + i * 120), new KeyValue(card.opacityProperty(), 1))
            );
            timeline.play();
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

