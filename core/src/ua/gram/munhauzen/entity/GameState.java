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
    public GalleryState galleryState;

    public GameState() {
        scenarioRegistry = new Array<>();
        imageRegistry = new Array<>();
        audioRegistry = new Array<>();
        audioFailRegistry = new Array<>();
        inventoryRegistry = new Array<>();
        chapterRegistry = new Array<>();
        menuState = new MenuState();
        galleryState = new GalleryState();
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

    public Array<Image> getGalleryImages() {
        Array<Image> items = new Array<>();

        for (Image image : imageRegistry) {
            if (image.isHiddenFromGallery) continue;

            items.add(image);
        }

        return items;
    }
}
