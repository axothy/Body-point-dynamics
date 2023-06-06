package ru.axothy.mechanics.energy;

import javafx.geometry.Point3D;
import ru.axothy.mechanics.bodypoint.MaterialPoint;
import ru.axothy.mechanics.bodypoint.MaterialPointMovingData;

import java.util.ArrayList;

public class MaterialPointEnergy implements Energy {
    private MaterialPoint materialPoint;
    private ArrayList<Double> energy;
    private MaterialPointMovingData movement;
    private double coulombConst;

    public MaterialPointEnergy() {
        energy = new ArrayList<>();
    }

    MaterialPointEnergy(MaterialPoint materialPoint, MaterialPointMovingData movement, double coulombConst) {
        this.movement = movement;
        this.materialPoint = materialPoint;
        this.coulombConst = coulombConst;
        energy = new ArrayList<>();
    }

    public void setMovingData(MaterialPointMovingData movement) {
        this.movement = movement;
    }

    public void setMaterialPoint(MaterialPoint materialPoint) {
        this.materialPoint = materialPoint;
    }

    public void setCoulombConst(double A) {
        this.coulombConst = A;
    }

    /**
     * E = K + U = mv^2/2 - A/r
     */
    @Override
    public ArrayList<Double> getEnergy() {
        ArrayList<Point3D> radiusVector = movement.getRadiusVector();
        ArrayList<Point3D> velocity = movement.getVelocity();
        double m = materialPoint.getMass();

        int i = 0;
        for (Point3D point : radiusVector) {
            double kinetic = getKineticEnergy(m, velocity.get(i));
            double potential = getPotentialEnergy(point, coulombConst);
            energy.add(kinetic + potential);
            i++;
        }

        return energy;
    }

    public ArrayList<Double> momentum() {
        ArrayList<Point3D> radiusVector = movement.getRadiusVector();
        ArrayList<Point3D> velocity = movement.getVelocity();

        ArrayList<Double> momentumZ = new ArrayList<>();

        int i = 0;
        for (Point3D point : radiusVector) {
            momentumZ.add(point.getX() * velocity.get(i).getY() - point.getY() * velocity.get(i).getX());
            i++;
        }

        return momentumZ;
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

    private double getKineticEnergy(double mass, Point3D velocity) {
        return (1.0 / 2.0) * mass * (velocity.getX() * velocity.getX() + velocity.getY() * velocity.getY());

    }
}
