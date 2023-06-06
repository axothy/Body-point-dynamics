package ru.axothy.mechanics.bodypoint;

import javafx.geometry.Point3D;

import java.util.ArrayList;

public class MaterialPointMovingData {
    private ArrayList<Point3D> radiusVector;
    private ArrayList<Point3D> velocity;

    public MaterialPointMovingData() {
        radiusVector = new ArrayList<>();
        velocity = new ArrayList<>();
    }

    public MaterialPointMovingData(ArrayList<Point3D> radiusVector, ArrayList<Point3D> velocity) {
        this.radiusVector = radiusVector;
        this.velocity = velocity;
    }

    public ArrayList<Point3D> getRadiusVector() {
        return radiusVector;
    }

    public ArrayList<Point3D> getVelocity() {
        return velocity;
    }

    public void setRadiusVector(ArrayList<Point3D> radiusVector) {
        this.radiusVector = radiusVector;
    }

    public void setVelocity(ArrayList<Point3D> velocity) {
        this.velocity = velocity;
    }
}
