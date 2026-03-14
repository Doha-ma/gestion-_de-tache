package com.virtualcampushub;

import javafx.animation.FadeTransition;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;

public class ViewManager {
    private final BorderPane root;
    private final Sidebar sidebar;
    private final TopBar topBar;

    private final DashboardView dashboardView;
    private final CoursesView coursesView;
    private final EventsView eventsView;
    private final ChatView chatView;
    private final ProfileView profileView;

    public ViewManager() {
        root = new BorderPane();

        dashboardView = new DashboardView();
        coursesView = new CoursesView();
        eventsView = new EventsView();
        chatView = new ChatView();
        profileView = new ProfileView();

        topBar = new TopBar(this);
        sidebar = new Sidebar(this);

        root.setLeft(sidebar.getContent());
        root.setTop(topBar.getContent());
        root.getStyleClass().add("root");
    }

    public BorderPane getRoot() {
        return root;
    }

    public void showDashboard() {
        switchTo(dashboardView.getContent());
        sidebar.setActive(Sidebar.Section.DASHBOARD);
    }

    public void showCourses() {
        switchTo(coursesView.getContent());
        sidebar.setActive(Sidebar.Section.COURSES);
    }

    public void showEvents() {
        switchTo(eventsView.getContent());
        sidebar.setActive(Sidebar.Section.EVENTS);
    }

    public void showChat() {
        switchTo(chatView.getContent());
        sidebar.setActive(Sidebar.Section.MESSAGING);
    }

    public void showProfile() {
        switchTo(profileView.getContent());
        sidebar.setActive(Sidebar.Section.PROFILE);
    }

    private void switchTo(Node newView) {
        Node old = root.getCenter();
        if (old != null) {
            FadeTransition out = new FadeTransition(Duration.millis(250), old);
            out.setFromValue(1);
            out.setToValue(0);
            out.setOnFinished(evt -> {
                root.setCenter(newView);
                animateIn(newView);
            });
            out.play();
        } else {
            root.setCenter(newView);
            animateIn(newView);
        }
    }

    private void animateIn(Node node) {
        FadeTransition in = new FadeTransition(Duration.millis(300), node);
        in.setFromValue(0);
        in.setToValue(1);
        in.play();
    }

    public void showToast(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Notification");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
        // Auto-close after 2 seconds
        new Thread(() -> {
            try {
                Thread.sleep(1800);
                if (alert.isShowing()) {
                    alert.close();
                }
            } catch (InterruptedException ignored) {
            }
        }).start();
    }
}
