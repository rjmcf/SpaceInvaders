
public interface GameWindow {
    void setTitle();
    void setResolution(int x, int y);
    void startRendering();
    void setGameWindowCallback(GameWindowCallback callBack);
    boolean isKeyPressed(int keyCode);
}
