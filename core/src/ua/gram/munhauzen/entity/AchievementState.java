package ua.gram.munhauzen.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AchievementState implements JsonEntry {

    @JsonProperty
    public boolean areAllImagesUnlocked;
    @JsonProperty
    public boolean areAllGoofsUnlocked;
    @JsonProperty
    public boolean areAllMenuInventoryUnlocked;
}
