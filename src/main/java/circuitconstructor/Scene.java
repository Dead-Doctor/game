package circuitconstructor;

import circuitconstructor.renderer.Camera;
import circuitconstructor.renderer.Renderer;
import circuitconstructor.scenes.Editor;
import circuitconstructor.scenes.Mainmenu;

import java.util.ArrayList;
import java.util.List;

public abstract class Scene {

    protected Renderer renderer = new Renderer();
    protected Camera camera;
    private boolean isRunning;
    protected List<GameObject> gameObjects = new ArrayList<>();

    private static Scene currentScene;

    public static Scene getCurrentScene() {
        return currentScene;
    }

    public static void changeScene(int index) {
        currentScene = switch (index) {
            default -> new Editor();
            case 1 -> new Mainmenu();
        };
        currentScene.init();
        currentScene.start();
    }

    public static void Update(float dTime) {
        if (currentScene == null) throw new IllegalStateException("No Scene selected!");
        currentScene.update(dTime);
    }

    public void init() {
    }

    public void start() {
        gameObjects.forEach(go -> {
            go.start();
            renderer.add(go);
        });
        isRunning = true;
    }

    public void addGameObjectToScene(GameObject go) {
        gameObjects.add(go);
        if (isRunning) {
            go.start();
            renderer.add(go);
        }
    }

    public abstract void update(float dTime);

    public Camera getCamera() {
        return camera;
    }
}
