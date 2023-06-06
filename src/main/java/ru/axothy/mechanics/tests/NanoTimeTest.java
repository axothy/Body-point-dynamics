package ru.axothy.mechanics.tests;

import javafx.geometry.Point3D;
import ru.axothy.mechanics.bodypoint.BodyPoint;
import ru.axothy.mechanics.energy.BodyPointEnergy;
import ru.axothy.mechanics.motion.BodyPointMotion;
import ru.axothy.mechanics.tensors.Tensor;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class NanoTimeTest {

    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writerN = new PrintWriter("N.txt", "UTF-8");
        PrintWriter writerT = new PrintWriter("Yo6_Time.txt", "UTF-8");

        BodyPointMotion motion = new BodyPointMotion();
        motion.setCoulombConstant(-0.5);
        motion.setBodyPoint(new BodyPoint(1, Tensor.UNARY_TENSOR, Tensor.UNARY_TENSOR.multiply(0.7)));

        motion.setInitialVelocityTrans(new Point3D(7.058, 0, 6.86));
        motion.setInitialVelocityRot(new Point3D(-2.941, 0, -9.8));
        motion.setInitialRadiusVector(new Point3D(-200, 49, 0));

        int n = 500;
        double h = 0.0001;
        do {

            long start = System.nanoTime();
            motion.startMotion(h, n);
            long end = System.nanoTime();

            //System.out.println("Time: " + (end - start) + ", N = " + n);
            writerT.println(end-start);
            writerN.println(n);

            n = n + 500;
        } while (n < 250000);

        writerN.close();
        writerT.close();
    }
}
