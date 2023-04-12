package ru.axothy.mechanics.graphics;

import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import ru.axothy.mechanics.motion.MaterialPointMotion;

public class PointGraphics {
    private final int number = counter++;
    private static int counter = 0;
    private Sphere sphere;
    private static final double DEFAULT_RADIUS = 10;
    private PhongMaterial material = new PhongMaterial();

    {
        material.setDiffuseColor(Color.valueOf("#D30000"));
        MaterialPointMotion motion = new MaterialPointMotion();
    }

    public PointGraphics() {
        sphere = new Sphere(DEFAULT_RADIUS);
        sphere.setMaterial(material);
    }

    public PointGraphics(double radius) {
        sphere = new Sphere(radius);
        sphere.setMaterial(material);
    }

    public void changeRadius(double radius) {
        sphere.setRadius(radius);
    }

    public void changeColor(Color color) {
        material.setDiffuseColor(color);
        sphere.setMaterial(material);
    }

    public Sphere getSphere() {
        return sphere;
    }

    public int getNumber() {
        return number;
    }

    public static int getCounter() {
        return counter;
    }


    @Override
    public String toString() {
        return "Sphere " + sphere;
    }
}
