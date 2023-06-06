package ru.axothy.mechanics.equationsolvers;

import javafx.geometry.Point3D;
import ru.axothy.mechanics.bodypoint.BodyPointMovingData;
import ru.axothy.mechanics.motion.BodyPointEquationWithoutTensors;

import java.util.ArrayList;

import static ru.axothy.mechanics.equationsolvers.YoshidaMethod6thOrder.YoshidaCoefficients.*;

public class YoshidaMethod6thOrder {

    class YoshidaCoefficients {
        public static final double X0 = -(Math.pow(2.0, 1.0 / 3.0)) / (2.0 - Math.pow(2.0, 1.0 / 3.0));
        public static final double X1 = (1.0) / (2.0 - Math.pow(2, 1.0 / 3.0));

        public static final double Y0 = -(Math.pow(2.0, 1.0 / 5.0)) / (2.0 - Math.pow(2.0, 1.0 / 5.0));
        public static final double Y1 = (1.0) / (2.0 - Math.pow(2, 1.0 / 5.0));

        public static final double D1 = X1 * Y1;
        public static final double D3 = D1, D7 = D1, D9 = D1;

        public static final double D2 = X0 * Y1;
        public static final double D8 = D2;

        public static final double D4 = X1 * Y0;
        public static final double D6 = D4;

        public static final double D5 = X0 * Y0;

        public static final double C1 = D1 / 2;
        public static final double C10 = D9 / 2.0;
        public static final double C2 = (D1 + D2) / 2.0;
        public static final double C3 = (D2 + D3) / 2.0;
        public static final double C4 = (D3 + D4) / 2.0;
        public static final double C5 = (D4 + D5) / 2.0;
        public static final double C6 = (D5 + D6) / 2.0;
        public static final double C7 = (D6 + D7) / 2.0;
        public static final double C8 = (D7 + D8) / 2.0;
        public static final double C9 = (D8 + D9) / 2.0;

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
            //-> Radius-vector and velocity from Yoshida integration:
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

            Point3D r_third = new Point3D(
                    r_second.getX() + C3 * v_second.getX() * dt,
                    r_second.getY() + C3 * v_second.getY() * dt,
                    r_second.getZ() + C3 * v_second.getZ() * dt
            );

            Point3D v_third = new Point3D(
                    v_second.getX() + D3 * eq.equationRightSide1(r_third, v.get(i - 1), w.get(i - 1)).getX() * dt,
                    v_second.getY() + D3 * eq.equationRightSide1(r_third, v.get(i - 1), w.get(i - 1)).getY() * dt,
                    v_second.getZ() + D3 * eq.equationRightSide1(r_first, v.get(i - 1), w.get(i - 1)).getZ() * dt
            );

            Point3D w_third = new Point3D(
                    w_second.getX() + D3 * eq.equationRightSide2(r_third, v.get(i - 1), w.get(i - 1)).getX() * dt,
                    w_second.getY() + D3 * eq.equationRightSide2(r_third, v.get(i - 1), w.get(i - 1)).getY() * dt,
                    w_second.getZ() + D3 * eq.equationRightSide2(r_first, v.get(i - 1), w.get(i - 1)).getZ() * dt
            );

            Point3D r_fourth = new Point3D(
                    r_third.getX() + C4 * v_third.getX() * dt,
                    r_third.getY() + C4 * v_third.getY() * dt,
                    r_third.getZ() + C4 * v_third.getZ() * dt
            );

            Point3D v_fourth = new Point3D(
                    v_third.getX() + D4 * eq.equationRightSide1(r_fourth, v.get(i - 1), w.get(i - 1)).getX() * dt,
                    v_third.getY() + D4 * eq.equationRightSide1(r_fourth, v.get(i - 1), w.get(i - 1)).getY() * dt,
                    v_third.getZ() + D4 * eq.equationRightSide1(r_fourth, v.get(i - 1), w.get(i - 1)).getZ() * dt
            );

            Point3D w_fourth = new Point3D(
                    w_third.getX() + D4 * eq.equationRightSide2(r_fourth, v.get(i - 1), w.get(i - 1)).getX() * dt,
                    w_third.getY() + D4 * eq.equationRightSide2(r_fourth, v.get(i - 1), w.get(i - 1)).getY() * dt,
                    w_third.getZ() + D4 * eq.equationRightSide2(r_fourth, v.get(i - 1), w.get(i - 1)).getZ() * dt
            );

            Point3D r_fifth = new Point3D(
                    r_fourth.getX() + C5 * v_fourth.getX() * dt,
                    r_fourth.getY() + C5 * v_fourth.getY() * dt,
                    r_fourth.getZ() + C5 * v_fourth.getZ() * dt
            );

            Point3D v_fifth = new Point3D(
                    v_fourth.getX() + D5 * eq.equationRightSide1(r_fifth, v.get(i - 1), w.get(i - 1)).getX() * dt,
                    v_fourth.getY() + D5 * eq.equationRightSide1(r_fifth, v.get(i - 1), w.get(i - 1)).getY() * dt,
                    v_fourth.getZ() + D5 * eq.equationRightSide1(r_fifth, v.get(i - 1), w.get(i - 1)).getZ() * dt
            );

            Point3D w_fifth = new Point3D(
                    w_fourth.getX() + D5 * eq.equationRightSide2(r_fifth, v.get(i - 1), w.get(i - 1)).getX() * dt,
                    w_fourth.getY() + D5 * eq.equationRightSide2(r_fifth, v.get(i - 1), w.get(i - 1)).getY() * dt,
                    w_fourth.getZ() + D5 * eq.equationRightSide2(r_fifth, v.get(i - 1), w.get(i - 1)).getZ() * dt
            );

            Point3D r_sixth = new Point3D(
                    r_fifth.getX() + C6 * v_fifth.getX() * dt,
                    r_fifth.getY() + C6 * v_fifth.getY() * dt,
                    r_fifth.getZ() + C6 * v_fifth.getZ() * dt
            );

            Point3D v_sixth = new Point3D(
                    v_fifth.getX() + D6 * eq.equationRightSide1(r_sixth, v.get(i - 1), w.get(i - 1)).getX() * dt,
                    v_fifth.getY() + D6 * eq.equationRightSide1(r_sixth, v.get(i - 1), w.get(i - 1)).getY() * dt,
                    v_fifth.getZ() + D6 * eq.equationRightSide1(r_sixth, v.get(i - 1), w.get(i - 1)).getZ() * dt
            );

            Point3D w_sixth = new Point3D(
                    w_fifth.getX() + D6 * eq.equationRightSide2(r_sixth, v.get(i - 1), w.get(i - 1)).getX() * dt,
                    w_fifth.getY() + D6 * eq.equationRightSide2(r_sixth, v.get(i - 1), w.get(i - 1)).getY() * dt,
                    w_fifth.getZ() + D6 * eq.equationRightSide2(r_sixth, v.get(i - 1), w.get(i - 1)).getZ() * dt
            );

            Point3D r_seventh = new Point3D(
                    r_sixth.getX() + C7 * v_sixth.getX() * dt,
                    r_sixth.getY() + C7 * v_sixth.getY() * dt,
                    r_sixth.getZ() + C7 * v_sixth.getZ() * dt
            );

            Point3D v_seventh = new Point3D(
                    v_sixth.getX() + D7 * eq.equationRightSide1(r_seventh, v.get(i - 1), w.get(i - 1)).getX() * dt,
                    v_sixth.getY() + D7 * eq.equationRightSide1(r_seventh, v.get(i - 1), w.get(i - 1)).getY() * dt,
                    v_sixth.getZ() + D7 * eq.equationRightSide1(r_seventh, v.get(i - 1), w.get(i - 1)).getZ() * dt
            );

            Point3D w_seventh = new Point3D(
                    w_sixth.getX() + D7 * eq.equationRightSide2(r_seventh, v.get(i - 1), w.get(i - 1)).getX() * dt,
                    w_sixth.getY() + D7 * eq.equationRightSide2(r_seventh, v.get(i - 1), w.get(i - 1)).getY() * dt,
                    w_sixth.getZ() + D7 * eq.equationRightSide2(r_seventh, v.get(i - 1), w.get(i - 1)).getZ() * dt
            );

            Point3D r_eighth = new Point3D(
                    r_seventh.getX() + C8 * v_seventh.getX() * dt,
                    r_seventh.getY() + C8 * v_seventh.getY() * dt,
                    r_seventh.getZ() + C8 * v_seventh.getZ() * dt
            );

            Point3D v_eighth = new Point3D(
                    v_seventh.getX() + D8 * eq.equationRightSide1(r_eighth, v.get(i - 1), w.get(i - 1)).getX() * dt,
                    v_seventh.getY() + D8 * eq.equationRightSide1(r_eighth, v.get(i - 1), w.get(i - 1)).getY() * dt,
                    v_seventh.getZ() + D8 * eq.equationRightSide1(r_eighth, v.get(i - 1), w.get(i - 1)).getZ() * dt
            );

            Point3D w_eighth = new Point3D(
                    w_seventh.getX() + D8 * eq.equationRightSide2(r_eighth, v.get(i - 1), w.get(i - 1)).getX() * dt,
                    w_seventh.getY() + D8 * eq.equationRightSide2(r_eighth, v.get(i - 1), w.get(i - 1)).getY() * dt,
                    w_seventh.getZ() + D8 * eq.equationRightSide2(r_eighth, v.get(i - 1), w.get(i - 1)).getZ() * dt
            );

            Point3D r_ninth = new Point3D(
                    r_eighth.getX() + C9 * v_eighth.getX() * dt,
                    r_eighth.getY() + C9 * v_eighth.getY() * dt,
                    r_eighth.getZ() + C9 * v_eighth.getZ() * dt
            );

            Point3D v_ninth = new Point3D(
                    v_eighth.getX() + D9 * eq.equationRightSide1(r_ninth, v.get(i - 1), w.get(i - 1)).getX() * dt,
                    v_eighth.getY() + D9 * eq.equationRightSide1(r_ninth, v.get(i - 1), w.get(i - 1)).getY() * dt,
                    v_eighth.getZ() + D9 * eq.equationRightSide1(r_ninth, v.get(i - 1), w.get(i - 1)).getZ() * dt
            );

            Point3D w_ninth = new Point3D(
                    w_eighth.getX() + D9 * eq.equationRightSide2(r_ninth, v.get(i - 1), w.get(i - 1)).getX() * dt,
                    w_eighth.getY() + D9 * eq.equationRightSide2(r_ninth, v.get(i - 1), w.get(i - 1)).getY() * dt,
                    w_eighth.getZ() + D9 * eq.equationRightSide2(r_ninth, v.get(i - 1), w.get(i - 1)).getZ() * dt
            );

            Point3D r_tenth = new Point3D(
                    r_ninth.getX() + C10 * v_ninth.getX() * dt,
                    r_ninth.getY() + C10 * v_ninth.getY() * dt,
                    r_ninth.getZ() + C10 * v_ninth.getZ() * dt
            );

            Point3D v_tenth = new Point3D(
                    v_ninth.getX(),
                    v_ninth.getY(),
                    v_ninth.getZ()
            );

            Point3D w_tenth = new Point3D(
                    w_ninth.getX(),
                    w_ninth.getY(),
                    w_ninth.getZ()
            );

            r.add(r_tenth);
            v.add(v_tenth);
            w.add(w_tenth);

            i++;
        } while (i < n);

        return new BodyPointMovingData(r, v, w);
    }

    public static void main(String[] args) {
        System.out.println(D1);
        System.out.println(D2);
        System.out.println(D3);
        System.out.println(D4);
        System.out.println(D5);
        System.out.println(D6);
        System.out.println(D7);
        System.out.println(D8);
        System.out.println(D9);
        /*
        Tensor J = Tensor.UNARY_TENSOR;
        Tensor B = Tensor.UNARY_TENSOR;

        BodyPointEquationWithoutTensors equation = new BodyPointEquationWithoutTensors(1.5, J, B, 10);
        YoshidaMethodForSystem solver = new YoshidaMethodForSystem();
        BodyPointMovingData data = solver.solve(equation,
                new Point3D(-300, 100, 0),
                new Point3D(2, 0, 0),
                new Point3D(2, 0, 0));

        System.out.println(data.getRadiusVector());

         */
    }
}
