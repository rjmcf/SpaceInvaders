package source.jogl;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.Animator;

import source.GameWindow;
import source.GameWindowCallback;
import source.Keyboard;

public class JoglGameWindow implements GameWindow, GLEventListener {
    private Frame frame;
    private GameWindowCallback callback;
    /** The canvas which gives us access to OpenGL */
    private GLCanvas canvas;
    /** The OpenGL content, we use this to access all the OpenGL commands */
    private GL gl;

    /** The loader responsible for converting images into OpenGL textures */
    private TextureLoader textureLoader;

    private int width;
    private int height;

    public JoglGameWindow() {
        frame = new Frame();
    }

    public TextureLoader getTextureLoader() {
        return textureLoader;
    }

    GL getGL() {
        return gl;
    }

    // All this is GameWindow stuff.
    //<editor-fold desc="GameWindow implementation">
    @Override
    public void setTitle(String title) {
        frame.setTitle(title);
    }

    @Override
    public void setResolution(int x, int y) {
        width = x;
        height = y;
    }

    @Override
    public void startRendering() {
        /*canvas = new GLCanvas();
        canvas.addGLEventListener(this);
        canvas.setNoAutoRedrawMode(true);
        canvas.setFocusable(true);

        Keyboard.init(canvas);

        Animator animator = new Animator(canvas);

        // Setup the canvas inside the main window
        frame.setLayout(new BorderLayout());
        frame.add(canvas);
        frame.setResizable(false);
        canvas.setSize(width, height);
        frame.pack();
        frame.setVisible(true);

        // add a listener to respond to the user closing the window. If they
        // do we'd like to exit the game
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                if (callback != null) {
                    callback.windowClosed();
                } else {
                    System.exit(0);
                }
            }
        });

        // start a animating thread (provided by JOGL) to actively update
        // the canvas
        animator.start();
        */
    }

    @Override
    public void setGameWindowCallback(GameWindowCallback callback) {
        this.callback = callback;

    }

    @Override
    public boolean isKeyPressed(int keyCode) {
        return Keyboard.isPressed(keyCode);
    }
    //</editor-fold>


    // All this is GLEventListener stuff.
    //<editor-fold desc="GLEventListener implementation">
    @Override
    public void init(GLAutoDrawable glDrawable) {
        // get hold of the GL content
        gl = glDrawable.getGL();

        // enable textures since we're going to use these for our sprites
        gl.glEnable(GL.GL_TEXTURE_2D);

        // set the background colour of the display to black
        gl.glClearColor(0, 0, 0, 0);
        // set the area being rendered
        gl.glViewport(0, 0, width, height);
        // disable the OpenGL depth test since we're rendering 2D graphics
        gl.glDisable(GL.GL_DEPTH_TEST);

        textureLoader = new TextureLoader(gl);

        if (callback != null) {
            callback.initialise();
        }

    }

    @Override
    public void dispose(GLAutoDrawable glAutoDrawable) {
        System.exit(0);
    }

    @Override
    public void display(GLAutoDrawable glDrawable) {
       /* // get hold of the GL content
        gl = canvas.getGL();

        // clear the screen and setup for rendering
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();

        // if a callback has been registered notify it that the
        // screen is being rendered
        if (callback != null) {
            callback.frameRendering();
        }

        // flush the graphics commands to the card
        gl.glFlush();*/
    }

    @Override
    public void reshape(GLAutoDrawable glDrawable, int i, int i1, int i2, int i3) {
        /*gl = canvas.getGL();

        // at reshape we're going to tell OPENGL that we'd like to
        // treat the screen on a pixel by pixel basis by telling
        // it to use Orthographic projection.
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();


        gl.glOrtho(0, i2, i3, 0, -1, 1);*/
    }
    //</editor-fold>
}
