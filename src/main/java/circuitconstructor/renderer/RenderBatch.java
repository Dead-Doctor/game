package circuitconstructor.renderer;

import circuitconstructor.Scene;
import circuitconstructor.Transform;
import circuitconstructor.components.SpriteRenderer;
import org.joml.Vector4f;

import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.opengl.GL30.*;

public class RenderBatch {
    // Vertex
    // ===========================================
    // Pos              Color
    // float, float,    float, float, float, float,
    private final int POS_SIZE = 2;
    private final int COLOR_SIZE = 4;

    private final int POS_OFFSET = 0;
    private final int COLOR_OFFSET = POS_OFFSET + POS_SIZE * Float.BYTES;

    private final int VERTEX_SIZE = POS_SIZE + COLOR_SIZE;
    private final int VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;

    private SpriteRenderer[] sprites;
    private int numSprites = 0;
    private boolean hasRoom;
    private float[] vertices;

    private int vaoID, vboID;
    private int maxBatchSize;
    private Shader shader;

    public RenderBatch(int maxBatchSize) {
        this.shader = new Shader("assets/shaders/default.glsl");
        this.shader.compile();

        this.maxBatchSize = maxBatchSize;
        this.sprites = new SpriteRenderer[maxBatchSize];

        // 4 vertices quads
        this.vertices = new float[maxBatchSize * 4 * VERTEX_SIZE];

        this.numSprites = 0;
        this.hasRoom = false;
    }

    public void start() {
        // generate and bind a vertex array object
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        // allocate space for vertices
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, (long) vertices.length * Float.BYTES, GL_DYNAMIC_DRAW);

        int eboID = glGenBuffers();
        int[] indices = generateIndices();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        // enable the buffer attribute pointers
        glVertexAttribPointer(0, POS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, POS_OFFSET);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, COLOR_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, COLOR_OFFSET);
        glEnableVertexAttribArray(1);
    }

    public void addSprite(SpriteRenderer sprite) {
        int i = numSprites;
        sprites[i] = sprite;
        numSprites++;

        // add properties to local vertices array
        loadVertexProperties(i);

        if (numSprites >= maxBatchSize) {
            hasRoom = false;
        }
    }

    public void render() {
        // TODO: only rebuffer changed
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);

        // use shader
        shader.use();
        shader.uploadMat4f("uProjection", Scene.getCurrentScene().getCamera().getProjectionMatrix());
        shader.uploadMat4f("uView", Scene.getCurrentScene().getCamera().getViewMatrix());

        glDrawElements(GL_TRIANGLES, numSprites * 6, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);

        shader.detach();
    }

    private void loadVertexProperties(int index) {
        SpriteRenderer sprite = sprites[index];

        // find offset within array (4 vertices per sprite)
        int offset = index * 4 * VERTEX_SIZE;

        Vector4f color = sprite.getColor();

        // add vertice with the appropiate properties

        boolean x = true;
        boolean y = true;
        for (int i = 0; i < 4; i++) {
            switch (i) {
                case 1 -> y = false;
                case 2 -> x = false;
                case 3 -> y = true;
            }

            // load position
            Transform transform = sprite.gameObject.transform;

            System.out.println(transform.position.x + (x ? transform.scale.x : 0));
            System.out.println(transform.position.y + (y ? transform.scale.y : 0));

            vertices[offset] = transform.position.x + (x ? transform.scale.x : 0);
            vertices[offset + 1] = transform.position.y + (y ? transform.scale.y : 0);

            // load color
            vertices[offset + 2] = color.x;
            vertices[offset + 3] = color.y;
            vertices[offset + 4] = color.z;
            vertices[offset + 5] = color.w;

            offset += VERTEX_SIZE;
        }
    }

    private int[] generateIndices() {
        // 6 indices per quad (2 triangles)
        int[] elements = new int[6 * maxBatchSize];
        for (int i = 0; i < maxBatchSize; i++) {
            loadElementIndices(elements, i);
        }
        return elements;
    }

    private void loadElementIndices(int[] elements, int i) {
        // index in new array
        // 6 indices per quad (2 triangles)
        int offsetIndices = i * 6;

        // index of point
        // 4 points per quad (1 rectangle)
        int offsetPoints = i * 4;

        // final quad indices == 3, 2, 0, 0, 2, 1,  7, 6, 4, 4, 6, 5
        // triangle 1
        elements[offsetIndices] = offsetPoints + 3;
        elements[offsetIndices + 1] = offsetPoints + 2;
        elements[offsetIndices + 2] = offsetPoints + 0;
        //triangle 2
        elements[offsetIndices + 3] = offsetPoints + 0;
        elements[offsetIndices + 4] = offsetPoints + 2;
        elements[offsetIndices + 5] = offsetPoints + 1;

    }

    public boolean hasRoom() {
        return hasRoom;
    }
}
