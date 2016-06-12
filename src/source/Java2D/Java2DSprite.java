package source.java2d;

import java.awt.Image;

import source.SpriteBase;

public class Java2DSprite implements SpriteBase{
    private Image image;
    private Java2DGameWindow window;

    public Java2DSprite(Java2DGameWindow window, Image image) {
        this.image = image;
        this.window = window;
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
    public void draw(int x, int y) {
        window.getDrawGraphics().drawImage(image,x,y,null);
    }
}
