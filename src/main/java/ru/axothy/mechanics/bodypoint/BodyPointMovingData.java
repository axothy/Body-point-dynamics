package ru.axothy.mechanics.bodypoint;

import javafx.geometry.Point3D;

import java.util.ArrayList;

public class BodyPointMovingData extends MaterialPointMovingData {
    private ArrayList<Point3D> velocityTrans;
    private ArrayList<Point3D> velocityRot;

    public BodyPointMovingData() {
        super();
        velocityTrans = new ArrayList<>();
        velocityRot = new ArrayList<>();
    }

    public BodyPointMovingData(ArrayList<Point3D> radiusVector,
                               ArrayList<Point3D> velocityTrans,
                               ArrayList<Point3D> velocityRot) {
        super(radiusVector);
        this.velocityTrans = velocityTrans;
        this.velocityRot = velocityRot;
    }

    public ArrayList<Point3D> getVelocityTrans() {
        return velocityTrans;
    }

    public void setVelocityTrans(ArrayList<Point3D> velocityTrans) {
        this.velocityTrans = velocityTrans;
    }

    public ArrayList<Point3D> getVelocityRot() {
        return velocityRot;
    }

    public void setVelocityRot(ArrayList<Point3D> velocityRot) {
        this.velocityRot = velocityRot;
    }
}

