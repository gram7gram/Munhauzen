package ua.gram.munhauzen.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Stack;

public class AchievementState implements JsonEntry {

    @JsonProperty
    public boolean areAllImagesUnlocked;
    @JsonProperty
    public boolean areAllGoofsUnlocked;
    @JsonProperty
    public boolean areAllMenuInventoryUnlocked;
    @JsonProperty
    public int points;
    @JsonProperty
    public Stack<String> achievementsToDisplay;
}
