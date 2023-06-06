package ru.axothy.mechanics.equationsolvers;

import javafx.geometry.Point3D;
import ru.axothy.mechanics.bodypoint.BodyPointMovingData;
import ru.axothy.mechanics.bodypoint.MaterialPointMovingData;
import ru.axothy.mechanics.motion.BodyPointEquationWithoutTensors;
import ru.axothy.mechanics.motion.MaterialPointEquation;
import ru.axothy.mechanics.tensors.Tensor;

import java.util.ArrayList;

import static ru.axothy.mechanics.equationsolvers.YoshidaMethod.YoshidaCoefficients.*;

public class YoshidaMethod {

    class YoshidaCoefficients {
        public static final double W0 = -(Math.pow(2.0, 1.0 / 3.0)) / (2.0 - Math.pow(2.0, 1.0 / 3.0));
        public static final double W1 = (1.0) / (2.0 - Math.pow(2, 1.0 / 3.0));
        public static final double C1 = W1 / 2.0;
        public static final double C4 = W1 / 2.0;
        public static final double C2 = (W0 + W1) / (2.0);
        public static final double C3 = (W0 + W1) / (2.0);
        public static final double D1 = W1;
        public static final double D3 = W1;
        public static final double D2 = W0;

    }

    public static MaterialPointMovingData solve(MaterialPointEquation eq, Point3D r0, Point3D v0) {
        ArrayList<Point3D> r = new ArrayList<>();
        ArrayList<Point3D> v = new ArrayList<>();
        double dt = 0.0001;

        r.add(r0);
        v.add(v0);

        int i = 1;
        do {
            //-> Radius-vector and velocity from Yoshida integration:
            Point3D r_first = new Point3D(
                    r.get(i - 1).getX() + C1 * v.get(i - 1).getX() * dt,
                    r.get(i - 1).getY() + C1 * v.get(i - 1).getY() * dt,
                    r.get(i - 1).getZ() + C1 * v.get(i - 1).getZ() * dt
            );

            Point3D v_first = new Point3D(
                    v.get(i - 1).getX() + D1 * eq.equationRightSide1(r_first).getX() * dt,
                    v.get(i - 1).getY() + D1 * eq.equationRightSide1(r_first).getY() * dt,
                    v.get(i - 1).getZ() + D1 * eq.equationRightSide1(r_first).getZ() * dt
            );

            Point3D r_second = new Point3D(
                    r_first.getX() + C2 * v_first.getX() * dt,
                    r_first.getY() + C2 * v_first.getY() * dt,
                    r_first.getZ() + C2 * v_first.getZ() * dt
            );

            Point3D v_second = new Point3D(
                    v_first.getX() + D2 * eq.equationRightSide1(r_second).getX() * dt,
                    v_first.getY() + D2 * eq.equationRightSide1(r_second).getY() * dt,
                    v_first.getZ() + D2 * eq.equationRightSide1(r_second).getZ() * dt
            );

            Point3D r_third = new Point3D(
                    r_second.getX() + C3 * v_second.getX() * dt,
                    r_second.getY() + C3 * v_second.getY() * dt,
                    r_second.getZ() + C3 * v_second.getZ() * dt
            );

            Point3D v_third = new Point3D(
                    v_second.getX() + D3 * eq.equationRightSide1(r_third).getX() * dt,
                    v_second.getY() + D3 * eq.equationRightSide1(r_third).getY() * dt,
                    v_second.getZ() + D3 * eq.equationRightSide1(r_first).getZ() * dt
            );

            Point3D r_fourth = new Point3D(
                    r_third.getX() + C4 * v_third.getX() * dt,
                    r_third.getY() + C4 * v_third.getY() * dt,
                    r_third.getZ() + C4 * v_third.getZ() * dt
            );

            Point3D v_fourth = new Point3D(
                    v_third.getX(),
                    v_third.getY(),
                    v_third.getZ()
            );

            r.add(r_fourth);
            v.add(v_fourth);


            i++;
        } while (i < 250000);

        return new MaterialPointMovingData(r, v);
    }

    public static void main(String[] args) {
        MaterialPointEquation equation = new MaterialPointEquation(1.5, 10);
        YoshidaMethod solver = new YoshidaMethod();
        MaterialPointMovingData data = solver.solve(equation,
                new Point3D(-300, 100, 0),
                new Point3D(2, 0, 0));

        System.out.println(data.getRadiusVector());
    }
}
