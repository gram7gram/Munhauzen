package ua.gram.munhauzen.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public abstract class StoryMedia<T> implements JsonEntry {

    @JsonProperty
    public int startsAt;
    @JsonProperty
    public int finishesAt;
    /**
     * Media is displayed or playing
     */
    @JsonProperty
    public boolean isActive;
    /** Media should be displayed or playing */
    @JsonProperty
    public boolean isLocked;
    @JsonProperty
    public boolean isCompleted;
    @JsonProperty
    public float progress;
    @JsonIgnore
    public T previous;
    @JsonIgnore
    public T next;
    @JsonProperty
    public boolean isPrepared;
    @JsonProperty
    public boolean isPreparing;
    @JsonProperty
    public Date prepareStartedAt;
    @JsonProperty
    public Date prepareCompletedAt;

    /** Media path to file */
    @JsonProperty
    public String resource;
}
