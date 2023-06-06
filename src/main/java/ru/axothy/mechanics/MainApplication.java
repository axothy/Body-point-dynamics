package ru.axothy.mechanics;

import javafx.animation.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import ru.axothy.mechanics.bodypoint.BodyPoint;
import ru.axothy.mechanics.bodypoint.BodyPointMovingData;
import ru.axothy.mechanics.bodypoint.MaterialPoint;
import ru.axothy.mechanics.bodypoint.MaterialPointMovingData;
import ru.axothy.mechanics.energy.BodyPointEnergy;
import ru.axothy.mechanics.energy.MaterialPointEnergy;
import ru.axothy.mechanics.graphics.CoulombCenterGraphics;
import ru.axothy.mechanics.graphics.MouseLook;
import ru.axothy.mechanics.graphics.PointGraphics;
import ru.axothy.mechanics.graphics.PolyLine3DCustom;
import ru.axothy.mechanics.motion.BodyPointMotion;
import ru.axothy.mechanics.motion.MaterialPointMotion;
import ru.axothy.mechanics.motion.Motion;
import ru.axothy.mechanics.tensors.Tensor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class MainApplication extends Application {
    private static final String MAIN_LABEL = "Body-point motion mechanics";
    public static final int WIDTH = 910;
    public static final int HEIGHT = 685;
    public Group group;
    public Map<PointGraphics, Motion> motions = new HashMap<>();
    public ArrayList<PolyLine3DCustom> trajectories = new ArrayList<>();
    public CoulombCenterGraphics coulombCenter;
    public static PerspectiveCamera camera;
    public SubScene scene;
    public MouseLook mouseLook;
    public Button addMaterialPointButton, addBodyPointButton;
    public TextField massMaterialPointInput, massBodyPointInput;
    public TextField R0MaterialInput, R0BodyInput;
    public TextField V0MaterialInput, V0BodyInput;
    public TextField W0BodyInput, TensorJInput, TensorBInput;
    public TextField coulombConstInput;
    public Label coordLabel;
    public ToggleButton lightButton;

    public ImageView solveButton;
    public Button clearButton;

    public static final Color MPCOLOR = Color.RED;
    public static final Color BPCOLOR = Color.WHITE;

    public static final double h = 0.00001;
    public static final int n = 2500000;


    public void init() {
        group = new Group();
        coulombCenter = new CoulombCenterGraphics(10);
        group.getChildren().add(coulombCenter.getSphere());

        setCameraOptions();
        setAxes();

        //Setting coulombCenter position
        coulombCenter.getSphere().setTranslateX(0 + WIDTH / 2);
        coulombCenter.getSphere().setTranslateY(HEIGHT / 2 - 0);

        mouseLook = new MouseLook(camera);
    }

    public void setCameraOptions() {
        //Setting camera
        camera = new PerspectiveCamera();

        //Setting camera position
        camera.translateXProperty().set(-125);
        camera.translateYProperty().set(-100);
        camera.translateZProperty().set(-700);

        camera.translateXProperty().addListener((observable, oldValue, newValue) -> {
            int x = (int) newValue.doubleValue();
            int y = (int) camera.getTranslateY();
            int z = (int) camera.getTranslateZ();
            coordLabel.setText("      XYZ : " + x + " / " + -y + " / " + z);
        });

        camera.translateYProperty().addListener((observable, oldValue, newValue) -> {
            int x = (int) camera.getTranslateX();
            int y = (int) newValue.doubleValue();
            int z = (int) camera.getTranslateZ();
            coordLabel.setText("      XYZ : " + x + " / " + -y + " / " + z);
        });

        camera.translateZProperty().addListener((observable, oldValue, newValue) -> {
            int x = (int) camera.getTranslateX();
            int y = (int) camera.getTranslateY();
            int z = (int) newValue.doubleValue();
            coordLabel.setText("      XYZ : " + x + " / " + -y + " / " + z);
        });
    }

    public void initElements(Parent root) {
        scene = (SubScene) root.lookup("#scene");
        addMaterialPointButton = (Button) root.lookup("#addMaterialPoint");
        massMaterialPointInput = (TextField) root.lookup("#massMaterialPointInput");
        R0MaterialInput = (TextField) root.lookup("#R0MaterialInput");
        V0MaterialInput = (TextField) root.lookup("#V0MaterialInput");
        solveButton = (ImageView) root.lookup("#solveButton");
        coulombConstInput = (TextField) root.lookup("#coulombConst");
        clearButton = (Button) root.lookup("#clearButton");
        coordLabel = (Label) root.lookup("#CoordsLabel");

        addBodyPointButton = (Button) root.lookup("#addBodyPoint");
        massBodyPointInput = (TextField) root.lookup("#massBodyPointInput");
        R0BodyInput = (TextField) root.lookup("#R0BodyInput");
        V0BodyInput = (TextField) root.lookup("#V0BodyInput");
        W0BodyInput = (TextField) root.lookup("#W0BodyInput");
        TensorJInput = (TextField) root.lookup("#TensorJ");
        TensorBInput = (TextField) root.lookup("#TensorB");
        lightButton = (ToggleButton) root.lookup("#togglebutton");
    }

    public void setSceneParameters() {
        scene.setFill(Color.valueOf("#181818"));
        scene.setCamera(camera);
        scene.setOnMouseDragged(mouseLook);
        scene.setRoot(group);
        scene.setFocusTraversable(true);
        scene.requestFocus();
    }

    public void setAxes() {
        final Box xAxis = new Box(12000, 0.5, 0.5);
        final Box yAxis = new Box(0.5, 12000, 0.5);
        final Box zAxis = new Box(0.5, 0.5, 12000);

        xAxis.setTranslateX(WIDTH / 2);
        xAxis.setTranslateY(HEIGHT / 2);

        yAxis.setTranslateX(WIDTH / 2);
        yAxis.setTranslateY(HEIGHT / 2);

        zAxis.setTranslateX(WIDTH / 2);
        zAxis.setTranslateY(HEIGHT / 2);

        xAxis.setMaterial(new PhongMaterial(Color.WHITE));
        yAxis.setMaterial(new PhongMaterial(Color.WHITE));
        zAxis.setMaterial(new PhongMaterial(Color.WHITE));

        Group axisGroup = new Group();
        axisGroup.getChildren().addAll(xAxis, yAxis, zAxis);
        group.getChildren().add(axisGroup);

    }

    public void setKeyHandlers() {
        scene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            switch (event.getCode()) {
                case W:
                    camera.translateYProperty().set(camera.getTranslateY() - 25);
                    break;
                case S:
                    camera.translateYProperty().set(camera.getTranslateY() + 25);
                    break;
                case A:
                    camera.translateXProperty().set(camera.getTranslateX() - 25);
                    break;
                case D:
                    camera.translateXProperty().set(camera.getTranslateX() + 25);
                    break;
            }
        });

        scene.addEventHandler(ScrollEvent.SCROLL, event -> {
            if (event.getDeltaY() < 0) {
                camera.translateZProperty().set(camera.getTranslateZ() - 25);
            }

            if (event.getDeltaY() > 0) {
                camera.translateZProperty().set(camera.getTranslateZ() + 25);
            }
        });

    }

    public void addMaterialPoint(Point3D r0, Point3D v0, double mass, double coulombConstant) {
        PointGraphics newPoint = new PointGraphics(5);
        group.getChildren().add(newPoint.getSphere());

        MaterialPointMotion motion = new MaterialPointMotion();
        motion.setCoulombConstant(coulombConstant);
        motion.setMaterialPoint(new MaterialPoint(mass));
        motion.setInitialRadiusVector(r0);
        motion.setInitialDerivativeRadiusVector(v0);

        motions.put(newPoint, motion);

        newPoint.getSphere().setTranslateX(r0.getX() + WIDTH / 2);
        newPoint.getSphere().setTranslateY(HEIGHT / 2 - r0.getY());
        newPoint.getSphere().setTranslateZ(r0.getZ());
        newPoint.changeColor(MPCOLOR);
    }

    public void addBodyPoint(Point3D r0, Point3D v0, Point3D w0, double mass, Tensor J, Tensor B, double coulombConstant) {
        PointGraphics newPoint = new PointGraphics(5);
        group.getChildren().add(newPoint.getSphere());

        BodyPointMotion motion = new BodyPointMotion();
        motion.setCoulombConstant(coulombConstant);
        motion.setBodyPoint(new BodyPoint(mass, J, B));
        motion.setInitialRadiusVector(r0);
        motion.setInitialVelocityTrans(v0);
        motion.setInitialVelocityRot(w0);

        motions.put(newPoint, motion);

        newPoint.getSphere().setTranslateX(r0.getX() + WIDTH / 2);
        newPoint.getSphere().setTranslateY(HEIGHT / 2 - r0.getY());
        newPoint.getSphere().setTranslateZ(r0.getZ());
        newPoint.changeColor(BPCOLOR);
    }

    public void clear() {
        motions.keySet().forEach(k -> {
            group.getChildren().remove(k.getSphere());
        });

        motions.clear();
        clearTrajectories();
    }

    public void motionAll() {
        motions.values().forEach(v -> v.startMotion(h, n));
    }

    public void saveDataToFile(List<Point3D> points) throws FileNotFoundException, UnsupportedEncodingException {
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

    public void saveEnergy(List<Double> energy, String filename) throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writerE = new PrintWriter(filename, "UTF-8");
        PrintWriter writerI = new PrintWriter("I.txt", "UTF-8");
        int i = 0;
        for (Double value : energy) {
            writerE.println(value);
            writerI.println(i);
            i++;
        }

        writerE.close();
        writerI.close();
    }

    public void drawTrajectory(List<Point3D> points, Color color) {
        ArrayList<org.fxyz3d.geometry.Point3D> screenPoints = new ArrayList<>();

        for (Point3D point : points) {
            screenPoints.add(new org.fxyz3d.geometry.Point3D(
                    (float) point.getX() + WIDTH / 2,
                    HEIGHT / 2 - (float) point.getY(),
                    (float) point.getZ()
            ));
        }

        PolyLine3DCustom trajectory = new PolyLine3DCustom(screenPoints, 1.5f, color);
        trajectories.add(trajectory);
        group.getChildren().addAll(trajectory);
    }

    public void clearTrajectories() {
        for (PolyLine3DCustom trajectory : trajectories) {
            group.getChildren().remove(trajectory);
        }
        trajectories.clear();
    }

    public void animateAll() {
        motions.forEach((k, v) -> {

            List<Point3D> points = v.getData().getRadiusVector();


            try {
                saveDataToFile(points);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            /*
            if (v.getMotionPoint() instanceof BodyPoint) {
                drawTrajectory(points, BPCOLOR);
            } else if (v.getMotionPoint() instanceof MaterialPoint)
                drawTrajectory(points, MPCOLOR);

            TranslateTransition[] transitions = new TranslateTransition[points.size()];

            TranslateTransition[] translateTransitions = new TranslateTransition[points.size()];
            for (int i = 0; i < points.size(); i++) {
                translateTransitions[i] = new TranslateTransition(Duration.seconds(0.0005), k.getSphere());
                translateTransitions[i].setToX(points.get(i).getX() + WIDTH / 2);
                translateTransitions[i].setToY(HEIGHT / 2 - points.get(i).getY());
                translateTransitions[i].setToZ(points.get(i).getZ());
            }

            SequentialTransition sequentialTransition = new SequentialTransition();
            sequentialTransition.getChildren().addAll(translateTransitions);

            sequentialTransition.play();

*/
        });


        energy();
    }


    /*
    public void energyMP() {
        motions.forEach((k, v) -> {
            MaterialPointEnergy energy = new MaterialPointEnergy();
            energy.setMaterialPoint(v.getMotionPoint());;
            energy.setCoulombConst(v.getCoulombConstant());
            energy.setMovingData(v.getData());

            ArrayList<Double> energies = energy.getEnergy();
            try {
                saveEnergy(energies, "Energy.txt");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            ArrayList<Double> errors = energy.getRelativeDev();
            try {
                saveEnergy(errors, "rlDev.txt");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            System.out.println(energy.stDev(energies, 500000));
        });
    } */

    public void energy() {
        motions.forEach((k, v) -> {
            BodyPointEnergy energy = new BodyPointEnergy();
            energy.setMaterialPoint(v.getMotionPoint());
            energy.setCoulombConst(v.getCoulombConstant());
            energy.setMovingData(v.getData());

            ArrayList<Double> energies = energy.getEnergy();
            try {
                saveEnergy(energies, "Energy_body_point.txt");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            ArrayList<Double> errors = energy.getRelativeDev();
            try {
                saveEnergy(errors, "Relative_Error.txt");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            System.out.println(energy.stDev(energies, 250000));
        });
    }



    public void light() {
        AmbientLight light = new AmbientLight(Color.WHITE);

        lightButton.setOnAction(event -> {
            if (lightButton.isSelected()) {
                group.getChildren().add(light);
            } else {
                group.getChildren().remove(light);
            }
        });
    }

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/sample.fxml"));
        stage.setTitle(MAIN_LABEL);
        stage.setScene(new Scene(root, 1200, 800));
        stage.getIcons().add(new Image("elasticity.png"));
        initElements(root);
        init();
        setSceneParameters();

        light();


        addBodyPoint(new Point3D(-200, 49, 0), new Point3D(7.058, 0, 6.86),
                new Point3D(-2.941, 0, -9.8), 1, Tensor.UNARY_TENSOR, Tensor.UNARY_TENSOR.multiply(0.7),
                -0.5);


        // addMaterialPoint(new Point3D(1,0,0), new Point3D(0,1.2,0), 1, 1);

        /*
                VERY GOOD EXAMPLE:
                addBodyPoint(new Point3D(-500, 200.8, 0), new Point3D(7.058, 0, 6.86),
                new Point3D(-2.941, 0, -9.8), 1, Tensor.UNARY_TENSOR, Tensor.UNARY_TENSOR.multiply(0.7),
                -2);
         */


        addMaterialPointButton.setOnAction(event -> {
                    double mass = Double.parseDouble(massMaterialPointInput.getText());
                    String[] numbers = R0MaterialInput.getText().split(" ");
                    Point3D r0 = new Point3D(
                            Double.parseDouble(numbers[0]),
                            Double.parseDouble(numbers[1]),
                            Double.parseDouble(numbers[2])
                    );

                    numbers = V0MaterialInput.getText().split(" ");
                    Point3D v0 = new Point3D(
                            Double.parseDouble(numbers[0]),
                            Double.parseDouble(numbers[1]),
                            Double.parseDouble(numbers[2])
                    );

                    double A = Double.parseDouble(coulombConstInput.getText());

                    addMaterialPoint(r0, v0, mass, A);

                    scene.requestFocus();
                }
        );

        addBodyPointButton.setOnAction(event -> {
                    double mass = Double.parseDouble(massBodyPointInput.getText());
                    String[] numbers = R0BodyInput.getText().split(" ");
                    Point3D r0 = new Point3D(
                            Double.parseDouble(numbers[0]),
                            Double.parseDouble(numbers[1]),
                            Double.parseDouble(numbers[2])
                    );

                    numbers = V0BodyInput.getText().split(" ");
                    Point3D v0 = new Point3D(
                            Double.parseDouble(numbers[0]),
                            Double.parseDouble(numbers[1]),
                            Double.parseDouble(numbers[2])
                    );

                    numbers = W0BodyInput.getText().split(" ");
                    Point3D w0 = new Point3D(
                            Double.parseDouble(numbers[0]),
                            Double.parseDouble(numbers[1]),
                            Double.parseDouble(numbers[2])
                    );

                    //Bad practice:

                    numbers = TensorJInput.getText().split(" ");

                    Tensor J;
                    if (numbers[0].equals("E")) {
                        J = Tensor.UNARY_TENSOR;
                    } else {
                        Double[] arr = new Double[numbers.length];
                        for (int i = 0; i < numbers.length; i++) {
                            arr[i] = Double.parseDouble(numbers[i]);
                        }
                        J = new Tensor(arr);
                    }

                    numbers = TensorBInput.getText().split(" ");

                    Tensor B;
                    if (numbers[0].equals("E")) {
                        B = Tensor.UNARY_TENSOR;
                    } else {
                        Double[] arr2 = new Double[numbers.length];
                        for (int i = 0; i < numbers.length; i++) {
                            arr2[i] = Double.parseDouble(numbers[i]);
                        }
                        B = new Tensor(arr2);
                    }

                    double A = Double.parseDouble(coulombConstInput.getText());
                    addBodyPoint(r0, v0, w0, mass, J, B, A);

                    scene.requestFocus();
                }
        );

        solveButton.setOnMouseClicked(event -> {
            clearTrajectories();
            motionAll();
            animateAll();

            scene.requestFocus();
        });

        clearButton.setOnAction(event -> {
            clear();
        });

        scene.setOnMouseClicked(event -> {
            scene.requestFocus();
        });

        setKeyHandlers();
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}

