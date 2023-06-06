package ru.axothy.mechanics.tests;

import javafx.geometry.Point3D;
import ru.axothy.mechanics.bodypoint.BodyPoint;
import ru.axothy.mechanics.energy.BodyPointEnergy;
import ru.axothy.mechanics.motion.BodyPointMotion;
import ru.axothy.mechanics.tensors.Tensor;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KxTest {
    private static double m = 1;
    private static double b = 0.7;
    private static double j = 1;

    public static Point3D getV(double K1, double Kz, double Kx) {
        double vX = (b * Kx - j * K1) / (b * b - m * j);
        double vY = 0;
        double vZ = -(b * Kz) / (b * b - m * j);

        return new Point3D(vX, vY, vZ);
    }

    public static Point3D getW(double K1, double Kz, double Kx) {
        double wX = (b * K1 - m * Kx) / (b * b - m * j);
        double wY = 0;
        double wZ = (m * Kz) / (b * b - m * j);

        return new Point3D(wX, wY, wZ);
    }

    public static void saveDataToFile(List<Point3D> points) throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writerX = new PrintWriter("X.txt", "UTF-8");
        PrintWriter writerY = new PrintWriter("Y.txt", "UTF-8");
        PrintWriter writerZ = new PrintWriter("Z.txt", "UTF-8");
        for (Point3D point : points) {
            writerX.println(point.getX());
            writerY.println(point.getY());
            writerZ.println(point.getZ());
        }

        writerX.close();
        writerY.close();
        writerZ.close();
    }

    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {

        PrintWriter writerKx = new PrintWriter("Kx.txt", "UTF-8");
        PrintWriter writerEnergy = new PrintWriter("RK4_Energy.txt", "UTF-8");
        PrintWriter writerK2 = new PrintWriter("RK4_K2.txt", "UTF-8");

        BodyPointMotion motion = new BodyPointMotion();
        motion.setCoulombConstant(-0.5);
        motion.setBodyPoint(new BodyPoint(m, Tensor.UNARY_TENSOR, Tensor.UNARY_TENSOR.multiply(0.7)));


        double K1 = 5;
        double Kz = 5;
        double Kx = -10; //start value
        motion.setInitialRadiusVector(new Point3D(-200, 49, 0));
        do {

            motion.setInitialVelocityTrans(getV(K1, Kz, Kx));
            motion.setInitialVelocityRot(getW(K1, Kz, Kx));
            motion.startMotion(0.0001, 250000);

            BodyPointEnergy energy = new BodyPointEnergy();
            energy.setMaterialPoint(motion.getMotionPoint());
            energy.setCoulombConst(motion.getCoulombConstant());
            energy.setMovingData(motion.getData());

            //saveDataToFile(motion.getData().getRadiusVector());

            ArrayList<Double> errors = energy.getRelativeDev();
            ArrayList<Double> K2errors = energy.getRelativeDevK2();
            System.out.println("Energy relative: " + Collections.max(errors) + ", K2 = " + Collections.max(K2errors) +
                    ", Kx = " + Kx);
            writerEnergy.println(Collections.max(errors));
            writerK2.println(Collections.max(K2errors));
            writerKx.println(Kx);

            Kx = Kx + 0.1;
        } while (Kx < 10.1);

        writerKx.close();
        writerEnergy.close();
        writerK2.close();
    }
}