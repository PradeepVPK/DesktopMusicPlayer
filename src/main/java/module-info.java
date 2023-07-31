module com.example.jfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;


    opens com.example.jfx to javafx.fxml;
    exports com.example.jfx;
}