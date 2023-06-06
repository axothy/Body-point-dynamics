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

    public Point3D equationRightSide1(Point3D radiusVector, Point3D velocity, Point3D omega) {
        Tensor coeff = J.multiply(mass).subtract(B.multiply(B)); //mJ-B^2
        double rCube = Math.pow(radiusVector.distance(ZERO), 3.0 / 2.0); //R^3

        double vX = (J.multiply(-A).multiply(coeff.inverse()).dotProduct(radiusVector).getX() / rCube) -
                (coeff.inverse().dotProduct(B.multiply(B).dotProduct(omega).crossProduct(velocity))).getX();

        double vY = (J.multiply(-A).multiply(coeff.inverse()).dotProduct(radiusVector).getY() / rCube) -
                (coeff.inverse().dotProduct(B.multiply(B).dotProduct(omega).crossProduct(velocity))).getY();

        double vZ = (J.multiply(-A).multiply(coeff.inverse()).dotProduct(radiusVector).getZ() / rCube) -
                (coeff.inverse().dotProduct(B.multiply(B).dotProduct(omega).crossProduct(velocity))).getZ();

        return new Point3D(vX, vY, vZ);

    }

    public Point3D equationRightSide2(Point3D radiusVector, Point3D velocity, Point3D omega) {
        Tensor coeff = B.multiply(B).subtract(J.multiply(mass)); //B^2-mJ
        double rCube = Math.pow(radiusVector.distance(ZERO), 3.0 / 2.0); //R^3

        double wX = ((B.multiply(-A)).multiply(coeff.inverse()).dotProduct(radiusVector).getX() / rCube) -
                (coeff.inverse().dotProduct(B.multiply(mass).dotProduct(omega).crossProduct(velocity))).getX();

        double wY = ((B.multiply(-A)).multiply(coeff.inverse()).dotProduct(radiusVector).getY() / rCube) -
                (coeff.inverse().dotProduct(B.multiply(mass).dotProduct(omega).crossProduct(velocity))).getY();

        double wZ = ((B.multiply(-A)).multiply(coeff.inverse()).dotProduct(radiusVector).getZ() / rCube) -
                (coeff.inverse().dotProduct(B.multiply(mass).dotProduct(omega).crossProduct(velocity))).getZ();

        return new Point3D(wX, wY, wZ);
    }

    public Point3D equationRightSide3(Point3D velocity) {
        double rX = velocity.getX();
        double rY = velocity.getY();
        double rZ = velocity.getZ();

        return new Point3D(rX, rY, rZ);
    }
}
