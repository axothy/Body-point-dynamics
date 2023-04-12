package ru.axothy.mechanics.equationsolvers;

import javafx.geometry.Point3D;
import ru.axothy.mechanics.bodypoint.BodyPointMovingData;
import ru.axothy.mechanics.motion.BodyPointEquation;
import ru.axothy.mechanics.tensors.Tensor;

import java.util.ArrayList;

public class EulerMethodForSystem {
    public static BodyPointMovingData solve(BodyPointEquation eq, Point3D r0, Point3D v0, Point3D w0) {
        ArrayList<Point3D> r = new ArrayList<>();
        ArrayList<Point3D> v = new ArrayList<>();
        ArrayList<Point3D> w = new ArrayList<>();
        ArrayList<Point3D> dw = new ArrayList<>();
        double dt = 0.1;

        r.add(r0);
        v.add(v0);
        w.add(w0);
        dw.add(w0);

        int i = 1;
        do {
            Point3D d2ydt20 = eq.equationRightSide(r.get(i - 1), dw.get(i - 1)); // --> r''(i-1)
            double rx = r.get(i - 1).getX() + dt * v.get(i - 1).getX();
            double ry = r.get(i - 1).getY() + dt * v.get(i - 1).getY();
            double rz = r.get(i - 1).getZ() + dt * v.get(i - 1).getZ();
            r.add(new Point3D(rx, ry, rz)); // --> r(i)

            double drx = v.get(i - 1).getX() + dt * eq.equationRightSide(r.get(i - 1), dw.get(i - 1)).getX();
            double dry = v.get(i - 1).getY() + dt * eq.equationRightSide(r.get(i - 1), dw.get(i - 1)).getY();
            double drz = v.get(i - 1).getZ() + dt * eq.equationRightSide(r.get(i - 1), dw.get(i - 1)).getZ();

            v.add(new Point3D(drx, dry, drz)); // --> r'(i)

            double wx = w.get(i - 1).getX() + dt * eq.equationRightSide2(d2ydt20, v.get(i - 1), w.get(i - 1)).getX();
            double wy = w.get(i - 1).getY() + dt * eq.equationRightSide2(d2ydt20, v.get(i - 1), w.get(i - 1)).getY();
            double wz = w.get(i - 1).getZ() + dt * eq.equationRightSide2(d2ydt20, v.get(i - 1), w.get(i - 1)).getZ();

            w.add(new Point3D(wx, wy, wz)); // --> w(i)

            //dx.add(x.get(i-1)); //v1: как оператор сдвига на такт назад
            //dx.add(h * g(x.get(i-1), dy.get(i-1), d2ydt20)); //v2: как h*g(i-1)
            //dx.add((x.get(i) - x.get(i - 1)) / 2.0); //как конечно разностное выражение

            double dwx = (w.get(i).getX() - w.get(i - 1).getX()) / 2.0;
            double dwy = (w.get(i).getY() - w.get(i - 1).getY()) / 2.0;
            double dwz = (w.get(i).getZ() - w.get(i - 1).getZ()) / 2.0;

            dw.add(new Point3D(dwx, dwy, dwz));

            i++;
        } while (i < 1000);

        return new BodyPointMovingData(r, v, w);
    }

    public static void main(String[] args) {
        Tensor J = new Tensor(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0);
        Tensor B = new Tensor(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0);

        BodyPointEquation equation = new BodyPointEquation(1.5, J, B, 10);
        EulerMethodForSystem solver = new EulerMethodForSystem();
        BodyPointMovingData data = solver.solve(equation,
                new Point3D(-300, 100, 0),
                new Point3D(2, 0, 0),
                new Point3D(2, 0, 0));

        System.out.println(data.getRadiusVector());
    }
}

