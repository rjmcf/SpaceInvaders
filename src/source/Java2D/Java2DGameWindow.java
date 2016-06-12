package source.java2d;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Canvas;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;

import source.GameWindow;
import source.GameWindowCallback;
import source.Keyboard;

public class Java2DGameWindow extends Canvas implements GameWindow{
    private BufferStrategy strategy;
    private boolean gameRunning = true;

    private JFrame frame;
    private int width;
    private int height;
    private GameWindowCallback callback;
    private Graphics2D g;

    public Java2DGameWindow() {
        frame = new JFrame();
    }

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
        JPanel panel = (JPanel) frame.getContentPane();
        panel.setPreferredSize(new Dimension(800,600));
        panel.setLayout(null);

        Keyboard.init(this);

        setBounds(0,0,width,height);
        panel.add(this);

        setIgnoreRepaint(true);

        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                if (callback != null) {
                    callback.windowClosed();
                } else {
                    System.exit(0);
                }
            }
        });

        requestFocus();

        createBufferStrategy(2);
        strategy = getBufferStrategy();

        if (callback != null) {
            callback.initialise();
        }

        gameLoop();
    }

    @Override
    public void setGameWindowCallback(GameWindowCallback callback) {
        this.callback = callback;

    }

    @Override
    public boolean isKeyPressed(int keyCode) {
        return Keyboard.isPressed(keyCode);
    }

    Graphics2D getDrawGraphics() {
        return g;
    }

    private void gameLoop() {
        while (gameRunning) {
            g = (Graphics2D) strategy.getDrawGraphics();
            g.setColor(Color.black);
            g.fillRect(0,0,800,600);

            if (callback != null) {
                callback.frameRendering();
            }

            g.dispose();
            strategy.show();
        }
    }
}
