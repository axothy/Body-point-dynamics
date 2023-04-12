package ru.axothy.mechanics.bodypoint;

public class MaterialPoint {
    protected double mass;

    protected MaterialPoint() {

    }

    public MaterialPoint(double mass) {
        this.mass = mass;
    }

    public double getMass() {
        return mass;
    }

    @Override
    public String toString() {
        return "Material point with mass: " + mass;
    }
}
