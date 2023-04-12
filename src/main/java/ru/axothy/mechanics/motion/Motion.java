package ru.axothy.mechanics.motion;

import javafx.geometry.Point3D;
import ru.axothy.mechanics.bodypoint.MaterialPointMovingData;

import java.util.ArrayList;

public interface Motion {
    MaterialPointMovingData getData();
    void startMotion();
}
