package aapopajunen.entity;

import org.joml.Vector3i;

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
}
