module com.example.jesspos {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires junit;

    opens com.example.jesspos to javafx.fxml;
    exports com.example.jesspos;
}