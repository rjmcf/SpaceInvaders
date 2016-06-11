import java.awt.Rectangle;
import java.awt.Graphics;

public abstract class Entity {
    protected SpriteBase sprites;
    protected double x;
    protected double y;
    // Speeds are in pixels/second
    protected double dx;
    protected double dy;
    protected boolean animated;
    private Rectangle me = new Rectangle();
    private Rectangle him = new Rectangle();

    public Entity(String ref,int x,int y, boolean isAnimated) {
        animated = isAnimated;
        if (animated) {
            this.sprites = SpriteStore.get().getSprites(ref, 2, 40, 1);
        } else {
            this.sprites = SpriteStore.get().getSprite(ref);
        }
        this.x = x;
        this.y = y;
    }

    public int getX() { return (int) x; }
    public int getY() { return (int) y; }
    public void setVerticalMovement(double dy) { this.dy = dy; }
    public void setHorizontalMovement(double dx) { this.dx = dx; }
    public double getVerticalMovement() { return dy; }
    public double getHorizontalMovement() { return dx; }

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

    public void animate() { }

}
