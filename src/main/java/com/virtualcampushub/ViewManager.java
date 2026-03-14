package com.virtualcampushub;

import javafx.animation.*;
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
    private final LoginView loginView;
    private final RegisterView registerView;
    private final ProfileView profileView;
    private ChatView chatView;

    private String currentUsername = "Utilisateur";
    private Node currentView;

    public ViewManager() {
        root = new BorderPane();

        dashboardView = new DashboardView();
        coursesView = new CoursesView();
        eventsView = new EventsView();
        loginView = new LoginView(this);
        registerView = new RegisterView(this);
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

    public void showChat() {
        showChat(currentUsername);
    }

    public void showChat(String username) {
        currentUsername = username == null || username.isBlank() ? "Utilisateur" : username;
        if (chatView != null) {
            chatView.shutdown();
        }
        chatView = new ChatView(currentUsername);
        switchToWithAnimation(chatView.getContent(), Sidebar.Section.CHAT, "slide-in-right");
    }

    /**
     * Version professionnelle avec animation sophistiquée pour PFE
     */
    public void showDashboardWithProfessionalAnimation() {
        switchToWithProfessionalAnimation(dashboardView.getContent(),
                                        Sidebar.Section.DASHBOARD,
                                        "professional-entrance");
    }

    public void showCourses() {
        switchToWithAnimation(coursesView.getContent(), Sidebar.Section.COURSES, "slide-in-right");
    }

    public void showEvents() {
        switchToWithAnimation(eventsView.getContent(), Sidebar.Section.EVENTS, "slide-in-left");
    }

    public void showLogin() {
        if (chatView != null) {
            chatView.shutdown();
            chatView = null;
        }
        switchToWithAnimation(loginView.getContent(), Sidebar.Section.LOGIN, "fade-in");
    }

    public void showRegister() {
        switchToWithAnimation(registerView.getContent(), Sidebar.Section.LOGIN, "fade-in");
    }

    public void showProfile() {
        switchToWithAnimation(profileView.getContent(), Sidebar.Section.PROFILE, "slide-in-right");
    }

    /**
     * Transition améliorée avec différents types d'animations
     */
    private void switchToWithAnimation(Node newView, Sidebar.Section section, String animationType) {
        Node old = root.getCenter();
        if (old != null) {
            // Animation de sortie sophistiquée
            ParallelTransition exitTransition = createExitTransition(old);
            exitTransition.setOnFinished(evt -> {
                root.setCenter(newView);
                animateEntrance(newView, animationType);
            });
            exitTransition.play();
        } else {
            root.setCenter(newView);
            animateEntrance(newView, animationType);
        }

        sidebar.setActive(section);
    }

    /**
     * Crée une animation de sortie élégante
     */
    private ParallelTransition createExitTransition(Node node) {
        FadeTransition fadeOut = new FadeTransition(Duration.millis(200), node);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);

        ScaleTransition scaleOut = new ScaleTransition(Duration.millis(200), node);
        scaleOut.setFromX(1.0);
        scaleOut.setFromY(1.0);
        scaleOut.setToX(0.95);
        scaleOut.setToY(0.95);

        TranslateTransition slideOut = new TranslateTransition(Duration.millis(200), node);
        slideOut.setFromX(0);
        slideOut.setToX(-20);

        ParallelTransition exitTransition = new ParallelTransition(fadeOut, scaleOut, slideOut);
        return exitTransition;
    }

    /**
     * Anime l'entrée d'une nouvelle vue avec différents effets
     */
    private void animateEntrance(Node node, String animationType) {
        // Reset des propriétés
        node.setOpacity(0);
        node.setScaleX(1);
        node.setScaleY(1);
        node.setTranslateX(0);
        node.setTranslateY(0);

        switch (animationType) {
            case "bounce-in":
                animateBounceIn(node);
                break;
            case "slide-in-left":
                animateSlideInLeft(node);
                break;
            case "slide-in-right":
                animateSlideInRight(node);
                break;
            case "fade-in":
            default:
                animateFadeIn(node);
                break;
        }
    }

    private void animateBounceIn(Node node) {
        // Animation de rebond sophistiquée
        ScaleTransition scaleIn = new ScaleTransition(Duration.millis(600), node);
        scaleIn.setFromX(0.3);
        scaleIn.setFromY(0.3);
        scaleIn.setToX(1.0);
        scaleIn.setToY(1.0);
        scaleIn.setInterpolator(Interpolator.SPLINE(0.68, -0.55, 0.265, 1.55));

        FadeTransition fadeIn = new FadeTransition(Duration.millis(400), node);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);

        ParallelTransition bounceIn = new ParallelTransition(scaleIn, fadeIn);
        bounceIn.play();
    }

    private void animateSlideInLeft(Node node) {
        node.setTranslateX(-50);
        node.setOpacity(0);

        TranslateTransition slideIn = new TranslateTransition(Duration.millis(400), node);
        slideIn.setFromX(-50);
        slideIn.setToX(0);
        slideIn.setInterpolator(Interpolator.EASE_OUT);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), node);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);

        ParallelTransition slideInLeft = new ParallelTransition(slideIn, fadeIn);
        slideInLeft.play();
    }

    private void animateSlideInRight(Node node) {
        node.setTranslateX(50);
        node.setOpacity(0);

        TranslateTransition slideIn = new TranslateTransition(Duration.millis(400), node);
        slideIn.setFromX(50);
        slideIn.setToX(0);
        slideIn.setInterpolator(Interpolator.EASE_OUT);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), node);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);

        ParallelTransition slideInRight = new ParallelTransition(slideIn, fadeIn);
        slideInRight.play();
    }

    private void animateFadeIn(Node node) {
        FadeTransition fadeIn = new FadeTransition(Duration.millis(500), node);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.setInterpolator(Interpolator.EASE_IN);
        fadeIn.play();
    }

    // Méthodes de compatibilité
    private void switchTo(Node newView) {
        switchToWithAnimation(newView, Sidebar.Section.DASHBOARD, "fade-in");
    }

    private void animateIn(Node node) {
        animateFadeIn(node);
    }

    /**
     * Méthode professionnelle avec animation sophistiquée pour PFE
     */
    private void switchToWithProfessionalAnimation(Node newContent, Sidebar.Section section, String animationClass) {
        if (currentView == newContent) return;

        // Animation de sortie professionnelle
        if (currentView != null) {
            FadeTransition fadeOut = new FadeTransition(Duration.millis(300), currentView);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.setInterpolator(Interpolator.EASE_OUT);

            ScaleTransition scaleOut = new ScaleTransition(Duration.millis(300), currentView);
            scaleOut.setFromX(1.0);
            scaleOut.setFromY(1.0);
            scaleOut.setToX(0.95);
            scaleOut.setToY(0.95);
            scaleOut.setInterpolator(Interpolator.EASE_OUT);

            ParallelTransition exitAnimation = new ParallelTransition(fadeOut, scaleOut);
            exitAnimation.setOnFinished(e -> {
                root.setCenter(newContent);
                startProfessionalEntranceAnimation(newContent, section);
            });
            exitAnimation.play();
        } else {
            root.setCenter(newContent);
            startProfessionalEntranceAnimation(newContent, section);
        }

        currentView = newContent;
    }

    /**
     * Animation d'entrée professionnelle sophistiquée
     */
    private void startProfessionalEntranceAnimation(Node content, Sidebar.Section section) {
        // Animation d'échelle progressive
        ScaleTransition scaleIn = new ScaleTransition(Duration.millis(500), content);
        scaleIn.setFromX(0.8);
        scaleIn.setFromY(0.8);
        scaleIn.setToX(1.0);
        scaleIn.setToY(1.0);
        scaleIn.setInterpolator(Interpolator.EASE_OUT);

        // Animation de fondu
        FadeTransition fadeIn = new FadeTransition(Duration.millis(500), content);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.setInterpolator(Interpolator.EASE_OUT);

        // Animation de translation subtile
        TranslateTransition slideIn = new TranslateTransition(Duration.millis(500), content);
        slideIn.setFromX(50);
        slideIn.setFromY(0);
        slideIn.setToX(0);
        slideIn.setToY(0);
        slideIn.setInterpolator(Interpolator.EASE_OUT);

        ParallelTransition entranceAnimation = new ParallelTransition(scaleIn, fadeIn, slideIn);
        entranceAnimation.setOnFinished(e -> {
            sidebar.setActive(section);
            // Animation de pulsation subtile pour indiquer l'activation
            ScaleTransition pulse = new ScaleTransition(Duration.millis(200), content);
            pulse.setFromX(1.0);
            pulse.setFromY(1.0);
            pulse.setToX(1.02);
            pulse.setToY(1.02);
            pulse.setAutoReverse(true);
            pulse.setCycleCount(2);
            pulse.setInterpolator(Interpolator.EASE_BOTH);
            pulse.play();
        });
        entranceAnimation.play();
    }

    public void showToast(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Notification");
        alert.setHeaderText(null);
        alert.setContentText(message);

        // Style moderne pour l'alerte
        alert.getDialogPane().getStyleClass().add("modern-alert");
        alert.getDialogPane().getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        alert.show();

        // Animation d'auto-fermeture améliorée
        FadeTransition fadeOut = new FadeTransition(Duration.millis(500), alert.getDialogPane());
        fadeOut.setDelay(Duration.seconds(2));
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(e -> alert.close());
        fadeOut.play();
    }
}
