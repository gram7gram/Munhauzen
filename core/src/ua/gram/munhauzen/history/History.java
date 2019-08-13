package ua.gram.munhauzen.history;

import java.util.ArrayList;
import java.util.HashSet;

import ua.gram.munhauzen.entity.Player;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class History {

    public static final int SAVE_LIMIT = 4;

    /**
     * Unique viewed images on all iterations
     */
    public HashSet<String> viewedImages;
    /**
     * Unique completed scenarios on all iterations
     */
    public HashSet<String> completedOptions;
    /**
     * Unique items the player found on all interactions
     */
    public HashSet<String> globalInventory;
    /**
     * Version of the history
     */
    public int version;
    /**
     * Identifier of the history. If empty - history is new
     */
    public String cid;
    /**
     * Saves for player
     */
    public HashSet<String> saves;
    /**
     * Opened images on all iterations
     */
    public ArrayList<AchievementEntry> achievements;
    /**
     * Opened interactions on all iterations
     */
    public ArrayList<InteractionEntry> completedInteractions;
    /**
     * General information about the player
     */
    public Player player;
    /**
     * Game iteration. "New Game"
     */
    public int ng;
    /**
     * Sum of achievement points on all iterations
     */
    public int achievementPoints;
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

        activeSaveId = "1";

        completedInteractions = new ArrayList<>(5);
        globalInventory = new HashSet<>();
        completedOptions = new HashSet<>();
        viewedImages = new HashSet<>();
        achievements = new ArrayList<>();
        ng = 0;
        achievementPoints = 1;
        activeSave = new Save(activeSaveId);
        player = new Player();
    }

}
