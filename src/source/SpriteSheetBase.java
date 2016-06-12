package source;

public interface SpriteSheetBase extends SpriteBase {
    boolean isChangedThisLoop();
    void setChangedThisLoop(boolean changed);
    void next();
    void previous();
}
