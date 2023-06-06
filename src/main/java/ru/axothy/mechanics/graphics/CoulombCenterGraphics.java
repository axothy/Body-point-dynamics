package ru.axothy.mechanics.graphics;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import ru.axothy.mechanics.motion.MaterialPointMotion;

public class CoulombCenterGraphics {
    private Sphere sphere;
    private static final double DEFAULT_RADIUS = 20;
    private static final PhongMaterial material = new PhongMaterial();

    static {
        material.setDiffuseColor(Color.YELLOW);
    }

    public CoulombCenterGraphics() {
        sphere = new Sphere(DEFAULT_RADIUS);
        sphere.setMaterial(material);
    }

    public CoulombCenterGraphics(double radius) {
        sphere = new Sphere(radius);
        sphere.setMaterial(material);
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
