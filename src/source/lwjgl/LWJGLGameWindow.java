package source.lwjgl;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

import source.GameWindow;
import source.GameWindowCallback;

import java.awt.event.KeyEvent;

public class LWJGLGameWindow implements GameWindow {

    long window;
    private GameWindowCallback callback;
    private int width;
    private int height;
    private TextureLoader textureLoader;
    private String title;

    // This allows reporting of errors even before GLFW is initialised
    private GLFWErrorCallback errorCallback = GLFWErrorCallback.createPrint(System.err);

    // This is a much neater way of reporting key presses and releases.
    private GLFWKeyCallback keyCallback = new GLFWKeyCallback() {
        @Override
        public void invoke(long window, int key, int scancode, int action, int mods) {
            if (action == GLFW.GLFW_PRESS) {
                switch (key) {
                    case GLFW.GLFW_KEY_LEFT:
                        LWJGLKeyboard.setPressed(LWJGLKeyboard.LEFT_KEY, true);
                        break;
                    case GLFW.GLFW_KEY_RIGHT:
                        LWJGLKeyboard.setPressed(LWJGLKeyboard.RIGHT_KEY, true);
                        break;
                    case GLFW.GLFW_KEY_SPACE:
                        LWJGLKeyboard.setPressed(LWJGLKeyboard.SPACE_KEY, true);
                        break;
                    case GLFW.GLFW_KEY_P:
                        LWJGLKeyboard.setPressed(LWJGLKeyboard.P_KEY, true);
                        break;
                }
            } else if (action == GLFW.GLFW_RELEASE) {
                switch (key) {
                    case GLFW.GLFW_KEY_LEFT:
                        LWJGLKeyboard.setPressed(LWJGLKeyboard.LEFT_KEY, false);
                        break;
                    case GLFW.GLFW_KEY_RIGHT:
                        LWJGLKeyboard.setPressed(LWJGLKeyboard.RIGHT_KEY, false);
                        break;
                    case GLFW.GLFW_KEY_SPACE:
                        LWJGLKeyboard.setPressed(LWJGLKeyboard.SPACE_KEY, false);
                        break;
                    case GLFW.GLFW_KEY_P:
                        LWJGLKeyboard.setPressed(LWJGLKeyboard.P_KEY, false);
                        break;
                }
            }
        }
    };

    private GLFWWindowCloseCallback windowCloseCallback = new GLFWWindowCloseCallback() {
        @Override
        public void invoke(long l) {
            // Destroys the window
            GLFW.glfwDestroyWindow(window);
            // Frees all callbacks.
            Callbacks.glfwFreeCallbacks(window);
            // Terminates GLFW
            GLFW.glfwTerminate();
            // Frees the last callback.
            errorCallback.free();
            if (callback != null) {
                callback.windowClosed();
            } else {
                System.exit(0);
            }
        }
    };

    public LWJGLGameWindow() {

    }

    TextureLoader getTextureLoader() {
        return textureLoader;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public void setResolution(int x, int y) {
        width = x;
        height = y;
    }

    @Override
    public void startRendering() {
        // Actually sets the callback.
        errorCallback.set();

        // This initialises GLFW and throws an exception on failure
        if (!GLFW.glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        // Sets some "hints", basically window options
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_TRUE); // the window will stay hidden after creation
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);

        window = GLFW.glfwCreateWindow(width, height, title, MemoryUtil.NULL, MemoryUtil.NULL);
        if (window == MemoryUtil.NULL) {
            GLFW.glfwTerminate();
            throw new RuntimeException("Failed to create the GLFW window");
        }

        // Actually attaches the callback to the window
        keyCallback.set(window);
        // Sets up window close callback with window
        windowCloseCallback.set(window);

        GLFW.glfwMakeContextCurrent(window);
        GL.createCapabilities();

        //textureLoader = new TextureLoader();

        if (callback != null) {
            callback.initialise();
        }

        // enable textures since we're going to use these for our sprites
       /* GL11.glEnable(GL11.GL_TEXTURE_2D);

        // disable the OpenGL depth test since we're rendering 2D graphics
        GL11.glDisable(GL11.GL_DEPTH_TEST);

        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();

        GL11.glOrtho(0, width, height, 0, -1, 1);*/

        gameLoop();
    }

    @Override
    public void setGameWindowCallback(GameWindowCallback callback) {
        this.callback = callback;
    }

    @Override
    public boolean isKeyPressed(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_LEFT:
                return LWJGLKeyboard.isPressed(LWJGLKeyboard.LEFT_KEY);
            case KeyEvent.VK_RIGHT:
                return LWJGLKeyboard.isPressed(LWJGLKeyboard.RIGHT_KEY);
            case KeyEvent.VK_SPACE:
                return LWJGLKeyboard.isPressed(LWJGLKeyboard.SPACE_KEY);
            case KeyEvent.VK_P:
                return LWJGLKeyboard.isPressed(LWJGLKeyboard.P_KEY);
            default:
                return false;
        }
    }

    private void gameLoop() {
        while (!GLFW.glfwWindowShouldClose(window)) {
            /*
            //GLFW provides a high resolution timer like this.
            double time = GLFW.glfwGetTime();

            // Buffers are swapped between rendering using
            GLFW.glfwSwapBuffers(window);

            // Events are processed by calling
            GLFW.glfwPollEvents();
            */

            // Set viewport and clear screen
            GL11.glViewport(0, 0, width, height);
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

            // Set up for rendering
            GL11.glMatrixMode(GL11.GL_MODELVIEW);
            GL11.glLoadIdentity();

            // if a callback has been registered notify it that the
            // screen is being rendered
            if (callback != null) {
                callback.frameRendering();
            }

            // flushing the graphics commands to the card is unnecessary because swapping buffers does that.
            GLFW.glfwSwapBuffers(window);
            GLFW.glfwPollEvents();
        }
    }

    public void testDraw() {
        GL11.glBegin(GL11.GL_TRIANGLES);
        GL11.glColor3f(1f, 0f, 0f);
        GL11.glVertex3f(-0.6f, -0.4f, 0f);
        GL11.glColor3f(0f, 1f, 0f);
        GL11.glVertex3f(0.6f, -0.4f, 0f);
        GL11.glColor3f(0f, 0f, 1f);
        GL11.glVertex3f(0f, 0.6f, 0f);
        GL11.glEnd();
    }
}
