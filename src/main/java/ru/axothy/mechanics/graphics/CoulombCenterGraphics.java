package ru.axothy.mechanics.graphics;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;

public class CoulombCenterGraphics {
    private Sphere sphere;
    private static final double DEFAULT_RADIUS = 20;

    public CoulombCenterGraphics() {
        sphere = new Sphere(DEFAULT_RADIUS);
    }

    public CoulombCenterGraphics(double radius) {
        sphere = new Sphere(radius);
    }

    public void changeRadius(double radius) {
        sphere.setRadius(radius);
    }

    public Sphere getSphere() {
        return sphere;
    }

    @Override
    public String toString() {
        return "Coulomb center";
    }
}
