package ua.gram.munhauzen.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import ua.gram.munhauzen.interaction.AbstractInteraction;

/**
 * @author Gram <gram7gram@gmail.com>
 */
public class StoryInteraction implements JsonEntry {

    @JsonProperty
    public String name;
    @JsonProperty
    public boolean isLocked;

    @JsonIgnore
    public AbstractInteraction interaction;
}
