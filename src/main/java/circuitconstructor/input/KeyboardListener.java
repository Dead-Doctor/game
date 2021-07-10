package circuitconstructor.input;

import static org.lwjgl.glfw.GLFW.*;

public class KeyboardListener {
    private static boolean[] keys = new boolean[GLFW_KEY_LAST + 1];

    public static void keyboardCallback(long window, int key, int scancode, int action, int mods) {
        if (action == GLFW_PRESS)
            keys[key] = true;
        else if (action == GLFW_RELEASE)
            keys[key] = false;
    }

    public static boolean isKeyPressed(int key) {
        return keys[key];
    }
}
