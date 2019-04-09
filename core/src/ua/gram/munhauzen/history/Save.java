package ua.gram.munhauzen.history;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import ua.gram.munhauzen.entity.Scenario;
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
     * Information about current scenario
     */
    public Scenario scenario;
    /**
     * Day count on current iteration
     */
    public int day;
    /**
     * Obtained items on current iteration
     */
    public ArrayList<Entry> inventory;
    /**
     * Visited options on current iteration
     */
    public Stack<Entry> steps;
    /**
     * Disabled options on current iteration
     */
    public Set<String> disabledOptions;
    /**
     * Completed actions in visited options on current iteration
     */
    public ArrayList<OptionActionEntry> completedOptionActions;
    /**
     * Current background image
     */
    public String currentBackground;
    /**
     * Last background image that was used instead of `unknown`
     */
    public String lastUnknownBackground;
    /**
     * Cid of the last branch that was clicked by player
     */
    public Stack<String> clickedBranches;
    /**
     * Opened interactions on current iteration
     */
    public ArrayList<InteractionEntry> completedInteractions;

    public Save() {

        id = StringUtils.cid();
        order = 0;
        steps = new Stack<>();
        inventory = new ArrayList<>();
        disabledOptions = new HashSet<>();
        completedOptionActions = new ArrayList<>();
        completedInteractions = new ArrayList<>();
        scenario = new Scenario();
        clickedBranches = new Stack<>();
    }

    public void reset() {
        inventory.clear();
        disabledOptions.clear();
        steps.clear();
        completedOptionActions.clear();
        completedInteractions.clear();

        scenario = new Scenario();

        clickedBranches.clear();
    }

}
