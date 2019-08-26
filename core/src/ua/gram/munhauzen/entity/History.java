package ua.gram.munhauzen.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashSet;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class History implements JsonEntry {

    @JsonProperty
    public String activeSaveId;

    @JsonProperty
    public HashSet<String> saves;

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
     * Unique items the player found on all interactions
     */
    @JsonProperty
    public HashSet<String> globalInventory;

    public History() {

        saves = new HashSet<>(GameState.SAVE_LIMIT);
        for (int i = 1; i <= GameState.SAVE_LIMIT; i++) {
            saves.add(i + "");//dummy ids
        }

        globalInventory = new HashSet<>();
        viewedImages = new HashSet<>();
        listenedAudio = new HashSet<>();
        openedFails = new HashSet<>();
    }

}
