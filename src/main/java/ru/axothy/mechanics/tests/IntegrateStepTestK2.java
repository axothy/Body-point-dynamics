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

public class IntegrateStepTestK2 {

    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writerH= new PrintWriter("h.txt", "UTF-8");
        PrintWriter writerK2 = new PrintWriter("Yo6_K2.txt", "UTF-8");

        BodyPointMotion motion = new BodyPointMotion();
        motion.setCoulombConstant(-0.5);
        motion.setBodyPoint(new BodyPoint(1, Tensor.UNARY_TENSOR, Tensor.UNARY_TENSOR.multiply(0.7)));

        motion.setInitialVelocityTrans(new Point3D(7.058, 0, 6.86));
        motion.setInitialVelocityRot(new Point3D(-2.941, 0, -9.8));
        motion.setInitialRadiusVector(new Point3D(-200, 49, 0));

        double h = 0.01;
        int timeOfModeling = 25;
        do {
            int n = (int) (25.0 / h);
            motion.startMotion(h, n);

            BodyPointEnergy energy = new BodyPointEnergy();
            energy.setMaterialPoint(motion.getMotionPoint());
            energy.setCoulombConst(motion.getCoulombConstant());
            energy.setMovingData(motion.getData());

            ArrayList<Double> errors = energy.getRelativeDev();
            ArrayList<Double> K2Errors = energy.getRelativeDevK2();

            System.out.println("Energy relative: " + Collections.max(errors) + ", K2^2 =" +
                    Collections.max(K2Errors) + ", h = " + h);


            writerK2.println(Collections.max(K2Errors));
            writerH.println(h);

            h /= 2;
        } while (h > 0.000005);

        writerH.close();
        writerK2.close();
    }
}
