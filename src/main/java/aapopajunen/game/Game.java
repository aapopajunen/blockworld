package aapopajunen.game;

import aapopajunen.engine.Camera;
import aapopajunen.engine.GameItem;
import aapopajunen.engine.GameLogic;
import aapopajunen.engine.MouseInput;
import aapopajunen.engine.Window;
import aapopajunen.engine.graph.*;
import aapopajunen.engine.graphical.Model;
import aapopajunen.engine.graphical.Texture;
import aapopajunen.entity.*;
import aapopajunen.util.OBJLoader;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static aapopajunen.entity.BlockType.*;
import static aapopajunen.entity.Direction.*;
import static aapopajunen.util.OBJLoader.loadModel;
import static org.lwjgl.glfw.GLFW.*;

public class Game implements GameLogic {

    private static final float MOUSE_SENSITIVITY = 0.2f;

    private final Vector3f cameraInc;

    private final Renderer renderer;

    private final Camera camera;

    private Chunk chunk;

    private Vector3f ambientLight;

    private static final float CAMERA_POS_STEP = 0.05f;

    public Game() {
        renderer = new Renderer();
        camera = new Camera();
        cameraInc = new Vector3f(0.0f, 0.0f, 0.0f);
    }

    @Override
    public void init(Window window) throws Exception {
        renderer.init(window);

        float reflectance = 1f;
        //Model mesh = OBJLoader.loadModel("/models/bunny.obj");
        //Material material = new Material(new Vector3f(0.2f, 0.5f, 0.5f), reflectance);

        Block.setFace(X_POS, loadModel("/resources/models/+x.obj"));
        Block.setFace(X_NEG, loadModel("/resources/models/-x.obj"));
        Block.setFace(Y_POS, loadModel("/resources/models/+y.obj"));
        Block.setFace(Y_NEG, loadModel("/resources/models/-y.obj"));
        Block.setFace(Z_POS, loadModel("/resources/models/+z.obj"));
        Block.setFace(Z_NEG, loadModel("/resources/models/-z.obj"));

        TextureManager.addFaceTextCoords(DIRT, X_POS, loadModel("/resources/models/dirt_+x").getTextCoords());

        Model mesh = loadModel("/Users/Aapo/Documents/IntelliJProjects/mapgenerator/src/main/resources/models/grass.obj");
        Texture texture = new Texture("/Users/Aapo/Documents/IntelliJProjects/mapgenerator/src/main/resources/textures/minecraft.png");

        ambientLight = new Vector3f(0.5f, 0.5f, 0.5f);
    }

    @Override
    public void input(Window window, MouseInput mouseInput) {
        int speed = 5;
        cameraInc.set(0, 0, 0);
        if (window.isKeyPressed(GLFW_KEY_W)) {
            cameraInc.z = -speed;
        } else if (window.isKeyPressed(GLFW_KEY_S)) {
            cameraInc.z = speed;
        }
        if (window.isKeyPressed(GLFW_KEY_A)) {
            cameraInc.x = -speed;
        } else if (window.isKeyPressed(GLFW_KEY_D)) {
            cameraInc.x = speed;
        }
        if (window.isKeyPressed(GLFW_KEY_Z)) {
            cameraInc.y = -speed;
        } else if (window.isKeyPressed(GLFW_KEY_X)) {
            cameraInc.y = speed;
        }
    }

    @Override
    public void update(float interval, MouseInput mouseInput) {
        // Update camera position
        camera.movePosition(cameraInc.x * CAMERA_POS_STEP, cameraInc.y * CAMERA_POS_STEP, cameraInc.z * CAMERA_POS_STEP);

        // Update camera based on mouse            
        if (mouseInput.isLeftButtonPressed()) {
            Vector2f rotVec = mouseInput.getDisplVec();
            camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY, rotVec.y * MOUSE_SENSITIVITY, 0);
        }
    }

    @Override
    public void render(Window window) {
        renderer.render(window, camera, gameItems, ambientLight, pointLight, null);
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
        for (GameItem gameItem : gameItems) {
            gameItem.getMesh().cleanUp();
        }
    }

}
