package circuitconstructor.scenes;

import circuitconstructor.GameObject;
import circuitconstructor.Scene;
import circuitconstructor.Transform;
import circuitconstructor.components.SpriteRenderer;
import circuitconstructor.renderer.Camera;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class Editor extends Scene {

    @Override
    public void init() {
        camera = new Camera(new Vector2f());

//        for (int x = 0; x < 10; x++) {
//            for (int y = 0; y < 10; y++) {
//                GameObject go = new GameObject("renderTest", new Transform(new Vector2f(x * 50f, y * 50f), new Vector2f(30f, 30f)));
//                go.addComponent(new SpriteRenderer(new Vector4f(0f, 0f, 0, 1f)));
//                addGameObjectToScene(go);
//            }
//        }

        GameObject go = new GameObject("renderTest", new Transform(new Vector2f(50f, 50f), new Vector2f(100f, 100f)));
        go.addComponent(new SpriteRenderer(new Vector4f(0f, 0f, 0, 1f)));
        addGameObjectToScene(go);
    }

    @Override
    public void update(float dTime) {

        gameObjects.forEach(c -> c.update(dTime));
        renderer.render();
    }
}
