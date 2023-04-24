package ru.axothy.mechanics.graphics;

import javafx.scene.AmbientLight;
import javafx.scene.DepthTest;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import org.fxyz3d.geometry.Point3D;

import java.util.List;

public class PolyLine3DCustom extends Group {

    public List<Point3D> points;
    public float width = 2.0f;
    public Color color = Color.WHITE;
    private TriangleMesh mesh;
    public MeshView meshView;
    public PhongMaterial material;


    //Creates a PolyLine3D with the user's choice of mesh style
    public PolyLine3DCustom(List<Point3D> points, float width, Color color) {
        this.points = points;
        this.width = width;
        this.color = color;
        setDepthTest(DepthTest.ENABLE);
        mesh = new TriangleMesh();

        buildTriangleTube();

        //Need to add the mesh to a MeshView before adding to our 3D scene
        meshView = new MeshView(mesh);
        meshView.setDrawMode(DrawMode.FILL);  //Fill so that the line shows width
        material = new PhongMaterial(color);
        material.setDiffuseColor(color);
        material.setSpecularColor(color);
        meshView.setMaterial(material);
        //Make sure you Cull the Back so that no black shows through
        meshView.setCullFace(CullFace.BACK);
        //AmbientLight light = new AmbientLight(Color.WHITE);
        //getChildren().add(light);
        getChildren().add(meshView);
    }

    private void buildTriangleTube() {
        //For each data point add three mesh points as an equilateral triangle
        float half = width / 2.0f;
        for (Point3D point : points) {
            //-0.288675f*hw, -0.5f*hw, -0.204124f*hw,
            mesh.getPoints().addAll(point.x - 0.288675f * half, point.y - 0.5f * half, point.z - 0.204124f * half);
            //-0.288675f*hw, 0.5f*hw, -0.204124f*hw,
            mesh.getPoints().addAll(point.x - 0.288675f * half, point.y + 0.5f * half, point.z - 0.204124f * half);
            //0.57735f*hw, 0f, -0.204124f*hw
            mesh.getPoints().addAll(point.x + 0.57735f * half, point.y + 0.5f * half, point.z - 0.204124f * half);
        }
        //add dummy Texture Coordinate
        mesh.getTexCoords().addAll(0, 0);
        //Beginning End Cap
        mesh.getFaces().addAll(0, 0, 1, 0, 2, 0);
        //Now generate trianglestrips between each point
        for (int i = 3; i < points.size() * 3; i += 3) {  //add each triangle tube segment
            //Vertices wound counter-clockwise which is the default front face of any Triange
            //Triangle Tube Face 1
            mesh.getFaces().addAll(i + 2, 0, i - 2, 0, i + 1, 0); //add secondary Width face
            mesh.getFaces().addAll(i + 2, 0, i - 1, 0, i - 2, 0); //add primary face
            //Triangle Tube Face 2
            mesh.getFaces().addAll(i + 2, 0, i - 3, 0, i - 1, 0); //add secondary Width face
            mesh.getFaces().addAll(i, 0, i - 3, 0, i + 2, 0); //add primary face
            //Triangle Tube Face 3
            mesh.getFaces().addAll(i, 0, i + 1, 0, i - 3, 0); //add primary face
            mesh.getFaces().addAll(i + 1, 0, i - 2, 0, i - 3, 0); //add secondary Width face
        }
        //Final End Cap
        int last = points.size() * 3 - 1;
        mesh.getFaces().addAll(last, 0, last - 1, 0, last - 2, 0);
    }
}
