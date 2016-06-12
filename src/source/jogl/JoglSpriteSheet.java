package source.jogl;

import java.io.IOException;

import com.jogamp.opengl.GL;

import source.SpriteSheetBase;

public class JoglSpriteSheet implements SpriteSheetBase{
    private Texture texture;
    private JoglGameWindow window;
    private int width;
    private int height;
    private int num;
    private int gap;
    private int current = 0;
    private boolean changedThisLoop = false;

    public JoglSpriteSheet(JoglGameWindow window,String ref, int numSprites, int width, int gap) {
        try {
            this.window = window;
            texture = window.getTextureLoader().getTexture(ref);

            this.width = width;
            this.height = texture.getImageHeight();
            this.num = numSprites;
            this.gap = gap;
        } catch (IOException e) {
            // a tad abrupt, but for our purposes if you can't find a
            // sprite's image you might as well give up.
            System.err.println("Unable to load texture: "+ref);
            System.exit(0);
        }
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public void draw(int x, int y) {
        // get hold of the GL content from the window in which we're drawning
        GL gl = window.getGL();

        // store the current model matrix
        gl.glPushMatrix();

        // bind to the appropriate texture for this sprite
        texture.bind(gl);
        // translate to the right location and prepare to draw
        gl.glTranslatef(x, y, 0);
        gl.glColor3f(1,1,1);

        // draw a quad textured to match the sprite
        gl.glBegin(GL.GL_QUADS);
        {
            gl.glTexCoord2f((current*width + current*gap)/texture.getWidthRatio(), 0);
            gl.glVertex2f(0, 0);
            gl.glTexCoord2f((current*width + current*gap)/texture.getWidthRatio(), texture.getHeight());
            gl.glVertex2f(0, height);
            gl.glTexCoord2f(((current+1)*width + current*gap)/texture.getWidthRatio(), texture.getHeight());
            gl.glVertex2f(width,height);
            gl.glTexCoord2f(((current+1)*width + current*gap)/texture.getWidthRatio(), 0);
            gl.glVertex2f(width,0);
        }
        gl.glEnd();

        // restore the model view matrix to prevent contamination
        gl.glPopMatrix();

    }

    @Override
    public boolean isChangedThisLoop() {
        return changedThisLoop;
    }

    @Override
    public void setChangedThisLoop(boolean changed) {
        changedThisLoop = changed;
    }

    @Override
    public void next() {
        current = (current + 1) % num;
    }

    @Override
    public void previous() {
        current = (current - 1) % num;
    }
}
