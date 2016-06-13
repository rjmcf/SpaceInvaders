package source;

import source.lwjgl.LWJGLGameWindow;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;


public class Game  implements GameWindowCallback{
    private LWJGLGameWindow window;
    private boolean paused = false;

    private Random r = new Random();

    private SpriteBase message;
    private SpriteBase pressAnyKey;
    private SpriteBase youWin;
    private SpriteBase gotYou;
    private boolean waitingForKeyPress = true;

    private ShipEntity ship;
    private double moveSpeed = 300;
    private long lastFire = 0;
    private long firingInterval = 500;
    private boolean fireHasBeenReleased = false;

    private int alienCount;
    private int fireChance;

    private int lastFrame = 0;
    private int currentFrames = 0;
    private long lastLoopTime = System.currentTimeMillis();
    private long delta;

    private boolean logicRequiredThisLoop = false;

    private ArrayList<Entity> entities = new ArrayList<>();
    private ArrayList<Entity> removeList = new ArrayList<>();

    private String windowTitle = "Space Invaders 103 - Version (0.3)";

    public Game() {
        // create a window based on a chosen rendering method
        window = ResourceFactory.get().getGameWindow();

        window.setResolution(800,600);
        window.setGameWindowCallback(this);
        window.setTitle(windowTitle);

        window.startRendering();
    }

    @Override
    public void initialise() {
        /*gotYou = ResourceFactory.get().getSprite("sprites/gotyou.gif");
        pressAnyKey = ResourceFactory.get().getSprite("sprites/pressanykey.gif");
        youWin = ResourceFactory.get().getSprite("sprites/youwin.gif");

        message = pressAnyKey;*/

        // setup the initial game state
        startGame();
    }

    private void startGame() {
        // clear out any existing entities and intialise a new set
        entities.clear();
        //initEntities();

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
        message = gotYou;
        waitingForKeyPress = true;
    }

    public void notifyWin() {
        message = youWin;
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
    @Override
    public void frameRendering() {

        // How long since last update?
        delta = System.currentTimeMillis() - lastLoopTime;
        lastLoopTime = System.currentTimeMillis();
        lastFrame += delta;
        currentFrames++;

        if (lastFrame >= 1000) {
            lastFrame = 0;
            window.setTitle(windowTitle+" (FPS: "+currentFrames+")");
            currentFrames = 0;
        }
        /*

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
            for (Entity e : entities) {
                if (e.isCurrentlyAnimated()) {
                    e.animate(delta);
                }
            }
        }
        ResourceFactory.get().resetAnimationLoop();


        // cycle round drawing all the entities we have in the game
        for (int i=0;i<entities.size();i++) {
            Entity entity = entities.get(i);

            entity.draw();
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
            message.draw(325,250);
        }

        /*
        // resolve the movement of the ship. First assume the ship
        // isn't moving. If either cursor key is pressed then
        // update the movement appropriately
        ship.setHorizontalMovement(0);

        boolean leftPressed = window.isKeyPressed(KeyEvent.VK_LEFT);
        boolean rightPressed = window.isKeyPressed(KeyEvent.VK_RIGHT);
        boolean firePressed = window.isKeyPressed(KeyEvent.VK_SPACE);

        if (!waitingForKeyPress) {
            if ((leftPressed) && (!rightPressed)) {
                ship.setHorizontalMovement(-moveSpeed);
            } else if ((rightPressed) && (!leftPressed)) {
                ship.setHorizontalMovement(moveSpeed);
            }

            // if we're pressing fire, attempt to fire
            if (firePressed) {
                tryToFire();
            }
        } else {
            if (!firePressed) {
                fireHasBeenReleased = true;
            }
            if ((firePressed) && (fireHasBeenReleased)) {
                waitingForKeyPress = false;
                fireHasBeenReleased = false;
                startGame();
            }
        }


        // if escape has been pressed, stop the game
        if (window.isKeyPressed(KeyEvent.VK_ESCAPE)) {
            System.exit(0);
        }

        // Wait a bit
        try { Thread.sleep(10); } catch (InterruptedException e) { e.printStackTrace(); }
        */
        window.testDraw();

    }



    @Override
    public void windowClosed() {
        System.exit(0);
    }



    // Our entry point to the game
    public static void main(String argv[]) {
        new Game();
    }
}
