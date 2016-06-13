package source;


import source.lwjgl.LWJGLGameWindow;
import source.lwjgl.LWJGLSprite;

public class ResourceFactory {
    // ResourceFactory is singleton
    private static final ResourceFactory single = new ResourceFactory();

    private ResourceFactory() {}

    public static ResourceFactory get() {
        return single;
    }

    private LWJGLGameWindow window;


    public LWJGLGameWindow getGameWindow() {
        // if we've yet to create the game window, create the appropriate one
        // now
        if (window == null) {
            window = new LWJGLGameWindow();
        }

        return window;
    }

    public LWJGLSprite getSprite(String ref) {
        if (window == null) {
            throw new RuntimeException("Attempt to retrieve sprite before game window was created");
        }
        return new LWJGLSprite(window, ref);
    }

    public SpriteBase getSprites(String ref, int numSprites, int width, int gap) {
        if (window == null) {
            throw new RuntimeException("Attempt to retrieve sprite before game window was created");
        }
         throw new RuntimeException("LWJGL not implemented yet");
    }

    public void resetAnimationLoop() {
        if (window == null) {
            throw new RuntimeException("Attempt to animate before game window created");
        }
        throw new RuntimeException("LWJGL not implemented yet");
    }
}
