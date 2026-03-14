package com.virtualcampushub;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Initialiser la DB en arrière-plan pour ne pas bloquer le démarrage
        Thread dbThread = new Thread(() -> {
            DatabaseConnection.initializeDatabase();
        });
        dbThread.setDaemon(true);
        dbThread.start();

        // Lancer l'interface
        ViewManager viewManager = new ViewManager(primaryStage);
        viewManager.showLogin();
    }

    @Override
    public void stop() {
        DatabaseConnection.closeConnection();
        Platform.exit();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
