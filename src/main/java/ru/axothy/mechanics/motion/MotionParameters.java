package ru.axothy.mechanics.motion;

import javafx.geometry.Point3D;

/**
 * Parameters for the motion of a body-point in the Coulomb repulsive field.
 * The parameters allow you to get the initial conditions for the given initial conditions
 * for the momentum and proper angular momentum.
 */
public class MotionParameters {
    private double L; //distance between body-point and center
    private double d; //aiming distance, L >> d (e.g L = 100d)

    //Inertia tensors:
    private double j;
    private double b;

    private double mass;

    /**
     * Initial values for:
     * K1 = mv + Bw = k1_i
     * K2 = Bv + Jw = -Kz_k + Kx_i
     */
    private double k1, kX, kZ;

    private MotionParameters() {
        throw new AssertionError();
    }

    public MotionParameters(double L, double d, double mass, double j, double b, double k1, double kX, double kZ) {
        this.L = L;
        this.d = d;
        this.mass = mass;
        this.j = j;
        this.b = b;
        this.k1 = k1;
        this.kX = kX;
        this.kZ = kZ;
    }

    Point3D getInitialValueForRadiusVector() {
        double x = -L;
        double y = d - kZ / k1;
        double z = 0;

        return new Point3D(x, y, z);
    }

    Point3D getInitialValueForTransVelocity() {
        double x = (b * kX - j * k1) / (b * b - mass * j);
        double y = 0;
        double z = - (b * kZ) / (b * b - mass * j);

        return new Point3D(x, y, z);
    }

    Point3D getInitialValueForAngularVelocity() {
        double x = (b * k1 - mass * kX) / (b * b - mass * j);
        double y = 0;
        double z = (mass * kZ) / (b * b - mass * j);

        return new Point3D(x, y, z);
    }
}
