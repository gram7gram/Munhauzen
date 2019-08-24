package ua.gram.munhauzen.history;

import java.util.HashSet;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class History {

    public static final int SAVE_LIMIT = 4;

    /**
     * Viewed images on all iterations
     */
    public HashSet<String> viewedImages;

    /**
     * Listened audio on all iterations
     */
    public HashSet<String> listenedAudio;

    /**
     * Opened audio-fails on all iterations
     */
    public HashSet<String> openedFails;
    /**
     * Unique items the player found on all interactions
     */
    public HashSet<String> globalInventory;
    /**
     * Version of the history
     */
    public int version;
    /**
     * Saves for player
     */
    public HashSet<String> saves;
    /**
     * Current save id
     */
    public String activeSaveId;
    /**
     * Current save
     */
    public Save activeSave;

    public History() {
        saves = new HashSet<>(SAVE_LIMIT);
        for (int i = 1; i <= SAVE_LIMIT; i++) {
            saves.add(i + "");//dummy ids
        }

        globalInventory = new HashSet<>();
        viewedImages = new HashSet<>();
        listenedAudio = new HashSet<>();
        openedFails = new HashSet<>();

        activeSaveId = "1";
        activeSave = new Save(activeSaveId);
    }

    public void setActiveSave(Save save) {
        activeSave = save;
        activeSaveId = save.id;
    }

}
