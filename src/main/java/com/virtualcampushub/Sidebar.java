package com.virtualcampushub;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class Sidebar {
    public enum Section {
        DASHBOARD,
        COURSES,
        EVENTS,
        MESSAGING,
        PROFILE
    }

    private final VBox content;
    private final SectionItem dashboard;
    private final SectionItem courses;
    private final SectionItem events;
    private final SectionItem messaging;
    private final SectionItem profile;

    public Sidebar(ViewManager viewManager) {
        content = new VBox(14);
        content.getStyleClass().add("sidebar");
        content.setPadding(new Insets(14));

        Label title = new Label("Virtual Campus");
        title.getStyleClass().add("sidebar-title");

        dashboard = new SectionItem("Dashboard", Section.DASHBOARD, viewManager::showDashboard);
        courses = new SectionItem("Cours", Section.COURSES, viewManager::showCourses);
        events = new SectionItem("Événements", Section.EVENTS, viewManager::showEvents);
        messaging = new SectionItem("Messagerie", Section.MESSAGING, viewManager::showChat);
        profile = new SectionItem("Profil", Section.PROFILE, viewManager::showProfile);

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        content.getChildren().addAll(title, dashboard, courses, events, messaging, spacer, profile);
    }

    public VBox getContent() {
        return content;
    }

    public void setActive(Section section) {
        dashboard.setActive(section == Section.DASHBOARD);
        courses.setActive(section == Section.COURSES);
        events.setActive(section == Section.EVENTS);
        messaging.setActive(section == Section.MESSAGING);
        profile.setActive(section == Section.PROFILE);
    }

    private static class SectionItem extends HBox {
        private final Label label;

        public SectionItem(String text, Section section, Runnable action) {
            getStyleClass().add("sidebar-item");
            setAlignment(Pos.CENTER_LEFT);
            setSpacing(10);
            setPadding(new Insets(10, 12, 10, 12));

            label = new Label(text);
            label.getStyleClass().add("sidebar-item-label");

            getChildren().add(label);
            setCursor(Cursor.HAND);

            addEventHandler(MouseEvent.MOUSE_ENTERED, e -> setEffect(new DropShadow(10, Color.rgb(0, 0, 0, 0.25))));
            addEventHandler(MouseEvent.MOUSE_EXITED, e -> setEffect(null));
            addEventHandler(MouseEvent.MOUSE_CLICKED, e -> action.run());
        }

        public void setActive(boolean active) {
            if (active) {
                if (!getStyleClass().contains("active")) {
                    getStyleClass().add("active");
                }
            } else {
                getStyleClass().remove("active");
            }
        }
    }
}
