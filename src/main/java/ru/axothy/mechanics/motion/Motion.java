package ru.axothy.mechanics.motion;

import javafx.geometry.Point3D;
import ru.axothy.mechanics.bodypoint.BodyPoint;
import ru.axothy.mechanics.bodypoint.BodyPointMovingData;
import ru.axothy.mechanics.bodypoint.MaterialPoint;
import ru.axothy.mechanics.bodypoint.MaterialPointMovingData;

import java.util.ArrayList;

public interface Motion {
    BodyPointMovingData getData();
    void startMotion(double step, int numberOfIterations);
    BodyPoint getMotionPoint();

    double getCoulombConstant();
}
