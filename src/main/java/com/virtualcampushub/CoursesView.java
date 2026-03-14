package com.virtualcampushub;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class CoursesView {
    private final VBox content;

    public CoursesView() {
        content = new VBox(16);
        content.setPadding(new Insets(20));
        content.getStyleClass().add("view");

        Label header = new Label("Cours");
        header.getStyleClass().add("view-title");

        TextField filterField = new TextField();
        filterField.setPromptText("Filtrer les cours...");
        filterField.getStyleClass().add("search-field");
        filterField.setPrefWidth(320);

        HBox toolbar = new HBox(12, filterField);
        toolbar.setAlignment(Pos.CENTER_LEFT);

        TableView<Course> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        table.getStyleClass().add("course-table");

        TableColumn<Course, String> titleCol = new TableColumn<>("Titre");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<Course, String> instructorCol = new TableColumn<>("Enseignant");
        instructorCol.setCellValueFactory(new PropertyValueFactory<>("instructor"));

        TableColumn<Course, Integer> progressCol = new TableColumn<>("Progression");
        progressCol.setCellValueFactory(new PropertyValueFactory<>("progress"));

        table.getColumns().setAll(titleCol, instructorCol, progressCol);

        ObservableList<Course> data = FXCollections.observableArrayList(
                new Course("Programmation Java", "Mme. Lemoine", 68),
                new Course("Design d'interaction", "Mr. Dupont", 82),
                new Course("Architecture logicielle", "Mme. Martin", 47),
                new Course("Bases de données", "Mr. Rousseau", 92),
                new Course("UX / UI", "Mme. Bernard", 74)
        );

        FilteredList<Course> filtered = new FilteredList<>(data, p -> true);
        filterField.textProperty().addListener((obs, oldV, newV) -> {
            String lower = newV == null ? "" : newV.toLowerCase();
            filtered.setPredicate(course -> course.getTitle().toLowerCase().contains(lower)
                    || course.getInstructor().toLowerCase().contains(lower));
        });

        SortedList<Course> sorted = new SortedList<>(filtered);
        sorted.comparatorProperty().bind(table.comparatorProperty());

        table.setItems(sorted);

        table.setRowFactory(tv -> {
            javafx.scene.control.TableRow<Course> row = new javafx.scene.control.TableRow<>();
            row.setOnMouseEntered(e -> row.getStyleClass().add("course-row-hover"));
            row.setOnMouseExited(e -> row.getStyleClass().remove("course-row-hover"));
            return row;
        });

        content.getChildren().addAll(header, toolbar, table);
    }

    public VBox getContent() {
        return content;
    }

    public static class Course {
        private final String title;
        private final String instructor;
        private final int progress;

        public Course(String title, String instructor, int progress) {
            this.title = title;
            this.instructor = instructor;
            this.progress = progress;
        }

        public String getTitle() {
            return title;
        }

        public String getInstructor() {
            return instructor;
        }

        public int getProgress() {
            return progress;
        }
    }
}
