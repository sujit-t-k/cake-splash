module com.ajikhoji.cakesplash {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires javafx.media;
    requires java.desktop;

    opens com.ajikhoji.cakesplash to javafx.fxml;
    exports com.ajikhoji.cakesplash;
}