package aapopajunen.game;

import aapopajunen.engine.graphical.Model;
import aapopajunen.entity.Block;
import aapopajunen.entity.Chunk;

import java.util.ArrayList;
import java.util.List;

import static aapopajunen.entity.Chunk.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.opengl.GL32.glDrawElementsBaseVertex;

public class ChunkRenderer2 {

    private final int vaoId;

    private final List<Integer> vboIdList;

    private int vertexCount = 0;

    public ChunkRenderer2(Chunk chunk) {
        vaoId = glGenVertexArrays();
        vboIdList = new ArrayList<>();

        int textCoordCount = 0;
        int normalCount = 0;
        int indexCount = 0;
        int offsetCount = CHUNK_X_DIM*CHUNK_Y_DIM*CHUNK_Z_DIM*3;

        for (int x = 0; x < CHUNK_X_DIM; x++) {
            for (int y = 0; y < CHUNK_Y_DIM; y++) {
                for (int z = 0; z < CHUNK_Z_DIM; z++) {
                    Block block = chunk.getBlocks()[x][y][z];
                    for (Model model : block.getFaces()) {
                        vertexCount += model.getVertices().length;
                        textCoordCount += model.getTextCoords().length;
                        normalCount += model.getNormals().length;
                        indexCount += model.getIndices().length;
                    }
                }
            }
        }

        int vertexVboId = glGenBuffers();
        int textCoordVboId = glGenBuffers();
        int normalVboId = glGenBuffers();
        int offsetVboId = glGenBuffers();
        int indexVboId = glGenBuffers();

        vboIdList.add(vertexVboId);
        vboIdList.add(textCoordVboId);
        vboIdList.add(normalVboId);
        vboIdList.add(offsetVboId);
        vboIdList.add(indexVboId);

        // bind Vao
        glBindVertexArray(vaoId);

        // Vertex Vbo
        glBindBuffer(GL_ARRAY_BUFFER, vertexVboId);
        glBufferData(GL_ARRAY_BUFFER, vertexCount*Float.SIZE/8,  GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

        // Texture Coordinate Vbo
        glBindBuffer(GL_ARRAY_BUFFER, textCoordVboId);
        glBufferData(GL_ARRAY_BUFFER, textCoordCount*Float.SIZE/8, GL_STATIC_DRAW);
        glVertexAttribPointer(1, 2, GL_FLOAT, true, 0, 0);

        // Normal Vbo
        glBindBuffer(GL_ARRAY_BUFFER, normalVboId);
        glBufferData(GL_ARRAY_BUFFER, normalCount*Float.SIZE/8, GL_STATIC_DRAW);
        glVertexAttribPointer(2, 3, GL_FLOAT, false, 0, 0);

        // Offset Vbo
        glBindBuffer(GL_ARRAY_BUFFER, offsetVboId);
        glBufferData(GL_ARRAY_BUFFER, offsetCount*Float.SIZE/8, GL_STATIC_DRAW);
        glVertexAttribPointer(3, 3, GL_FLOAT, false, 0, 0);

        // Index Vbo
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexVboId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexCount*Integer.SIZE/8, GL_STATIC_DRAW);

        for (int x = 0; x < CHUNK_X_DIM; x++) {
            for (int y = 0; y < CHUNK_Y_DIM; y++) {
                for (int z = 0; z < CHUNK_Z_DIM; z++) {
                    Block block = chunk.getBlocks()[x][y][z];

                    long vertexWriteOffset       = 0;
                    long textCoordWriteOffset    = 0;
                    long normalWriteOffset       = 0;
                    long offsetWriteOffset       = 0;
                    long indexWriteOffset        = 0;
                    for (Model model : block.getFaces()) {

                        // Generate vertexOffsets
                        float[] vertexOffsets = new float[model.getVertices().length];
                        for (int i = 0; i < vertexOffsets.length/3; i++) {
                            vertexOffsets[i]       = x;
                            vertexOffsets[i + 1]   = y;
                            vertexOffsets[i + 2]   = z;
                        }

                        // Vertex Vbo
                        glBindBuffer(GL_ARRAY_BUFFER, vertexVboId);
                        glBufferSubData(GL_ARRAY_BUFFER, vertexWriteOffset, model.getVertices());
                        vertexWriteOffset += model.getVertices().length*Float.SIZE/8;

                        // Texture Coordinate Vbo
                        glBindBuffer(GL_ARRAY_BUFFER, textCoordVboId);
                        glBufferSubData(GL_ARRAY_BUFFER, textCoordWriteOffset, model.getTextCoords());
                        textCoordWriteOffset += model.getTextCoords().length*Float.SIZE/8;

                        // Normal Vbo
                        glBindBuffer(GL_ARRAY_BUFFER, normalVboId);
                        glBufferSubData(GL_ARRAY_BUFFER, normalWriteOffset, model.getNormals());
                        normalWriteOffset += model.getNormals().length*Float.SIZE/8;

                        // Offset Vbo
                        glBindBuffer(GL_ARRAY_BUFFER, offsetVboId);
                        glBufferSubData(GL_ARRAY_BUFFER, offsetWriteOffset, vertexOffsets);
                        offsetWriteOffset += vertexOffsets.length*Float.SIZE/8;

                        // Index Vbo
                        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexVboId);
                        glBufferSubData(GL_ELEMENT_ARRAY_BUFFER, indexWriteOffset, model.getIndices());
                        indexWriteOffset += model.getIndices().length*Integer.SIZE/8;
                    }
                }
            }
        }

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    public void render() {
        // Draw the mesh
        glBindVertexArray(vaoId);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);
        glEnableVertexAttribArray(3);

        // TODO verify vertex count
        glDrawElements(GL_TRIANGLES, vertexCount/3, GL_UNSIGNED_INT, 1);
        glDrawElementsBaseVertex(GL_TRIANGLES, 3*2, GL_UNSIGNED_INT, );

        // Restore state
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glDisableVertexAttribArray(3);

        glBindVertexArray(0);
        glBindTexture(GL_TEXTURE_2D, 0);
    }
}