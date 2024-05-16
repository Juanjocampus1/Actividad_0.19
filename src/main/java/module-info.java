module com.empresa.actividad19 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens com.empresa.actividad19 to javafx.fxml;
    exports com.empresa.actividad19;
}