package ua.gram.munhauzen.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class Scenario implements JsonEntry {

    public static final String VICTORY = "aVictory";
    public static final String FADE_IN = "FADE";

    @JsonProperty
    public ArrayList<StoryImage> images;
    @JsonProperty
    public ArrayList<StoryAudio> audio;
    @JsonProperty
    public ArrayList<Decision> decisions;
    @JsonProperty
    public boolean isBegin;
    @JsonProperty
    public String text;
    @JsonProperty
    public String name;
    @JsonProperty
    public String chapter;
    @JsonProperty
    public String expansion;
    @JsonProperty
    public String interaction;
    @JsonProperty
    public String action;
    @JsonProperty
    public String source;

    public Scenario() {
        audio = new ArrayList<>();
        images = new ArrayList<>();
        decisions = new ArrayList<>();
    }

    @JsonIgnore
    public StoryImage lastImage() {
        return images.get(images.size() - 1);
    }

    @JsonIgnore
    public StoryAudio firstAudio() {
        return audio.get(0);
    }

    @JsonIgnore
    public StoryAudio lastAudio() {
        return audio.get(audio.size() - 1);
    }

    @JsonIgnore
    public boolean isVictory() {
        return VICTORY.equals(name);
    }
}