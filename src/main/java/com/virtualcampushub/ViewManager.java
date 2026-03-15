package com.virtualcampushub;

import javafx.animation.*;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ViewManager {

    private final Stage stage;
    private BorderPane mainLayout;
    private Sidebar sidebar;
    private TopBar topBar;
    private String currentUsername = "Utilisateur";
    private int currentUserId = -1;

    public ViewManager(Stage stage) {
        this.stage = stage;
        stage.setTitle("Virtual Campus Hub");
        stage.setMinWidth(1100);
        stage.setMinHeight(700);

        // Centrer la fenêtre
        Rectangle2D screen = Screen.getPrimary().getVisualBounds();
        stage.setWidth(Math.min(1400, screen.getWidth() * 0.9));
        stage.setHeight(Math.min(850, screen.getHeight() * 0.9));
        stage.centerOnScreen();
    }

    // ─────────────────────────────────────────
    // VUES PUBLIQUES (sans layout principal)
    // ─────────────────────────────────────────

    public void showLogin() {
        LoginView loginView = new LoginView(this);
        Scene scene = new Scene(loginView.getView());
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
        fadeIn(loginView.getView());
    }

    public void showRegister() {
        RegisterView registerView = new RegisterView(this);
        Scene scene = new Scene(registerView.getView());
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        stage.setScene(scene);
        fadeIn(registerView.getView());
    }

    // ─────────────────────────────────────────
    // INITIALISATION LAYOUT PRINCIPAL
    // ─────────────────────────────────────────

    public void initMainLayout() {
        if (mainLayout == null) {
            mainLayout = new BorderPane();
            sidebar = new Sidebar(this);
            topBar = new TopBar(this);
            mainLayout.setLeft(sidebar.getView());
            mainLayout.setTop(topBar.getView());
        }
    }

    // ─────────────────────────────────────────
    // NAVIGATION INTERNE
    // ─────────────────────────────────────────

    public void showDashboard() {
        initMainLayout();
        sidebar.setActive("dashboard");
        topBar.setTitle("Tableau de bord");
        DashboardView view = new DashboardView(this);
        switchCenter(view.getView());
        ensureMainScene();
    }

    public void showCourses() {
        initMainLayout();
        sidebar.setActive("courses");
        topBar.setTitle("Mes Cours");
        CoursesView view = new CoursesView(this);
        switchCenter(view.getView());
        ensureMainScene();
    }

    public void showEvents() {
        initMainLayout();
        sidebar.setActive("events");
        topBar.setTitle("Événements");
        EventsView view = new EventsView(this);
        switchCenter(view.getView());
        ensureMainScene();
    }

    public void showProfile() {
        initMainLayout();
        sidebar.setActive("profile");
        topBar.setTitle("Mon Profil");
        ProfileView view = new ProfileView(this);
        switchCenter(view.getView());
        ensureMainScene();
    }

    public void showChat() {
        initMainLayout();
        sidebar.setActive("chat");
        topBar.setTitle("Messagerie");
        ChatView view = new ChatView(this);
        switchCenter(view.getView());
        ensureMainScene();
    }

    // ─────────────────────────────────────────
    // HELPERS
    // ─────────────────────────────────────────

    private void ensureMainScene() {
        if (stage.getScene() == null || stage.getScene().getRoot() != mainLayout) {
            Scene scene = new Scene(mainLayout);
            scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
            stage.setScene(scene);
        }
    }

    private void switchCenter(Node newContent) {
        if (mainLayout.getCenter() != null) {
            Node old = mainLayout.getCenter();
            FadeTransition fadeOut = new FadeTransition(Duration.millis(150), old);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.setOnFinished(e -> {
                mainLayout.setCenter(newContent);
                fadeIn(newContent);
            });
            fadeOut.play();
        } else {
            mainLayout.setCenter(newContent);
            fadeIn(newContent);
        }
    }

    private void fadeIn(Node node) {
        FadeTransition ft = new FadeTransition(Duration.millis(300), node);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.play();
    }

    // ─────────────────────────────────────────
    // GETTERS / SETTERS
    // ─────────────────────────────────────────

    public String getCurrentUsername() { return currentUsername; }
    public void setCurrentUsername(String username) { this.currentUsername = username; }

    public int getCurrentUserId() { return currentUserId; }
    public void setCurrentUserId(int id) { this.currentUserId = id; }

    public Stage getStage() { return stage; }
}
