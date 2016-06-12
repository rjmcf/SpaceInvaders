
public interface GameWindowCallback {
    /**
     * Notification to initialise resources.
     */
    void initialise();

    /**
     * Notification that display is being rendered. Implementor renders scene and updates game logic.
     */
    void frameRendering();

    /**
     * Notification that game window has been closed.
     */
    void windowClosed();
}
