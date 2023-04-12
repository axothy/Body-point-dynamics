package ru.axothy.mechanics;

import javafx.animation.*;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;
import ru.axothy.mechanics.bodypoint.BodyPoint;
import ru.axothy.mechanics.bodypoint.MaterialPoint;
import ru.axothy.mechanics.graphics.CoulombCenterGraphics;
import ru.axothy.mechanics.graphics.PointGraphics;
import ru.axothy.mechanics.motion.BodyPointMotion;
import ru.axothy.mechanics.motion.MaterialPointMotion;
import ru.axothy.mechanics.motion.Motion;
import ru.axothy.mechanics.tensors.Tensor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MainApplication extends Application {
    private static final String MAIN_LABEL = "Body-point motion mechanics";
    private static double coulombConstant = -3;
    public static final int WIDTH = 910;
    public static final int HEIGHT = 685;
    public Line axeX;
    public Line axeY;
    public Group group;
    public Map<PointGraphics, Motion> motions = new HashMap<>();
    public CoulombCenterGraphics coulombCenter;
    public static PerspectiveCamera camera;
    public SubScene scene;
    public Button addMaterialPointButton;
    public TextField massMaterialPointInput;
    public TextField R0MaterialInput;
    public TextField V0MaterialInput;
    public TextField coulombConstInput;
    public Label coordLabel;

    public ImageView solveButton;
    public Button clearButton;

    public static final Color MPCOLOR = Color.RED;
    public static final Color BPCOLOR = Color.YELLOW;


    public void init() {
        group = new Group();
        coulombCenter = new CoulombCenterGraphics(10);
        group.getChildren().add(coulombCenter.getSphere());

        setCameraOptions();
        setAxes();

        //Setting coulombCenter position
        coulombCenter.getSphere().setTranslateX(0 + WIDTH / 2);
        coulombCenter.getSphere().setTranslateY(HEIGHT / 2 - 0);
    }

    public void setCameraOptions() {
        //Setting camera
        camera = new PerspectiveCamera();

        //Setting camera position
        camera.translateXProperty().set(0);
        camera.translateYProperty().set(0);
        camera.translateZProperty().set(-700);

        camera.translateXProperty().addListener((observable, oldValue, newValue) -> {
            int x = (int) newValue.doubleValue();
            int y = (int) camera.getTranslateY();
            int z = (int) camera.getTranslateZ();
            coordLabel.setText("      XYZ : " + x + " / " + y + " / " + z);
        });

        camera.translateYProperty().addListener((observable, oldValue, newValue) -> {
            int x = (int) camera.getTranslateX();
            int y = (int) newValue.doubleValue();
            int z = (int) camera.getTranslateZ();
            coordLabel.setText("      XYZ : " + x + " / " + y + " / " + z);
        });

        camera.translateZProperty().addListener((observable, oldValue, newValue) -> {
            int x = (int) camera.getTranslateX();
            int y = (int) camera.getTranslateY();
            int z = (int) newValue.doubleValue();
            coordLabel.setText("      XYZ : " + x + " / " + y + " / " + z);
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
    }

    public void setSceneParameters() {
        scene.setFill(Color.valueOf("#181818"));
        scene.setCamera(camera);
        scene.setOnMouseDragged(new MouseLook());
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

    public void addMaterialPoint(Point3D r0, Point3D v0, double mass) {
        PointGraphics newPoint = new PointGraphics(10);
        group.getChildren().add(newPoint.getSphere());

        MaterialPointMotion motion = new MaterialPointMotion();
        motion.setCoulombConstant(this.coulombConstant);
        motion.setMaterialPoint(new MaterialPoint(mass));
        motion.setInitialRadiusVector(r0);
        motion.setInitialDerivativeRadiusVector(v0);

        motions.put(newPoint, motion);

        newPoint.getSphere().setTranslateX(r0.getX() + WIDTH / 2);
        newPoint.getSphere().setTranslateY(HEIGHT / 2 - r0.getY());
        newPoint.getSphere().setTranslateZ(r0.getZ());
        newPoint.changeColor(MPCOLOR);
    }

    public void addBodyPoint(Point3D r0, Point3D v0, Point3D w0, double mass, Tensor J, Tensor B) {
        PointGraphics newPoint = new PointGraphics(10);
        group.getChildren().add(newPoint.getSphere());

        BodyPointMotion motion = new BodyPointMotion();
        motion.setCoulombConstant(this.coulombConstant);
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
        motions.clear();
    }

    public void setCoulombConstant(double value) {
        coulombConstant = value;
    }

    public void motionAll() {
        motions.values().forEach(v -> v.startMotion());
    }

    public void animateAll() {
        motions.forEach((k, v) -> {

            ArrayList<Point3D> points = v.getData().getRadiusVector();
            TranslateTransition[] transitions = new TranslateTransition[points.size()];

            TranslateTransition[] translateTransitions = new TranslateTransition[points.size()];
            for (int i = 0; i < points.size(); i++) {
                translateTransitions[i] = new TranslateTransition(Duration.seconds(0.01), k.getSphere());
                translateTransitions[i].setToX(points.get(i).getX() + WIDTH / 2);
                translateTransitions[i].setToY(HEIGHT / 2 - points.get(i).getY());
                translateTransitions[i].setToZ(points.get(i).getZ());


                Sphere mini = new Sphere(1);
                mini.setTranslateX(points.get(i).getX() + WIDTH / 2);
                mini.setTranslateY(HEIGHT / 2 - points.get(i).getY());
                mini.setTranslateZ(points.get(i).getZ());
                group.getChildren().add(mini);


            }

            SequentialTransition sequentialTransition = new SequentialTransition();
            sequentialTransition.getChildren().addAll(translateTransitions);

            sequentialTransition.play();

        });
    }

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/sample.fxml"));
        stage.setTitle(MAIN_LABEL);
        stage.setScene(new Scene(root, 1200, 800));
        initElements(root);
        init();
        setSceneParameters();


        addMaterialPoint(new Point3D(-200, 100, 0), new Point3D(2, 0, 0), 15);

        addBodyPoint(new Point3D(-200, 100, 0),
                new Point3D(2, 0, 0),
                new Point3D(0, 0, 0),
                15,
                new Tensor(10.0, 0.0, 0.0, 0.0, 10.0, 0.0, 0.0, 0.0, 10.0),
                new Tensor(10.0, 0.0, 0.0, 0.0, 10.0, 0.0, 0.0, 0.0, 10.0)
        );




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

                    addMaterialPoint(r0, v0, mass);

                }
        );

        solveButton.setOnMouseClicked(event -> {
            //double A = Double.parseDouble(coulombConstInput.getText());
           //setCoulombConstant(A);
            motionAll();
            animateAll();
        });

        clearButton.setOnAction(event -> {
            clear();
        });


        /*
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
        */

        setKeyHandlers();
        stage.show();
    }

    static class MouseLook implements EventHandler<MouseEvent> {
        private static Rotate rotation;
        private static int oldX, newX;
        private static boolean alreadyMoved = false;

        @Override
        public void handle(MouseEvent event) {
            if (alreadyMoved) {
                newX = (int) event.getScreenX();

                if (oldX < newX) { // if mouse moved to right
                    rotation = new Rotate(0.5,
                            // camera rotates around its location
                            camera.getTranslateX(), camera.getTranslateY(), camera.getTranslateZ(),
                            Rotate.Y_AXIS);


                } else if (oldX > newX) { // if mouse moved to left
                    rotation = new Rotate(-0.5,
                            // camera rotates around its location
                            camera.getTranslateX(), camera.getTranslateY(), camera.getTranslateZ(),
                            Rotate.Y_AXIS);

                }
                camera.getTransforms().addAll(rotation);

                oldX = newX;
            } else {
                oldX = (int) event.getScreenX();
                alreadyMoved = true;
            }
        }
    }

    public static void main(String[] args) {
        launch();
    }
}

