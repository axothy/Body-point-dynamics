module ru.axothy.mechanics {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;
    requires org.fxyz3d.core;

    opens ru.axothy.mechanics to javafx.fxml;
    exports ru.axothy.mechanics;
}