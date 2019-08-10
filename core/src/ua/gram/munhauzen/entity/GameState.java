package ua.gram.munhauzen.entity;

import com.badlogic.gdx.utils.Array;

import ua.gram.munhauzen.history.History;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class GameState {

    private static final String tag = GameState.class.getSimpleName();

    public static boolean isPaused = false;
    public static boolean isMute = false;
    public static boolean isFinaleReached = false;

    public short NG;
    public boolean isContinueEnabled;
    public History history;
    public Array<Scenario> scenarioRegistry;
    public Array<Image> imageRegistry;
    public Array<Audio> audioRegistry;
    public Array<AudioFail> audioFailRegistry;
    public Array<Inventory> inventoryRegistry;
    public Array<Chapter> chapterRegistry;
    public MenuState menuState;

    public GameState() {
        scenarioRegistry = new Array<>();
        imageRegistry = new Array<>();
        audioRegistry = new Array<>();
        audioFailRegistry = new Array<>();
        inventoryRegistry = new Array<>();
        chapterRegistry = new Array<>();
        menuState = new MenuState();
    }

    public void reset() {
        Log.i(tag, "reset");
        try {
            isPaused = false;
            isFinaleReached = false;
        } catch (Throwable e) {
            Log.e(tag, e);
        }
    }

    public static void pause() {
        if (isPaused) return;
        Log.i(tag, "pause");
        isPaused = true;
    }

    public static void unpause() {
        if (!isPaused) return;
        Log.i(tag, "unpause");
        isPaused = false;
    }
}
