package source.java2d;

import java.awt.Image;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import javax.imageio.ImageIO;

import source.SpriteBase;

public class Java2DSpriteStore {
    // Singleton
    private static Java2DSpriteStore single = new Java2DSpriteStore();

    private Java2DSpriteStore() {}

    public static Java2DSpriteStore get() {
        return single;
    }

    private HashMap<String, SpriteBase> sprites = new HashMap();

    public SpriteBase getSprite(Java2DGameWindow window, String ref) {
        // if we've already got the sprite in the cache then just return the existing version
        if (sprites.get(ref) != null) {
            return sprites.get(ref);
        }

        // otherwise, go away and grab the sprite from the resource loader
        BufferedImage sourceImage = null;

        try {
            URL url = this.getClass().getClassLoader().getResource(ref);

            if (url == null) {
                fail("Can't find ref: "+ref);
            }

            // use ImageIO to read the image in
            sourceImage = ImageIO.read(url);
        } catch (IOException e) {
            fail("Failed to load: "+ref);
        }

        // create an accelerated image of the right size to store our sprite in
        GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        Image image = gc.createCompatibleImage(sourceImage.getWidth(),sourceImage.getHeight(),Transparency.BITMASK);

        // draw our source image into the accelerated image
        image.getGraphics().drawImage(sourceImage,0,0,null);

        // create a sprite, add it the cache then return it
        SpriteBase sprite = new Java2DSprite(window,image);
        sprites.put(ref,sprite);

        return sprite;
    }

    public SpriteBase getSprites(Java2DGameWindow window, String ref, int num, int width, int gap) {
        if (sprites.get(ref) != null) {
            return sprites.get(ref);
        }

        BufferedImage sourceImage = null;

        try {
            URL url = this.getClass().getClassLoader().getResource(ref);

            if (url == null) {
                fail("Can't find ref: "+ref);
            }

            // use ImageIO to read the image in
            sourceImage = ImageIO.read(url);
        } catch (IOException ioe) {
            fail("Failed to load " + ref);
        }

        ArrayList<Image> spritesInSheet = new ArrayList<>();
        Image image;
        BufferedImage extract;
        GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        for (int i = 0; i < num; i++) {
            image = gc.createCompatibleImage(width,sourceImage.getHeight(),Transparency.BITMASK);
            extract = sourceImage.getSubimage(width*i + gap*i,0,width,sourceImage.getHeight());
            image.getGraphics().drawImage(extract,0,0,null);

            spritesInSheet.add(image);
        }

        SpriteBase sSheet = new Java2DSpriteSheet(window, spritesInSheet);

        sprites.put(ref, sSheet);

        return sSheet;
    }

    private void fail(String message) {
        // we're pretty dramatic here, if a resource isn't available
        // we dump the message and exit the game
        System.err.println(message);
        System.exit(0);
    }

    public void resetAnimationLoop() {
        for (SpriteBase sb : sprites.values()) {
            if (sb instanceof Java2DSpriteSheet) {
                ((Java2DSpriteSheet) sb).setChangedThisLoop(false);
            }
        }
    }
}
