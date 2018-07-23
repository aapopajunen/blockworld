package aapopajunen.engine.graphical;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class Model {
    private float[] vertices;
    private float[] normals;
    private float[] textCoords;
    private int[] indices;

    private Vector3f rotation;
    private Vector3f position;
    private float scale;

    public Model() {
    }

    public Model(float[] vertices, float[] normals, float[] textCoords, int[] indices) {
        this.vertices = vertices;
        this.normals = normals;
        this.textCoords = textCoords;
        this.indices = indices;
    }

    public float[] getVertices() {
        return vertices;
    }

    public void setVertices(float[] vertices) {
        this.vertices = vertices;
    }

    public float[] getNormals() {
        return normals;
    }

    public void setNormals(float[] normals) {
        this.normals = normals;
    }

    public float[] getTextCoords() {
        return textCoords;
    }

    public void setTextCoords(float[] textCoords) {
        this.textCoords = textCoords;
    }

    public int[] getIndices() {
        return indices;
    }

    public void setIndices(int[] indices) {
        this.indices = indices;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public void setRotation(Vector3f rotation) {
        this.rotation = rotation;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }
}
