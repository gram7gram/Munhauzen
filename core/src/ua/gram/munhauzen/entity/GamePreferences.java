package ua.gram.munhauzen.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashSet;

public class GamePreferences implements JsonEntry {
    @JsonProperty
    public String currentScreen;
    @JsonProperty
    public boolean isLegalViewed;
    @JsonProperty
    public HashSet<Integer> ignoredAppUpdates;
}
