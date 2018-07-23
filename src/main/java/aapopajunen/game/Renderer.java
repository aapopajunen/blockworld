package aapopajunen.game;

import aapopajunen.engine.Camera;
import aapopajunen.engine.Window;
import aapopajunen.engine.graphical.DirectionalLight;
import aapopajunen.engine.graphical.Model;
import aapopajunen.engine.graphical.ShaderProgram;
import aapopajunen.engine.graphical.Transformation;
import com.aapopajunen.engine.GameItem;
import com.aapopajunen.engine.Utils;
import com.aapopajunen.engine.Window;
import com.aapopajunen.engine.graph.*;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import static org.lwjgl.opengl.GL11.*;

public class Renderer {

    /**
     * Field of View in Radians
     */
    private static final float FOV = (float) Math.toRadians(60.0f);

    private static final float Z_NEAR = 0.01f;

    private static final float Z_FAR = 1000.f;

    private final Transformation transformation;

    private ShaderProgram shaderProgram;

    private final float specularPower;

    public Renderer() {
        transformation = new Transformation();
        specularPower = 10f;
    }

    public void init(Window window) throws Exception {
        // Create shader
        shaderProgram = new ShaderProgram();
        shaderProgram.createVertexShader(Utils.loadResource("/shaders/vertex.glsl"));
        shaderProgram.createFragmentShader(Utils.loadResource("/shaders/fragment.glsl"));
        shaderProgram.link();

        // Create uniforms for modelView and projection matrices and texture
        shaderProgram.createUniform("projectionMatrix");
        shaderProgram.createUniform("modelViewMatrix");
        shaderProgram.createUniform("texture_sampler");
        // Create uniform for material
        shaderProgram.createMaterialUniform("material");
        // Create lighting related uniforms
        shaderProgram.createUniform("specularPower");
        shaderProgram.createUniform("ambientLight");
        shaderProgram.createPointLightUniform("pointLight");
        shaderProgram.createDirectionalLightUniform("directionalLight");
    }

    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void render(Window window, Camera camera, GameItem[] gameItems, Vector3f ambientLight, DirectionalLight directionalLight) {

        clear();

        if ( window.isResized() ) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }

        shaderProgram.bind();

        // Update projection Matrix
        Matrix4f projectionMatrix = transformation.getProjectionMatrix(FOV, window.getWidth(), window.getHeight(), Z_NEAR, Z_FAR);
        shaderProgram.setUniform("projectionMatrix", projectionMatrix);

        // Update view Matrix
        Matrix4f viewMatrix = transformation.getViewMatrix(camera);

        // Update Light Uniforms
        shaderProgram.setUniform("ambientLight", ambientLight);

        // Get a copy of the directional light object and transform its position to view coordinates
        DirectionalLight currDirLight = new DirectionalLight(directionalLight);
        Vector4f dir = new Vector4f(currDirLight.getDirection(), 0);
        dir.mul(viewMatrix);
        currDirLight.setDirection(new Vector3f(dir.x, dir.y, dir.z));
        shaderProgram.setUniform("directionalLight", currDirLight);

        shaderProgram.setUniform("texture_sampler", 0);
        // Render each gameItem
//        for(GameItem gameItem : gameItems) {
//
//            Model model = gameItem.getMesh();
//            // Set model view matrix for this item
//
//            for(int i = 0; i < 1; i++) {
//                for(int j = 0; j < 1; j++){
//                    Matrix4f modelViewMatrix = transformation.getModelViewMatrix(gameItem, viewMatrix);
//                    modelViewMatrix.translate(i,0,-j);
//                    shaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
//                    // Render the mesh for this game item
//                    shaderProgram.setUniform("material", mesh.getMaterial());
//                    mesh.render();
//                }
//            }
//        }




        shaderProgram.unbind();
    }

    public void cleanup() {
        if (shaderProgram != null) {
            shaderProgram.cleanup();
        }
    }
}