module ru.axothy.mechanics {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;

    opens ru.axothy.mechanics to javafx.fxml;
    exports ru.axothy.mechanics;
}