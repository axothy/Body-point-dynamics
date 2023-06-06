package ru.axothy.mechanics.equationsolvers;

import javafx.geometry.Point3D;
import ru.axothy.mechanics.bodypoint.BodyPointMovingData;
import ru.axothy.mechanics.bodypoint.MaterialPointMovingData;
import ru.axothy.mechanics.motion.BodyPointEquation;
import ru.axothy.mechanics.motion.MaterialPointEquation;
import ru.axothy.mechanics.tensors.Tensor;

import java.util.ArrayList;

public class RungeKutta4 {
    public static MaterialPointMovingData solve(MaterialPointEquation eq, Point3D r0, Point3D v0) {
        ArrayList<Point3D> r = new ArrayList<>();
        ArrayList<Point3D> v = new ArrayList<>();
        double dt = 0.001;

        r.add(r0);
        v.add(v0);

        int i = 1;
        do {
            Point3D K1_v = eq.equationRightSide1(r.get(i - 1));
            Point3D K1_r = eq.equationRightSide2(v.get(i - 1));

            Point3D K2_v = eq.equationRightSide1(r.get(i - 1).add(K1_r.multiply(dt / 2.0)));
            Point3D K2_r = eq.equationRightSide2(v.get(i - 1).add(K1_v.multiply(dt / 2.0)));

            Point3D K3_v = eq.equationRightSide1(r.get(i - 1).add(K2_r.multiply(dt / 2.0)));
            Point3D K3_r = eq.equationRightSide2(v.get(i - 1).add(K2_v.multiply(dt / 2.0)));

            Point3D K4_v = eq.equationRightSide1(r.get(i - 1).add(K3_r.multiply(dt)));
            Point3D K4_r = eq.equationRightSide2(v.get(i - 1).add(K3_v.multiply(dt)));

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

            i++;
        } while (i < 50000);

        return new MaterialPointMovingData(r, v);
    }

    public static void main(String[] args) {
        MaterialPointEquation equation = new MaterialPointEquation(2, -2);
        RungeKutta4 solver = new RungeKutta4();
        MaterialPointMovingData data = solver.solve(equation,
                new Point3D(-300, 100, 0),
                new Point3D(2, 0, 0));

        System.out.println(data.getRadiusVector());
    }
}
