package ua.gram.munhauzen.history;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import ua.gram.munhauzen.entity.Audio;
import ua.gram.munhauzen.entity.Branch;
import ua.gram.munhauzen.entity.GameState;
import ua.gram.munhauzen.entity.Image;
import ua.gram.munhauzen.entity.Interaction;
import ua.gram.munhauzen.entity.InventoryBranch;
import ua.gram.munhauzen.entity.Item;
import ua.gram.munhauzen.entity.Scenario;
import ua.gram.munhauzen.entity.Tree;
import ua.gram.munhauzen.entity.chrono.Player;
import ua.gram.munhauzen.entity.chrono.PlayerEvent;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class History {

    public static final int DEFAULT_DAY = 1;
    public static final int SAVE_LIMIT = 4;

    private GameState gameState;

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
     * Opened images on all iterations
     */
    public ArrayList<ImageEntry> images;
    /**
     * Opened audio on all iterations
     */
    public ArrayList<AudioEntry> audio;
    /**
     * Collection of all events for chrono
     */
    public ArrayList<PlayerEvent> playerEvents;
    /**
     * Collection of events for chrono, which where not synced with server
     */
    public ArrayList<PlayerEvent> unsyncedPlayerEvents;
    /**
     * Opened interactions on all iterations
     */
    public ArrayList<InteractionEntry> completedInteractions;
    /**
     * Completed riddles on all iterations
     */
    public ArrayList<String> completedRiddles;
    /**
     * Update date of the history
     */
    public String updatedAt;
    /**
     * General information about the player
     */
    public Player player;
    /**
     * Game iteration. "New Game"
     */
    public int ng;
    /**
     * Is all audio opened
     */
    public boolean isAllAudioOpened;
    /**
     * Is all images opened
     */
    public boolean isAllWinnerOptionsOpened;
    /**
     * Is all images opened
     */
    public boolean isAllImagesOpened;
    /**
     * Sum of achievement points on all iterations
     */
    public int achievementPoints;
    /**
     * Current save
     */
    public Save activeSave;
    /**
     * Branch the scenario starts from. Found in runtime
     */
    public Branch initialBranch;

    public History(GameState gameState) {
        this.gameState = gameState;
        saves = new ArrayList<>(SAVE_LIMIT);
        audio = new ArrayList<>(10);
        images = new ArrayList<>(10);
        completedInteractions = new ArrayList<>(5);
        globalInventory = new HashSet<>();
        completedOptions = new HashSet<>();
        completedRiddles = new ArrayList<>();
        unsyncedPlayerEvents = new ArrayList<>();
        playerEvents = new ArrayList<>();
        achievements = new ArrayList<>();
        ng = 0;
        achievementPoints = 1;
        activeSave = new Save();
        player = new Player();
    }

    public Save getActiveSave() {
        return activeSave;
    }

    public void setActiveSave(Save activeSave) {
        this.activeSave = activeSave;
    }

    public Branch getInitialBranch() {
        return initialBranch;
    }

    public void updateInitialBranch() {
        if (gameState == null) return;

        Tree tree = gameState.getTree();
        if (tree == null) return;

//        Branch initial = TreeParser.findBranchByOption(gameState, GameState.INITIAL_BRANCH, true, null);
//        if (initial == null) return;
//
//        setInitialBranch(initial);
    }

    public void setInitialBranch(Branch initialBranch) {
        this.initialBranch = initialBranch;
    }

    public int getNg() {
        return ng;
    }

    public void setNg(int value) {
        this.ng = value;
    }

    public void NGplus() {
        setNg(getNg() + 1);
    }

    public void clear() {
        Save save = getActiveSave();

        save.reset();

        Tree tree = gameState.getTree();
        resetBranches(tree);

        updateInitialBranch();

        save.scenario.cid = null;
    }

    private void resetBranches(Branch root) {
        if (root == null) return;

        for (InventoryBranch inventoryBranch : root.getInventoryBranches()) {
            for (Branch branch : inventoryBranch.getBranches()) {
                resetBranches(branch);
            }
        }
    }

    public boolean addImage(Image image) {
        return addImage(image.id);
    }

    public boolean addImage(final String id) {

        for (ImageEntry input : images) {
            if (id.equals(input.name)) {
                return false;
            }
        }

        ImageEntry entry = new ImageEntry(id);

        images.add(entry);

        return true;
    }

    public boolean addAudio(final String id) {
        for (Entry input : audio) {
            if (id.equals(input.name)) {
                return false;
            }
        }

        AudioEntry entry = new AudioEntry(id);

        audio.add(entry);

        return true;
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

    public List<ImageEntry> getImages() {
        return images;
    }

    public List<AudioEntry> getAudio() {
        return audio;
    }

    public List<Entry> getInventory() {
        return getActiveSave().inventory;
    }

    public void setInventory(ArrayList<Entry> items) {
        getActiveSave().inventory = items;
    }

    public HashSet<String> getUniqueInventory() {
        Save save = getActiveSave();
        HashSet<String> uniqueItems = new HashSet<>(save.inventory.size());
        for (Entry entry : save.inventory) {
            uniqueItems.add(entry.name);
        }

        return uniqueItems;
    }

    public int getDay() {
        return getActiveSave().day;
    }

    public void setDay(int day) {
        getActiveSave().day = day;
    }

    public void incrementDay() {
        Save save = getActiveSave();
        save.day += 1;
    }

    public boolean containsInImageHistory(String id) {
        for (Entry entry : images) {
            if (entry.name.equals(id)) {
                return true;
            }
        }
        return false;
    }

    public boolean containsInImageHistory(Image image) {
        return containsInImageHistory(image.id);
    }

    public ImageEntry getEntry(Image image) {
        ImageEntry entry = null;

        for (ImageEntry imageEntry : images) {
            if (image.id.equals(imageEntry.name)) {
                entry = imageEntry;
                break;
            }
        }

        return entry;
    }

    public AudioEntry getEntry(Audio item) {
        AudioEntry entry = null;

        for (AudioEntry i : audio) {
            if (item.id.equals(i.name)) {
                entry = i;
                break;
            }
        }

        return entry;
    }

    public boolean containsInAudioHistory(Audio item) {
        return containsInAudioHistory(item.id);
    }

    public boolean containsInAudioHistory(String id) {
        for (Entry entry : audio) {
            if (entry.name.equals(id)) {
                return true;
            }
        }
        return false;
    }

    public boolean containsInInventory(String id) {
        for (Entry entry : getInventory()) {
            if (entry.name.equals(id)) {
                return true;
            }
        }
        return false;
    }

    public boolean isInteractionCompleted(String name, String option) {
        for (InteractionEntry entry : getActiveSave().completedInteractions) {
            if (entry.name.equals(name) && option.equals(entry.option)) {
                return entry.isCompleted;
            }
        }

        return false;
    }

    public Set<String> getDisabledOptions() {
        Save save = getActiveSave();
        return save.disabledOptions;
    }

    public void addDisabledOption(String option) {
        Save save = getActiveSave();
        save.disabledOptions.add(option);
    }

    public void addCompletedInteraction(Interaction interaction, String subtype, boolean isCompleted) {
        addCompletedInteraction(interaction.name, subtype, interaction.option, isCompleted);
    }

    public void addCompletedInteraction(String name, String subtype, String option, boolean isCompleted) {
        InteractionEntry entry = new InteractionEntry(name);
        entry.type = subtype;
        entry.isCompleted = isCompleted;
        entry.option = option;

        completedInteractions.add(entry);

        getActiveSave().completedInteractions.add(entry);
    }

    public void addCompletedRiddle(String id) {
        completedRiddles.add(id);
    }

    public boolean addStep(Branch branch) {
        if (branch == null) return false;

        Save save = getActiveSave();

        String cid = branch.cid;
        if (save.steps.size() > 0) {
            Entry last = save.steps.get(save.steps.size() - 1);
            if (last.name.equals(cid))
                return false;
        }

        save.steps.push(new Entry(cid));

        return true;
    }

    public boolean hasBranch(Branch branch) {
        Save save = getActiveSave();
        for (Entry step : save.steps) {
            if (step.name.equals(branch.getCid())) {
                return true;
            }
        }

        return false;
    }

    public Stack<Entry> getSteps() {
        Save save = getActiveSave();
        return save.steps;
    }

    public ArrayList<String> getCompletedRiddles() {
        return completedRiddles;
    }

    public ArrayList<InteractionEntry> getCompletedInteractions() {
        return completedInteractions;
    }

    public Set<String> getCompletedOptions() {
        return completedOptions;
    }

    public boolean addCompletedOption(String id) {
        if (completedOptions.contains(id)) return false;

        completedOptions.add(id);

        return true;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void validateProgress() {

        int max = getScenario().duration;
        int progress = getScenario().progress;
        if (max > 0 && progress > max) {
            getScenario().update(0, max);
        }
    }

    public void decreaseProgress() {
        int max = getScenario().duration;
        if (max > 0) {
            int progress;
            if (max > 2000) {
                progress = Math.max(0, getScenario().progress - 2000);
            } else {
                progress = 0;
            }

            getScenario().update(progress, max);
        }
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getCurrentBackground() {
        Save save = getActiveSave();
        String value = save.currentBackground;

        return value;
    }

    public void setCurrentBackground(String currentBackground) {

        if (currentBackground != null) {
            switch (currentBackground) {
                case "black":
                    return;
            }
        }

        Save save = getActiveSave();
        save.currentBackground = currentBackground;
    }

    public Player getPlayer() {
        return player;
    }

    public void addPlayerEvent(String name, String type) {
        if (name == null || type == null) return;

        PlayerEvent event = new PlayerEvent();
        event.name = name;
        event.type = type;

        addUnsyncedPlayerEvent(event);
        addTotalPlayerEvent(event);
    }

    private void addUnsyncedPlayerEvent(PlayerEvent event) {

        for (PlayerEvent e : unsyncedPlayerEvents) {
            if (event.name.equals(e.name)
                    && event.type.equals(e.type)) {
                return;
            }
        }

        unsyncedPlayerEvents.add(event);
    }

    private void addTotalPlayerEvent(PlayerEvent event) {

        for (PlayerEvent e : playerEvents) {
            if (event.name.equals(e.name)
                    && event.type.equals(e.type)) {
                return;
            }
        }

        playerEvents.add(event);
    }

    public int getProtractorAttemptCount() {
        Save save = getActiveSave();
        return save.protractorAttemptCount;
    }

    public void setProtractorAttemptCount(int count) {
        Save save = getActiveSave();
        save.protractorAttemptCount = count;
    }

    public ArrayList<Save> getSaves() {
        return saves;
    }

    public void setSaves(ArrayList<Save> saves) {
        this.saves.clear();
        this.saves.addAll(saves);
    }

    public Scenario getScenario() {
        return getActiveSave().scenario;
    }

    public void setScenario(Scenario scenario) {
        getActiveSave().scenario = scenario;
    }

    public void addAchievement(AchievementEntry achievement) {
        achievements.add(achievement);
        achievementPoints += achievement.reward;
    }
}
