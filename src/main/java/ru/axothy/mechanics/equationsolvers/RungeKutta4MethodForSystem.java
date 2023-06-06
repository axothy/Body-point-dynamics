package ru.axothy.mechanics.equationsolvers;

import javafx.geometry.Point3D;
import ru.axothy.mechanics.bodypoint.BodyPointMovingData;
import ru.axothy.mechanics.motion.BodyPointEquation;
import ru.axothy.mechanics.motion.BodyPointEquationWithoutTensors;
import ru.axothy.mechanics.tensors.Tensor;

import java.util.ArrayList;

public class RungeKutta4MethodForSystem {
    public static BodyPointMovingData solve(BodyPointEquationWithoutTensors eq, Point3D r0, Point3D v0, Point3D w0, double dt, int n) {
        ArrayList<Point3D> r = new ArrayList<>();
        ArrayList<Point3D> v = new ArrayList<>();
        ArrayList<Point3D> w = new ArrayList<>();

        r.add(r0);
        v.add(v0);
        w.add(w0);

        int i = 1;
        do {
            Point3D K1_v = new Point3D(
                    eq.equationRightSide1(r.get(i - 1), v.get(i - 1), w.get(i - 1)).getX(),
                    eq.equationRightSide1(r.get(i - 1), v.get(i - 1), w.get(i - 1)).getY(),
                    eq.equationRightSide1(r.get(i - 1), v.get(i - 1), w.get(i - 1)).getZ()
            );

            Point3D K1_w = new Point3D(
                    eq.equationRightSide2(r.get(i - 1), v.get(i - 1), w.get(i - 1)).getX(),
                    eq.equationRightSide2(r.get(i - 1), v.get(i - 1), w.get(i - 1)).getY(),
                    eq.equationRightSide2(r.get(i - 1), v.get(i - 1), w.get(i - 1)).getZ()
            );

            Point3D K1_r = new Point3D(
                    eq.equationRightSide3(v.get(i - 1)).getX(),
                    eq.equationRightSide3(v.get(i - 1)).getY(),
                    eq.equationRightSide3(v.get(i - 1)).getZ()
            );


            Point3D K2_v = new Point3D(
                    eq.equationRightSide1(r.get(i - 1).add(K1_r.multiply(dt / 2.0)),
                            v.get(i - 1).add(K1_v.multiply(dt / 2.0)), w.get(i - 1).add(K1_w.multiply(dt / 2.0))).getX(),
                    eq.equationRightSide1(r.get(i - 1).add(K1_r.multiply(dt / 2.0)),
                            v.get(i - 1).add(K1_v.multiply(dt / 2.0)), w.get(i - 1).add(K1_w.multiply(dt / 2.0))).getY(),
                    eq.equationRightSide1(r.get(i - 1).add(K1_r.multiply(dt / 2.0)),
                            v.get(i - 1).add(K1_v.multiply(dt / 2.0)), w.get(i - 1).add(K1_w.multiply(dt / 2.0))).getZ()
            );


            Point3D K2_w = new Point3D(
                    eq.equationRightSide2(r.get(i - 1).add(K1_r.multiply(dt / 2.0)),
                            v.get(i - 1).add(K1_v.multiply(dt / 2.0)), w.get(i - 1).add(K1_w.multiply(dt / 2.0))).getX(),
                    eq.equationRightSide2(r.get(i - 1).add(K1_r.multiply(dt / 2.0)),
                            v.get(i - 1).add(K1_v.multiply(dt / 2.0)), w.get(i - 1).add(K1_w.multiply(dt / 2.0))).getY(),
                    eq.equationRightSide2(r.get(i - 1).add(K1_r.multiply(dt / 2.0)),
                            v.get(i - 1).add(K1_v.multiply(dt / 2.0)), w.get(i - 1).add(K1_w.multiply(dt / 2.0))).getZ()
            );


            Point3D K2_r = new Point3D(
                    eq.equationRightSide3(v.get(i - 1).add(K1_v.multiply(dt / 2.0))).getX(),
                    eq.equationRightSide3(v.get(i - 1).add(K1_v.multiply(dt / 2.0))).getY(),
                    eq.equationRightSide3(v.get(i - 1).add(K1_v.multiply(dt / 2.0))).getZ()
            );

            Point3D K3_v = new Point3D(
                    eq.equationRightSide1(r.get(i - 1).add(K2_r.multiply(dt / 2.0)),
                            v.get(i - 1).add(K2_v.multiply(dt / 2.0)), w.get(i - 1).add(K2_w.multiply(dt / 2.0))).getX(),
                    eq.equationRightSide1(r.get(i - 1).add(K2_r.multiply(dt / 2.0)),
                            v.get(i - 1).add(K2_v.multiply(dt / 2.0)), w.get(i - 1).add(K2_w.multiply(dt / 2.0))).getY(),
                    eq.equationRightSide1(r.get(i - 1).add(K2_r.multiply(dt / 2.0)),
                            v.get(i - 1).add(K2_v.multiply(dt / 2.0)), w.get(i - 1).add(K2_w.multiply(dt / 2.0))).getZ()
            );

            Point3D K3_w = new Point3D(
                    eq.equationRightSide2(r.get(i - 1).add(K2_r.multiply(dt / 2.0)),
                            v.get(i - 1).add(K2_v.multiply(dt / 2.0)), w.get(i - 1).add(K2_w.multiply(dt / 2.0))).getX(),
                    eq.equationRightSide2(r.get(i - 1).add(K2_r.multiply(dt / 2.0)),
                            v.get(i - 1).add(K2_v.multiply(dt / 2.0)), w.get(i - 1).add(K2_w.multiply(dt / 2.0))).getY(),
                    eq.equationRightSide2(r.get(i - 1).add(K2_r.multiply(dt / 2.0)),
                            v.get(i - 1).add(K2_v.multiply(dt / 2.0)), w.get(i - 1).add(K2_w.multiply(dt / 2.0))).getZ()
            );


            Point3D K3_r = new Point3D(
                    eq.equationRightSide3(v.get(i - 1).add(K2_v.multiply(dt / 2.0))).getX(),
                    eq.equationRightSide3(v.get(i - 1).add(K2_v.multiply(dt / 2.0))).getY(),
                    eq.equationRightSide3(v.get(i - 1).add(K2_v.multiply(dt / 2.0))).getZ()
            );

            Point3D K4_v = new Point3D(
                    eq.equationRightSide1(r.get(i - 1).add(K3_r.multiply(dt)),
                            v.get(i - 1).add(K3_v.multiply(dt)), w.get(i - 1).add(K3_w.multiply(dt))).getX(),
                    eq.equationRightSide1(r.get(i - 1).add(K3_r.multiply(dt)),
                            v.get(i - 1).add(K3_v.multiply(dt)), w.get(i - 1).add(K3_w.multiply(dt))).getY(),
                    eq.equationRightSide1(r.get(i - 1).add(K3_r.multiply(dt)),
                            v.get(i - 1).add(K3_v.multiply(dt)), w.get(i - 1).add(K3_w.multiply(dt))).getZ()
            );

            Point3D K4_w = new Point3D(
                    eq.equationRightSide2(r.get(i - 1).add(K3_r.multiply(dt)),
                            v.get(i - 1).add(K3_v.multiply(dt)), w.get(i - 1).add(K3_w.multiply(dt))).getX(),
                    eq.equationRightSide2(r.get(i - 1).add(K3_r.multiply(dt)),
                            v.get(i - 1).add(K3_v.multiply(dt)), w.get(i - 1).add(K3_w.multiply(dt))).getY(),
                    eq.equationRightSide2(r.get(i - 1).add(K3_r.multiply(dt)),
                            v.get(i - 1).add(K3_v.multiply(dt)), w.get(i - 1).add(K3_w.multiply(dt))).getZ()
            );


            Point3D K4_r = new Point3D(
                    eq.equationRightSide3(v.get(i - 1).add(K3_v.multiply(dt))).getX(),
                    eq.equationRightSide3(v.get(i - 1).add(K3_v.multiply(dt))).getY(),
                    eq.equationRightSide3(v.get(i - 1).add(K3_v.multiply(dt))).getZ()
            );


            //-> Velocity from RK4 schema
            double vX = v.get(i - 1).getX() + (dt / 6.0) * (K1_v.getX() + 2 * K2_v.getX() + 2 * K3_v.getX() + K4_v.getX());
            double vY = v.get(i - 1).getY() + (dt / 6.0) * (K1_v.getY() + 2 * K2_v.getY() + 2 * K3_v.getY() + K4_v.getY());
            double vZ = v.get(i - 1).getZ() + (dt / 6.0) * (K1_v.getZ() + 2 * K2_v.getZ() + 2 * K3_v.getZ() + K4_v.getZ());

            v.add(new Point3D(vX, vY, vZ));

            //-> Radius-vector from RK4 schema
            double rX = r.get(i - 1).getX() + (dt / 6.0) * (K1_r.getX() + 2 * K2_r.getX() + 2 * K3_r.getX() + K4_r.getX());
            double rY = r.get(i - 1).getY() + (dt / 6.0) * (K1_r.getY() + 2 * K2_r.getY() + 2 * K3_r.getY() + K4_r.getY());
            double rZ = r.get(i - 1).getZ() + (dt / 6.0) * (K1_r.getZ() + 2 * K2_r.getZ() + 2 * K3_r.getZ() + K4_r.getZ());

            r.add(new Point3D(rX, rY, rZ));

            //-> Angular velocity from RK4 schema
            double wX = w.get(i - 1).getX() + (dt / 6.0) * (K1_w.getX() + 2 * K2_w.getX() + 2 * K3_w.getX() + K4_w.getX());
            double wY = w.get(i - 1).getY() + (dt / 6.0) * (K1_w.getY() + 2 * K2_w.getY() + 2 * K3_w.getY() + K4_w.getY());
            double wZ = w.get(i - 1).getZ() + (dt / 6.0) * (K1_w.getZ() + 2 * K2_w.getZ() + 2 * K3_w.getZ() + K4_w.getZ());
            w.add(new Point3D(wX, wY, wZ));

            i++;
        } while (i < n);

        return new BodyPointMovingData(r, v, w);
    }

    public static void main(String[] args) {
        Tensor J = Tensor.UNARY_TENSOR;
        Tensor B = Tensor.UNARY_TENSOR;

        BodyPointEquationWithoutTensors equation = new BodyPointEquationWithoutTensors(1.5, J, B, 10);
        RungeKutta4MethodForSystem solver = new RungeKutta4MethodForSystem();
        BodyPointMovingData data = solver.solve(equation,
                new Point3D(-300, 100, 0),
                new Point3D(2, 0, 0),
                new Point3D(2, 0, 0), 0.0001, 250000);

        System.out.println(data.getRadiusVector());
    }
}
