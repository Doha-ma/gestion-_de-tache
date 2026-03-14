

module com.virtualcampushub {
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;
    requires java.naming;

    opens com.virtualcampushub to javafx.fxml;
    exports com.virtualcampushub;
}
