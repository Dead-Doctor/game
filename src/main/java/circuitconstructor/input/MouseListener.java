package circuitconstructor.input;

import static org.lwjgl.glfw.GLFW.*;

public class MouseListener {
    private static double posX = 0d;
    private static double posY = 0d;
    private static double lastPosX = 0d;
    private static double lastPosY = 0d;

    private static double scrollX = 0d;
    private static double scrollY = 0d;
    private static double zoom = 0d;

    private static boolean dragging = false;

    private static boolean[] buttons = new boolean[GLFW_MOUSE_BUTTON_LAST + 1];

    public static void cursorPositionCallback(long window, double xpos, double ypos) {
        lastPosX = posX;
        lastPosY = posY;
        posX = xpos;
        posY = ypos;

        if (!(posX == lastPosX && posY == lastPosY))
            if (buttons[GLFW_MOUSE_BUTTON_LEFT])
                dragging = true;
    }

    public static void mouseButtonCallback(long window, int button, int action, int mods) {
        if (action == GLFW_PRESS) {
            buttons[button] = true;
        } else if (action == GLFW_RELEASE) {
            if (button == GLFW_MOUSE_BUTTON_LEFT) dragging = false;
            buttons[button] = false;
        }
    }

    public static void scrollCallback(long window, double xoffset, double yoffset) {
        boolean shift = glfwGetKey(window, GLFW_KEY_LEFT_SHIFT) == GLFW_PRESS || glfwGetKey(window, GLFW_KEY_RIGHT_SHIFT) == GLFW_PRESS;
        boolean control = glfwGetKey(window, GLFW_KEY_LEFT_CONTROL) == GLFW_PRESS || glfwGetKey(window, GLFW_KEY_RIGHT_CONTROL) == GLFW_PRESS;

        if (!control) {
            if (!shift) {
                scrollX += xoffset;
                scrollY += yoffset;
            } else {
                scrollX += yoffset;
                scrollY += xoffset;
            }
        } else {
            zoom += xoffset;
            zoom += yoffset;
        }
    }

    public static void endFrame() {
        posX = 0;
        posY = 0;

        scrollX = 0d;
        scrollY = 0d;
        zoom = 0d;
    }

    public static float getX() {
        return (float) posX;
    }

    public static float getY() {
        return (float) posY;
    }

    public static float getScrollX() {
        return (float) scrollX;
    }

    public static float getScrollY() {
        return (float) scrollY;
    }

    public static float getZoom() {
        return (float) zoom;
    }

    public static boolean isDragging() {
        return dragging;
    }

    public static boolean isButtonPressed(int button) {
        return buttons[button];
    }
}
