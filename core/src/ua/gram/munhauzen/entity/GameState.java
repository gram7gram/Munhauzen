package ua.gram.munhauzen.entity;

import com.badlogic.gdx.utils.Timer;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;

import ua.gram.munhauzen.expansion.response.ExpansionResponse;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class GameState {

    private static final String tag = GameState.class.getSimpleName();

    public static boolean isPaused = false;
    public static boolean isMute = false;
    public static boolean isEndingReached = false;

    public History history;
    public Save activeSave;

    public ArrayList<Scenario> scenarioRegistry;
    public ArrayList<Image> imageRegistry;
    public ArrayList<Audio> audioRegistry;
    public ArrayList<AudioFail> audioFailRegistry;
    public ArrayList<Inventory> inventoryRegistry;
    public ArrayList<Chapter> chapterRegistry;

    public MenuState menuState;
    public GalleryState galleryState;
    public FailsState failsState;
    public AchievementState achievementState;
    public ExpansionResponse expansionInfo;
    public GamePreferences preferences;
    public PurchaseState purchaseState;

    public GameState() {
        history = new History();
        scenarioRegistry = new ArrayList<>();
        audioRegistry = new ArrayList<>();
        imageRegistry = new ArrayList<>();
        audioFailRegistry = new ArrayList<>();
        inventoryRegistry = new ArrayList<>();
        chapterRegistry = new ArrayList<>();
        setActiveSave(new Save());
    }

    public void setActiveSave(Save save) {
        activeSave = save;
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

    public static void clearTimer(String tag) {
        Log.i(tag, "clearTimer " + tag);
        Timer.instance().clear();
    }

    @JsonIgnore
    public ArrayList<Image> getGalleryImages() {
        ArrayList<Image> items = new ArrayList<>();

        for (Image image : imageRegistry) {
            if (image.isHiddenFromGallery) continue;

            items.add(image);
        }

        return items;
    }
}
