package ru.axothy.mechanics.energy;

import javafx.geometry.Point3D;
import ru.axothy.mechanics.bodypoint.BodyPoint;
import ru.axothy.mechanics.bodypoint.BodyPointMovingData;
import ru.axothy.mechanics.bodypoint.MaterialPoint;
import ru.axothy.mechanics.bodypoint.MaterialPointMovingData;

import java.util.ArrayList;

public class BodyPointEnergy implements Energy {
    private BodyPoint bodyPoint;
    private ArrayList<Double> energy;
    private BodyPointMovingData movement;
    private double coulombConst;

    public BodyPointEnergy() {
        energy = new ArrayList<>();
    }

    BodyPointEnergy(BodyPoint bodyPoint, BodyPointMovingData movement, double coulombConst) {
        this.movement = movement;
        this.bodyPoint = bodyPoint;
        this.coulombConst = coulombConst;
        energy = new ArrayList<>();
    }

    public void setMovingData(BodyPointMovingData movement) {
        this.movement = movement;
    }

    public void setMaterialPoint(BodyPoint bodyPoint) {
        this.bodyPoint = bodyPoint;
    }

    public void setCoulombConst(double A) {
        coulombConst = A;
    }

    /**
     * E = K + U = 1/2m*v*v + Bv*w + 1/2Jw*w - A/r
     */
    @Override
    public ArrayList<Double> getEnergy() {
        ArrayList<Point3D> radiusVector = movement.getRadiusVector();
        ArrayList<Point3D> transVelocity = movement.getVelocityTrans();
        ArrayList<Point3D> angularVelocity = movement.getVelocityRot();

        int i = 0;
        for (Point3D point : radiusVector) {
            double kinetic = getKineticEnergy(bodyPoint.getMass(), transVelocity.get(i), angularVelocity.get(i));
            double potential = getPotentialEnergy(point, coulombConst);
            energy.add(kinetic + potential);
            i++;
        }

        return energy;
    }

    public ArrayList<Double> getK2() {
        ArrayList<Point3D> transVelocity = movement.getVelocityTrans();
        ArrayList<Point3D> angularVelocity = movement.getVelocityRot();
        ArrayList<Double> K2list = new ArrayList<>();

        int i = 0;
        for (Point3D point : transVelocity) {
            Point3D K2 = (bodyPoint.getB().dotProduct(point).add(bodyPoint.getJ().dotProduct(angularVelocity.get(i))));

            K2list.add(K2.dotProduct(K2));
            i++;
        }

        return K2list;
    }

    public ArrayList<Double> getRelativeDevK2() {
        ArrayList<Double> K2list = getK2();
        ArrayList<Double> errors = new ArrayList<>();
        double startK2;

        startK2 = K2list.get(0);
        for (Double number : K2list) {
            errors.add(Math.abs(startK2 - number) / (startK2));
        }

        return errors;
    }

    @Override
    public ArrayList<Double> getRelativeDev() {
        ArrayList<Double> errors = new ArrayList<>();
        double startEnergy;

        if (energy.isEmpty()) {
            getEnergy();
        }

        startEnergy = energy.get(0);

        for (Double number : energy) {
            errors.add(Math.abs(startEnergy - number) / (startEnergy));
        }

        return errors;
    }

    /**
     * K = 1/2 m * v * v + Bv * w + 1/2 Jw*w
     * @return Kinetic energy of body-point
     */
    private double getKineticEnergy(double mass, Point3D transVelocity, Point3D angularVelocity) {
        return (1.0 / 2.0) * mass * transVelocity.dotProduct(transVelocity)  +
                bodyPoint.getB().dotProduct(transVelocity).dotProduct(angularVelocity) +
                (1.0 / 2.0) * bodyPoint.getJ().dotProduct(angularVelocity).dotProduct(angularVelocity);
    }

}
