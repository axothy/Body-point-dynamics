package ru.axothy.mechanics.motion;

import javafx.geometry.Point3D;
import ru.axothy.mechanics.tensors.Tensor;

import static javafx.geometry.Point3D.ZERO;

public class BodyPointEquation {
    //Parameters of equation:
    private final double mass;
    private final Tensor J;
    private final Tensor B;
    private double A;

    private BodyPointEquation() {
        throw new AssertionError();
    }

    public BodyPointEquation(double mass, Tensor J, Tensor B, double coulombConst) {
        this.mass = mass;
        this.J = J;
        this.B = B;
        this.A = coulombConst;
    }

    //Not efficient...
    public Point3D equationRightSide(Point3D radiusVector, Point3D dOmega) {
        double coeff = 1.0 / mass;

        double x = -coeff * ((A * radiusVector.getX()) / (Math.pow(radiusVector.distance(ZERO), 3.0 / 2.0))) -
                coeff * (B.dotProduct(dOmega).getX());

        double y = -coeff * ((A * radiusVector.getY()) / (Math.pow(radiusVector.distance(ZERO), 3.0 / 2.0))) -
                coeff * (B.dotProduct(dOmega).getY());

        double z = -coeff * ((A * radiusVector.getZ()) / (Math.pow(radiusVector.distance(ZERO), 3.0 / 2.0))) -
                coeff * (B.dotProduct(dOmega).getZ());

        return new Point3D(x, y, z);
    }

    //Not efficient...
    public Point3D equationRightSide2(Point3D d2RadiusVector, Point3D dRadiusVector, Point3D omega) {
        Tensor inverseJ = J.inverse();

        double omega_x = (inverseJ.dotProduct((B.dotProduct(omega).crossProduct(dRadiusVector)).subtract(B.dotProduct(d2RadiusVector))))
                .getX();

        double omega_y = (inverseJ.dotProduct((B.dotProduct(omega).crossProduct(dRadiusVector)).subtract(B.dotProduct(d2RadiusVector))))
                .getY();

        double omega_z = (inverseJ.dotProduct((B.dotProduct(omega).crossProduct(dRadiusVector)).subtract(B.dotProduct(d2RadiusVector))))
                .getZ();

        return new Point3D(omega_x, omega_y, omega_z);
    }
}
