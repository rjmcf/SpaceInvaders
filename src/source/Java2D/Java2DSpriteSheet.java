package source.java2d;

import source.SpriteSheetBase;

import java.awt.Image;
import java.util.ArrayList;

public class Java2DSpriteSheet implements SpriteSheetBase {
    private Java2DGameWindow window;
    private ArrayList<Image> images = new ArrayList<>();
    private int current = 0;
    private int num;
    private boolean changedThisLoop = false;

    public Java2DSpriteSheet(Java2DGameWindow window, ArrayList<Image> images) {
        this.window = window;
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
    public void draw(int x, int y) {
        window.getDrawGraphics().drawImage(images.get(current), x,y, null);
    }

    @Override
    public void next() { current = (current + 1) % num; }

    @Override
    public void previous() {
        current = (current - 1) % num;
    }

    @Override
    public boolean isChangedThisLoop() { return changedThisLoop; }

    @Override
    public void setChangedThisLoop(boolean changed) { changedThisLoop = changed; }
}
