package source.jogl;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Hashtable;
import javax.imageio.ImageIO;

import com.jogamp.opengl.GL;


public class TextureLoader {

    private HashMap<String,Texture> table = new HashMap<>();
    /** The GL context used to load textures */
    private GL gl;
    /** The colour model including alpha for the GL image */
    private ColorModel glAlphaColorModel;
    /** The colour model for the GL image */
    private ColorModel glColorModel;

    public TextureLoader(GL gl) {
        this.gl = gl;

        glAlphaColorModel = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB),
                new int[] {8,8,8,8},
                true,
                false,
                ComponentColorModel.TRANSLUCENT,
                DataBuffer.TYPE_BYTE);

        glColorModel = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB),
                new int[] {8,8,8,0},
                false,
                false,
                ComponentColorModel.OPAQUE,
                DataBuffer.TYPE_BYTE);
    }

    private int createTextureID()
    {
        IntBuffer tmp = IntBuffer.allocate(1);
        gl.glGenTextures(1, tmp);
        return tmp.get(0);
    }

    public Texture getTexture(String resourceName) throws IOException {
        Texture tex = (Texture) table.get(resourceName);

        if (tex != null) {
            return tex;
        }

        tex = getTexture(resourceName,
                GL.GL_TEXTURE_2D, // target
                GL.GL_RGBA,     // dst pixel format
                GL.GL_LINEAR, // min filter (unused)
                GL.GL_LINEAR);

        table.put(resourceName,tex);

        return tex;
    }

    public Texture getTexture(String resourceName,
                              int target,
                              int dstPixelFormat,
                              int minFilter,
                              int magFilter) throws IOException
    {
        int srcPixelFormat = 0;

        // create the texture ID for this texture
        int textureID = createTextureID();
        Texture texture = new Texture(target,textureID);

        // bind this texture
        gl.glBindTexture(target, textureID);

        BufferedImage bufferedImage = loadImage(resourceName);
        texture.setImageWidth(bufferedImage.getWidth());
        texture.setImageHeight(bufferedImage.getHeight());

        if (bufferedImage.getColorModel().hasAlpha()) {
            srcPixelFormat = GL.GL_RGBA;
        } else {
            srcPixelFormat = GL.GL_RGB;
        }

        // convert that image into a byte buffer of texture data
        ByteBuffer textureBuffer = convertImageData(bufferedImage,texture);

        if (target == GL.GL_TEXTURE_2D)
        {
            gl.glTexParameteri(target, GL.GL_TEXTURE_MIN_FILTER, minFilter);
            gl.glTexParameteri(target, GL.GL_TEXTURE_MAG_FILTER, magFilter);
        }

        // produce a texture from the byte buffer
        gl.glTexImage2D(target,
                0,
                dstPixelFormat,
                get2Fold(bufferedImage.getWidth()),
                get2Fold(bufferedImage.getHeight()),
                0,
                srcPixelFormat,
                GL.GL_UNSIGNED_BYTE,
                textureBuffer );

        return texture;
    }

    private int get2Fold(int fold) {
        int ret = 2;
        while (ret < fold) {
            ret *= 2;
        }
        return ret;
    }

    private ByteBuffer convertImageData(BufferedImage bufferedImage,Texture texture) {
        ByteBuffer imageBuffer = null;
        WritableRaster raster;
        BufferedImage texImage;

        int texWidth = 2;
        int texHeight = 2;

        // find the closest power of 2 for the width and height
        // of the produced texture
        while (texWidth < bufferedImage.getWidth()) {
            texWidth *= 2;
        }
        while (texHeight < bufferedImage.getHeight()) {
            texHeight *= 2;
        }

        texture.setHeight(texHeight);
        texture.setWidth(texWidth);

        // create a raster that can be used by OpenGL as a source
        // for a texture
        if (bufferedImage.getColorModel().hasAlpha()) {
            raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE,texWidth,texHeight,4,null);
            texImage = new BufferedImage(glAlphaColorModel,raster,false,new Hashtable());
        } else {
            raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE,texWidth,texHeight,3,null);
            texImage = new BufferedImage(glColorModel,raster,false,new Hashtable());
        }

        // copy the source image into the produced image
        Graphics g = texImage.getGraphics();
        g.setColor(new Color(0f,0f,0f,0f));
        g.fillRect(0,0,texWidth,texHeight);
        g.drawImage(bufferedImage,0,0,null);

        // build a byte buffer from the temporary image
        // that be used by OpenGL to produce a texture.
        byte[] data = ((DataBufferByte) texImage.getRaster().getDataBuffer()).getData();

        imageBuffer = ByteBuffer.allocateDirect(data.length);
        imageBuffer.order(ByteOrder.nativeOrder());
        imageBuffer.put(data, 0, data.length);

        return imageBuffer;
    }

    private BufferedImage loadImage(String ref) throws IOException
    {
        URL url = TextureLoader.class.getClassLoader().getResource(ref);

        if (url == null) {
            throw new IOException("Cannot find: "+ref);
        }

        BufferedImage bufferedImage = ImageIO.read(new BufferedInputStream(getClass().getClassLoader().getResourceAsStream(ref)));

        return bufferedImage;
    }
}
