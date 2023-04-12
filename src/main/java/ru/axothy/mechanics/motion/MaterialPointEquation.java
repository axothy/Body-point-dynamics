package ru.axothy.mechanics.motion;

import javafx.geometry.Point3D;
import ru.axothy.mechanics.tensors.Tensor;

import static javafx.geometry.Point3D.ZERO;

public class MaterialPointEquation implements Equation {

    //Parameters of equation:
    private final double mass;
    private final Tensor J = Tensor.ZERO_TENSOR;
    private final Tensor B = Tensor.ZERO_TENSOR;
    private double A;

    private MaterialPointEquation() {
        throw new AssertionError();
    }
    public MaterialPointEquation(double mass, double coulombConst) {
        this.mass = mass;
        this.A = coulombConst;
    }

    @Override
    public Point3D equationRightSide(Point3D radiusVector) {
        double coeff = 1.0 / mass;

        double x = -coeff * (A * radiusVector.getX()) / (Math.pow(radiusVector.distance(ZERO), 3.0 / 2.0));
        double y = -coeff * (A * radiusVector.getY()) / (Math.pow(radiusVector.distance(ZERO), 3.0 / 2.0));
        double z = ZERO.getZ();

        return new Point3D(x, y, z);
    }
}
