package ua.gram.munhauzen.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import ua.gram.munhauzen.history.History;
import ua.gram.munhauzen.utils.Log;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class GameState {

    public static final String INITIAL_BRANCH = "BEGIN";
    private static final String tag = GameState.class.getSimpleName();

    public static boolean isPaused = false;
    public static boolean isMute = false;
    public static boolean isFinaleReached = false;
    public static boolean isHighEndDevice = true;

    private Tree tree;
    private History history;
    private List<Option> optionRegistry;
    private List<Image> imageRegistry;
    private List<Audio> audioRegistry;
    private List<Item> itemRegistry;

    public GameState() {
        optionRegistry = new ArrayList<>();
        imageRegistry = new ArrayList<>();
        audioRegistry = new ArrayList<>();
        itemRegistry = new ArrayList<>();
    }

    public List<Option> getOptionRegistry() {
        return optionRegistry;
    }

    public void setOptionRegistry(List<Option> optionRegistry) {
        this.optionRegistry = optionRegistry;
    }

    public List<Image> getImageRegistry() {
        return imageRegistry;
    }

    public void setImageRegistry(List<Image> imageRegistry) {
        this.imageRegistry = imageRegistry;
    }

    public List<Image> getImageRegistryForGallery() {
        ArrayList<Image> filtered = new ArrayList<>(imageRegistry.size());
        for (Image i : imageRegistry) {
            if (!i.isHidden) {
                filtered.add(i);
            }
        }
        return filtered;
    }

    public List<Image> getOpenedImageRegistryForGallery() {
        List<Image> source = getImageRegistryForGallery();
        List<Image> filtered = new ArrayList<>(source.size());
        for (Image i : source) {
            if (history.containsInImageHistory(i)) {
                filtered.add(i);
            }
        }
        return filtered;
    }

    public List<Audio> getAudioRegistryForGallery() {
        ArrayList<Audio> filtered = new ArrayList<>(getAudioRegistry().size());
        for (Audio i : getAudioRegistry()) {
            if (i.isFail) {
                filtered.add(i);
            }
        }
        return filtered;
    }

    public List<Audio> getAudioRegistry() {
        return audioRegistry;
    }

    public void setAudioRegistry(List<Audio> audioRegistry) {
        this.audioRegistry = audioRegistry;
    }

    public List<Item> getItemRegistry() {
        return itemRegistry;
    }

    public void setItemRegistry(List<Item> itemRegistry) {
        this.itemRegistry = itemRegistry;
    }

    public HashSet<String> getInventory() {
        HashSet<String> registry = new HashSet<>(itemRegistry.size());
        for (Item item : itemRegistry) {
            registry.add(item.id);
        }
        return registry;
    }

    public Tree getTree() {
        return tree;
    }

    public void setTree(Tree tree) {
        this.tree = tree;
    }

    public History getHistory() {
        return history;
    }

    public void setHistory(History history) {
        this.history = history;
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
