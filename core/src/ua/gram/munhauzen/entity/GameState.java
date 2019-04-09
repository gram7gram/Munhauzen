package ua.gram.munhauzen.entity;

import com.badlogic.gdx.utils.Array;

import ua.gram.munhauzen.history.History;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class GameState {

    public static final String INITIAL_OPTION = "BEGIN";
    private static final String tag = GameState.class.getSimpleName();

    public static boolean isPaused = false;
    public static boolean isMute = false;
    public static boolean isFinaleReached = false;
    public static boolean isHighEndDevice = true;

    public History history;
    public Array<Option> optionRegistry;
    public Array<Image> imageRegistry;
    public Array<Audio> audioRegistry;
    public Array<Item> itemRegistry;

    public GameState() {
        optionRegistry = new Array<>();
        imageRegistry = new Array<>();
        audioRegistry = new Array<>();
        itemRegistry = new Array<>();
    }

    public boolean hasHistory() {
        return history != null
                && history.getActiveSave().clickedBranches.size() > 0;
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
}
