package com.virtualcampushub;

import javafx.animation.*;
import javafx.beans.property.*;
import javafx.collections.*;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.util.Duration;

public class CoursesView {

    private final ViewManager viewManager;
    private ScrollPane root;

    public static class Course {
        private final StringProperty title;
        private final StringProperty teacher;
        private final IntegerProperty progress;
        private final StringProperty color;

        public Course(String title, String teacher, int progress, String color) {
            this.title = new SimpleStringProperty(title);
            this.teacher = new SimpleStringProperty(teacher);
            this.progress = new SimpleIntegerProperty(progress);
            this.color = new SimpleStringProperty(color);
        }

        public String getTitle() { return title.get(); }
        public StringProperty titleProperty() { return title; }
        public String getTeacher() { return teacher.get(); }
        public StringProperty teacherProperty() { return teacher; }
        public int getProgress() { return progress.get(); }
        public IntegerProperty progressProperty() { return progress; }
        public String getColor() { return color.get(); }
        public StringProperty colorProperty() { return color; }
    }

    private final ObservableList<Course> allCourses = FXCollections.observableArrayList(
            new Course("Algorithmique & Structures de données", "Prof. Benali", 78, "#4facfe"),
            new Course("Développement Web Full-Stack", "Prof. Martin", 55, "#43e97b"),
            new Course("Intelligence Artificielle", "Prof. Dupont", 91, "#6C63FF"),
            new Course("Bases de données avancées", "Prof. Rousseau", 40, "#f093fb"),
            new Course("Réseaux et Protocoles", "Prof. Alami", 65, "#f7971e"),
            new Course("Mathématiques Appliquées", "Prof. Leblanc", 30, "#ff6b6b")
    );

    public CoursesView(ViewManager viewManager) {
        this.viewManager = viewManager;
        buildUI();
    }

    private void buildUI() {
        VBox content = new VBox(24);
        content.setPadding(new Insets(32));
        content.setStyle("-fx-background-color: #0f0e1a;");

        // En-tête
        HBox header = new HBox(16);
        header.setAlignment(Pos.CENTER_LEFT);

        VBox titleBox = new VBox(4);
        Label title = new Label("Mes Cours");
        title.setStyle("-fx-font-size: 24; -fx-font-weight: bold; -fx-text-fill: white; -fx-font-family: 'Segoe UI';");
        Label subtitle = new Label(allCourses.size() + " cours inscrits");
        subtitle.setStyle("-fx-font-size: 13; -fx-text-fill: rgba(255,255,255,0.5); -fx-font-family: 'Segoe UI';");
        titleBox.getChildren().addAll(title, subtitle);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Filtre / Recherche
        TextField searchField = new TextField();
        searchField.setPromptText("🔍  Rechercher un cours...");
        searchField.setPrefWidth(240);
        searchField.setPrefHeight(38);
        searchField.setStyle("""
            -fx-background-color: #1e1d2e;
            -fx-text-fill: white;
            -fx-prompt-text-fill: rgba(255,255,255,0.35);
            -fx-background-radius: 12;
            -fx-border-color: rgba(255,255,255,0.12);
            -fx-border-radius: 12;
            -fx-border-width: 1;
            -fx-padding: 0 12 0 12;
            -fx-font-size: 12;
            """);

        Button addBtn = createPrimaryButton("+ Ajouter un cours");

        header.getChildren().addAll(titleBox, spacer, searchField, addBtn);

        // Cartes de cours
        FlowPane coursesGrid = new FlowPane();
        coursesGrid.setHgap(16);
        coursesGrid.setVgap(16);

        FilteredList<Course> filtered = new FilteredList<>(allCourses, c -> true);
        searchField.textProperty().addListener((obs, old, val) -> {
            filtered.setPredicate(c ->
                    val == null || val.isEmpty() ||
                            c.getTitle().toLowerCase().contains(val.toLowerCase()) ||
                            c.getTeacher().toLowerCase().contains(val.toLowerCase())
            );
            refreshCourseCards(coursesGrid, filtered);
        });

        refreshCourseCards(coursesGrid, filtered);

        content.getChildren().addAll(header, coursesGrid);

        root = new ScrollPane(content);
        root.setFitToWidth(true);
        root.setStyle("-fx-background-color: #0f0e1a; -fx-background: #0f0e1a;");
        root.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    }

    private void refreshCourseCards(FlowPane grid, FilteredList<Course> courses) {
        grid.getChildren().clear();
        for (int i = 0; i < courses.size(); i++) {
            Course course = courses.get(i);
            VBox card = buildCourseCard(course);
            card.setOpacity(0);
            card.setTranslateY(15);
            grid.getChildren().add(card);

            final int idx = i;
            PauseTransition delay = new PauseTransition(Duration.millis(idx * 60));
            delay.setOnFinished(e -> {
                ParallelTransition pt = new ParallelTransition(
                        fadeIn(card), slideUp(card)
                );
                pt.play();
            });
            delay.play();
        }
    }

    private VBox buildCourseCard(Course course) {
        VBox card = new VBox(14);
        card.setPrefWidth(310);
        card.setPadding(new Insets(20));
        card.setStyle("""
            -fx-background-color: #1e1d2e;
            -fx-background-radius: 16;
            -fx-border-color: rgba(255,255,255,0.07);
            -fx-border-radius: 16;
            -fx-border-width: 1;
            -fx-cursor: hand;
            """);

        // Bande colorée en haut
        Rectangle topBar = new Rectangle(270, 4);
        topBar.setArcWidth(4);
        topBar.setArcHeight(4);
        topBar.setFill(Color.web(course.getColor()));

        // Icône + titre
        HBox titleRow = new HBox(10);
        titleRow.setAlignment(Pos.CENTER_LEFT);

        StackPane iconBox = new StackPane();
        Rectangle iconBg = new Rectangle(40, 40);
        iconBg.setArcWidth(10);
        iconBg.setArcHeight(10);
        iconBg.setFill(Color.web(course.getColor(), 0.15));

        Label courseIcon = new Label("📚");
        courseIcon.setStyle("-fx-font-size: 18;");
        iconBox.getChildren().addAll(iconBg, courseIcon);

        VBox titleInfo = new VBox(3);
        Label titleLabel = new Label(course.getTitle());
        titleLabel.setStyle("-fx-font-size: 13; -fx-font-weight: bold; -fx-text-fill: white; -fx-font-family: 'Segoe UI';");
        titleLabel.setWrapText(true);
        titleLabel.setMaxWidth(200);

        Label teacherLabel = new Label("👨‍🏫 " + course.getTeacher());
        teacherLabel.setStyle("-fx-font-size: 11; -fx-text-fill: rgba(255,255,255,0.5); -fx-font-family: 'Segoe UI';");
        titleInfo.getChildren().addAll(titleLabel, teacherLabel);
        titleRow.getChildren().addAll(iconBox, titleInfo);

        // Progression
        VBox progressBox = new VBox(6);
        HBox progressHeader = new HBox();
        progressHeader.setAlignment(Pos.CENTER_LEFT);
        Label progressLabel = new Label("Progression");
        progressLabel.setStyle("-fx-font-size: 11; -fx-text-fill: rgba(255,255,255,0.45); -fx-font-family: 'Segoe UI';");
        Region progSpacer = new Region();
        HBox.setHgrow(progSpacer, Priority.ALWAYS);
        Label progressValue = new Label(course.getProgress() + "%");
        progressValue.setStyle("-fx-font-size: 12; -fx-font-weight: bold; -fx-text-fill: " + course.getColor() + "; -fx-font-family: 'Segoe UI';");
        progressHeader.getChildren().addAll(progressLabel, progSpacer, progressValue);

        StackPane progressBar = new StackPane();
        progressBar.setPrefHeight(6);

        Rectangle bgBar = new Rectangle(Double.MAX_VALUE, 6);
        bgBar.setFill(Color.web("#ffffff", 0.08));
        bgBar.setArcWidth(6);
        bgBar.setArcHeight(6);
        bgBar.widthProperty().bind(progressBar.widthProperty());

        Rectangle fgBar = new Rectangle(0, 6);
        fgBar.setFill(Color.web(course.getColor()));
        fgBar.setArcWidth(6);
        fgBar.setArcHeight(6);
        progressBar.getChildren().addAll(bgBar, fgBar);
        StackPane.setAlignment(fgBar, Pos.CENTER_LEFT);

        progressBox.getChildren().addAll(progressHeader, progressBar);

        // Animer la barre
        progressBar.widthProperty().addListener((obs, old, w) -> {
            if (w.doubleValue() > 0) {
                Timeline tl = new Timeline(
                        new KeyFrame(Duration.millis(800),
                                new KeyValue(fgBar.widthProperty(),
                                        w.doubleValue() * course.getProgress() / 100.0,
                                        Interpolator.EASE_OUT))
                );
                tl.play();
            }
        });

        // Bouton Accéder
        Button accessBtn = new Button("Accéder au cours →");
        accessBtn.setMaxWidth(Double.MAX_VALUE);
        accessBtn.setPrefHeight(36);
        accessBtn.setStyle(String.format("""
            -fx-background-color: %s22;
            -fx-text-fill: %s;
            -fx-background-radius: 10;
            -fx-border-color: %s44;
            -fx-border-radius: 10;
            -fx-border-width: 1;
            -fx-font-size: 12;
            -fx-cursor: hand;
            """, course.getColor(), course.getColor(), course.getColor()));

        card.getChildren().addAll(topBar, titleRow, progressBox, accessBtn);

        // Hover
        card.setOnMouseEntered(e -> {
            card.setStyle("""
                -fx-background-color: #252436;
                -fx-background-radius: 16;
                -fx-border-color: rgba(255,255,255,0.15);
                -fx-border-radius: 16;
                -fx-border-width: 1;
                -fx-cursor: hand;
                -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 20, 0, 0, 8);
                """);
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

        return card;
    }

    private Button createPrimaryButton(String text) {
        Button btn = new Button(text);
        btn.setPrefHeight(38);
        btn.setStyle("""
            -fx-background-color: #6C63FF;
            -fx-text-fill: white;
            -fx-font-size: 12;
            -fx-font-weight: bold;
            -fx-background-radius: 12;
            -fx-cursor: hand;
            -fx-padding: 0 16 0 16;
            """);
        return btn;
    }

    private FadeTransition fadeIn(javafx.scene.Node node) {
        FadeTransition ft = new FadeTransition(Duration.millis(350), node);
        ft.setToValue(1.0);
        return ft;
    }

    private TranslateTransition slideUp(javafx.scene.Node node) {
        TranslateTransition tt = new TranslateTransition(Duration.millis(350), node);
        tt.setToY(0);
        tt.setInterpolator(Interpolator.EASE_OUT);
        return tt;
    }

    public ScrollPane getView() { return root; }
}
