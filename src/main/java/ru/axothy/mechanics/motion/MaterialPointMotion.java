package ru.axothy.mechanics.motion;

import javafx.geometry.Point3D;
import ru.axothy.mechanics.bodypoint.BodyPoint;
import ru.axothy.mechanics.bodypoint.BodyPointMovingData;
import ru.axothy.mechanics.bodypoint.MaterialPoint;
import ru.axothy.mechanics.bodypoint.MaterialPointMovingData;
import ru.axothy.mechanics.equationsolvers.EulerMethod;
import ru.axothy.mechanics.equationsolvers.RungeKutta4;
import ru.axothy.mechanics.equationsolvers.YoshidaMethod;

import java.lang.invoke.MethodType;
import java.util.ArrayList;

public class MaterialPointMotion implements Motion {
    public static final int INITIAL = 0;

    private double A; //Coulomb constant
    private MaterialPoint materialPoint;

    private MaterialPointMovingData data;
    private ArrayList<Point3D> v;

    public MaterialPointMotion() {
        data = new MaterialPointMovingData();
        v = new ArrayList<>();
    }

    public MaterialPointMotion(double coulombConstant) {
        A = coulombConstant;
        data = new MaterialPointMovingData();
        v = new ArrayList<>();
    }

    public MaterialPoint getMaterialPoint() {
        return materialPoint;
    }

    public void setMaterialPoint(MaterialPoint materialPoint) {
        this.materialPoint = materialPoint;
    }

    public void setMaterialPoint(MaterialPoint materialPoint,
                                 Point3D initialRadiusVector,
                                 Point3D initialDerivativeRadiusVector) {
        this.materialPoint = materialPoint;
        data.getRadiusVector().add(INITIAL, initialRadiusVector);
        v.add(INITIAL, initialDerivativeRadiusVector);
    }

    public void setCoulombConstant(double coulombConstant) {
        this.A = coulombConstant;
    }

    public double getCoulombConstant() {
        return A;
    }

    public void setInitialRadiusVector(Point3D radiusVector) {
        data.getRadiusVector().add(INITIAL, radiusVector);
    }

    public void setInitialDerivativeRadiusVector(Point3D derivativeRadiusVector) {
        v.add(INITIAL, derivativeRadiusVector);
    }

    public Point3D getInitialRadiusVector() {
        return data.getRadiusVector().get(INITIAL);
    }

    public Point3D getInitialVelocity() {
        return v.get(INITIAL);
    }

    public void startMotion(double h, int n) {
        MaterialPointEquation equation = new MaterialPointEquation(materialPoint.getMass(), A);
        data = RungeKutta4.solve(equation, data.getRadiusVector().get(INITIAL), v.get(INITIAL));
    }

    public ArrayList<Point3D> getRadiusVectorData() {
        return data.getRadiusVector();
    }

    public BodyPointMovingData getData() {
        return (BodyPointMovingData) data;
    }

    public ArrayList<Point3D> getDerivativeRadiusVectorData() {
        return v;
    }

    public BodyPoint getMotionPoint() {
        return (BodyPoint) materialPoint;
    }

    @Override
    public String toString() {
        return "Motion of point: " + materialPoint;
    }

    public static void main(String[] args) {
        MaterialPointMotion motion = new MaterialPointMotion(10);
        motion.setMaterialPoint(new MaterialPoint(10));
        motion.setInitialRadiusVector(new Point3D(-100, 40, 0));
        motion.setInitialDerivativeRadiusVector(new Point3D(Math.sqrt(20), 0, 0));
        motion.startMotion(0.0001, 250000);
        System.out.println(motion.getRadiusVectorData());
    }
}
