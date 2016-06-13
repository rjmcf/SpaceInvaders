package source.lwjgl;

public class LWJGLKeyboard {
    public static final int LEFT_KEY = 0;
    public static final int RIGHT_KEY = 1;
    public static final int SPACE_KEY = 2;
    public static final int P_KEY = 3;

    private static boolean[] keys = new boolean[4];

    public static boolean isPressed(int key) {
        return keys[key];
    }
    public static void setPressed(int key, boolean pressed) { keys[key] = pressed; }
}
