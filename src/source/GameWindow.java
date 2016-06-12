package source;

public interface GameWindow {
    void setTitle(String title);
    void setResolution(int x, int y);
    void startRendering();
    void setGameWindowCallback(GameWindowCallback callBack);
    boolean isKeyPressed(int keyCode);
}
