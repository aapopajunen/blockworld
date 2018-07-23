package aapopajunen.entity;

import org.joml.Vector2f;

import java.util.HashMap;
import java.util.Map;

public class TextureManager {

    private static Map<BlockType, Map<Direction, float[]>> blockTypeFaceTextureCoordinatesMap = new HashMap<>();
    private

    public static void addFaceTextCoords(BlockType type, Direction direction, float[] textureCoordinates) {
        if (!blockTypeFaceTextureCoordinatesMap.containsKey(type)) {
            blockTypeFaceTextureCoordinatesMap.put(type, new HashMap<>());
        }
        blockTypeFaceTextureCoordinatesMap.get(type).put(direction, textureCoordinates);
    }

    public static float[] getFaceTextureCoordinates(BlockType type, Direction direction) {
        return blockTypeFaceTextureCoordinatesMap.get(type).get(direction);
    }
}