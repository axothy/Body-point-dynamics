package ru.axothy.mechanics.equationsolvers;

import javafx.geometry.Point3D;
import ru.axothy.mechanics.bodypoint.BodyPointMovingData;
import ru.axothy.mechanics.motion.BodyPointEquationWithoutTensors;
import ru.axothy.mechanics.tensors.Tensor;

import java.util.ArrayList;

import static ru.axothy.mechanics.equationsolvers.VerletMethodForSystem.Coefficients.*;


public class VerletMethodForSystem {

    class Coefficients {
        public static final double C1 = 0;
        public static final double C2 = 1;
        public static final double D1 = 0.5;
        public static final double D2 = 0.5;

    }

    public static BodyPointMovingData solve(BodyPointEquationWithoutTensors eq, Point3D r0, Point3D v0, Point3D w0, double dt , int n) {
        ArrayList<Point3D> r = new ArrayList<>();
        ArrayList<Point3D> v = new ArrayList<>();
        ArrayList<Point3D> w = new ArrayList<>();
        r.add(r0);
        v.add(v0);
        w.add(w0);

        int i = 1;
        do {
            //-> Radius-vector and velocity from Euler symplectic integration:
            Point3D r_first = new Point3D(
                    r.get(i - 1).getX() + C1 * v.get(i - 1).getX() * dt,
                    r.get(i - 1).getY() + C1 * v.get(i - 1).getY() * dt,
                    r.get(i - 1).getZ() + C1 * v.get(i - 1).getZ() * dt
            );

            Point3D v_first = new Point3D(
                    v.get(i - 1).getX() + D1 * eq.equationRightSide1(r_first, v.get(i - 1), w.get(i - 1)).getX() * dt,
                    v.get(i - 1).getY() + D1 * eq.equationRightSide1(r_first, v.get(i - 1), w.get(i - 1)).getY() * dt,
                    v.get(i - 1).getZ() + D1 * eq.equationRightSide1(r_first, v.get(i - 1), w.get(i - 1)).getZ() * dt
            );

            Point3D w_first = new Point3D(
                    w.get(i - 1).getX() + D1 * eq.equationRightSide2(r_first, v.get(i - 1), w.get(i - 1)).getX() * dt,
                    w.get(i - 1).getY() + D1 * eq.equationRightSide2(r_first, v.get(i - 1), w.get(i - 1)).getY() * dt,
                    w.get(i - 1).getZ() + D1 * eq.equationRightSide2(r_first, v.get(i - 1), w.get(i - 1)).getZ() * dt
            );

            Point3D r_second = new Point3D(
                    r_first.getX() + C2 * v_first.getX() * dt,
                    r_first.getY() + C2 * v_first.getY() * dt,
                    r_first.getZ() + C2 * v_first.getZ() * dt
            );

            Point3D v_second = new Point3D(
                    v_first.getX() + D2 * eq.equationRightSide1(r_second, v.get(i - 1), w.get(i - 1)).getX() * dt,
                    v_first.getY() + D2 * eq.equationRightSide1(r_second, v.get(i - 1), w.get(i - 1)).getY() * dt,
                    v_first.getZ() + D2 * eq.equationRightSide1(r_second, v.get(i - 1), w.get(i - 1)).getZ() * dt
            );

            Point3D w_second = new Point3D(
                    w_first.getX() + D2 * eq.equationRightSide2(r_second, v.get(i - 1), w.get(i - 1)).getX() * dt,
                    w_first.getY() + D2 * eq.equationRightSide2(r_second, v.get(i - 1), w.get(i - 1)).getY() * dt,
                    w_first.getZ() + D2 * eq.equationRightSide2(r_second, v.get(i - 1), w.get(i - 1)).getZ() * dt
            );

            r.add(r_second);
            v.add(v_second);
            w.add(w_second);

            i++;
        } while (i < n);

        return new BodyPointMovingData(r, v, w);
    }

    public static void main(String[] args) {
        Tensor J = Tensor.UNARY_TENSOR;
        Tensor B = Tensor.UNARY_TENSOR;

        BodyPointEquationWithoutTensors equation = new BodyPointEquationWithoutTensors(1.5, J, B, 10);
        VerletMethodForSystem solver = new VerletMethodForSystem();
        BodyPointMovingData data = solver.solve(equation,
                new Point3D(-300, 100, 0),
                new Point3D(2, 0, 0),
                new Point3D(2, 0, 0), 0.0001, 250000);

        System.out.println(data.getRadiusVector());
    }
}
