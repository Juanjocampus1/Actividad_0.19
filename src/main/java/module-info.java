module com.empresa.actividad_019 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires static lombok;
    requires mongo.java.driver;

    opens com.empresa.actividad_019 to javafx.fxml;
    exports com.empresa.actividad_019;
}