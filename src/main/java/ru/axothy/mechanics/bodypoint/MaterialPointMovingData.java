package ru.axothy.mechanics.bodypoint;

import javafx.geometry.Point3D;

import java.util.ArrayList;

public class MaterialPointMovingData {
    private ArrayList<Point3D> radiusVector;

    public MaterialPointMovingData() {
        radiusVector = new ArrayList<>();
    }

    public MaterialPointMovingData(ArrayList<Point3D> radiusVector) {
        this.radiusVector = radiusVector;
    }

    public ArrayList<Point3D> getRadiusVector() {
        return radiusVector;
    }

    public void setRadiusVector(ArrayList<Point3D> radiusVector) {
        this.radiusVector = radiusVector;
    }
}
