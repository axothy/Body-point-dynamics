package ru.axothy.mechanics.bodypoint;

import ru.axothy.mechanics.tensors.Tensor;

/**
 * Body-point with inertia tensors J and B
 */
public final class BodyPoint extends MaterialPoint {
    private Tensor J;
    private Tensor B;

    private BodyPoint() {
        throw new AssertionError();
    }

    public BodyPoint(double mass) {
        super(mass);
    }

    public BodyPoint(double mass, Tensor J, Tensor B) {
        super(mass);
        this.J = J;
        this.B = B;
    }

    public void setInertiaTensors(Tensor J, Tensor B) {
        this.J = J;
        this.B = B;
    }

    public Tensor getJ() {
        return J;
    }

    public void setJ(Tensor tensor) {
        J = tensor;
    }

    public Tensor getB() {
        return B;
    }

    public void setB(Tensor tensor) {
        B = tensor;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Body-point with mass and inertia tensors J and B:\n");
        stringBuilder.append("Mass: " + mass + "\n");
        stringBuilder.append(J.toString());
        stringBuilder.append(B.toString());

        return stringBuilder.toString();
    }


}
