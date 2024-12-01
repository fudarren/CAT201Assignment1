module com.dagolee.asgn_1 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.dagolee.asgn_1 to javafx.fxml;
    exports com.dagolee.asgn_1;
}