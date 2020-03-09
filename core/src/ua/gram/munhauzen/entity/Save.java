package ua.gram.munhauzen.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashSet;
import java.util.Set;

import ua.gram.munhauzen.utils.DateUtils;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class Save implements JsonEntry {

    /**
     * Id of the save
     */
    @JsonProperty
    public String id;
    /**
     * Date of last update
     */
    @JsonProperty
    public String updatedAt;
    /**
     * Current chapter
     */
    @JsonProperty
    public String chapter;
    /**
     * Last image from scenario of interaction
     */
    @JsonProperty
    public Image lastImage;
    /**
     * Save order
     */
    @JsonProperty
    public int order;
    /**
     * Information about current story
     */
    @JsonProperty
    public Story story;
    /**
     * Obtained items on current iteration
     */
    @JsonProperty
    public Set<String> inventory;

    @JsonProperty
    public ServantsState servantsInteractionState;

    @JsonProperty
    public HashSet<String> visitedStories;

    @JsonProperty
    public HashSet<String> visitedChapters;

    public Save() {
        this("active");
    }

    public Save(String id) {

        this.id = id;
        updatedAt = DateUtils.now();
        order = 0;
        inventory = new HashSet<>();
        visitedStories = new HashSet<>();
        visitedChapters = new HashSet<>();
        story = new Story();
        servantsInteractionState = new ServantsState();
    }

    public void reset() {
        inventory.clear();
        visitedChapters.clear();
        visitedStories.clear();

        updatedAt = DateUtils.now();

        story = new Story();
        servantsInteractionState = new ServantsState();
    }

}
