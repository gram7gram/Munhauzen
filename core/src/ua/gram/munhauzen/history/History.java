package ua.gram.munhauzen.history;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Stack;

import ua.gram.munhauzen.entity.Inventory;
import ua.gram.munhauzen.entity.Player;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class History {

    public static final int SAVE_LIMIT = 4;

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
     * Create date of the history
     */
    public String createdAt;
    /**
     * Saves for player
     */
    public ArrayList<Save> saves;
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
     * Current save
     */
    public Save activeSave;

    public History() {
        saves = new ArrayList<>(SAVE_LIMIT);
        completedInteractions = new ArrayList<>(5);
        globalInventory = new HashSet<>();
        completedOptions = new HashSet<>();
        achievements = new ArrayList<>();
        ng = 0;
        achievementPoints = 1;
        activeSave = new Save();
        player = new Player();
    }

    public Save getActiveSave() {
        return activeSave;
    }

//    public boolean addStep(Branch branch) {
//        if (branch == null) return false;
//
//        Save save = getActiveSave();
//
//        String id = branch.id;
//        if (save.steps.size() > 0) {
//            Entry last = save.steps.get(save.steps.size() - 1);
//            if (last.name.equals(id))
//                return false;
//        }
//
//        save.steps.push(new Entry(id));
//
//        return true;
//    }

    public Stack<Entry> getSteps() {
        Save save = getActiveSave();
        return save.steps;
    }
}
