package com.virtualcampushub;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
    public static final String APP_TITLE = "Virtual Campus Hub";

    @Override
    public void start(Stage stage) {
        ViewManager viewManager = new ViewManager();
        Scene scene = new Scene(viewManager.getRoot(), 1200, 800);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        stage.setTitle(APP_TITLE);
        stage.setScene(scene);
        stage.setMinWidth(1000);
        stage.setMinHeight(700);
        stage.show();

        viewManager.showDashboard();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
