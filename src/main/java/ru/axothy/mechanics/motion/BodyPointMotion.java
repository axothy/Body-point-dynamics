package ru.axothy.mechanics.motion;

import javafx.geometry.Point3D;
import ru.axothy.mechanics.bodypoint.BodyPoint;
import ru.axothy.mechanics.bodypoint.BodyPointMovingData;
import ru.axothy.mechanics.bodypoint.MaterialPoint;
import ru.axothy.mechanics.equationsolvers.EulerMethodForSystem;
import ru.axothy.mechanics.tensors.Tensor;

import java.util.ArrayList;

public class BodyPointMotion implements Motion {
    public static final int INITIAL = 0;

    private double A; //Coulomb constant
    private BodyPoint bodyPoint;

    private BodyPointMovingData data;

    public BodyPointMotion() {
        data = new BodyPointMovingData();
    }

    public BodyPointMotion(double coulombConstant) {
        A = coulombConstant;
        data = new BodyPointMovingData();
    }

    public MaterialPoint getBodyPoint() {
        return bodyPoint;
    }

    public void setMaterialPoint(MaterialPoint materialPoint) {
        this.bodyPoint = bodyPoint;
    }

    public void setMaterialPoint(BodyPoint bodyPoint,
                                 Point3D initialRadiusVector,
                                 Point3D initialVelocityTrans,
                                 Point3D initialVelocityRot) {
        this.bodyPoint = bodyPoint;
        data.getRadiusVector().add(INITIAL, initialRadiusVector);
        data.getVelocityTrans().add(INITIAL, initialVelocityTrans);
        data.getVelocityTrans().add(INITIAL, initialVelocityRot);
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

    public void setInitialVelocityTrans(Point3D velocity) {
        data.getVelocityTrans().add(INITIAL, velocity);
    }

    public void setInitialVelocityRot(Point3D velocity) {
        data.getVelocityRot().add(INITIAL, velocity);
    }

    public Point3D getInitialRadiusVector() {
        return data.getRadiusVector().get(INITIAL);
    }

    public Point3D getInitialVelocityTrans() {
        return data.getVelocityTrans().get(INITIAL);
    }

    public Point3D getInitialVelocityRot() {
        return data.getVelocityRot().get(INITIAL);
    }

    public void startMotion() {
        BodyPointEquation equation = new BodyPointEquation(bodyPoint.getMass(),
                bodyPoint.getJ(),
                bodyPoint.getB(),
                A);


        data = EulerMethodForSystem.solve(equation,
                getInitialRadiusVector(),
                getInitialVelocityTrans(),
                getInitialVelocityRot());
    }

    public void setBodyPoint(BodyPoint bodyPoint) {
        this.bodyPoint = bodyPoint;
    }

    public BodyPointMovingData getData() {
        return data;
    }

    public void setData(BodyPointMovingData data) {
        this.data = data;
    }

    public static void main(String[] args) {
        double mass = 50;
        Tensor J = Tensor.UNARY_TENSOR;
        Tensor B = Tensor.UNARY_TENSOR;

        BodyPointMotion motion = new BodyPointMotion();
        motion.setBodyPoint(new BodyPoint(mass, J, B));
        motion.setCoulombConstant(10);
        motion.setInitialRadiusVector(new Point3D(-30, 10, 0));
        motion.setInitialVelocityTrans(new Point3D(2, 0, 0));
        motion.setInitialVelocityRot(new Point3D(0, 0, 0));

        motion.startMotion();
        System.out.println(motion.data.getRadiusVector());
    }
}
