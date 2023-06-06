package ru.axothy.mechanics.energy;

import javafx.geometry.Point3D;

import java.util.ArrayList;

/**
 * Full energy of movement in coulomb or gravitational field
 */

//Make as abstract class..
public interface Energy {
    ArrayList<Double> getEnergy();
    ArrayList<Double> getRelativeDev();

    default double getPotentialEnergy(Point3D radiusVector, double constant) {
        return (-constant) / (Math.sqrt(radiusVector.getX() * radiusVector.getX() + radiusVector.getY() * radiusVector.getY() +
                radiusVector.getZ() * radiusVector.getZ()));
    }

    default double getMeanEnergy(ArrayList<Double> energies, long numberOfIterations) {
        double sum = 0;
        for (Double energy : energies) {
            sum += energy;
        }

        return (sum / numberOfIterations);
    }

    /**
     * Standart deviation of energy
     */
    default double stDev(ArrayList<Double> energies, long numberOfIterations) {
        double meanEnergy = getMeanEnergy(energies, numberOfIterations);
        double result = 0;

        for (Double energy : energies) {
            result += (energy - meanEnergy) * (energy - meanEnergy);
        }
        result = result / numberOfIterations;
        result = Math.sqrt(result);

        return result;
    }
}
