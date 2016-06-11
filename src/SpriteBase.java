import java.awt.Graphics;


public abstract class SpriteBase {

    public abstract int getWidth();
    public abstract int getHeight();
    public abstract void draw(Graphics g, int x, int y);
}
