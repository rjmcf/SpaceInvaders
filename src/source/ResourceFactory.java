package source;

import source.Java2D.Java2DGameWindow;
import source.Java2D.Java2DSpriteStore;

public class ResourceFactory {
    // ResourceFactory is singleton
    private static final ResourceFactory single = new ResourceFactory();

    private ResourceFactory() {}

    public static ResourceFactory get() {
        return single;
    }

    public static final int JAVA2D = 1;
    public static final int OPENGL_JOGL = 2;

    private int renderingType = JAVA2D;
    private GameWindow window;

    public void setRenderingType(int renderingType) {
        if ((renderingType != JAVA2D) && (renderingType != OPENGL_JOGL)) {
            // In general RuntimeException should be subclassed and thrown, not thrown directly.
            throw new RuntimeException("Unknown rendering type specified: "+renderingType);
        }

        // If the window has already been created then we have already created resources in
        // the current rendering method, we are not allowed to change rendering types
        if (window != null) {
            throw new RuntimeException("Attempt to change rendering method at game runtime");
        }

        this.renderingType = renderingType;
    }

    public GameWindow getGameWindow() {
        // if we've yet to create the game window, create the appropriate one
        // now
        if (window == null) {
            switch (renderingType) {
                case JAVA2D:
                {
                    window = new Java2DGameWindow();
                    break;
                }
                case OPENGL_JOGL:
                {
                    /*window = new JoglGameWindow();
                    break;*/
                    throw new RuntimeException("JOGL not implemented yet");
                }
            }
        }

        return window;
    }

    public SpriteBase getSprite(String ref) {
        if (window == null) {
            throw new RuntimeException("Attempt to retrieve sprite before game window was created");
        }

        switch (renderingType) {
            case JAVA2D:
            {
                return Java2DSpriteStore.get().getSprite((Java2DGameWindow) window,ref);
            }
            case OPENGL_JOGL:
            {
                /*return new JoglSprite((JoglGameWindow) window,ref);*/
                throw new RuntimeException("JOGL not implemented yet");
            }
        }

        throw new RuntimeException("Unknown rendering type: "+renderingType);
    }

    public SpriteBase getSprites(String ref, int numSprites, int width, int gap) {
        if (window == null) {
            throw new RuntimeException("Attempt to retrieve sprite before game window was created");
        }

        switch (renderingType) {
            case JAVA2D:
            {
                return Java2DSpriteStore.get().getSprites((Java2DGameWindow) window, ref, numSprites, width, gap);
            }
            case OPENGL_JOGL:
            {
                /*return new JoglSprite((JoglGameWindow) window,ref);*/
                throw new RuntimeException("JOGL not implemented yet");
            }
        }

        throw new RuntimeException("Unknown rendering type: "+renderingType);
    }

    public void resetAnimationLoop() {
        if (window == null) {
            throw new RuntimeException("Attempt to animate before game window created");
        }

        switch (renderingType) {
            case JAVA2D:
            {
                Java2DSpriteStore.get().resetAnimationLoop();
                return;
            }
            case OPENGL_JOGL:
            {
                /*return new JoglSprite((JoglGameWindow) window,ref);*/
                throw new RuntimeException("JOGL not implemented yet");
            }
        }

        throw new RuntimeException("Unknown rendering type: "+renderingType);

    }
}
