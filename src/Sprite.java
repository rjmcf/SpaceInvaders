import java.awt.Image;
import java.awt.Graphics;

public class Sprite extends SpriteBase {
    /** The image to be drawn for this sprite */
    private Image image;

    public Sprite(Image image) {
        this.image = image;
    }

    @Override
    public int getWidth() {
        return image.getWidth(null);
    }

    @Override
    public int getHeight() {
        return image.getHeight(null);
    }

    @Override
    public void draw(Graphics g,int x,int y) {
        g.drawImage(image,x,y,null);
    }
}
