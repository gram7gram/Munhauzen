package ua.gram.munhauzen.entity;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;

import java.util.ArrayList;

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

    public History history;
    public ArrayList<Scenario> scenarioRegistry;
    public ArrayList<Image> imageRegistry;
    public ArrayList<Audio> audioRegistry;
    public ArrayList<AudioFail> audioFailRegistry;
    public ArrayList<Inventory> inventoryRegistry;
    public ArrayList<Chapter> chapterRegistry;
    public MenuState menuState;
    public GalleryState galleryState;
    public FailsState failsState;

    public void reset() {
        Log.i(tag, "reset");
        try {
            isPaused = false;
            isFinaleReached = false;

            history = new History();
            menuState = new MenuState();
            galleryState = new GalleryState();
            failsState = new FailsState();

        } catch (Throwable e) {
            Log.e(tag, e);
        }
    }

    public static void pause(String referer) {
        if (isPaused) return;
        Log.i(tag, "pause " + referer);
        isPaused = true;
    }

    public static void unpause(String referer) {
        if (!isPaused) return;
        Log.i(tag, "unpause " + referer);
        isPaused = false;
    }

    public static void clearTimer() {
        Log.i(tag, "clearTimer");
        Timer.instance().clear();
    }

    public Array<Image> getGalleryImages() {
        Array<Image> items = new Array<>();

        for (Image image : imageRegistry) {
            if (image.isHiddenFromGallery) continue;

            items.add(image);
        }

        return items;
    }
}
