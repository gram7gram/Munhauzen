package ua.gram.munhauzen.entity;

import com.badlogic.gdx.utils.Timer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class GameState implements JsonEntry {

    private static final String tag = GameState.class.getSimpleName();

    public static boolean isPaused = false;
    public static boolean isMute = false;

    @JsonProperty
    public History history;
    @JsonProperty
    public Save activeSave;

    @JsonIgnore
    public ArrayList<Scenario> scenarioRegistry;
    @JsonIgnore
    public ArrayList<Image> imageRegistry;
    @JsonIgnore
    public ArrayList<Audio> audioRegistry;
    @JsonIgnore
    public ArrayList<AudioFail> audioFailRegistry;
    @JsonIgnore
    public ArrayList<Inventory> inventoryRegistry;
    @JsonIgnore
    public ArrayList<Chapter> chapterRegistry;

    @JsonProperty
    public MenuState menuState;
    @JsonProperty
    public GalleryState galleryState;
    @JsonProperty
    public FailsState failsState;
    @JsonProperty
    public boolean areAllImagesUnlocked;
    @JsonProperty
    public boolean areAllGoofsUnlocked;

    public GameState() {
        history = new History();
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

    public static void clearTimer() {
        Log.i(tag, "clearTimer");
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
