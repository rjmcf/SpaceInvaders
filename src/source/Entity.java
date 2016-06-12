package source;

import java.awt.Rectangle;
import java.awt.Graphics;

public abstract class Entity {
    protected SpriteBase sprites;
    protected double x;
    protected double y;
    // Speeds are in pixels/second
    private double dx;
    private double dy;
    protected long sinceLastFrame = 0;
    protected long frameLength;
    protected boolean currentlyAnimated;
    private Rectangle me = new Rectangle();
    private Rectangle him = new Rectangle();

    public Entity(String ref,int x,int y, boolean canBeAnimated, boolean isAnimated) {
        currentlyAnimated = isAnimated;
        if (!canBeAnimated) {
            this.sprites = SpriteStore.get().getSprite(ref);
        }
        this.x = x;
        this.y = y;
    }

    public Entity(String ref,int x,int y, boolean canBeAnimated, boolean isAnimated, long frameLenth, int numSprites, int width, int gap) {
        this(ref,x,y,canBeAnimated,isAnimated);
        this.sprites = SpriteStore.get().getSprites(ref,numSprites,width,gap);
        this.frameLength = frameLenth;
    }

    public int getX() { return (int) x; }
    public int getY() { return (int) y; }
    public void setVerticalMovement(double dy) { this.dy = dy; }
    public void setHorizontalMovement(double dx) { this.dx = dx; }
    public double getVerticalMovement() { return dy; }
    public double getHorizontalMovement() { return dx; }
    public boolean isCurrentlyAnimated() { return currentlyAnimated; }
    public void setCurrentlyAnimated(boolean animated) { currentlyAnimated = animated; }

    public void move(long delta) {
        // update the location of the entity based on move speeds
        x += (delta * dx) / 1000;
        y += (delta * dy) / 1000;
    }

    public void draw(Graphics g) {
        sprites.draw(g,(int) x,(int) y);
    }

    public boolean collidesWith(Entity other) {
        me.setBounds((int) x,(int) y,sprites.getWidth(),sprites.getHeight());
        him.setBounds((int) other.x,(int) other.y,other.sprites.getWidth(),other.sprites.getHeight());

        return me.intersects(him);
    }

    public abstract void collidedWith(Entity other);

    public void doLogic() { }

    public void animate(long delta) {
        sinceLastFrame += delta;
        if (sinceLastFrame >= frameLength) {
            sinceLastFrame = 0;
            if (!((SpriteSheet)sprites).isChangedThisLoop()) {
                ((SpriteSheet) sprites).next();
                ((SpriteSheet) sprites).setChangedThisLoop(true);
            }
        }
    }
}
