package ru.axothy.mechanics.graphics;

import javafx.event.EventHandler;
import javafx.scene.Camera;
import javafx.scene.input.MouseEvent;
import javafx.scene.transform.Rotate;

public class MouseLook implements EventHandler<MouseEvent> {
    private static Rotate rotation;
    private static int oldX, newX;
    private static boolean alreadyMoved = false;
    private Camera camera;

    public MouseLook(Camera camera) {
        this.camera = camera;
    }

    @Override
    public void handle(MouseEvent event) {
        if (alreadyMoved) {
            newX = (int) event.getScreenX();

            if (oldX < newX) {
                rotation = new Rotate(0.5,
                        camera.getTranslateX(), camera.getTranslateY(), camera.getTranslateZ(),
                        Rotate.Y_AXIS);


            } else if (oldX > newX) {
                rotation = new Rotate(-0.5,
                        camera.getTranslateX(), camera.getTranslateY(), camera.getTranslateZ(),
                        Rotate.Y_AXIS);

            }
            camera.getTransforms().addAll(rotation);

            oldX = newX;
        } else {
            oldX = (int) event.getScreenX();
            alreadyMoved = true;
        }
    }
}
