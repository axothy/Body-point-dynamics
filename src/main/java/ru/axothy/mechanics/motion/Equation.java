package ru.axothy.mechanics.motion;

import javafx.geometry.Point3D;
import ru.axothy.mechanics.bodypoint.MaterialPoint;
import ru.axothy.mechanics.tensors.Tensor;

public interface Equation {
    Point3D equationRightSide(Point3D radiusVector);
}
