package aapopajunen.entity;

import aapopajunen.engine.graphical.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static aapopajunen.entity.Direction.directionToByte;

public class Block {

    private static Map<Direction, Model> facesByDirection = new HashMap<>();
    private static byte renderedFaces = 0b00111111; // - - +x -x +y -y +z -z
    private BlockType type;

    public static void setFace(Direction direction, Model face) {
        facesByDirection.put(direction, face);
    }

    private static Model getFace(Direction direction) {
        return facesByDirection.get(direction);
    }

    public List<Model> getFaces() {
        List<Model> faces = new ArrayList<>();
        for (Direction direction : getRenderedFaceDirections()) {
            faces.add(getFace(direction));
        }
        return faces;
    }

    public List<Direction> getRenderedFaceDirections() {
        List<Direction> directions = new ArrayList<>();
        for (Direction direction : Direction.values()) {
            if((directionToByte(direction) & renderedFaces) != 0) {
                directions.add(direction);
            }
        }
        return directions;
    }

    public void setRenderFace(Direction direction, boolean shouldBeRendered) {
        if (shouldBeRendered) {
            renderedFaces |= directionToByte(direction);
        } else {
            renderedFaces &= ~directionToByte(direction);
        }
    }
}