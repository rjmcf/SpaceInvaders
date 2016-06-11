import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.image.BufferStrategy;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import java.util.ArrayList;
import java.util.Random;


public class Game extends Canvas {
    private BufferStrategy strategy;
    private boolean gameRunning = true;
    private boolean paused = false;

    private Random r = new Random();

    private String message = "";
    private boolean waitingForKeyPress = true;

    private ShipEntity ship;
    private double moveSpeed = 300;

    private int alienCount;

    private long lastFire = 0;

    private int fireChance;

    private long firingInterval = 500;

    private int lastFrame = 0;
    private int currentFrames = 0;
    private int frames = 0;

    private int lastAnimated = 0;
    private int animateTime = 500;

    private boolean leftPressed = false;
    private boolean rightPressed = false;
    private boolean firePressed = false;
    private boolean logicRequiredThisLoop = false;

    private ArrayList<Entity> entities = new ArrayList<>();
    private ArrayList<Entity> removeList = new ArrayList<>();

    public Game() {
        // This frame contains our game
        JFrame container = new JFrame("Space Invaders 101");

        // This panel is the actual content of our frame.
        JPanel panel = (JPanel) container.getContentPane();
        panel.setPreferredSize(new Dimension(800,600));
        panel.setLayout(null);

        // Sets bounds of our game, and places it in frame
        setBounds(0,0,800,600);
        panel.add(this);

        // Tells AWT not to bother repainting our canvas since we're going to do that ourselves in accelerated mode
        setIgnoreRepaint(true);

        // Make the window visible, and also disallow player from resizing it.
        container.pack();
        container.setResizable(false);
        container.setVisible(true);

        // add a listener to respond to the user closing the window. If they
        // do we'd like to exit the game
        container.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        // add a key input system (defined below) to our canvas
        // so we can respond to key pressed
        addKeyListener(new KeyInputHandler());

        // request the focus so key events come to us
        requestFocus();

        // A buffer strategy allows us to use accelerated graphics. We will use 2 buffers.
        createBufferStrategy(2);
        strategy = getBufferStrategy();

        // initialise the entities in our game so there's something
        // to see at startup
        initEntities();

    }

    private void startGame() {
        // clear out any existing entities and intialise a new set
        entities.clear();
        initEntities();

        // blank out any keyboard settings we might currently have
        leftPressed = false;
        rightPressed = false;
        firePressed = false;
        fireChance = 5000;
    }

    private void initEntities() {
        // create the player ship and place it roughly in the center of the screen
        ship = new ShipEntity(this,"sprites/ship.gif",370,550);
        entities.add(ship);

        // create a block of aliens (5 rows, by 12 aliens, spaced evenly)
        alienCount = 0;
        for (int row=0;row<5;row++) {
            for (int x=0;x<12;x++) {
                Entity alien = new AlienEntity(this,"sprites/alienAlt.gif",100+(x*50),(50)+row*30, 4, 40, 1);
                entities.add(alien);
                alienCount++;
            }
        }
    }

    public void updateLogic() {
        logicRequiredThisLoop = true;
    }

    public void removeEntity(Entity entity) {
        removeList.add(entity);
    }

    public void notifyDeath() {
        message = "Oh no! They got you, try again?";
        waitingForKeyPress = true;
    }

    public void notifyWin() {
        message = "Well done! You Win!";
        waitingForKeyPress = true;
    }

    public void notifyAlienKilled() {
        // reduce the alient count, if there are none left, the player has won!
        alienCount--;

        fireChance -= 83;

        if (alienCount == 0) {
            notifyWin();
        }

        // if there are still some aliens left then they all need to get faster, so
        // speed up all the existing aliens
        for (int i=0;i<entities.size();i++) {
            Entity entity = entities.get(i);

            if (entity instanceof AlienEntity) {
                // speed up by 2%
                entity.setHorizontalMovement(entity.getHorizontalMovement() * 1.02);
            }
        }
    }

    public void tryToFire() {
        // check that we have waiting long enough to fire
        if (System.currentTimeMillis() - lastFire < firingInterval) {
            return;
        }

        // if we waited long enough, create the shot entity, and record the time.
        lastFire = System.currentTimeMillis();
        ShotEntity shot = new ShotEntity(this,"sprites/shotAlt.gif",ship.getX()+10,ship.getY()-30,4,11,1);
        entities.add(shot);
    }

    public void enemyFire(AlienEntity ae) {
        AlienShotEntity shot = new AlienShotEntity(this, "sprites/shotBack.gif", ae.getX()+10, ae.getY());
        entities.add(shot);
    }

    // Our actual game loop
    public void gameLoop() {
        long lastLoopTime = System.currentTimeMillis();
        long delta;

        while(gameRunning) {
            // How long since last update?
            delta = System.currentTimeMillis() - lastLoopTime;
            lastLoopTime = System.currentTimeMillis();
            lastFrame += delta;

            if (lastFrame >= 1000) {
                lastFrame = 0;
                frames = currentFrames;
                currentFrames = 0;
            }

            // Get graphics context for accelerated surface and blank it out
            Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
            g.setColor(Color.black);
            g.fillRect(0,0,800,600);



            // cycle round asking each entity to move itself
            if (!waitingForKeyPress && !paused) {
                for (int i=0;i<entities.size();i++) {
                    Entity entity = (Entity) entities.get(i);

                    entity.move(delta);

                    if (entity instanceof AlienEntity) {
                        int fire = r.nextInt(fireChance);
                        if (fire == 1) {
                            enemyFire((AlienEntity) entity);
                        }
                    }
                }
            }

            if (!waitingForKeyPress && !paused) {
                lastAnimated += delta;
                if (lastAnimated >= animateTime) {
                    lastAnimated = 0;
                    for (Entity e : entities) {
                        if (e.isCurrentlyAnimated()) {
                            e.animate();
                        }
                    }
                }
            }
            SpriteStore.get().resetAnimationLoop();

            // cycle round drawing all the entities we have in the game
            for (int i=0;i<entities.size();i++) {
                Entity entity = entities.get(i);

                entity.draw(g);
            }

            // brute force collisions, compare every entity against
            // every other entity. If any of them collide notify
            // both entities that the collision has occurred
            for (int p=0;p<entities.size();p++) {
                for (int s=p+1;s<entities.size();s++) {
                    Entity me = (Entity) entities.get(p);
                    Entity him = (Entity) entities.get(s);

                    if (me.collidesWith(him)) {
                        me.collidedWith(him);
                        him.collidedWith(me);
                    }
                }
            }

            // remove any entity that has been marked for clear up
            entities.removeAll(removeList);
            removeList.clear();

            // if a game event has indicated that game logic should
            // be resolved, cycle round every entity requesting that
            // their personal logic should be considered.
            if (logicRequiredThisLoop) {
                for (int i=0;i<entities.size();i++) {
                    Entity entity = entities.get(i);
                    entity.doLogic();
                }

                logicRequiredThisLoop = false;
            }

            // if we're waiting for an "any key" press then draw the
            // current message
            if (waitingForKeyPress) {
                g.setColor(Color.white);
                g.drawString(message,(800-g.getFontMetrics().stringWidth(message))/2,250);
                g.drawString("Press any key",(800-g.getFontMetrics().stringWidth("Press any key"))/2,300);
            }
            g.setColor(Color.white);
            g.drawString("Framecount:", 10,20);
            if (!waitingForKeyPress && !paused) {
                g.drawString(Integer.toString(frames), 85,20);
            }

            if (paused) {
                g.drawString("Paused", (800-g.getFontMetrics().stringWidth("Paused"))/2, 300);
            }

            // We've drawn enough, so clear graphics and flip buffer
            g.dispose();
            strategy.show();

            // resolve the movement of the ship. First assume the ship
            // isn't moving. If either cursor key is pressed then
            // update the movement appropriately
            ship.setHorizontalMovement(0);

            if ((leftPressed) && (!rightPressed)) {
                ship.setHorizontalMovement(-moveSpeed);
            } else if ((rightPressed) && (!leftPressed)) {
                ship.setHorizontalMovement(moveSpeed);
            }

            // if we're pressing fire, attempt to fire
            if (firePressed) {
                tryToFire();
            }

            currentFrames++;

            // Wait a bit
            try { Thread.sleep(10); } catch (InterruptedException e) { e.printStackTrace(); }
        }
    }

    private class KeyInputHandler extends KeyAdapter {
        /** The number of key presses we've had while waiting for an "any key" press */
        private int pressCount = 1;

        public void keyPressed(KeyEvent e) {
            // if we're waiting for an "any key" typed then we don't
            // want to do anything with just a "press"
            if (waitingForKeyPress) {
                return;
            }


            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                leftPressed = true;
            }
            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                rightPressed = true;
            }
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                firePressed = true;
            }
        }

        public void keyReleased(KeyEvent e) {
            // if we're waiting for an "any key" typed then we don't
            // want to do anything with just a "released"
            if (waitingForKeyPress) {
                return;
            }

            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                leftPressed = false;
            }
            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                rightPressed = false;
            }
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                firePressed = false;
            }
        }

        public void keyTyped(KeyEvent e) {
            // if we're waiting for a "any key" type then
            // check if we've recieved any recently. We may
            // have had a keyType() event from the user releasing
            // the shoot or move keys, hence the use of the "pressCount"
            // counter.
            if (waitingForKeyPress) {
                if (pressCount == 1) {
                    // since we've now recieved our key typed
                    // event we can mark it as such and start
                    // our new game
                    waitingForKeyPress = false;
                    startGame();
                    pressCount = 0;
                } else {
                    pressCount++;
                }
            }

            // if we hit escape, then quit the game
            if (e.getKeyChar() == KeyEvent.VK_ESCAPE) {
                System.exit(0);
            }

            if (e.getKeyChar() == 'p') {
                paused = !paused;
            }
        }
    }



    // Our entry point to the game
    public static void main(String[] args) {
        Game g = new Game();

        g.gameLoop();
    }
}
