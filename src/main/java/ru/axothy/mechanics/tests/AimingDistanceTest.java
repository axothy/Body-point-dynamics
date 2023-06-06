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

public class AimingDistanceTest {
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

    public static void saveEnergy(List<Double> energy, String filename) throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writerE = new PrintWriter(filename, "UTF-8");
        PrintWriter writerI = new PrintWriter("I.txt", "UTF-8");
        int i = 0;
        for (Double value : energy) {
            writerE.println(value);
            writerI.println(i);
            i++;
        }

        writerE.close();
        writerI.close();
    }

    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writerD = new PrintWriter("D.txt", "UTF-8");
        PrintWriter writerK2 = new PrintWriter("Yo6_K2.txt", "UTF-8");

        BodyPointMotion motion = new BodyPointMotion();
        motion.setCoulombConstant(-0.5);
        motion.setBodyPoint(new BodyPoint(1, Tensor.UNARY_TENSOR, Tensor.UNARY_TENSOR.multiply(0.7)));

        motion.setInitialVelocityTrans(new Point3D(7.058, 0, 6.86));
        motion.setInitialVelocityRot(new Point3D(-2.941, 0, -9.8));

        double aimingDistance = 5.0;
        do {

            motion.setInitialRadiusVector(new Point3D(-200, aimingDistance - 1.0, 0));
            motion.startMotion(0.0001, 250000);

            //saveDataToFile(motion.getData().getRadiusVector());

            BodyPointEnergy energy = new BodyPointEnergy();
            energy.setMaterialPoint(motion.getMotionPoint());
            energy.setCoulombConst(motion.getCoulombConstant());
            energy.setMovingData(motion.getData());

            ArrayList<Double> errors = energy.getRelativeDev();
            ArrayList<Double> K2Errors = energy.getRelativeDevK2();

            System.out.println("Energy relative: " + Collections.max(errors) + ", K2^2 =" +
                    Collections.max(K2Errors) + ", D = " + aimingDistance);


            writerK2.println(Collections.max(K2Errors));
            writerD.println(aimingDistance);

            aimingDistance++;
        } while (aimingDistance < 200);

        writerD.close();
        writerK2.close();
    }
}
