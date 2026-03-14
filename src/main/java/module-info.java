

module com.virtualcampushub {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.virtualcampushub to javafx.fxml;
    exports com.virtualcampushub;
}
