package ru.axothy.mechanics.equationsolvers;

import java.util.ArrayList;

/**
 * Зная 4 НУ, решаем систему:
 * d^2y/dt^2 = -y - dx/dt = f
 * dx/dt = x*dy/dt - d^2y/dt^2 = g
 * <p>
 * i=0: находим y’’0 из 1-го уравнения прямой подстановкой НУ.
 * Находим y1, y’1 решая как систему по Эйлеру.
 * <p>
 * Находим x1 по Эйлеру.
 * <p>
 * i=1: находим y’’1 из 1-го прямой подстановкой y1, а dx/dt1 заменить h*f0. (Или на x0?).
 * Находим y2, y’2 Эйлером.
 * Находим x2 Эйлером.
 * <p>
 * i=2: находим y’’2 подстановкой y2 и заменяя dx/dt2 на h*f1 (или на x1?)
 * Находим y3, y’3 Эйлером и x3 тоже.
 * <p>
 * …
 * i=k:
 * (1) -> y’’k
 * (1) -> y’k+1, yk+1
 * (2) -> xk+1
 */
public class EulerMethodTest {
    public static final double h = 0.1;
    public static ArrayList<Double> y = new ArrayList<>();
    public static ArrayList<Double> dy = new ArrayList<>();
    public static ArrayList<Double> x = new ArrayList<>();
    public static ArrayList<Double> dx = new ArrayList<>();

    public static double x0 = 0, dx0 = 0, y0 = 1, dy0 = 1;

    public static double f(double y, double dxdt) {
        return -y - dxdt; //d^2y/dt^2
    }

    public static double g(double x, double dydt, double d2ydt2) {
        return x * dydt - d2ydt2; //dx/dt
    }

    public static void solve() {
        y.add(y0);
        dy.add(dy0);
        x.add(x0);
        dx.add(dx0);

        int i = 1;
        do {
            double d2ydt20 = f(y.get(i - 1), dx.get(i - 1)); // --> y''(i-1)
            y.add(y.get(i - 1) + h * dy.get(i - 1)); // y(i) = y(i-1) + h * y'(i-1) --> y(i)
            dy.add(dy.get(i - 1) + h * f(y.get(i - 1), dx.get(i - 1))); // y'(i) = y'(i-1) + h * f(i-1) --> y'(i)

            x.add(x.get(i - 1) + h * g(x.get(i - 1), dy.get(i - 1), d2ydt20)); // --> x(i)
            //dx.add(x.get(i-1)); //v1: как оператор сдвига на такт назад
            //dx.add(h * g(x.get(i-1), dy.get(i-1), d2ydt20)); //v2: как h*g(i-1)
            dx.add((x.get(i) - x.get(i - 1)) / 2.0); //как конечно разностное выражение

            i++;
        } while (i < 100);
    }

    public static void main(String[] args) {
        EulerMethodTest.solve();

        System.out.println(EulerMethodTest.y);
        System.out.println(EulerMethodTest.dy);
        System.out.println(EulerMethodTest.x);
    }

}
