package ua.gram.munhauzen.history;

import com.badlogic.gdx.utils.Array;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import ua.gram.munhauzen.entity.Story;
import ua.gram.munhauzen.utils.StringUtils;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class Save {


    public boolean isEnabled;

    /**
     * Id of the save
     */
    public String id;
    /**
     * Name of the save
     */
    public String name;
    /**
     * Save order
     */
    public int order;
    /**
     * List of completed story
     */
    public Stack<Story> storyStack;
    /**
     * Information about current story
     */
    public Story story;
    /**
     * Day count on current iteration
     */
    public int day;
    /**
     * Obtained items on current iteration
     */
    public Set<String> inventory;
    /**
     * Visited scenarios on current iteration
     */
    public Stack<Entry> steps;
    /**
     * Disabled scenarios on current iteration
     */
    public Set<String> disabledOptions;
    /**
     * Completed actions in visited scenarios on current iteration
     */
    public Array<OptionActionEntry> completedOptionActions;
    /**
     * Current background drawable
     */
    public String currentBackground;
    /**
     * Last background drawable that was used instead of `unknown`
     */
    public String lastUnknownBackground;
    /**
     * Cid of the last branch that was clicked by player
     */
    public Stack<String> clickedBranches;
    /**
     * Opened interactions on current iteration
     */
    public Array<InteractionEntry> completedInteractions;

    public Save() {

        id = StringUtils.cid();
        order = 0;
        steps = new Stack<>();
        inventory = new HashSet<>();
        disabledOptions = new HashSet<>();
        completedOptionActions = new Array<>();
        completedInteractions = new Array<>();
        story = new Story();
        clickedBranches = new Stack<>();
        storyStack = new Stack<>();
    }

    public void reset() {
        inventory.clear();
        disabledOptions.clear();
        steps.clear();
        completedOptionActions.clear();
        completedInteractions.clear();

        story = new Story();

        clickedBranches.clear();
    }

    public Set<String> getUniqueInventory() {
        HashSet<String> values = new HashSet<>(inventory.size());

        values.addAll(inventory);

        values.add("DEFAULT");

        return values;
    }

}
