package aapopajunen.entity;

import aapopajunen.engine.graphical.Model;
import com.sun.tools.javac.util.ArrayUtils;
import org.joml.Vector3f;
import org.joml.Vector3i;

import javax.sound.midi.SysexMessage;
import java.util.ArrayList;
import java.util.List;

public class Chunk {
    public static int CHUNK_X_DIM = 16;
    public static int CHUNK_Y_DIM = 16;
    public static int CHUNK_Z_DIM = 16;

    private Vector3i position;

    private Block[][][] blocks = new Block[CHUNK_X_DIM][CHUNK_Y_DIM][CHUNK_Z_DIM];

    public Block[][][] getBlocks() {
        return blocks;
    }

    public Vector3i getPosition() {
        return position;
    }

    public Vector3i indexToVertex(int index) {
        int xIndicesPerRow = CHUNK_X_DIM + 1;
        int yIndicesPerRow = CHUNK_Y_DIM + 1;
        int zIndicesPerRow = CHUNK_Z_DIM + 1;
        new Vector3i(
                (index/zIndicesPerRow/yIndicesPerRow)%xIndicesPerRow,
                (index/zIndicesPerRow)%yIndicesPerRow,
                (index)%zIndicesPerRow);
        return null;
    }

    public int vertexToIndex(int x, int y, int z) {
        int xIndicesPerRow = CHUNK_X_DIM + 1;
        int yIndicesPerRow = CHUNK_Y_DIM + 1;
        int zIndicesPerRow = CHUNK_Z_DIM + 1;
        return zIndicesPerRow*yIndicesPerRow*x + zIndicesPerRow*y + xIndicesPerRow;
    }

    public int nofIndices() {
        return CHUNK_X_DIM*CHUNK_Y_DIM*CHUNK_Z_DIM;
    }

    public int[] verticesToIndices(int[][] vertices) {
        int[] indices = new int[vertices.length/3];

        for (int i = 0; i < vertices.length; i++) {
            indices[i] = vertexToIndex(vertices[i][0], vertices[i][1], vertices[i][2]);
        }
        return indices;
    }

    public int[] getIndices(int x, int y, int z, Direction direction) {
        switch (direction) {
            case X_POS: {
                return verticesToIndices(
                        new int[][] {
                                { 1+x,   y,   z}, { 1+x, 1+y,   z}, { 1+x,   y, 1+z},
                                { 1+x, 1+y,   z}, { 1+x, 1+y, 1+z}, { 1+x,   y, 1+z}
                        }
                );
            }
            case X_NEG: {
                return verticesToIndices(
                        new int[][] {
                                {   x,   y,   z}, {   x,   y, 1+z}, {   x, 1+y,   z},
                                {   x, 1+y,   z}, {   x,   y, 1+z}, {   x, 1+y, 1+z}
                        }
                );
            }
            case Y_POS: {
                return verticesToIndices(
                        new int[][] {
                                {   x, 1+y,   z}, {   x, 1+y, 1+z}, { 1+x, 1+y,   z},
                                { 1+x, 1+y,   z}, {   x, 1+y, 1+z}, { 1+x, 1+y, 1+z}
                        }
                );
            }
            case Y_NEG: {
                return verticesToIndices(
                        new int[][] {
                                {   x,   y,   z}, { 1+x,   y,   z}, {   x,   y, 1+z},
                                { 1+x,   y,   z}, { 1+x,   y, 1+z}, {   x,   y, 1+z}
                        }
                );
            }
            case Z_POS: {
                return verticesToIndices(
                        new int[][] {
                                {   x,   y, 1+z}, { 1+x,   y, 1+z}, {   x, 1+y, 1+z},
                                {   x, 1+y, 1+z}, { 1+x,   y, 1+z}, { 1+x, 1+y, 1+z}
                        }
                );
            }
            case Z_NEG: {
                return verticesToIndices(
                        new int[][] {
                                {   x,   y, 1+z}, {   x, 1+y, 1+z}, { 1+x,   y, 1+z},
                                { 1+x,   y, 1+z}, {   x, 1+y, 1+z}, { 1+x, 1+y, 1+z}
                        }
                );
            }
            default: return null;
        }
    }

    public int[] getIndices() {
        int[] indices = new int[0];
        for (int x = 0; x < CHUNK_X_DIM; x++) {
            for (int y = 0; y < CHUNK_Y_DIM; y++) {
                for (int z = 0; z < CHUNK_Z_DIM; z++) {
                    for (Direction direction : blocks[x][y][z].getRenderedFaceDirections()) {
                        int[] nextIndices = getIndices(x,y,z,direction);
                        int[] newIndices = new int[indices.length + nextIndices.length];
                        System.arraycopy(indices, 0, newIndices, 0, indices.length);
                        System.arraycopy(newIndices, 0, newIndices, indices.length, newIndices.length);
                        indices = newIndices;
                    }
                }
            }
        }
        return indices;
    }
}
