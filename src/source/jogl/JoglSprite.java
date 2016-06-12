package source.jogl;

import java.io.IOException;


import com.jogamp.opengl.GL;

import source.SpriteBase;

public class JoglSprite implements SpriteBase{
    private Texture texture;
    private JoglGameWindow window;
    private int width;
    private int height;

    public JoglSprite(JoglGameWindow window,String ref) {
        try {
            this.window = window;
            texture = window.getTextureLoader().getTexture(ref);

            width = texture.getImageWidth();
            height = texture.getImageHeight();
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
        /*// get hold of the GL content from the window in which we're drawning
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
            gl.glTexCoord2f(0, 0);
            gl.glVertex2f(0, 0);
            gl.glTexCoord2f(0, texture.getHeight());
            gl.glVertex2f(0, height);
            gl.glTexCoord2f(texture.getWidth(), texture.getHeight());
            gl.glVertex2f(width,height);
            gl.glTexCoord2f(texture.getWidth(), 0);
            gl.glVertex2f(width,0);
        }
        gl.glEnd();

        // restore the model view matrix to prevent contamination
        gl.glPopMatrix();*/
    }
}
