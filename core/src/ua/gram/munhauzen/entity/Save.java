package ua.gram.munhauzen.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    /**
     * Information about current story
     */
    @JsonIgnore
    public Story story;

    public Save() {
        this("active");
    }

    public Save(String id) {

        this.id = id;
        updatedAt = DateUtils.now();
        order = 0;

        if (inventory == null)
            inventory = new HashSet<>();
        if (visitedStories == null)
            visitedStories = new HashSet<>();
        if (visitedChapters == null)
            visitedChapters = new HashSet<>();
        if (story == null)
            story = new Story();
        if (servantsInteractionState == null)
            servantsInteractionState = new ServantsState();
    }
//
//    public void reset() {
//        inventory.clear();
//        visitedChapters.clear();
//        visitedStories.clear();
//
//        updatedAt = DateUtils.now();
//
//        story = new Story();
//        servantsInteractionState = new ServantsState();
//    }

}
