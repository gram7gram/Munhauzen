package ua.gram.munhauzen.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashSet;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class History implements JsonEntry {

    /**
     * Viewed images on all iterations
     */
    @JsonProperty
    public HashSet<String> viewedImages;

    /**
     * Listened audio on all iterations
     */
    @JsonProperty
    public HashSet<String> listenedAudio;

    /**
     * Opened audio-fails on all iterations
     */
    @JsonProperty
    public HashSet<String> openedFails;

    /**
     * Visited stories on all iterations (allows progressbar skipping)
     */
    @JsonProperty
    public HashSet<String> visitedStories;

    /**
     * Visited chapters on all iterations
     */
    @JsonProperty
    public HashSet<String> visitedChapters;

    /**
     * Unique items the player found on all interactions
     */
    @JsonProperty
    public HashSet<String> globalInventory;

    public History() {
        if (globalInventory == null)
            globalInventory = new HashSet<>();
        if (viewedImages == null)
            viewedImages = new HashSet<>();
        if (listenedAudio == null)
            listenedAudio = new HashSet<>();
        if (openedFails == null)
            openedFails = new HashSet<>();
        if (visitedStories == null)
            visitedStories = new HashSet<>();
        if (visitedChapters == null)
            visitedChapters = new HashSet<>();
    }

}
