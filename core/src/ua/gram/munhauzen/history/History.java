package ua.gram.munhauzen.history;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.Stack;

import ua.gram.munhauzen.entity.Item;
import ua.gram.munhauzen.entity.Player;
import ua.gram.munhauzen.entity.Scenario;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class History {

    public static final int DEFAULT_DAY = 1;
    public static final int SAVE_LIMIT = 4;

    /**
     * Unique completed options on all iterations
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

    public void clear() {
        Save save = getActiveSave();

        save.reset();

        save.scenario.cid = null;
    }

    public boolean addItem(Item item) {

        if (item.isGlobal) {
            globalInventory.add(item.id);
        }

        return addItem(item.id);
    }

    public boolean addItem(final String id) {
        Save save = getActiveSave();
        for (Entry input : save.inventory) {
            if (id.equals(input.name)) {
                return false;
            }
        }

        save.inventory.add(new Entry(id));


        return true;
    }

    public void removeItem(String id) {
        Save save = getActiveSave();
        for (Entry item : save.inventory) {
            if (item.name.equals(id)) {
                save.inventory.remove(item);
                return;
            }
        }
    }

    public HashSet<String> getUniqueInventory() {
        Save save = getActiveSave();
        HashSet<String> uniqueItems = new HashSet<>(save.inventory.size());
        for (Entry entry : save.inventory) {
            uniqueItems.add(entry.name);
        }

        return uniqueItems;
    }

    public void incrementDay() {
        Save save = getActiveSave();
        save.day += 1;
    }

//    public boolean addStep(Branch branch) {
//        if (branch == null) return false;
//
//        Save save = getActiveSave();
//
//        String cid = branch.cid;
//        if (save.steps.size() > 0) {
//            Entry last = save.steps.get(save.steps.size() - 1);
//            if (last.name.equals(cid))
//                return false;
//        }
//
//        save.steps.push(new Entry(cid));
//
//        return true;
//    }

    public Stack<Entry> getSteps() {
        Save save = getActiveSave();
        return save.steps;
    }

    public Scenario getScenario() {
        return getActiveSave().scenario;
    }
}
