package ru.axothy.mechanics.equationsolvers;

import javafx.geometry.Point3D;
import ru.axothy.mechanics.bodypoint.MaterialPointMovingData;
import ru.axothy.mechanics.motion.Equation;
import ru.axothy.mechanics.tensors.Tensor;

import java.util.ArrayList;

public class EulerMethod {
    public static MaterialPointMovingData solve(Equation equation, Point3D r0, Point3D v0) {
        ArrayList<Point3D> result = new ArrayList<>();
        result.add(r0);

        double x = r0.getX();
        double y = r0.getY();
        double dx = v0.getX();
        double dy = v0.getY();

        // Задаем шаг по времени
        double dt = 0.1;

        // Вычисляем значения производных и новые значения x и y для каждого шага по времени
        for (double t = 0; t < 25000; t += dt) {
            // Вычисляем значения производных
            double d2x = equation.equationRightSide(new Point3D(x, y, Point3D.ZERO.getZ())).getX();
            double d2y = equation.equationRightSide(new Point3D(x, y, Point3D.ZERO.getZ())).getY();

            // Вычисляем новые значения x и y
            double x_new = x + dt * dx;
            double y_new = y + dt * dy;

            // Обновляем значения x, y, dx/dt и dy/dt для следующего шага по времени
            x = x_new;
            y = y_new;
            dx += dt * d2x;
            dy += dt * d2y;

            result.add(new Point3D(x, y, 0));
        }

        return new MaterialPointMovingData(result);
    }
}
