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

public class K1KzTest {
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

        PrintWriter writerKz = new PrintWriter("Kz.txt", "UTF-8");
        PrintWriter writerEnergy = new PrintWriter("Euler_Energy.txt", "UTF-8");

        BodyPointMotion motion = new BodyPointMotion();
        motion.setCoulombConstant(-0.5);
        motion.setBodyPoint(new BodyPoint(1, Tensor.UNARY_TENSOR, Tensor.UNARY_TENSOR.multiply(0.7)));


        double K1 = 20;
        double Kz = 20;
        double Kx = 2; //start value
        motion.setInitialRadiusVector(new Point3D(-200, 49, 0));
        //do {

            motion.setInitialVelocityTrans(getV(K1, Kz, Kx));
            motion.setInitialVelocityRot(getW(K1, Kz, Kx));
            motion.startMotion(0.0001, 250000);

            BodyPointEnergy energy = new BodyPointEnergy();
            energy.setMaterialPoint(motion.getMotionPoint());
            energy.setCoulombConst(motion.getCoulombConstant());
            energy.setMovingData(motion.getData());

            saveDataToFile(motion.getData().getRadiusVector());

            ArrayList<Double> errors = energy.getRelativeDev();
            System.out.println("Energy relative: " + Collections.max(errors) + ", Kz = " + Kz + ", K1 = " + K1);
            writerEnergy.println(Collections.max(errors));
            writerKz.println(K1);

            Kz = Kz + 0.1;
            K1 = K1 + 0.1;
       // } while (Kz < 10);

        writerKz.close();
        writerEnergy.close();
    }
}
