module com.example.secondbudgettrackergui {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;

    opens com.example.secondbudgettrackergui to javafx.fxml;
    exports com.example.secondbudgettrackergui;
}
