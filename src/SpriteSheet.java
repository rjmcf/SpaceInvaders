import java.awt.Image;
import java.awt.Graphics;
import java.util.ArrayList;

public class SpriteSheet extends SpriteBase {
    private ArrayList<Image> images = new ArrayList<>();
    private int current = 0;
    private int num;

    public SpriteSheet(ArrayList<Image> images) {
        num = images.size();
        for (int i = 0; i < num; i++) {
            this.images.add(images.get(i));
        }
    }

    @Override
    public int getWidth() {
        return images.get(0).getWidth(null);
    }

    @Override
    public int getHeight() {
        return images.get(0).getHeight(null);
    }

    @Override
    public void draw(Graphics g, int x, int y) { g.drawImage(images.get(current), x,y, null); }

    public void next() { current = (current + 1) % num; }

    public void previous() {
        current = (current - 1) % num;
    }
}
