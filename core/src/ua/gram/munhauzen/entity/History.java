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
     * Unique items the player found on all interactions
     */
    @JsonProperty
    public HashSet<String> globalInventory;

    public History() {
        globalInventory = new HashSet<>();
        viewedImages = new HashSet<>();
        listenedAudio = new HashSet<>();
        openedFails = new HashSet<>();
        visitedStories = new HashSet<>();
    }

}
