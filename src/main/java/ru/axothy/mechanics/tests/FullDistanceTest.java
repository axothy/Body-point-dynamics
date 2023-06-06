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

public class FullDistanceTest {

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
        PrintWriter writerD = new PrintWriter("L.txt", "UTF-8");
        PrintWriter writerEnergy = new PrintWriter("Yo6_Energy.txt", "UTF-8");

        BodyPointMotion motion = new BodyPointMotion();
        motion.setCoulombConstant(-0.5);
        motion.setBodyPoint(new BodyPoint(1, Tensor.UNARY_TENSOR, Tensor.UNARY_TENSOR.multiply(0.7)));

        motion.setInitialVelocityTrans(new Point3D(7.058, 0, 6.86));
        motion.setInitialVelocityRot(new Point3D(-2.941, 0, -9.8));

        double fullDistance = 1500.0;
        //do {
            motion.setInitialRadiusVector(new Point3D(-fullDistance, 49, 0));
            motion.startMotion(0.0001, 250000);

            saveDataToFile(motion.getData().getRadiusVector());
            BodyPointEnergy energy = new BodyPointEnergy();
            energy.setMaterialPoint(motion.getMotionPoint());
            energy.setCoulombConst(motion.getCoulombConstant());
            energy.setMovingData(motion.getData());

            ArrayList<Double> errors = energy.getRelativeDev();
            System.out.println("Energy relative: " + Collections.max(errors) + ", L = " + fullDistance);
            writerEnergy.println(Collections.max(errors));
            writerD.println(fullDistance);

            fullDistance = fullDistance + 5;
        //} while (fullDistance < 1005);

        writerD.close();
        writerEnergy.close();
    }
}

