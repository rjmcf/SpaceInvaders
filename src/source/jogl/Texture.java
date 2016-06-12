package source.jogl;


import com.jogamp.opengl.GL;

/**
 * A texture to be bound within JOGL. This object is responsible for
 * keeping track of a given OpenGL texture and for calculating the
 * texturing mapping coordinates of the full image.
 *
 * Since textures need to be powers of 2 the actual texture may be
 * considerably bigger that the source image and hence the texture
 * mapping coordinates need to be adjusted to match up drawing the
 * sprite against the texture.
 *
 * @author Kevin Glass
 */
public class Texture {
    /** The GL target type */
    private int target;
    /** The GL texture ID */
    private int textureID;
    /** The height of the image */
    private int height;
    /** The width of the image */
    private int width;
    /** The width of the texture */
    private int texWidth;
    /** The height of the texture */
    private int texHeight;
    /** The ratio of the width of the image to the texture */
    private float widthRatio;
    /** The ratio of the height of the image to the texture */
    private float heightRatio;

    /**
     * Create a new texture
     *
     * @param target The GL target
     * @param textureID The GL texture ID
     */
    public Texture(int target,int textureID) {
        this.target = target;
        this.textureID = textureID;
    }

    public void bind(GL gl) {
        gl.glBindTexture(target, textureID);
    }

    public float getHeight() {
        return texHeight;
    }

    public float getWidth() {
        return texWidth;
    }

    public void setHeight(int texHeight) {
        this.texHeight = texHeight;
        setHeightRatio();
    }

    public void setWidth(int texWidth) {
        this.texWidth = texWidth;
        setWidthRatio();
    }

    private void setHeightRatio() {
        if (texHeight != 0) {
            heightRatio = ((float) height)/texHeight;
        }
    }

    private void setWidthRatio() {
        if (texWidth != 0) {
            widthRatio = ((float) width)/texWidth;
        }
    }

    public float getWidthRatio() { return widthRatio; }

    public float getHeightRatio() { return heightRatio; }

    public int getImageHeight() {
        return height;
    }

    public int getImageWidth() {
        return width;
    }

    public void setImageHeight(int height) {
        this.height = height;
        setHeightRatio();
    }

    public void setImageWidth(int width) {
        this.width = width;
        setWidthRatio();
    }
}
