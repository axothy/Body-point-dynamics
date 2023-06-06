package ru.axothy.mechanics.equationsolvers;

import javafx.geometry.Point3D;
import ru.axothy.mechanics.bodypoint.MaterialPointMovingData;
import ru.axothy.mechanics.motion.Equation;
import ru.axothy.mechanics.motion.MaterialPointEquation;
import ru.axothy.mechanics.tensors.Tensor;

import java.util.ArrayList;

public class EulerMethod {
    public static MaterialPointMovingData solve(MaterialPointEquation equation, Point3D r0, Point3D v0) {
        ArrayList<Point3D> v = new ArrayList<>();
        ArrayList<Point3D> r = new ArrayList<>();
        r.add(r0);
        v.add(v0);

        double dt = 0.0001;

        int i = 1;
        do {
            double vX = v.get(i - 1).getX() + dt * equation.equationRightSide1(r.get(i - 1)).getX();
            double vY = v.get(i - 1).getY() + dt * equation.equationRightSide1(r.get(i - 1)).getY();
            v.add(new Point3D(vX, vY, Point3D.ZERO.getZ()));

            double rX = r.get(i - 1).getX() + dt * equation.equationRightSide2(v.get(i - 1)).getX();
            double rY = r.get(i - 1).getY() + dt * equation.equationRightSide2(v.get(i - 1)).getY();
            r.add(new Point3D(rX, rY, Point3D.ZERO.getZ()));

            i++;
        } while (i < 250000);

        return new MaterialPointMovingData(r, v);
    }
}
