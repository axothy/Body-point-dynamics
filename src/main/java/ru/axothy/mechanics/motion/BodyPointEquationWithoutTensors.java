package ru.axothy.mechanics.motion;

import javafx.geometry.Point3D;
import ru.axothy.mechanics.tensors.Tensor;

import static javafx.geometry.Point3D.ZERO;

public class BodyPointEquationWithoutTensors {
    private final double mass;
    private final double J;
    private final double B;
    private double A;

    private BodyPointEquationWithoutTensors() {
        throw new AssertionError();
    }

    public BodyPointEquationWithoutTensors(double mass, Tensor J, Tensor B, double coulombConst) {
        this.mass = mass;
        this.J = J.getArray().get(0).get(0);
        this.B = B.getArray().get(0).get(0);
        this.A = coulombConst;
    }

    //Not efficient...

    public Point3D equationRightSide1(Point3D radiusVector, Point3D velocity, Point3D omega) {
        double coeff = (1.0) / (mass * J - B * B);//1/(mJ-B^2)
        double rCube = Math.pow(radiusVector.distance(ZERO), 3.0 / 2.0); //R^3

        double vX = (-J * A * coeff * radiusVector.getX()) / (rCube) -
                (coeff * B * B * omega.crossProduct(velocity).getX());

        double vY = (-J * A * coeff * radiusVector.getY()) / (rCube) -
                (coeff * B * B * omega.crossProduct(velocity).getY());

        double vZ = (-J * A * coeff * radiusVector.getZ()) / (rCube) -
                (coeff * B * B * omega.crossProduct(velocity).getZ());

        return new Point3D(vX, vY, vZ);

    }

    public Point3D equationRightSide2(Point3D radiusVector, Point3D velocity, Point3D omega) {
        double coeff = (1.0) / (B * B - mass * J); //(1/(B^2-mJ))
        double rCube = Math.pow(radiusVector.distance(ZERO), 3.0 / 2.0); //R^3

        double wX = (-A * B * coeff * radiusVector.getX() / rCube) -
                coeff * mass * B * omega.crossProduct(velocity).getX();

        double wY = (-A * B * coeff * radiusVector.getY() / rCube) -
                coeff * mass * B * omega.crossProduct(velocity).getY();

        double wZ = (-A * B * coeff * radiusVector.getZ() / rCube) -
                coeff * mass * B * omega.crossProduct(velocity).getZ();

        return new Point3D(wX, wY, wZ);
    }

    public Point3D equationRightSide3(Point3D velocity) {
        double rX = velocity.getX();
        double rY = velocity.getY();
        double rZ = velocity.getZ();

        return new Point3D(rX, rY, rZ);
    }
}
